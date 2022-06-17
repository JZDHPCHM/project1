CREATE OR REPLACE PROCEDURE SP_MDS_HASLCLAIMS(V_INDATE  IN  VARCHAR2,
                                          V_RTNCODE OUT VARCHAR2,
                                          V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_HASLCLAIMS                                     */
  /* Developed By   : SY                                                */
  /* Developed Date : 2018-11-07                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  DBMS_OUTPUT.ENABLE(1000000);
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR06002'' AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  --赔款支付平均时长
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
  (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
  SELECT ''OR06002'',
         ''86'',
         NULL,
        ROUND(SUM(ROUND((PAY_TIME - APP_TIME) * 24 * 60, 2))/ COUNT(*)/24/60,4) AS paytime,
         CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
         TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
SELECT M.REPORT_ID,SUBSTR(T3,1,1) AS T2,T3,ACC.ACC_GET_TIME,APP_TIME,END_TIME,
ROUND((PAY_TIME - APP_TIME) * 60* 24,2)  AS "花费时间（分钟）",PAY_TIME,HAS_REFU
FROM(
SELECT T.REPORT_ID,
SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
       APP_TIME,
       T.END_TIME,
       (CASE
         WHEN NOT_PAY.REPORT_ID IS NOT NULL
           THEN T.END_TIME
               --拒赔类付款时间为空
         WHEN refu.HAS_REFU >0  THEN NULL
           --豁免养育类 付款时间=结案时间
        WHEN HY.HAS_HY >0  THEN T.END_TIME

             WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL
           THEN  PAYINFO.MAX_PAY_SEND_TIME
             WHEN payee.PAY_DATE IS NOT NULL THEN payee.pay_date
             ELSE NULL END) AS PAY_TIME,
               ---1 存在拒赔、0不存在拒赔
              ( CASE WHEN REFU.HAS_REFU >0  THEN 1 ELSE 0 END) AS HAS_REFU

  FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME
 , T3.END_TIME
          FROM (

                SELECT DISTINCT T.REPORT_ID
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                 WHERE/* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                   AND */T.HIS_TAG = ''0'') T1,
                --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID =''1''
                          GROUP BY T.REPORT_ID) T2,
               (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_08''
                 GROUP BY T.REPORT_ID) T3
         WHERE T1.REPORT_ID = T2.REPORT_ID
          AND T1.REPORT_ID = T3.REPORT_ID
         ) T,E_BDS_CLAIMS_CLAIMS CLAIMS
         --支付号
 LEFT JOIN (SELECT TTF.REPORT_ID,TO_DATE(MAX(TR.PAYSENDDATE),''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
  FROM E_BDS_CLAIMS_FINANCE_PAYMENT TP,E_BDS_CLAIMS_FINANCE_RESPONSE TR,E_BDS_CLAIMS_TASK_TO_FINANCE TTF
WHERE TP.BIDUIID=TR.BIDUIID AND TTF.BIDUIID=TP.BIDUIID AND TTF.BIDUIID=TR.BIDUIID
AND TTF.STATUS=''2''
AND TR.TRANSSTATE=''2''
GROUP BY TTF.REPORT_ID
  ) PAYINFO
 ON PAYINFO.REPORT_ID=CLAIMS.REPORT_ID

    --支付号
 LEFT JOIN (SELECT REPORT_ID,MAX(payee.pay_SEND_time) AS PAY_DATE　FROM (
SELECT DISTINCT REPORT_ID,SUBSTR(ENTITY,1,1) || REQNO
 AS PAY_NO,FPAY.* 
 FROM  E_BDS_CLAIMS_PAYMENT
 LEFT JOIN E_BDS_CLAIMS_FINANCIAL_PAY FPAY 
 ON SUBSTR(ENTITY,1,1) || REQNO=FPAY.PAY_NO
  WHERE REQNO IS NOT NULL) PAYEE
  GROUP BY REPORT_ID
  ) PAYEE
 ON PAYEE.REPORT_ID=CLAIMS.REPORT_ID
 LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
 ON NOT_PAY.REPORT_ID=CLAIMS.REPORT_ID
 --是否包含豁免、养育
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_HY
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION = ''05'' OR ca.conclusion=''06'') AND ca.his_tag=''0''
GROUP BY ca.report_id) HY ON HY.REPORT_ID=CLAIMS.REPORT_ID

 --是否包含拒赔
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_REFU
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION  in (''07'',''08'',''09'',''10'')  ) AND ca.his_tag=''0''
GROUP BY ca.report_id) REFU ON REFU.REPORT_ID=CLAIMS.REPORT_ID


               WHERE T.REPORT_ID=CLAIMS.REPORT_ID
               ) M
               --受理时间
               LEFT JOIN (  SELECT T.REPORT_ID, MIN(t.start_time) AS ACC_GET_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_03''
                 GROUP BY T.REPORT_ID) ACC  ON ACC.REPORT_ID=M.REPORT_ID
                 )M
             WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') AND has_refu=''0''';
  EXECUTE IMMEDIATE V_SQL;
  DBMS_OUTPUT.PUT_LINE(V_SQL);
  -----------------------------开始加载日志信息-------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR06002',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR06001''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  --索赔核定平均时长
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR06001'',
                 ''86'',
                 NULL,
                ROUND(SUM(trunc(END_TIME)-TRUNC(ACC_GET_TIME))/COUNT(1),4),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
        SELECT T.REPORT_ID,
                SUBSTR(CLAIMS.ORG_CODE, 1, 1) AS T3,
                T.END_TIME
          FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME, T3.END_TIME
                   FROM (
                         
                         SELECT T.REPORT_ID
                           FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                          WHERE  T.HIS_TAG = ''0'') T1,
                        --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                         
                           FROM E_BDS_CLAIMS_TASKTIME          T,
                                E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID = ''1''
                          GROUP BY T.REPORT_ID) T2,
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_08''
                          GROUP BY T.REPORT_ID) T3
                  WHERE T1.REPORT_ID = T2.REPORT_ID
                    AND T1.REPORT_ID = T3.REPORT_ID) T
                   JOIN  E_BDS_CLAIMS_CLAIMS CLAIMS
         ON T.REPORT_ID = CLAIMS.REPORT_ID) M
  LEFT JOIN (SELECT T.REPORT_ID, MIN(T.START_TIME) AS ACC_GET_TIME
               FROM E_BDS_CLAIMS_TASKTIME T
              WHERE T.CODE = ''ClaimWf001_03''
              GROUP BY T.REPORT_ID) ACC
    ON ACC.REPORT_ID = M.REPORT_ID
 LEFT JOIN E_BDS_BIDM_MGECMP B
