CREATE OR REPLACE PROCEDURE SP_MDS_HASLCSBPM(V_INDATE  IN VARCHAR2,
                                         V_RTNCODE OUT VARCHAR2,
                                          V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_HASLCSBPM                                  */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-11-02                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_EVAL_NEXT_STARTTIME VARCHAR2(10); --上个季度末月第一天
  V_EVAL_END_STARTTIME  VARCHAR2(10); --上个季度末月最后一天
  V_EVAL_ENDTIME_STARTTIME VARCHAR2(10); --上季度月初第一天
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  DBMS_OUTPUT.ENABLE(100000);
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  V_EVAL_NEXT_STARTTIME  := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),'yyyymmdd')+1,-4),'yyyymmdd');
  V_EVAL_END_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd'),-3),'yyyymmdd');
  V_EVAL_ENDTIME_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd')+1,-6),'yyyymmdd'); 
  --清除E_BDS_CSBPM_CUSTNO_TEMP表数据
  BEGIN
  EXECUTE IMMEDIATE 'truncate table E_BDS_CSBPM_CUSTNO_TEMP';
  EXCEPTION WHEN OTHERS THEN 
    NULL;
  END;
 V_START := SYSDATE;
  --向E_BDS_CSBPM_CUSTNO_TEMP表插入数据
 V_SQL:='INSERT INTO E_BDS_CSBPM_CUSTNO_TEMP(APP_CUSTOMERNO,ADD_ORGCOD,ADD_CHNLCOD)
 SELECT APP_CUSTOMERNO, ADD_ORGCOD,ADD_CHNLCOD
          FROM (SELECT A.APP_CUSTOMERNO,
                       A.ADD_ORGCOD,
                       A.ADD_CHNLCOD,
                       ROW_NUMBER() OVER(PARTITION BY A.APP_CUSTOMERNO ORDER BY A.CHDRCOY ASC) NUM
                  FROM (SELECT DISTINCT D.APP_CUSTOMERNO,
                                        P.ADD_ORGCOD,
                                        p.ADD_CHNLCOD,
                                        P.CHDRCOY
                          FROM (SELECT D.APP_CUSTOMERNO, D.INNER_POLNO
                                  FROM E_BDS_BIDW_POL_PERSONER_DETAIL D
                                UNION ALL
                                SELECT D.ISS_CUSTOMERNO, D.INNER_POLNO
                                  FROM E_BDS_BIDW_POL_PERSONER_DETAIL D
                                UNION ALL
                                SELECT D.BNF_CUSTOMERNO, D.INNER_POLNO
                                  FROM E_BDS_BIDW_POL_PERSONER_DETAIL D
                                UNION ALL
                                SELECT D.EB_CUSTOMERNO, D.INNER_POLNO
                                  FROM E_BDS_BIDW_POL_PERSONER_DETAIL D) D
                            JOIN   E_BDS_BIDW_LA_F_POLICY P
                         ON D.INNER_POLNO = P.INNER_POLNO) A) C
         WHERE C.NUM = 1';
 EXECUTE IMMEDIATE V_SQL;
    COMMIT;
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_END        := SYSDATE;
  /**************************************** 开始加载日志信息 ****************************************/
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
     'SP_MDS_HASLCSBPM',
     'E_BDS_CSBPM_CUSTNO_TEMP',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
   --清除E_BDS_CSBPM_CUST_ORG_TEMP表数据
  BEGIN
  EXECUTE IMMEDIATE 'truncate table E_BDS_CSBPM_CUST_ORG_TEMP';
  EXCEPTION WHEN OTHERS THEN 
    NULL;
  END;
  V_START := SYSDATE;
   --向E_BDS_CSBPM_CUST_ORG_TEMP表插入数据
 V_SQL:='INSERT INTO E_BDS_CSBPM_CUST_ORG_TEMP
(PROVINCECOMCODE, CLTNUM, STATUS, POLICY_NOS,
  SERVICE_NAME,CHANNEL,START_DATE, START_TIME,MINDATE)
