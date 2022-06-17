CREATE OR REPLACE PROCEDURE SP_MDS_CONSULT(V_INDATE  IN VARCHAR2,
                                       V_RTNCODE OUT VARCHAR2,
                                       V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_CONSULT                                    */
  /* Developed By   : SY                                                */
  /* Developed Date : 2018-10-31                                        */
  /* Modify Date : WH 2018-12-26                                        */
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
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR06004'' AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  --投诉处理平均时长
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
           SELECT ''OR06004'',
                  ''86'',
                  NULL,
                  ROUND(SUM(to_date(CLOSURE_TIME,''yyyymmdd'')-to_date(COMPLAINT_DATE,''yyyymmdd''))   --结案日期-受理日期
                  /COUNT(ID),4),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
                  FROM E_BDS_CCCMP_COMPLAINTSRISK
                  WHERE MAIN_STATUSNAME = ''投诉件'' --案件类型
                  AND TO_DATE(CLOSURE_TIME,''yyyy-mm-dd'') BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')';--结案日期区间
  EXECUTE IMMEDIATE V_SQL;
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
     'OR06004',
     'SP_MDS_CONSULT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR04012''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期公司受理的有效投诉件数总量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR04012'',
                B.PROVINCECOMCODE,
                DECODE(TYPENAME,''个险'',''FC'',''多元'',''AD'',''团险'',''GP'',''银保'',''BK'',''RP''),
                COUNT(1),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM E_BDS_CCCMP_COMPLAINTSRISK A
  LEFT JOIN E_BDS_BIDM_MGECMP B
    ON B.LAORIGORGCODE = A.CHDRCOY
 WHERE A.MAIN_STATUSNAME = ''投诉件'' --案件类型
   AND TO_DATE(A.CLOSURE_TIME,''yyyy-mm-dd'') BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') --结案日期区间
 GROUP BY B.PROVINCECOMCODE,DECODE(TYPENAME,''个险'',''FC'',''多元'',''AD'',''团险'',''GP'',''银保'',''BK'',''RP'')';
 DBMS_OUTPUT.PUT_LINE(V_SQL);
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
     'OR04012',
     'SP_MDS_CONSULT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR06005''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期保险公司关于理赔、保全业务线的投诉次数
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR06005'',
                 ''86'',
                 NULL,
                 COUNT(1),
               CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM E_BDS_CCCMP_COMPLAINTSRISK A
               LEFT JOIN E_BDS_BIDM_MGECMP B
               ON B.LAORIGORGCODE = A.CHDRCOY
            WHERE TO_DATE(CLOSURE_TIME,''yyyy-mm-dd'') BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
            AND REGEXP_REPLACE(NAME, ''[0-9]+、'')=''理赔纠纷''--投诉原因
            AND MAIN_STATUSNAME = ''投诉件''--案件类型'; 

  EXECUTE IMMEDIATE V_SQL;
   DBMS_OUTPUT.PUT_LINE(V_SQL);
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
     'OR06005',
     'SP_MDS_CONSULT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR02011''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期保险公司关于承保、销售业务线的投诉次数
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR02011'',
                 ''86'',
                 NULL,
                 COUNT(1),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM E_BDS_CCCMP_COMPLAINTSRISK
            WHERE TO_DATE(CLOSURE_TIME,''yyyy-mm-dd'') BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
       AND  MAIN_STATUSNAME = ''投诉件'' --案件类型
            AND REGEXP_REPLACE(NAME, ''[0-9]+、'')--投诉原因
     IN (''误导客户'',''代签名'',''不实告知'',''展业不规范'',''承保纠纷'',''一站式手写保单'',''滞留保险费、保险金'')';
  EXECUTE IMMEDIATE V_SQL;
   DBMS_OUTPUT.PUT_LINE(V_SQL);
  --------------------------------加载日志信息完成-----------------------------
  --------------------------------开始加载日志信息-----------------------------
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
     'OR02011',
     'SP_MDS_CONSULT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------异常处理-------------------------------------
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
         'SP_MDS_CONSULT',
         'E_MDS_IRR_INDEX_VALUE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
  --------------------------------加载日志信息完成-----------------------------
    END;
END SP_MDS_CONSULT;
/