ON B.LAORIGORGCODE = M.T3
 WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')'; 
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
    INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR06001',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08002''
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = '||V_INDATE||'';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  --评估期内所有赔案支付时点至核赔完成时点差值之和
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
 SELECT ''OR08002'',
         PROVINCECOMCODE,
         NULL,
        SUM(ROUND((PAY_TIME - APP_TIME), 4)) AS paytime,
         CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
         TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
      FROM (
SELECT M.REPORT_ID,SUBSTR(T3,1,1) AS T2,T3,ACC.ACC_GET_TIME,APP_TIME,END_TIME,
ROUND((PAY_TIME - APP_TIME) * 60* 24,2)  AS "花费时间（分钟）",PAY_TIME,HAS_REFU
FROM(
SELECT T.REPORT_ID,
SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
       APP_TIME,
       T.END_TIME,
       (CASE
         WHEN NOT_PAY.REPORT_ID IS NOT NULL
           THEN T.END_TIME
               --拒赔类付款时间为空
         WHEN refu.HAS_REFU >0  THEN NULL
           --豁免养育类 付款时间=结案时间
        WHEN HY.HAS_HY >0  THEN T.END_TIME

             WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL
           THEN  PAYINFO.MAX_PAY_SEND_TIME
             WHEN payee.PAY_DATE IS NOT NULL THEN payee.pay_date
             ELSE NULL END) AS PAY_TIME,
               ---1 存在拒赔、0不存在拒赔
              ( CASE WHEN REFU.HAS_REFU >0  THEN 1 ELSE 0 END) AS HAS_REFU

  FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME
 , T3.END_TIME
          FROM (

                SELECT DISTINCT T.REPORT_ID
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                 WHERE/* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                   AND */T.HIS_TAG = ''0'') T1,
                --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID =''1''
                          GROUP BY T.REPORT_ID) T2,
               (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_08''
                 GROUP BY T.REPORT_ID) T3
         WHERE T1.REPORT_ID = T2.REPORT_ID
          AND T1.REPORT_ID = T3.REPORT_ID
         ) T,E_BDS_CLAIMS_CLAIMS CLAIMS
         --支付号
 LEFT JOIN (SELECT TTF.REPORT_ID,TO_DATE(MAX(TR.PAYSENDDATE),''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
  FROM E_BDS_CLAIMS_FINANCE_PAYMENT TP,E_BDS_CLAIMS_FINANCE_RESPONSE TR,E_BDS_CLAIMS_TASK_TO_FINANCE TTF
WHERE TP.BIDUIID=TR.BIDUIID AND TTF.BIDUIID=TP.BIDUIID AND TTF.BIDUIID=TR.BIDUIID
AND TTF.STATUS=''2''
AND TR.TRANSSTATE=''2''
GROUP BY TTF.REPORT_ID
  ) PAYINFO
 ON PAYINFO.REPORT_ID=CLAIMS.REPORT_ID

    --支付号
 LEFT JOIN (SELECT REPORT_ID,MAX(payee.pay_SEND_time) AS PAY_DATE　FROM (
SELECT DISTINCT REPORT_ID,SUBSTR(ENTITY,1,1) || REQNO
 AS PAY_NO,FPAY.* 
 FROM  E_BDS_CLAIMS_PAYMENT
 LEFT JOIN E_BDS_CLAIMS_FINANCIAL_PAY FPAY 
 ON SUBSTR(ENTITY,1,1) || REQNO=FPAY.PAY_NO
  WHERE REQNO IS NOT NULL) PAYEE
  GROUP BY REPORT_ID
  ) PAYEE
 ON PAYEE.REPORT_ID=CLAIMS.REPORT_ID
 LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
 ON NOT_PAY.REPORT_ID=CLAIMS.REPORT_ID
 --是否包含豁免、养育
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_HY
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION = ''05'' OR ca.conclusion=''06'') AND ca.his_tag=''0''
GROUP BY ca.report_id) HY ON HY.REPORT_ID=CLAIMS.REPORT_ID

 --是否包含拒赔
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_REFU
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION  in (''07'',''08'',''09'',''10'')  ) AND ca.his_tag=''0''
GROUP BY ca.report_id) REFU ON REFU.REPORT_ID=CLAIMS.REPORT_ID


               WHERE T.REPORT_ID=CLAIMS.REPORT_ID
               ) M
               --受理时间
               LEFT JOIN (  SELECT T.REPORT_ID, MIN(t.start_time) AS ACC_GET_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_03''
                 GROUP BY T.REPORT_ID) ACC  ON ACC.REPORT_ID=M.REPORT_ID
                 )M
                 LEFT JOIN E_BDS_BIDM_MGECMP B
            ON LAORIGORGCODE = M.T2
             WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') 
 GROUP BY PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
   INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08002',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
 --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08005''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = '|| V_INDATE ||'';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期内所有已决赔案出险日至结案的天数总和
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
 SELECT ''OR08005'',
        PROVINCECOMCODE,
        NULL,
         SUM(trunc(END_TIME)-TRUNC(ACC_GET_TIME)),
        CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
         TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
    FROM (
        SELECT T.REPORT_ID,
                SUBSTR(CLAIMS.ORG_CODE, 1, 1) AS T3,
                T.END_TIME
          FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME, T3.END_TIME
                   FROM (
                         SELECT T.REPORT_ID
                           FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                          WHERE  T.HIS_TAG = ''0'') T1,
                        --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                         
                           FROM E_BDS_CLAIMS_TASKTIME          T,
                                E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID = ''1''
                          GROUP BY T.REPORT_ID) T2,
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_08''
                          GROUP BY T.REPORT_ID) T3
                  WHERE T1.REPORT_ID = T2.REPORT_ID
                    AND T1.REPORT_ID = T3.REPORT_ID) T
                   JOIN  E_BDS_CLAIMS_CLAIMS CLAIMS
         ON T.REPORT_ID = CLAIMS.REPORT_ID) M
  LEFT JOIN (SELECT T.REPORT_ID, MIN(T.START_TIME) AS ACC_GET_TIME
               FROM E_BDS_CLAIMS_TASKTIME T
              WHERE T.CODE = ''ClaimWf001_03''
              GROUP BY T.REPORT_ID) ACC
    ON ACC.REPORT_ID = M.REPORT_ID
 LEFT JOIN E_BDS_BIDM_MGECMP B
ON B.LAORIGORGCODE = M.T3
WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
GROUP BY PROVINCECOMCODE
ORDER BY PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_SQL        := 'INSERT INTO ETL_LOG_DETAIL(
   ID,SP_NAME,INDEX_ID,TABLE_NAME,START_TIME,END_TIME,DATA_DATE,RECORD_NUM,ERR_CODE,ERR_MSG) VALUES