SELECT B.PROVINCECOMCODE, CLTNUM, STATUS, POLICY_NOS,
  SERVICE_NAME,CHANNEL,START_DATE, START_TIME,MINDATE
    FROM   E_BDS_BIDM_MGECMP B
   RIGHT JOIN  (SELECT
          --机构号
           SUBSTR(T.DID, 0, 1) COMPANY,
           T.FIN,
           T.BUS_SOURCE,
           --状态
           T.STATUS,
           T.CHANNEL,
           T.SERVICE_NAME,
          CASE WHEN  PAYLIST.MINDATE
             IS NULL THEN FIN
               ELSE  PAYLIST.MINDATE
          END MINDATE,
           TO_CHAR(T.START_DATE,''YYYYMMDD'')  START_DATE,
           --开始时间
         TO_CHAR(T.START_DATE, ''HH24:MI:SS'') START_TIME,
           --保单号
           T.POLICY_NOS,
           --客户号
           (CASE T.SERVICE_ID
             WHEN ''105'' THEN
              (SELECT M.CUSTOMER_ID
                 FROM E_BDS_CSBPM_CS_105_MAIN M
                WHERE M.CHANGE_ID = T.CHANGE_ID
                  AND M.SUB_CHANGE_ID = T.SUB_CHANGE_ID)
             WHEN ''107'' THEN
              (SELECT M.CUSTOMER_ID
                 FROM E_BDS_CSBPM_CS_107_MAIN M
                WHERE M.CHANGE_ID = T.CHANGE_ID
                  AND M.SUB_CHANGE_ID = T.SUB_CHANGE_ID)
             ELSE
               --NULL
              (SELECT CUSTOMER_ID FROM E_BDS_CSBPM_POLICY_SERVICE T1 WHERE T1.CHANGE_ID = T.CHANGE_ID AND ROWNUM <2 )
           END) AS CLTNUM,
           --审核通过日期
           TRUNC((CASE
                   WHEN SERVICE_ID = ''804'' THEN
                    CHECKER_SUBMIT_TIME
                   WHEN SERVICE_ID IN (SELECT T.PARAM_CODE
                                         FROM E_BDS_CSBPM_PARAM T
                                        WHERE T.PARAM_VALUE = ''AUTO''
                                          AND T.USING = ''1'') THEN
                    T.START_DATE
                   WHEN SERVICE_ID = ''101'' THEN
                    (CASE
                      WHEN P_ACC.OPERATOR_TIME > T.FIN THEN
                       T.FIN
                      ELSE
                       P_ACC.OPERATOR_TIME
                    END)
                   WHEN SERVICE_ID = ''917'' THEN
                    DD.SEND_DD_TIME
                   WHEN DD.SEND_DD_TIME IS NOT NULL THEN
                    (SELECT MAX(OPERATOR_TIME) AS OPERATOR_TIME
                       FROM E_BDS_CSBPM_PROCESSSTEP VER_DD
                      WHERE VER_DD.STEP_NAME = ''verify''
                        AND VER_DD.CHANGE_ID = T.CHANGE_ID
                        AND VER_DD.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                        AND VER_DD.OPERATOR_TIME <= DD.SEND_DD_TIME)
                 --20180913 临时增加
                   WHEN (P_APP.OPERATOR_TIME IS NOT NULL) AND
                        (P_PAY.OPERATOR_TIME <= P_APP.OPERATOR_TIME) THEN
                    P_PAY.OPERATOR_TIME
                   WHEN (P_VER.OPERATOR_TIME IS NOT NULL) AND
                        (P_PAY.OPERATOR_TIME <= P_VER.OPERATOR_TIME) THEN
                    P_PAY.OPERATOR_TIME
                   WHEN P_APP.OPERATOR_TIME IS NOT NULL THEN
                    P_APP.OPERATOR_TIME
                   WHEN P_VER.OPERATOR_TIME IS NOT NULL THEN
                    P_VER.OPERATOR_TIME
                 END)) AS AUDIT_PASS_DATE
            FROM (SELECT T2.DID,
                         (SELECT P.PARAM_VALUE
                            FROM E_BDS_CSBPM_PARAM P
                           WHERE P.PARAM_TYPE = ''busSourceAll''
                             AND P.PARAM_CODE = T2.BUS_SOURCE) BUS_SOURCE,
                         (SELECT MIN(OPERATOR_TIME)
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE CHANGE_ID = T1.CHANGE_ID
                             AND STEP_NAME = ''accept'') ACC,
                         (SELECT MAX(OPERATOR_TIME)
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE CHANGE_ID = T1.CHANGE_ID
                             AND STEP_NAME = ''accept'') ACC_MAX_TIME,
                         ((CASE
                           WHEN VERIFY_RTN.OPERATOR_TIME IS NULL THEN
                            (SELECT MAX(OPERATOR_TIME)
                               FROM E_BDS_CSBPM_PROCESSSTEP
                              WHERE CHANGE_ID = T1.CHANGE_ID
                                AND STEP_NAME = ''verify''
                                AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID)
                           ELSE
                            ((SELECT MAX(OPERATOR_TIME)
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE T.CHANGE_ID = T1.CHANGE_ID
                                 AND T.STEP_NAME = ''verify''
                                 AND T.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                 AND T.OPERATOR_TIME < VERIFY_RTN.OPERATOR_TIME))
                         END)) VERIFY_MAX_TIME,
                         ((CASE
                           WHEN APP_RTN.OPERATOR_TIME IS NULL THEN
                            (SELECT MAX(OPERATOR_TIME)
                               FROM E_BDS_CSBPM_PROCESSSTEP
                              WHERE CHANGE_ID = T1.CHANGE_ID
                                AND STEP_NAME = ''approval''
                                AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID)
                           ELSE
                            ((SELECT MAX(OPERATOR_TIME)
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE T.CHANGE_ID = T1.CHANGE_ID
                                 AND T.STEP_NAME = ''approval''
                                 AND T.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                 AND T.OPERATOR_TIME < APP_RTN.OPERATOR_TIME))
                         END)) APP_MAX_TIME,
                         (SELECT MAX(OPERATOR_TIME)
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE CHANGE_ID = T1.CHANGE_ID
                             AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                             AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                             AND STEP_NAME IN (''end'', ''cancel'')) FIN,
                         T1.CHANGE_ID,
                         T1.SUB_CHANGE_ID,
                         T1.CUSTOMER_ID,
                         T1.SERVICE_NAME,
                         T1.SERVICE_ID,
                         (SELECT MIN(OPERATOR_TIME)
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE CHANGE_ID = T1.CHANGE_ID) START_DATE,
                         (SELECT NVL(PARAM_VALUE, T1.STATUS)
                            FROM E_BDS_CSBPM_PARAM
                           WHERE PARAM_TYPE = ''POST''
                             AND T1.STATUS = PARAM_CODE) STATUS,
                         T1.POLICY_NOS,
                         DECODE(T1.CHANNEL, ''GB'', ''GP'', T1.CHANNEL) CHANNEL,
                         CHECKER.CHECKER_SUBMIT_TIME
                    FROM E_BDS_CSBPM_POLICY_SERVICE T1
                    LEFT JOIN (SELECT T.CHANGE_ID,
                                     T.SUB_CHANGE_ID,
                                     MIN(OPERATOR_TIME) AS OPERATOR_TIME
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE (T.ACTION_NAME = ''复核等待岗退回'' OR
                                     T.ACTION_NAME = ''自动任务返回'')
                                 AND T.STEP_NAME = ''verify''
                               GROUP BY T.CHANGE_ID, T.SUB_CHANGE_ID) VERIFY_RTN
                      ON VERIFY_RTN.CHANGE_ID = T1.CHANGE_ID
                     AND VERIFY_RTN.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                    LEFT JOIN (SELECT T.CHANGE_ID,
                                     T.SUB_CHANGE_ID,
                                     MIN(OPERATOR_TIME) AS OPERATOR_TIME
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE (T.ACTION_NAME = ''复核等待岗退回'' OR
                                     T.ACTION_NAME = ''自动任务返回'')
                                 AND T.STEP_NAME = ''approval''
                               GROUP BY T.CHANGE_ID, T.SUB_CHANGE_ID) APP_RTN
                      ON APP_RTN.CHANGE_ID = T1.CHANGE_ID
                     AND APP_RTN.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                  --20180705增加核保时间
                    LEFT JOIN (SELECT T.CHANGE_ID,
                                     T.SUB_CHANGE_ID,
                                     MAX(T.OPERATOR_TIME) AS CHECKER_SUBMIT_TIME
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE T.STEP_NAME = ''checker''
                               GROUP BY T.CHANGE_ID, T.SUB_CHANGE_ID) CHECKER
                      ON CHECKER.CHANGE_ID = T1.CHANGE_ID
                     AND CHECKER.SUB_CHANGE_ID = T1.SUB_CHANGE_ID,
                   E_BDS_CSBPM_POLICY_CHANGE T2,
                   (SELECT CHANGE_ID, MIN(OPERATOR_TIME) OPERATOR_TIME
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE OPERATOR_TIME IS NOT NULL
                           GROUP BY CHANGE_ID) T3
                   WHERE T1.CHANGE_ID = T2.CHANGE_ID
                     AND T1.CHANGE_ID = T3.CHANGE_ID
                     --AND T1.STATUS IN (''end'', ''cancel'')
                        --限定保全项目
                     AND T1.SERVICE_ID IN
                         (SELECT T.PARAM_CODE
                            FROM E_BDS_CSBPM_PARAM T
                           WHERE T.PARAM_VALUE IN (''AUTO'', ''OUTER''))) T
            LEFT JOIN (SELECT MAX(P.OPERATOR_NAME) AS OPERATOR_NAME,
                             P.CHANGE_ID,
                             MAX(P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME = ''accept''
                         --AND P.ACTION_VERSION = ''1''
                       GROUP BY P.STEP_NAME, P.CHANGE_ID) P_ACC
              ON P_ACC.CHANGE_ID = T.CHANGE_ID
             AND P_ACC.OPERATOR_TIME = T.ACC_MAX_TIME
            LEFT JOIN (SELECT (P.OPERATOR_NAME) AS OPERATOR_NAME,
                             P.CHANGE_ID,
                             P.SUB_CHANGE_ID,
                             (P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME = ''verify'') P_VER
              ON P_VER.CHANGE_ID = T.CHANGE_ID
             AND P_VER.SUB_CHANGE_ID = T.SUB_CHANGE_ID
             AND P_VER.OPERATOR_TIME = T.VERIFY_MAX_TIME
            LEFT JOIN (SELECT (P.OPERATOR_NAME) AS OPERATOR_NAME,
                             P.CHANGE_ID,
                             P.SUB_CHANGE_ID,
                             (P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME = ''approval'') P_APP
              ON P_APP.CHANGE_ID = T.CHANGE_ID
             AND P_APP.SUB_CHANGE_ID = T.SUB_CHANGE_ID
             AND P_APP.OPERATOR_TIME = T.APP_MAX_TIME
          --20180913 临时增加，解决审核通过时间系统记录有误的问题
            LEFT JOIN (SELECT P.CHANGE_ID,
                             P.SUB_CHANGE_ID,
                             MIN(P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME IN
                             (''payWait'', ''paymentWait'', ''H355H208Wait'')
                       GROUP BY P.CHANGE_ID, P.SUB_CHANGE_ID) P_PAY
              ON P_PAY.CHANGE_ID = T.CHANGE_ID
             AND P_PAY.SUB_CHANGE_ID = T.SUB_CHANGE_ID
            LEFT JOIN (SELECT DD.CHANGE_ID,
                             DD.SUB_CHANGE_ID,
                             MIN(DD.CREATE_TIME) AS SEND_DD_TIME
                        FROM E_BDS_CSBPM_SERVICE_DD DD
                       GROUP BY DD.CHANGE_ID, DD.SUB_CHANGE_ID) DD
              ON DD.CHANGE_ID = T.CHANGE_ID
             AND DD.SUB_CHANGE_ID = T.SUB_CHANGE_ID
          --进入支付队列时间
            LEFT JOIN (SELECT PAY.CHANGE_ID,
                             PAY.SUB_CHANGE_ID,
                             MIN(PAY.CREATE_TIME) AS MINDATE
                        FROM E_BDS_CSBPM_SERVICE_PAY PAY
                       GROUP BY PAY.CHANGE_ID, PAY.SUB_CHANGE_ID) PAYLIST
              ON PAYLIST.CHANGE_ID = T.CHANGE_ID
             AND PAYLIST.SUB_CHANGE_ID = T.SUB_CHANGE_ID
			 LEFT JOIN (SELECT PAY.CHANGE_ID,PAY.SUB_CHANGE_ID,MIN(PAY.CREATE_TIME) AS IN_PAY_SEQ_TIME
             FROM E_BDS_CSBPM_SERVICE_PAY PAY GROUP BY PAY.CHANGE_ID,PAY.SUB_CHANGE_ID) PAY_SEQ
             ON PAY_SEQ.CHANGE_ID=t.CHANGE_ID
             AND PAY_SEQ.SUB_CHANGE_ID=t.SUB_CHANGE_ID
			 ) M
			ON COMPANY = B.LAORIGORGCODE
   WHERE SUBSTR(TRUNC(M.START_DATE),1,6) BETWEEN substr(''' ||V_EVAL_NEXT_STARTTIME||''',1,6)
     AND  substr(''' ||V_EVAL_ENDTIME||''',1,6)
     ';
 EXECUTE IMMEDIATE V_SQL;
    COMMIT;
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_END        := SYSDATE;
  /**************************************** 开始加载日志信息 ****************************************/
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
     'SP_MDS_HASLCSBPM',
     'E_BDS_CSBPM_CUST_ORG_TEMP',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --保全处理平均时长
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR06003'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE ;
       COMMIT;
  --插入数据日期数据
 V_START := SYSDATE;
 V_SQL   := '
  INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
SELECT ''OR06003'',
          ''86'',
           null,
           round(SUM(TO_DATE(TO_CHAR(mindate, ''YYYY/MM/DD''), ''YYYY/MM/DD'') -
           TO_DATE(START_DATE, ''YYYY/MM/DD'') +
           (TO_DATE(TO_CHAR(mindate, ''HH24:MI:SS''), ''hh24:mi:ss'') -
            TO_DATE(START_TIME, ''hh24:mi:ss'')) / 24)/COUNT(POLICY_NOS),2),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM E_BDS_CSBPM_CUST_ORG_TEMP t2
WHERE  STATUS NOT IN ( ''撤单'',''废弃'')
AND SERVICE_NAME<>''生存金自动给付''
AND (TO_DATE(to_char(MINDATE,''YYYYMMDD''),''YYYYMMDD'') BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||
             ''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',''YYYYMMDD''))';
  EXECUTE IMMEDIATE V_SQL;
    COMMIT;
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_END        := SYSDATE;
  /**************************************** 开始加载日志信息 ****************************************/
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
     'OR06003',
     'SP_MDS_HASLCSBPM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --评估期保全变更完成件数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04025'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT  ''OR04025'',
           org,
           CHANNEL,
           SUM(custno),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (SELECT PROVINCECOMCODE org,count(T2.policy_nos) custno,T2.CHANNEL
FROM E_BDS_CSBPM_CUSTNO_TEMP t1 JOIN E_BDS_CSBPM_CUST_ORG_TEMP t2
ON t1.APP_CUSTOMERNO = ''9'' || TRIM(t2.CLTNUM)
LEFT JOIN E_BDS_BIDM_MGECMP t3
ON substr(ADD_ORGCOD,1,1)=t3.LAORIGORGCODE
WHERE T2.PROVINCECOMCODE IS NULL
AND T2.STATUS NOT IN ( ''撤单'',''废弃'')
AND T2.SERVICE_NAME<>''生存金自动给付''
AND (TO_DATE(to_char(MINDATE,''YYYYMMDD''),''YYYYMMDD'') BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||
             ''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',''YYYYMMDD''))
GROUP BY PROVINCECOMCODE,T2.CHANNEL
UNION ALL
SELECT PROVINCECOMCODE org,count(T2.policy_nos) custno,T2.CHANNEL
FROM E_BDS_CSBPM_CUST_ORG_TEMP t2
WHERE T2.PROVINCECOMCODE IS NOT NULL
AND T2.STATUS NOT IN ( ''撤单'',''废弃'')
AND T2.SERVICE_NAME<>''生存金自动给付''
AND (TO_DATE(to_char(MINDATE,''YYYYMMDD''),''YYYYMMDD'') BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||
             ''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',''YYYYMMDD''))
GROUP BY PROVINCECOMCODE,T2.CHANNEL
)t
 GROUP BY org,CHANNEL';
EXECUTE IMMEDIATE V_SQL;
COMMIT;
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_END        := SYSDATE;
  /**************************************** 开始加载日志信息 ****************************************/
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
     'OR04025',
     'SP_MDS_HASLCSBPM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --评估期期初保全变更留存件数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04026'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
     SELECT 
        ''OR04026'',
        T.PROVINCECOMCODE,
        T.CHANNEL,
        COUNT(T.POLICY_NOS),
        CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (SELECT  DISTINCT 
       T1.PROVINCECOMCODE AS PROVINCECOMCODE,
       T2.CHANNEL AS CHANNEL,
       T2.POLICY_NOS||T2.Service_Name AS POLICY_NOS
FROM E_BDS_BIDM_MGECMP T1
LEFT JOIN (SELECT 
           PROVINCECOMCODE,
           CHANNEL,
           POLICY_NOS,
           Service_Name
FROM  E_BDS_CSBPM_CUST_ORG_TEMP t2
WHERE T2.PROVINCECOMCODE IS NOT NULL
AND T2.STATUS NOT IN ( ''撤单'',''废弃'')
AND T2.SERVICE_NAME<>''生存金自动给付''
AND T2.START_DATE BETWEEN  '''||V_EVAL_ENDTIME_STARTTIME||''' AND '''||V_EVAL_END_STARTTIME||'''
AND (TO_DATE(to_char(MINDATE,''YYYYMMDD''),''YYYYMMDD'') 
BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||''',''YYYYMMDD''))) T2
ON T1.PROVINCECOMCODE = T2.PROVINCECOMCODE) T
 GROUP BY T.PROVINCECOMCODE,T.CHANNEL';    
  EXECUTE IMMEDIATE V_SQL;
   DBMS_OUTPUT.PUT_LINE(V_SQL);
    COMMIT;
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_END        := SYSDATE;
  /**************************************** 开始加载日志信息 ****************************************/
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
     'OR04026',
     'SP_MDS_HASLCSBPM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --评估期保全变更新增件数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04027'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
 SELECT  ''OR04027'',
           org,
           CHANNEL,
           SUM(custno),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (SELECT PROVINCECOMCODE org,count(T2.policy_nos) custno,T2.CHANNEL
FROM E_BDS_CSBPM_CUSTNO_TEMP t1 JOIN E_BDS_CSBPM_CUST_ORG_TEMP t2
ON t1.APP_CUSTOMERNO = ''9'' || TRIM(t2.CLTNUM)
LEFT JOIN E_BDS_BIDM_MGECMP t3
ON substr(ADD_ORGCOD,1,1)=t3.LAORIGORGCODE
WHERE T2.PROVINCECOMCODE IS NULL
AND T2.STATUS NOT IN ( ''撤单'',''废弃'')
AND T2.SERVICE_NAME<>''生存金自动给付''
AND (TO_DATE(START_DATE,''YYYYMMDD'') BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||
             ''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',''YYYYMMDD''))
GROUP BY PROVINCECOMCODE,T2.CHANNEL
UNION ALL
SELECT PROVINCECOMCODE org,count(T2.policy_nos) custno,T2.CHANNEL
FROM E_BDS_CSBPM_CUST_ORG_TEMP t2
WHERE T2.PROVINCECOMCODE IS NOT NULL
AND T2.STATUS NOT IN ( ''撤单'',''废弃'')
AND T2.SERVICE_NAME<>''生存金自动给付''
AND (TO_DATE(START_DATE,''YYYYMMDD'') BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||
             ''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',''YYYYMMDD''))
GROUP BY PROVINCECOMCODE,T2.CHANNEL
)t
 GROUP BY org,CHANNEL';
  EXECUTE IMMEDIATE V_SQL;
    COMMIT;
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_END        := SYSDATE;
  /**************************************** 开始加载日志信息 ****************************************/
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
     'OR04027',
     'SP_MDS_HASLCSBPM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --评估期操作的确认生效的保全件总量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04034'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
 SELECT  ''OR04034'',
           org,
           CHANNEL,
           SUM(custno),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (SELECT PROVINCECOMCODE org,count(T2.policy_nos) custno,T2.CHANNEL
FROM E_BDS_CSBPM_CUSTNO_TEMP t1 JOIN E_BDS_CSBPM_CUST_ORG_TEMP t2
ON t1.APP_CUSTOMERNO = ''9'' || TRIM(t2.CLTNUM)
LEFT JOIN E_BDS_BIDM_MGECMP t3
ON substr(ADD_ORGCOD,1,1)=t3.LAORIGORGCODE
WHERE T2.PROVINCECOMCODE IS NULL
AND T2.STATUS NOT IN ( ''撤单'',''废弃'')
AND T2.SERVICE_NAME<>''生存金自动给付''
AND (TO_DATE(to_char(MINDATE,''YYYYMMDD''),''YYYYMMDD'') BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||
             ''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',''YYYYMMDD''))
GROUP BY PROVINCECOMCODE,T2.CHANNEL
UNION ALL
SELECT PROVINCECOMCODE org,count(T2.policy_nos) custno,T2.CHANNEL
FROM E_BDS_CSBPM_CUST_ORG_TEMP t2
WHERE T2.PROVINCECOMCODE IS NOT NULL
AND T2.STATUS NOT IN ( ''撤单'',''废弃'')
AND T2.SERVICE_NAME<>''生存金自动给付''
AND (TO_DATE(to_char(MINDATE,''YYYYMMDD''),''YYYYMMDD'') BETWEEN TO_DATE(''' || V_EVAL_STARTTIME ||
             ''',''YYYYMMDD'')
 AND TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',''YYYYMMDD''))
GROUP BY PROVINCECOMCODE,T2.CHANNEL
)t
 GROUP BY org,CHANNEL';
  EXECUTE IMMEDIATE V_SQL;
    COMMIT;
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_RTNCODE    := '0';
  V_RTNMSG     := '执行成功';
  V_END        := SYSDATE;
  /**************************************** 开始加载日志信息 ****************************************/
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
     'OR04034',
     'SP_MDS_HASLCSBPM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  /******************************************** 异常处理 ********************************************/
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
      V_END     := SYSDATE;
      /**************************************** 开始加载日志信息 ****************************************/
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
         'SP_MDS_HASLCSBPM',
         'E_MDS_IRR_INDEX_VALUE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
      /**************************************** 加载日志信息完成 ****************************************/
    END;
END SP_MDS_HASLCSBPM;
/
