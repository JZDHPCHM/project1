CREATE OR REPLACE PROCEDURE SP_MDS_CAPITAL(V_INDATE  IN  VARCHAR2,
                                       V_RTNCODE OUT VARCHAR2,
                                       V_RTNMSG  OUT VARCHAR2) AS
/**********************************************************************/
/* Procedure Name : SP_MDS_CAPITAL                                    */
/* Developed By   : SY                                                */
/* Developed Date : 2018-10-31                                        */
/************************ Souce table *********************************/
/* Target table   : E_MDS_IRR_INDEX_VALUE                             */
/********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10);   --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10);   --评估期结束时间
  V_START          DATE;           --执行开始时间
  V_END            DATE;           --执行结束时间
  V_RECORD_NUM     NUMBER;         --记录条数
  V_SQL            VARCHAR2(2000); --动态语句
/***********************************************************************/
BEGIN
   V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
   V_EVAL_ENDTIME :=  FUN_ETL_Q_DATE(V_INDATE);
--------------------------------删除数据-------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR13007''
   AND TO_CHAR(LOAD_DT,''yyyymmdd'') = '|| V_INDATE ||'';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
--------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  --评估期内退保金
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(  
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)

SELECT ''OR13007'',
       PROVINCECOMCODE,
       REQRESERVED13,
       SUM(A.AMOUNT) amt,
       CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  from E_BDS_ATS_CHOUDAN_TMP a
  LEFT JOIN  E_BDS_BIDM_MGECMP B
  ON SUBSTR(A.APPLYENTITY, 1, 1)=B.LAORIGORGCODE
 where  a.transcode = ''1988'' --付款
   AND A.PAYMADEDATE BETWEEN to_date('''||V_EVAL_STARTTIME||'''||''000000'',''yyyymmddhh24miss'') AND
      to_date( '''||V_EVAL_ENDTIME||'''||''235959'',''yyyymmddhh24miss'') -- 月份
   and a.transstate = ''2'' --支付成功
   and a.transsource = ''CUSTOMER SERVICE''
   and a.reqreserved18 in (''301'', -- 正常退保
                           ''302'', -- 通融退保
                           ''304'', --  附加险退保 
                           ''305'', --  犹豫期内退保  
                           ''306'', --  银行在线CFI 
                           ''307'' -- 银行在线退保
                          )
 GROUP BY PROVINCECOMCODE,REQRESERVED13
 ORDER BY PROVINCECOMCODE,REQRESERVED13
';
dbms_output.put_Line(V_SQL);
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------开始加载日志信息-------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    :='0';
  V_RTNMSG     :='执行成功';
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
     'OR13007',
     'SP_MDS_CAPITAL',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
 --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR13006''
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = '|| V_INDATE ||'';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
    --评估期内赔付金
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
SELECT ''OR13006'',
       PROVINCECOMCODE,
       REQRESERVED13,
       SUM(a.amount),
       CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM E_BDS_ats_choudan_tmp a
  LEFT JOIN  E_BDS_BIDM_MGECMP B
  ON SUBSTR(A.APPLYENTITY, 1, 1)=B.LAORIGORGCODE
 WHERE  a.transcode = ''1988''          -- 付款
  AND A.PAYMADEDATE BETWEEN to_date('''||V_EVAL_STARTTIME||'''||''000000'',''yyyymmddhh24miss'') AND
      to_date( '''||V_EVAL_ENDTIME||'''||''235959'',''yyyymmddhh24miss'') -- 月份
   AND a.transstate = ''2''             -- 支付成功
   AND (a.transsource IN (''GR'', ''CUSTOMER CLAIM'',''STIS'',''MID'') OR
       (a.transsource = ''CUSTOMER SERVICE'' AND
       a.reqreserved18 NOT IN (''301'',     -- 正常退保
                               ''302'',     -- 通融退保
                               ''304'',     -- 附加险退保
                               ''305'',     -- 犹豫期内退保
                               ''306'',     -- 银行在线CFI
                               ''307'')      -- 银行在线退保
                           ))
  GROUP BY PROVINCECOMCODE,REQRESERVED13
 ORDER BY PROVINCECOMCODE,REQRESERVED13';
 dbms_output.put_Line(V_SQL);
  EXECUTE IMMEDIATE V_SQL;
  -------------------------------加载日志信息完成----------------------------------
  -------------------------------开始加载日志信息----------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END := SYSDATE;
  V_RTNCODE :='0';
  V_RTNMSG :='执行成功';
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
     'OR13006',
     'SP_MDS_CAPITAL',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------异常处理-----------------------------------------
   EXCEPTION WHEN OTHERS THEN
     BEGIN
       ROLLBACK;
    V_END := SYSDATE;
    V_RTNCODE :='1';
    V_RTNMSG := SQLERRM;
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
     'SP_MDS_CAPITAL',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  -------------------------------加载日志信息完成----------------------------------
  END;
END SP_MDS_CAPITAL;
/