(SYS_GUID(),''SP_HASLCLAIMS'',''OR08005'',''E_MDS_IRR_INDEX_VALUE'',''' ||
                  V_START || ''',''' || V_END || ''',''' || '' || ''',''' ||
                  V_RECORD_NUM || ''',''' || V_RTNCODE || ''',''' ||
                  V_RTNMSG || ''')';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08006''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期内所有已决赔案件数
  V_START := SYSDATE;
  V_SQL   :='INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
         SELECT ''OR08006'',
                 PROVINCECOMCODE,
                 NULL,
                 COUNT(1),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM (
SELECT M.REPORT_ID,SUBSTR(T3,1,1) AS T2,T3,ACC.ACC_GET_TIME,APP_TIME,END_TIME,
ROUND((PAY_TIME - APP_TIME) * 60* 24,2)  AS "花费时间（分钟）",PAY_TIME,HAS_REFU
FROM(
SELECT T.REPORT_ID,
SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
       APP_TIME,
       T.END_TIME,
       (CASE
         WHEN NOT_PAY.REPORT_ID IS NOT NULL
           THEN T.END_TIME
               --拒赔类付款时间为空
         WHEN refu.HAS_REFU >0  THEN NULL
           --豁免养育类 付款时间=结案时间
        WHEN HY.HAS_HY >0  THEN T.END_TIME

             WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL
           THEN  PAYINFO.MAX_PAY_SEND_TIME
             WHEN payee.PAY_DATE IS NOT NULL THEN payee.pay_date
             ELSE NULL END) AS PAY_TIME,
               ---1 存在拒赔、0不存在拒赔
              ( CASE WHEN REFU.HAS_REFU >0  THEN 1 ELSE 0 END) AS HAS_REFU

  FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME
 , T3.END_TIME
          FROM (

                SELECT DISTINCT T.REPORT_ID
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                 WHERE/* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                   AND */T.HIS_TAG = ''0'') T1,
                --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID =''1''
                          GROUP BY T.REPORT_ID) T2,
               (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_08''
                 GROUP BY T.REPORT_ID) T3
         WHERE T1.REPORT_ID = T2.REPORT_ID
          AND T1.REPORT_ID = T3.REPORT_ID
         ) T,E_BDS_CLAIMS_CLAIMS CLAIMS
         --支付号
 LEFT JOIN (SELECT TTF.REPORT_ID,TO_DATE(MAX(TR.PAYSENDDATE),''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
  FROM E_BDS_CLAIMS_FINANCE_PAYMENT TP,E_BDS_CLAIMS_FINANCE_RESPONSE TR,E_BDS_CLAIMS_TASK_TO_FINANCE TTF
WHERE TP.BIDUIID=TR.BIDUIID AND TTF.BIDUIID=TP.BIDUIID AND TTF.BIDUIID=TR.BIDUIID
AND TTF.STATUS=''2''
AND TR.TRANSSTATE=''2''
GROUP BY TTF.REPORT_ID
  ) PAYINFO
 ON PAYINFO.REPORT_ID=CLAIMS.REPORT_ID

    --支付号
 LEFT JOIN (SELECT REPORT_ID,MAX(payee.pay_SEND_time) AS PAY_DATE　FROM (
SELECT DISTINCT REPORT_ID,SUBSTR(ENTITY,1,1) || REQNO
 AS PAY_NO,FPAY.* 
 FROM  E_BDS_CLAIMS_PAYMENT
 LEFT JOIN E_BDS_CLAIMS_FINANCIAL_PAY FPAY 
 ON SUBSTR(ENTITY,1,1) || REQNO=FPAY.PAY_NO
  WHERE REQNO IS NOT NULL) PAYEE
  GROUP BY REPORT_ID
  ) PAYEE
 ON PAYEE.REPORT_ID=CLAIMS.REPORT_ID
 LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
 ON NOT_PAY.REPORT_ID=CLAIMS.REPORT_ID
 --是否包含豁免、养育
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_HY
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION = ''05'' OR ca.conclusion=''06'') AND ca.his_tag=''0''
GROUP BY ca.report_id) HY ON HY.REPORT_ID=CLAIMS.REPORT_ID

 --是否包含拒赔
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_REFU
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION  in (''07'',''08'',''09'',''10'')  ) AND ca.his_tag=''0''
GROUP BY ca.report_id) REFU ON REFU.REPORT_ID=CLAIMS.REPORT_ID


               WHERE T.REPORT_ID=CLAIMS.REPORT_ID
               ) M
               --受理时间
               LEFT JOIN (  SELECT T.REPORT_ID, MIN(t.start_time) AS ACC_GET_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_03''
                 GROUP BY T.REPORT_ID) ACC  ON ACC.REPORT_ID=M.REPORT_ID
                 )M
                 LEFT JOIN E_BDS_BIDM_MGECMP B
            ON LAORIGORGCODE = M.T2
             WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') 
 GROUP BY PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
    INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08006',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
   --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08012''
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') ='||V_INDATE||'';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期内所有正常结案赔案的已决赔款之和
 /* V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR08012'',
                 B.PROVINCECOMCODE,
                 NULL,
                 nvl(SUM(BEFORE_AMOUNT),0),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
     FROM (SELECT
        --最终给付金额
         PAY.FINAL_ADJ_AMOUNT,
         --预估金额金额
         INS.EXPECTED_AMOUNT,
         --保额
         INS.INSURANCE_AMOUNT,
         --调整金额
         ABS(PAY.CHANGE_AMOUNT) CHANGE_AMOUNT,
         --调整之前金额  最终给付金额-调整金额
         (PAY.FINAL_ADJ_AMOUNT - PAY.CHANGE_AMOUNT) AS BEFORE_AMOUNT,
         --T3
         SUBSTR(M.POLICY_NO, 1, 1) AS T3,
         --报案号
         M.REPORT_ID,
         --保单号
         M.POLICY_NO,
         --理赔结论代码
         CONCLUSION,
         (SELECT P.PAR_VALUE
            FROM E_BDS_CLAIMS_PARAM P
           WHERE P.PAR_TYPE LIKE ''%pa_claims_conclusion%''
             AND P.PAR_KEY = CONCLUSION) AS "理赔结论"
          FROM (SELECT DISTINCT CA.REPORT_ID, CA.POLICY_NO, CA.CONCLUSION
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                 WHERE CA.HIS_TAG = ''0''
                   AND CA.CONCLUSION IN (''01'', ''02'', ''03'', ''04'')
                   AND CA.INSURANCE_CODE IN
                       (SELECT T.INSURANCE_CODE
                          FROM E_BDS_CLAIMS_LIABILITY T
                         WHERE T.PRODUCT_TYPE IN (''健康险'', ''意外险''))
                   AND CA.REPORT_ID IN
                       (SELECT TT.REPORT_ID
                          FROM E_BDS_CLAIMS_TASKTIME TT, E_BDS_CLAIMS_CLAIMS C
                         WHERE (TT.CODE = ''ClaimWf001_08'' OR
                               TT.CODE = ''ClaimWfPayment_Success'')
                           AND C.STATUS = ''8''
                           AND C.SUB_STATUS = ''0''
                           AND TT.REPORT_ID = C.REPORT_ID HAVING
                         MAX(TRUNC(TT.END_TIME)) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
                         GROUP BY TT.REPORT_ID)) M
          LEFT JOIN (SELECT REPORT_ID,
                           POLICY_NO,
                           SUM(FINAL_ADJ_AMOUNT) AS FINAL_ADJ_AMOUNT,
                           SUM(CHANGE_AMOUNT) AS CHANGE_AMOUNT
                      FROM (SELECT E_BDS_CLAIMS_DUTY.REPORT_ID REPORT_ID,
                                   E_BDS_CLAIMS_DUTY.POLICY_NO POLICY_NO,
                                   E_BDS_CLAIMS_DUTY_DETAIL.HCRTABLE INSURANCE_CODE,
                                   SUM(NVL(E_BDS_CLAIMS_DUTY_DETAIL.ACTVALUE, ''0'') +
                                       NVL(E_BDS_CLAIMS_DUTY_DETAIL.EMV, ''0'') +
                                       NVL(E_BDS_CLAIMS_DUTY_DETAIL.ZDISCAMT, ''0'')) FINAL_ADJ_AMOUNT,
                                   SUM(NVL(E_BDS_CLAIMS_DUTY_DETAIL.ZDISCAMT, ''0'')) AS CHANGE_AMOUNT
                              FROM E_BDS_CLAIMS_DUTY
                              LEFT JOIN E_BDS_CLAIMS_DUTY_DETAIL
                                ON E_BDS_CLAIMS_DUTY.SERNO = E_BDS_CLAIMS_DUTY_DETAIL.DUTY_SERNO
                             WHERE E_BDS_CLAIMS_DUTY.COMPUTE_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.ASS_CLAIMS_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.INSURANCE_CODE IS NULL
                             GROUP BY E_BDS_CLAIMS_DUTY.REPORT_ID,
                                      E_BDS_CLAIMS_DUTY.POLICY_NO,
                                      E_BDS_CLAIMS_DUTY_DETAIL.HCRTABLE
                            UNION ALL
                            SELECT E_BDS_CLAIMS_DUTY.REPORT_ID REPORT_ID,
                                   E_BDS_CLAIMS_DUTY.POLICY_NO POLICY_NO,
                                   E_BDS_CLAIMS_DUTY.INSURANCE_CODE INSURANCE_CODE,
                                   NVL(E_BDS_CLAIMS_DUTY.FINAL_ADJ_AMOUNT, ''0'') FINAL_ADJ_AMOUNT,
                                   NVL(E_BDS_CLAIMS_DUTY.ADJUSTMENT_AMOUNT, ''0'') AS CHANGE_AMOUNT
                              FROM E_BDS_CLAIMS_DUTY
                             WHERE E_BDS_CLAIMS_DUTY.COMPUTE_FLAG = ''0''
                               AND E_BDS_CLAIMS_DUTY.ASS_CLAIMS_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.INSURANCE_CODE IS NOT NULL) DUTY
                     GROUP BY REPORT_ID, POLICY_NO) PAY
            ON PAY.REPORT_ID = M.REPORT_ID
           AND PAY.POLICY_NO = M.POLICY_NO
          LEFT JOIN (SELECT INSURANCE.REPORT_ID,
                           INSURANCE.POLICY_NO,
                           SUM(INSURANCE.INSURED_AMOUNT) AS INSURANCE_AMOUNT,
                           SUM(INSURANCE.EXPECTED_AMOUNT) AS EXPECTED_AMOUNT
                      FROM E_BDS_CLAIMS_INSURANCE INSURANCE
                     WHERE INSURANCE.ASS_CLAIMS_FLAG = ''1''
                     GROUP BY INSURANCE.REPORT_ID, INSURANCE.POLICY_NO) INS
            ON INS.REPORT_ID = M.REPORT_ID
           AND INS.POLICY_NO = M.POLICY_NO) T
           LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = T.T3
  WHERE 理赔结论 NOT IN (''通融给付'', ''协议给付'', ''部分给付'', ''拒赔解约退现价、拒赔'', ''解约退保费、拒赔'',''解约不退费、拒赔'',''保费豁免'',''拒付'')
  GROUP BY B.PROVINCECOMCODE
  ORDER BY B.PROVINCECOMCODE';
dbms_output.put_Line(V_SQL);
  EXECUTE IMMEDIATE V_SQL;

  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
    INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08012',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT; */
    --------------------------------插入数据--------------------------------
  --评估期内所有正常结案赔案的已决赔款之和
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR08012'',
                 B.PROVINCECOMCODE,
                 NULL,
                 nvl(SUM(BEFORE_AMOUNT),0),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
     FROM (SELECT

--最终给付金额
PAY.FINAL_ADJ_AMOUNT,
--预估金额金额
INS.EXPECTED_AMOUNT,
--保额
INS.INSURANCE_AMOUNT,
--调整金额
PAY.CHANGE_AMOUNT,
--调整之前金额  最终给付金额-调整金额
(PAY.FINAL_ADJ_AMOUNT-PAY.CHANGE_AMOUNT) AS BEFORE_AMOUNT,
SUBSTR(M.POLICY_NO,1,1) AS T2,
--T3
SUBSTR(M.POLICY_NO,1,3) AS T3,
--报案号
M.REPORT_ID,
--保单号
M.POLICY_NO,
--理赔结论代码
CONCLUSION,
(SELECT p.par_value FROM E_BDS_CLAIMS_PARAM P
WHERE P.PAR_TYPE LIKE ''%pa_claims_conclusion%'' AND P.PAR_KEY= CONCLUSION) AS "理赔结论"
  FROM (
  
  
  
  
  
  SELECT CA3.REPORT_ID,
               CA3.POLICY_NO,
                --listagg(ca3.conclusion,'','') WITHIN GROUP (ORDER BY ca3.conclusion)
                ltrim(fun_to_irr(CAST(COLLECT(ca3.conclusion) AS tab_irr)),'','')
               --WM_CONCAT(CA3.CONCLUSION)
               AS CONCLUSION

          FROM (SELECT DISTINCT CA2.*
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                  LEFT JOIN ( --赔付险种及结论
                            SELECT CA1.REPORT_ID,
                                    CA1.POLICY_NO,
                                   --listagg(ca1.conclusion,'','') WITHIN GROUP (ORDER BY ca1.conclusion)
                                   --  WM_CONCAT(CA1.CONCLUSION)
                                   ltrim(fun_to_irr(CAST(COLLECT(ca1.conclusion) AS tab_irr)),'','')
                                     AS CONCLUSION
                              FROM (SELECT DISTINCT CA.REPORT_ID,
                                                     CA.POLICY_NO,
                                                     CA.CONCLUSION
                                       FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                                      WHERE CA.HIS_TAG = ''0''
                                        AND CA.CONCLUSION IN
                                            (''01'', ''02'', ''03'', ''04'')) CA1
                             GROUP BY CA1.REPORT_ID, CA1.POLICY_NO) CA2
                    ON CA.REPORT_ID = CA2.REPORT_ID
                   AND CA.POLICY_NO = CA2.POLICY_NO
                 WHERE CA.HIS_TAG = ''0''
                   AND CA.CONCLUSION IN (''01'', ''02'', ''03'', ''04'')
                   AND CA.INSURANCE_CODE IN
                       (SELECT T.INSURANCE_CODE
                          FROM E_BDS_CLAIMS_LIABILITY T
                         WHERE T.PRODUCT_TYPE IN (''健康险'',''意外险''))
                   AND CA.REPORT_ID IN
                       (
                       SELECT M.REPORT_ID--, PAY_TIME, HAS_REFU

  FROM (

        SELECT T.REPORT_ID,
                --SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
                -- APP_TIME,
                T.END_TIME,
                (CASE
                  WHEN NOT_PAY.REPORT_ID IS NOT NULL THEN
                   T.END_TIME
                --拒赔类付款时间为空
                  WHEN REFU.HAS_REFU > 0 THEN
                   NULL
                --豁免养育类 付款时间=结案时间
                  WHEN HY.HAS_HY > 0 THEN
                   T.END_TIME

                  WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL THEN
                   PAYINFO.MAX_PAY_SEND_TIME
                  WHEN PAYEE.PAY_DATE IS NOT NULL THEN
                   PAYEE.PAY_DATE
                  ELSE
                   NULL
                END) AS PAY_TIME,
                ---1 存在拒赔、0不存在拒赔
                (CASE
                  WHEN REFU.HAS_REFU > 0 THEN
                   1
                  ELSE
                   0
                END) AS HAS_REFU

          FROM (SELECT DISTINCT T1.REPORT_ID --, T2.APP_TIME
                                ,
                                 T3.END_TIME
                   FROM (

                         SELECT DISTINCT T.REPORT_ID
                           FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                          WHERE /* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                                            AND */
                          T.HIS_TAG = ''0'') T1,
                        --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID = ''1''
                          GROUP BY T.REPORT_ID) T2,
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE (T.CODE = ''ClaimWf001_08'' OR
                                T.CODE = ''ClaimWfPayment_Success'')
                          GROUP BY T.REPORT_ID) T3
                  WHERE T1.REPORT_ID = T2.REPORT_ID
                    AND T1.REPORT_ID = T3.REPORT_ID) T,
                E_BDS_CLAIMS_CLAIMS CLAIMS
        --支付号
          LEFT JOIN (SELECT TTF.REPORT_ID,
                            TO_DATE(MAX(TR.PAYSENDDATE), ''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
                       FROM E_BDS_CLAIMS_FINANCE_PAYMENT  TP,
                            E_BDS_CLAIMS_FINANCE_RESPONSE TR,
                            E_BDS_CLAIMS_TASK_TO_FINANCE  TTF
                      WHERE TP.BIDUIID = TR.BIDUIID
                        AND TTF.BIDUIID = TP.BIDUIID
                        AND TTF.BIDUIID = TR.BIDUIID
                        AND TTF.STATUS = ''2''
                        AND TR.TRANSSTATE = ''2''
                      GROUP BY TTF.REPORT_ID) PAYINFO
            ON PAYINFO.REPORT_ID = CLAIMS.REPORT_ID

        --支付号
          LEFT JOIN (SELECT REPORT_ID,
                            MAX(PAYEE.PAY_SEND_TIME) AS PAY_DATE　FROM 
        (SELECT DISTINCT REPORT_ID,
                          SUBSTR(ENTITY,
                                 1,
                                 1) ||
                          REQNO AS PAY_NO,
                          FPAY.*
            FROM E_BDS_CLAIMS_PAYMENT
            LEFT JOIN E_BDS_CLAIMS_FIN_PAY_HIS FPAY --初始化
              ON SUBSTR(ENTITY,
                        1,
                        1) ||
                 REQNO =
                 FPAY.PAY_NO
                                                                        WHERE REQNO IS NOT NULL) PAYEE GROUP BY REPORT_ID) PAYEE
            ON PAYEE.REPORT_ID = CLAIMS.REPORT_ID

          LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
            ON NOT_PAY.REPORT_ID = CLAIMS.REPORT_ID
        --是否包含豁免、养育
          LEFT JOIN (SELECT CA.REPORT_ID, COUNT(1) AS HAS_HY
                       FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                      WHERE (CA.CONCLUSION = ''05'' OR CA.CONCLUSION = ''06'')
                        AND CA.HIS_TAG = ''0''
                      GROUP BY CA.REPORT_ID) HY
            ON HY.REPORT_ID = CLAIMS.REPORT_ID

        --是否包含拒赔
          LEFT JOIN (SELECT CA.REPORT_ID, COUNT(1) AS HAS_REFU
                       FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                      WHERE (CA.CONCLUSION IN (''07'', ''08'', ''09'', ''10''))
                        AND CA.HIS_TAG = ''0''
                      GROUP BY CA.REPORT_ID) REFU
            ON REFU.REPORT_ID = CLAIMS.REPORT_ID

         WHERE T.REPORT_ID = CLAIMS.REPORT_ID
           AND CLAIMS.STATUS = ''8''
           AND CLAIMS.SUB_STATUS = ''0'') M

 WHERE TRUNC(M.PAY_TIME) BETWEEN TO_DATE('''||V_EVAL_STARTTIME||''', ''YYYY-MM-DD'') AND
       TO_DATE('''||V_EVAL_ENDTIME||''', ''YYYY-MM-DD'')
   AND HAS_REFU = ''0''
                       )) CA3
         GROUP BY CA3.REPORT_ID, CA3.POLICY_NO) M
LEFT JOIN (SELECT REPORT_ID,
                           POLICY_NO,
                           SUM(FINAL_ADJ_AMOUNT) AS FINAL_ADJ_AMOUNT,
                           SUM(CHANGE_AMOUNT) AS CHANGE_AMOUNT
                      FROM (SELECT E_BDS_CLAIMS_DUTY.REPORT_ID REPORT_ID,
                                   E_BDS_CLAIMS_DUTY.POLICY_NO POLICY_NO,
                                   E_BDS_CLAIMS_DUTY_DETAIL.HCRTABLE INSURANCE_CODE,
                                   SUM(NVL(E_BDS_CLAIMS_DUTY_DETAIL.ACTVALUE, ''0'') +
                                       NVL(E_BDS_CLAIMS_DUTY_DETAIL.EMV, ''0'') +
                                       NVL(E_BDS_CLAIMS_DUTY_DETAIL.ZDISCAMT, ''0'')) FINAL_ADJ_AMOUNT,
                                   SUM(NVL(E_BDS_CLAIMS_DUTY_DETAIL.ZDISCAMT, ''0'')) AS CHANGE_AMOUNT
                              FROM E_BDS_CLAIMS_DUTY
                              LEFT JOIN E_BDS_CLAIMS_DUTY_DETAIL
                                ON E_BDS_CLAIMS_DUTY.SERNO = E_BDS_CLAIMS_DUTY_DETAIL.DUTY_SERNO
                             WHERE E_BDS_CLAIMS_DUTY.COMPUTE_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.ASS_CLAIMS_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.INSURANCE_CODE IS NULL
                             GROUP BY E_BDS_CLAIMS_DUTY.REPORT_ID,
                                      E_BDS_CLAIMS_DUTY.POLICY_NO,
                                      E_BDS_CLAIMS_DUTY_DETAIL.HCRTABLE
                            UNION ALL
                            SELECT E_BDS_CLAIMS_DUTY.REPORT_ID REPORT_ID,
                                   E_BDS_CLAIMS_DUTY.POLICY_NO POLICY_NO,
                                   E_BDS_CLAIMS_DUTY.INSURANCE_CODE INSURANCE_CODE,
                                   NVL(E_BDS_CLAIMS_DUTY.FINAL_ADJ_AMOUNT, ''0'') FINAL_ADJ_AMOUNT,
                                   NVL(E_BDS_CLAIMS_DUTY.ADJUSTMENT_AMOUNT, ''0'') AS CHANGE_AMOUNT
                              FROM E_BDS_CLAIMS_DUTY
                             WHERE E_BDS_CLAIMS_DUTY.COMPUTE_FLAG = ''0''
                               AND E_BDS_CLAIMS_DUTY.ASS_CLAIMS_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.INSURANCE_CODE IS NOT NULL) DUTY
                     GROUP BY REPORT_ID, POLICY_NO) PAY
            ON PAY.REPORT_ID = M.REPORT_ID
           AND PAY.POLICY_NO = M.POLICY_NO
          LEFT JOIN (SELECT INSURANCE.REPORT_ID,
                           INSURANCE.POLICY_NO,
                           SUM(INSURANCE.INSURED_AMOUNT) AS INSURANCE_AMOUNT,
                           SUM(INSURANCE.EXPECTED_AMOUNT) AS EXPECTED_AMOUNT
                      FROM E_BDS_CLAIMS_INSURANCE INSURANCE
                     WHERE INSURANCE.ASS_CLAIMS_FLAG = ''1''
                     GROUP BY INSURANCE.REPORT_ID, INSURANCE.POLICY_NO) INS
            ON INS.REPORT_ID = M.REPORT_ID
           AND INS.POLICY_NO = M.POLICY_NO) T
           LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = T.T3
  WHERE 理赔结论 NOT IN  (''部分给付'',''通融给付'',''协议给付'',''保费豁免'',''拒付'', ''解约退现价、拒赔'',
  ''解约退保费、拒赔'',''解约不退费、拒赔'')
  GROUP BY B.PROVINCECOMCODE
  ORDER BY B.PROVINCECOMCODE';
dbms_output.put_Line(V_SQL);
  EXECUTE IMMEDIATE V_SQL;

  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
    INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08012',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08011''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期所有赔案的未决估计赔款与已决赔款差值之和
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
         SELECT ''OR08011'',
                 B.PROVINCECOMCODE,
                 NULL,
                 NVL(SUM(CHANGE_AMOUNT),0),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
          FROM (SELECT
        --最终给付金额
         PAY.FINAL_ADJ_AMOUNT,
         --预估金额金额
         INS.EXPECTED_AMOUNT,
         --保额
         INS.INSURANCE_AMOUNT,
         --调整金额
         ABS(PAY.CHANGE_AMOUNT) CHANGE_AMOUNT,
         --调整之前金额  最终给付金额-调整金额
         (PAY.FINAL_ADJ_AMOUNT - PAY.CHANGE_AMOUNT) AS BEFORE_AMOUNT,
         --T3
         SUBSTR(M.POLICY_NO, 1, 1) AS T3,
         --报案号
         M.REPORT_ID,
         --保单号
         M.POLICY_NO,
         --理赔结论代码
         CONCLUSION,
         (SELECT P.PAR_VALUE
            FROM E_BDS_CLAIMS_PARAM P
           WHERE P.PAR_TYPE LIKE ''%pa_claims_conclusion%''
             AND P.PAR_KEY = CONCLUSION) AS "理赔结论"
          FROM (SELECT CA3.REPORT_ID,
               CA3.POLICY_NO,
                --listagg(ca3.conclusion,'','') WITHIN GROUP (ORDER BY ca3.conclusion)
                ltrim(fun_to_irr(CAST(COLLECT(ca3.conclusion) AS tab_irr)),'','')
               --WM_CONCAT(CA3.CONCLUSION)
               AS CONCLUSION

          FROM (SELECT DISTINCT CA2.*
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                  LEFT JOIN ( --赔付险种及结论
                            SELECT CA1.REPORT_ID,
                                    CA1.POLICY_NO,
                                   --listagg(ca1.conclusion,'','') WITHIN GROUP (ORDER BY ca1.conclusion)
                                   --  WM_CONCAT(CA1.CONCLUSION)
                                   ltrim(fun_to_irr(CAST(COLLECT(ca1.conclusion) AS tab_irr)),'','')
                                     AS CONCLUSION
                              FROM (SELECT DISTINCT CA.REPORT_ID,
                                                     CA.POLICY_NO,
                                                     CA.CONCLUSION
                                       FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                                      WHERE CA.HIS_TAG = ''0''
                                        AND CA.CONCLUSION IN
                                            (''01'', ''02'', ''03'', ''04'')) CA1
                             GROUP BY CA1.REPORT_ID, CA1.POLICY_NO) CA2
                    ON CA.REPORT_ID = CA2.REPORT_ID
                   AND CA.POLICY_NO = CA2.POLICY_NO
                 WHERE CA.HIS_TAG = ''0''
                   AND CA.CONCLUSION IN (''01'', ''02'', ''03'', ''04'')
                   AND CA.INSURANCE_CODE IN
                       (SELECT T.INSURANCE_CODE
                          FROM E_BDS_CLAIMS_LIABILITY T
                         WHERE T.PRODUCT_TYPE IN (''健康险'',''意外险''))
                   AND CA.REPORT_ID IN
                       (
                       SELECT M.REPORT_ID--, PAY_TIME, HAS_REFU

  FROM (

        SELECT T.REPORT_ID,
                --SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
                -- APP_TIME,
                T.END_TIME,
                (CASE
                  WHEN NOT_PAY.REPORT_ID IS NOT NULL THEN
                   T.END_TIME
                --拒赔类付款时间为空
                  WHEN REFU.HAS_REFU > 0 THEN
                   NULL
                --豁免养育类 付款时间=结案时间
                  WHEN HY.HAS_HY > 0 THEN
                   T.END_TIME

                  WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL THEN
                   PAYINFO.MAX_PAY_SEND_TIME
                  WHEN PAYEE.PAY_DATE IS NOT NULL THEN
                   PAYEE.PAY_DATE
                  ELSE
                   NULL
                END) AS PAY_TIME,
                ---1 存在拒赔、0不存在拒赔
                (CASE
                  WHEN REFU.HAS_REFU > 0 THEN
                   1
                  ELSE
                   0
                END) AS HAS_REFU

          FROM (SELECT DISTINCT T1.REPORT_ID --, T2.APP_TIME
                                ,
                                 T3.END_TIME
                   FROM (

                         SELECT DISTINCT T.REPORT_ID
                           FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                          WHERE /* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                                            AND */
                          T.HIS_TAG = ''0'') T1,
                        --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID = ''1''
                          GROUP BY T.REPORT_ID) T2,
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE (T.CODE = ''ClaimWf001_08'' OR
                                T.CODE = ''ClaimWfPayment_Success'')
                          GROUP BY T.REPORT_ID) T3
                  WHERE T1.REPORT_ID = T2.REPORT_ID
                    AND T1.REPORT_ID = T3.REPORT_ID) T,
                E_BDS_CLAIMS_CLAIMS CLAIMS
        --支付号
          LEFT JOIN (SELECT TTF.REPORT_ID,
                            TO_DATE(MAX(TR.PAYSENDDATE), ''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
                       FROM E_BDS_CLAIMS_FINANCE_PAYMENT  TP,
                            E_BDS_CLAIMS_FINANCE_RESPONSE TR,
                            E_BDS_CLAIMS_TASK_TO_FINANCE  TTF
                      WHERE TP.BIDUIID = TR.BIDUIID
                        AND TTF.BIDUIID = TP.BIDUIID
                        AND TTF.BIDUIID = TR.BIDUIID
                        AND TTF.STATUS = ''2''
                        AND TR.TRANSSTATE = ''2''
                      GROUP BY TTF.REPORT_ID) PAYINFO
            ON PAYINFO.REPORT_ID = CLAIMS.REPORT_ID

        --支付号
          LEFT JOIN (SELECT REPORT_ID,
                            MAX(PAYEE.PAY_SEND_TIME) AS PAY_DATE　FROM 
        (SELECT DISTINCT REPORT_ID,
                          SUBSTR(ENTITY,
                                 1,
                                 1) ||
                          REQNO AS PAY_NO,
                          FPAY.*
            FROM E_BDS_CLAIMS_PAYMENT
            LEFT JOIN E_BDS_CLAIMS_FIN_PAY_HIS FPAY --初始化
              ON SUBSTR(ENTITY,
                        1,
                        1) ||
                 REQNO =
                 FPAY.PAY_NO
               WHERE REQNO IS NOT NULL) PAYEE GROUP BY REPORT_ID) PAYEE
            ON PAYEE.REPORT_ID = CLAIMS.REPORT_ID

          LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
            ON NOT_PAY.REPORT_ID = CLAIMS.REPORT_ID
        --是否包含豁免、养育
          LEFT JOIN (SELECT CA.REPORT_ID, COUNT(1) AS HAS_HY
                       FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                      WHERE (CA.CONCLUSION = ''05'' OR CA.CONCLUSION = ''06'')
                        AND CA.HIS_TAG = ''0''
                      GROUP BY CA.REPORT_ID) HY
            ON HY.REPORT_ID = CLAIMS.REPORT_ID

        --是否包含拒赔
          LEFT JOIN (SELECT CA.REPORT_ID, COUNT(1) AS HAS_REFU
                       FROM E_BDS_CLAIMS_CLAIMSAUDIT CA
                      WHERE (CA.CONCLUSION IN (''07'', ''08'', ''09'', ''10''))
                        AND CA.HIS_TAG = ''0''
                      GROUP BY CA.REPORT_ID) REFU
            ON REFU.REPORT_ID = CLAIMS.REPORT_ID

         WHERE T.REPORT_ID = CLAIMS.REPORT_ID
           AND CLAIMS.STATUS = ''8''
           AND CLAIMS.SUB_STATUS = ''0'') M

 WHERE TRUNC(M.PAY_TIME) BETWEEN TO_DATE('''||V_EVAL_STARTTIME||''', ''YYYY-MM-DD'') AND
       TO_DATE('''||V_EVAL_ENDTIME||''', ''YYYY-MM-DD'')
   AND HAS_REFU = ''0''
                       )) CA3
         GROUP BY CA3.REPORT_ID, CA3.POLICY_NO) M
          LEFT JOIN (SELECT REPORT_ID,
                           POLICY_NO,
                           SUM(FINAL_ADJ_AMOUNT) AS FINAL_ADJ_AMOUNT,
                           SUM(CHANGE_AMOUNT) AS CHANGE_AMOUNT
                      FROM (SELECT E_BDS_CLAIMS_DUTY.REPORT_ID REPORT_ID,
                                   E_BDS_CLAIMS_DUTY.POLICY_NO POLICY_NO,
                                   E_BDS_CLAIMS_DUTY_DETAIL.HCRTABLE INSURANCE_CODE,
                                   SUM(NVL(E_BDS_CLAIMS_DUTY_DETAIL.ACTVALUE, ''0'') +
                                       NVL(E_BDS_CLAIMS_DUTY_DETAIL.EMV, ''0'') +
                                       NVL(E_BDS_CLAIMS_DUTY_DETAIL.ZDISCAMT, ''0'')) FINAL_ADJ_AMOUNT,
                                   SUM(NVL(E_BDS_CLAIMS_DUTY_DETAIL.ZDISCAMT, ''0'')) AS CHANGE_AMOUNT
                              FROM E_BDS_CLAIMS_DUTY
                              LEFT JOIN E_BDS_CLAIMS_DUTY_DETAIL
                                ON E_BDS_CLAIMS_DUTY.SERNO = E_BDS_CLAIMS_DUTY_DETAIL.DUTY_SERNO
                             WHERE E_BDS_CLAIMS_DUTY.COMPUTE_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.ASS_CLAIMS_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.INSURANCE_CODE IS NULL
                             GROUP BY E_BDS_CLAIMS_DUTY.REPORT_ID,
                                      E_BDS_CLAIMS_DUTY.POLICY_NO,
                                      E_BDS_CLAIMS_DUTY_DETAIL.HCRTABLE
                            UNION ALL
                            SELECT E_BDS_CLAIMS_DUTY.REPORT_ID REPORT_ID,
                                   E_BDS_CLAIMS_DUTY.POLICY_NO POLICY_NO,
                                   E_BDS_CLAIMS_DUTY.INSURANCE_CODE INSURANCE_CODE,
                                   NVL(E_BDS_CLAIMS_DUTY.FINAL_ADJ_AMOUNT, ''0'') FINAL_ADJ_AMOUNT,
                                   NVL(E_BDS_CLAIMS_DUTY.ADJUSTMENT_AMOUNT, ''0'') AS CHANGE_AMOUNT
                              FROM E_BDS_CLAIMS_DUTY
                             WHERE E_BDS_CLAIMS_DUTY.COMPUTE_FLAG = ''0''
                               AND E_BDS_CLAIMS_DUTY.ASS_CLAIMS_FLAG = ''1''
                               AND E_BDS_CLAIMS_DUTY.INSURANCE_CODE IS NOT NULL) DUTY
                     GROUP BY REPORT_ID, POLICY_NO) PAY
            ON PAY.REPORT_ID = M.REPORT_ID
           AND PAY.POLICY_NO = M.POLICY_NO
          LEFT JOIN (SELECT INSURANCE.REPORT_ID,
                           INSURANCE.POLICY_NO,
                           SUM(INSURANCE.INSURED_AMOUNT) AS INSURANCE_AMOUNT,
                           SUM(INSURANCE.EXPECTED_AMOUNT) AS EXPECTED_AMOUNT
                      FROM E_BDS_CLAIMS_INSURANCE INSURANCE
                     WHERE INSURANCE.ASS_CLAIMS_FLAG = ''1''
                     GROUP BY INSURANCE.REPORT_ID, INSURANCE.POLICY_NO) INS
            ON INS.REPORT_ID = M.REPORT_ID
           AND INS.POLICY_NO = M.POLICY_NO) T
           LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = T.T3
  WHERE 理赔结论 IN (''正常给付'')
  GROUP BY B.PROVINCECOMCODE
  ORDER BY B.PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
   INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08011',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08009''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期已决赔案数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
         SELECT ''OR08009'',
                 PROVINCECOMCODE,
                 NULL,
                 COUNT(DISTINCT M.REPORT_ID),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
SELECT M.REPORT_ID,SUBSTR(T3,1,1) AS T2,T3,ACC.ACC_GET_TIME,APP_TIME,END_TIME,
ROUND((PAY_TIME - APP_TIME) * 60* 24,2)  AS "花费时间（分钟）",PAY_TIME,HAS_REFU
FROM(
SELECT T.REPORT_ID,
SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
       APP_TIME,
       T.END_TIME,
       (CASE
         WHEN NOT_PAY.REPORT_ID IS NOT NULL
           THEN T.END_TIME
               --拒赔类付款时间为空
         WHEN refu.HAS_REFU >0  THEN NULL
           --豁免养育类 付款时间=结案时间
        WHEN HY.HAS_HY >0  THEN T.END_TIME

             WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL
           THEN  PAYINFO.MAX_PAY_SEND_TIME
             WHEN payee.PAY_DATE IS NOT NULL THEN payee.pay_date
             ELSE NULL END) AS PAY_TIME,
               ---1 存在拒赔、0不存在拒赔
              ( CASE WHEN REFU.HAS_REFU >0  THEN 1 ELSE 0 END) AS HAS_REFU

  FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME
 , T3.END_TIME
          FROM (

                SELECT DISTINCT T.REPORT_ID
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                 WHERE/* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                   AND */T.HIS_TAG = ''0'') T1,
                --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID =''1''
                          GROUP BY T.REPORT_ID) T2,
               (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_08''
                 GROUP BY T.REPORT_ID) T3
         WHERE T1.REPORT_ID = T2.REPORT_ID
          AND T1.REPORT_ID = T3.REPORT_ID
         ) T,E_BDS_CLAIMS_CLAIMS CLAIMS
         --支付号
 LEFT JOIN (SELECT TTF.REPORT_ID,TO_DATE(MAX(TR.PAYSENDDATE),''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
  FROM E_BDS_CLAIMS_FINANCE_PAYMENT TP,E_BDS_CLAIMS_FINANCE_RESPONSE TR,E_BDS_CLAIMS_TASK_TO_FINANCE TTF
WHERE TP.BIDUIID=TR.BIDUIID AND TTF.BIDUIID=TP.BIDUIID AND TTF.BIDUIID=TR.BIDUIID
AND TTF.STATUS=''2''
AND TR.TRANSSTATE=''2''
GROUP BY TTF.REPORT_ID
  ) PAYINFO
 ON PAYINFO.REPORT_ID=CLAIMS.REPORT_ID

    --支付号
 LEFT JOIN (SELECT REPORT_ID,MAX(payee.pay_SEND_time) AS PAY_DATE　FROM (
SELECT DISTINCT REPORT_ID,SUBSTR(ENTITY,1,1) || REQNO
 AS PAY_NO,FPAY.* 
 FROM  E_BDS_CLAIMS_PAYMENT
 LEFT JOIN E_BDS_CLAIMS_FINANCIAL_PAY FPAY 
 ON SUBSTR(ENTITY,1,1) || REQNO=FPAY.PAY_NO
  WHERE REQNO IS NOT NULL) PAYEE
  GROUP BY REPORT_ID
  ) PAYEE
 ON PAYEE.REPORT_ID=CLAIMS.REPORT_ID
 LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
 ON NOT_PAY.REPORT_ID=CLAIMS.REPORT_ID
 --是否包含豁免、养育
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_HY
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION = ''05'' OR ca.conclusion=''06'') AND ca.his_tag=''0''
GROUP BY ca.report_id) HY ON HY.REPORT_ID=CLAIMS.REPORT_ID

 --是否包含拒赔
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_REFU
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION  in (''07'',''08'',''09'',''10'')  ) AND ca.his_tag=''0''
GROUP BY ca.report_id) REFU ON REFU.REPORT_ID=CLAIMS.REPORT_ID


               WHERE T.REPORT_ID=CLAIMS.REPORT_ID
               ) M
               --受理时间
               LEFT JOIN (  SELECT T.REPORT_ID, MIN(t.start_time) AS ACC_GET_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_03''
                 GROUP BY T.REPORT_ID) ACC  ON ACC.REPORT_ID=M.REPORT_ID
                 )M
                 LEFT JOIN E_BDS_BIDM_MGECMP B
            ON LAORIGORGCODE = M.T2
             WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') AND has_refu=''0''
 GROUP BY PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
    INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08009',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08008''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期转账支付至被保险人（或受益人）银行账户的赔款件数
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR08008'',
                 B.PROVINCECOMCODE,
                 NULL,
                 COUNT(1),
                  CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
SELECT M.REPORT_ID,SUBSTR(T3,1,1) AS T2,T3,ACC.ACC_GET_TIME,APP_TIME,END_TIME,
ROUND((PAY_TIME - APP_TIME) * 60* 24,2)  AS "花费时间（分钟）",PAY_TIME,HAS_REFU
FROM(
SELECT T.REPORT_ID,
SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
       APP_TIME,
       T.END_TIME,
       (CASE
         WHEN NOT_PAY.REPORT_ID IS NOT NULL
           THEN T.END_TIME
               --拒赔类付款时间为空
         WHEN refu.HAS_REFU >0  THEN NULL
           --豁免养育类 付款时间=结案时间
        WHEN HY.HAS_HY >0  THEN T.END_TIME

             WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL
           THEN  PAYINFO.MAX_PAY_SEND_TIME
             WHEN payee.PAY_DATE IS NOT NULL THEN payee.pay_date
             ELSE NULL END) AS PAY_TIME,
               ---1 存在拒赔、0不存在拒赔
              ( CASE WHEN REFU.HAS_REFU >0  THEN 1 ELSE 0 END) AS HAS_REFU

  FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME
 , T3.END_TIME
          FROM (

                SELECT DISTINCT T.REPORT_ID
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                 WHERE/* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                   AND */T.HIS_TAG = ''0'') T1,
                --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID =''1''
                          GROUP BY T.REPORT_ID) T2,
               (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_08''
                 GROUP BY T.REPORT_ID) T3
         WHERE T1.REPORT_ID = T2.REPORT_ID
          AND T1.REPORT_ID = T3.REPORT_ID
         ) T,E_BDS_CLAIMS_CLAIMS CLAIMS
         --支付号
 LEFT JOIN (SELECT TTF.REPORT_ID,TO_DATE(MAX(TR.PAYSENDDATE),''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
  FROM E_BDS_CLAIMS_FINANCE_PAYMENT TP,E_BDS_CLAIMS_FINANCE_RESPONSE TR,E_BDS_CLAIMS_TASK_TO_FINANCE TTF
WHERE TP.BIDUIID=TR.BIDUIID AND TTF.BIDUIID=TP.BIDUIID AND TTF.BIDUIID=TR.BIDUIID
AND TTF.STATUS=''2''
AND TR.TRANSSTATE=''2''
GROUP BY TTF.REPORT_ID
  ) PAYINFO
 ON PAYINFO.REPORT_ID=CLAIMS.REPORT_ID

    --支付号
 LEFT JOIN (SELECT REPORT_ID,MAX(payee.pay_SEND_time) AS PAY_DATE　FROM (
SELECT DISTINCT REPORT_ID,SUBSTR(ENTITY,1,1) || REQNO
 AS PAY_NO,FPAY.* 
 FROM  E_BDS_CLAIMS_PAYMENT
 LEFT JOIN E_BDS_CLAIMS_FINANCIAL_PAY FPAY 
 ON SUBSTR(ENTITY,1,1) || REQNO=FPAY.PAY_NO
  WHERE REQNO IS NOT NULL) PAYEE
  GROUP BY REPORT_ID
  ) PAYEE
 ON PAYEE.REPORT_ID=CLAIMS.REPORT_ID
 LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
 ON NOT_PAY.REPORT_ID=CLAIMS.REPORT_ID
 --是否包含豁免、养育
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_HY
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION = ''05'' OR ca.conclusion=''06'') AND ca.his_tag=''0''
GROUP BY ca.report_id) HY ON HY.REPORT_ID=CLAIMS.REPORT_ID

 --是否包含拒赔
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_REFU
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION  in (''07'',''08'',''09'',''10'')  ) AND ca.his_tag=''0''
GROUP BY ca.report_id) REFU ON REFU.REPORT_ID=CLAIMS.REPORT_ID


               WHERE T.REPORT_ID=CLAIMS.REPORT_ID
               ) M
               --受理时间
               LEFT JOIN (  SELECT T.REPORT_ID, MIN(t.start_time) AS ACC_GET_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_03''
                 GROUP BY T.REPORT_ID) ACC  ON ACC.REPORT_ID=M.REPORT_ID
                 )M
                 LEFT JOIN E_BDS_BIDM_MGECMP B
            ON LAORIGORGCODE = M.T2
             WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') AND has_refu=''0''
 GROUP BY PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
   INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08008',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR08003''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --正常结案数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
 SELECT ''OR08003'',
                 PROVINCECOMCODE,
                 NULL,
                 COUNT(*),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM (
SELECT M.REPORT_ID,SUBSTR(T3,1,1) AS T2,T3,ACC.ACC_GET_TIME,APP_TIME,END_TIME,
ROUND((PAY_TIME - APP_TIME) * 60* 24,2)  AS "花费时间（分钟）",PAY_TIME,HAS_REFU
FROM(
SELECT T.REPORT_ID,
SUBSTR(CLAIMS.ORG_CODE,1,3) AS T3,
       APP_TIME,
       T.END_TIME,
       (CASE
         WHEN NOT_PAY.REPORT_ID IS NOT NULL
           THEN T.END_TIME
               --拒赔类付款时间为空
         WHEN refu.HAS_REFU >0  THEN NULL
           --豁免养育类 付款时间=结案时间
        WHEN HY.HAS_HY >0  THEN T.END_TIME

             WHEN PAYINFO.MAX_PAY_SEND_TIME IS NOT NULL
           THEN  PAYINFO.MAX_PAY_SEND_TIME
             WHEN payee.PAY_DATE IS NOT NULL THEN payee.pay_date
             ELSE NULL END) AS PAY_TIME,
               ---1 存在拒赔、0不存在拒赔
              ( CASE WHEN REFU.HAS_REFU >0  THEN 1 ELSE 0 END) AS HAS_REFU

  FROM (SELECT DISTINCT T1.REPORT_ID, T2.APP_TIME
 , T3.END_TIME
          FROM (

                SELECT DISTINCT T.REPORT_ID
                  FROM E_BDS_CLAIMS_CLAIMSAUDIT T
                 WHERE/* T.CONCLUSION IN (''01'',''02'',''03'',''04'')
                   AND */T.HIS_TAG = ''0'') T1,
                --T2审批时间增加小额自动理赔 add by jason 20171009
                        (SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T
                          WHERE T.CODE = ''ClaimWf001_06''
                          GROUP BY T.REPORT_ID
                         UNION
                         SELECT T.REPORT_ID, MAX(T.END_TIME) AS APP_TIME
                           FROM E_BDS_CLAIMS_TASKTIME T, E_BDS_CLAIMS_SMALL_AUTO_CLAIMS SAC
                          WHERE T.CODE = ''ClaimWf001_08''
                            AND T.REPORT_ID = SAC.REPORT_ID
                            AND SAC.IS_VALID =''1''
                          GROUP BY T.REPORT_ID) T2,
               (SELECT T.REPORT_ID, MAX(T.END_TIME) AS END_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_08''
                 GROUP BY T.REPORT_ID) T3
         WHERE T1.REPORT_ID = T2.REPORT_ID
          AND T1.REPORT_ID = T3.REPORT_ID
         ) T,E_BDS_CLAIMS_CLAIMS CLAIMS
         --支付号
 LEFT JOIN (SELECT TTF.REPORT_ID,TO_DATE(MAX(TR.PAYSENDDATE),''YYYYMMDDHH24MISS'') AS MAX_PAY_SEND_TIME
  FROM E_BDS_CLAIMS_FINANCE_PAYMENT TP,E_BDS_CLAIMS_FINANCE_RESPONSE TR,E_BDS_CLAIMS_TASK_TO_FINANCE TTF
WHERE TP.BIDUIID=TR.BIDUIID AND TTF.BIDUIID=TP.BIDUIID AND TTF.BIDUIID=TR.BIDUIID
AND TTF.STATUS=''2''
AND TR.TRANSSTATE=''2''
GROUP BY TTF.REPORT_ID
  ) PAYINFO
 ON PAYINFO.REPORT_ID=CLAIMS.REPORT_ID

    --支付号
 LEFT JOIN (SELECT REPORT_ID,MAX(payee.pay_SEND_time) AS PAY_DATE　FROM (
SELECT DISTINCT REPORT_ID,SUBSTR(ENTITY,1,1) || REQNO
 AS PAY_NO,FPAY.* 
 FROM  E_BDS_CLAIMS_PAYMENT
 LEFT JOIN E_BDS_CLAIMS_FINANCIAL_PAY FPAY 
 ON SUBSTR(ENTITY,1,1) || REQNO=FPAY.PAY_NO
  WHERE REQNO IS NOT NULL) PAYEE
  GROUP BY REPORT_ID
  ) PAYEE
 ON PAYEE.REPORT_ID=CLAIMS.REPORT_ID
 LEFT JOIN E_BDS_CLAIMS_LOAN_NOT_PAY NOT_PAY
 ON NOT_PAY.REPORT_ID=CLAIMS.REPORT_ID
 --是否包含豁免、养育
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_HY
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION = ''05'' OR ca.conclusion=''06'') AND ca.his_tag=''0''
GROUP BY ca.report_id) HY ON HY.REPORT_ID=CLAIMS.REPORT_ID

 --是否包含拒赔
 LEFT JOIN
 (SELECT  ca.report_id,COUNT(1) AS HAS_REFU
 FROM E_BDS_CLAIMS_CLAIMSAUDIT CA WHERE (CA.CONCLUSION  in (''07'',''08'',''09'',''10'')  ) AND ca.his_tag=''0''
GROUP BY ca.report_id) REFU ON REFU.REPORT_ID=CLAIMS.REPORT_ID


               WHERE T.REPORT_ID=CLAIMS.REPORT_ID
               ) M
               --受理时间
               LEFT JOIN (  SELECT T.REPORT_ID, MIN(t.start_time) AS ACC_GET_TIME
                  FROM E_BDS_CLAIMS_TASKTIME T
                 WHERE T.CODE = ''ClaimWf001_03''
                 GROUP BY T.REPORT_ID) ACC  ON ACC.REPORT_ID=M.REPORT_ID
                 )M
                 LEFT JOIN E_BDS_BIDM_MGECMP  B
            ON LAORIGORGCODE = M.T2
             WHERE TRUNC(M.END_TIME) BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') AND has_refu=''0''
 GROUP BY PROVINCECOMCODE
 ORDER BY PROVINCECOMCODE
 ';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  -----------------------------------开始加载日志信息--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
   INSERT INTO ETL_LOG_DETAIL
    (ID,
     INDEX_ID,
     SP_NAME,
     TABLE_NAME,
     START_TIME,
     END_TIME,
     DATA_DATE,
     RECORD_NUM,
     ERR_CODE,
     ERR_MSG)
  VALUES
    (SYS_GUID(),
     'OR08003',
     'SP_MDS_HASLCLAIMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------异常处理-------------------------------
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_END     := SYSDATE;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
     INSERT INTO ETL_LOG_DETAIL
        (ID,
         INDEX_ID,
         SP_NAME,
         TABLE_NAME,
         START_TIME,
         END_TIME,
         DATA_DATE,
         RECORD_NUM,
         ERR_CODE,
         ERR_MSG)
      VALUES
        (SYS_GUID(),
         '',
         'SP_MDS_HASLCLAIMS',
         'E_MDS_IRR_INDEX_VALUE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
    -------------------------------加载日志信息完成-------------------------
    END;
END SP_MDS_HASLCLAIMS;
/
