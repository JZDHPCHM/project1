CREATE PROCEDURE SP_MDS_PLATINUM_HRM(V_INDATE IN VARCHAR2, V_RTNCODE OUT VARCHAR2, V_RTNMSG OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_PLATINUM_HRM                               */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-12-18                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_EVAL_DATE      VARCHAR2(10); --评估期12个月
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(10000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  V_EVAL_DATE      := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(V_INDATE,'YYYYMMDD'),-11),'YYYYMMDD'),1,6);
  --评估期内离职的销售人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02002'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID <> 'FC'
     AND ORG_ID='86';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02002'',
           ''86'',
          DECODE(T.CODE,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',T.CODE) CHANNEL,
           SUM(T.A3),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM 
(SELECT DISTINCT A3,B.CODE            
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN E_BDS_HRM_ORGANIZATION b 
ON a.orgcode4=b.code
AND b.typeid=''4''
AND A.PAYRID = ''30''
AND SUBSTR(A.DATA_DATE,1,6) BETWEEN  SUBSTR('''||V_EVAL_STARTTIME||''',1,6)  AND SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
and B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'')) T
GROUP BY T.CODE';
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
     'OR02002',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--评估期初销售人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02003'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID <> 'FC'
     AND ORG_ID='86' ;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02003'',
           ''86'',
           DECODE(T.CODE,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',T.CODE) CHANNEL,
           SUM(A),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM 
(SELECT
    DISTINCT A,B.CODE           
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN   E_BDS_HRM_ORGANIZATION b 
ON a.orgcode4=b.code
AND a.payrid=''30''--销售体系
AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'')
AND b.typeid=''4''
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_STARTTIME||''',1,6)) T
GROUP BY T.CODE
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
     'OR02003',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--评估期末销售人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02034'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID <> 'FC'
     AND ORG_ID='86';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02034'',
           ''86'',
           DECODE(T.CODE,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',T.CODE) CHANNEL,
           SUM(T.A1),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (           
SELECT DISTINCT A1,B.CODE
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN   E_BDS_HRM_ORGANIZATION b 
ON a.orgcode4=b.code
AND a.payrid=''30''--销售体系
AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'')
AND b.typeid=''4''
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)) T
GROUP BY T.CODE
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
     'OR02034',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/

--评估期末销售人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02004'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID <> 'FC'
     AND ORG_ID='86';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02004'',
           ''86'',
           DECODE(T.CODE,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',T.CODE) CHANNEL,
           SUM(A1),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
   FROM(
SELECT DISTINCT A1,B.CODE           
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN   E_BDS_HRM_ORGANIZATION b 
ON a.orgcode4=b.code
AND a.payrid=''30''--销售体系
AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'')
AND b.typeid=''4''
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)) T
GROUP BY T.CODE
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
     'OR02004',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--评估期末，销售人员中大专以上学历人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02006'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID <> 'FC'
     AND ORG_ID='86';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02006'',
           ''86'',
          DECODE(T.CODE,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',T.CODE) CHANNEL,
           SUM(A),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM(           
SELECT DISTINCT A,B.CODE           
FROM E_BDS_HRM_EMPLOYEEDEGREE a 
INNER  JOIN   E_BDS_HRM_ORGANIZATION b 
ON a.orgcode4=b.code
AND a.payrid=''30''--销售体系
AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'')
AND b.typeid=''4''
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)) T
GROUP BY T.CODE';
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
     'OR02006',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--最近4个季度省级分公司总经理室成员及中心支公司主要负责人离职人数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04002'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID IS NULL;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04002'',
            b.CODE,
            null,
           SUM(A3),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM E_BDS_HRM_MANAGESTATE A 
INNER  JOIN E_BDS_HRM_ORG b 
ON a.orgcode2=b.orgcode
AND a.orgcode2 <> ''HO''
AND SUBSTR(A.DATA_DATE,1,6) BETWEEN SUBSTR('''||V_EVAL_ENDTIME||''',1,6) AND '''||V_EVAL_DATE||'''
GROUP BY b.code
ORDER BY b.code
';
dbms_output.put_line(V_SQL);
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
     'OR04002',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--评估期期末省级分公司总经理室成员及中心支公司主要负责人在职人数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04003'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID IS NULL;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04003'',
            b.CODE,
            null,
           SUM(A2),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM E_BDS_HRM_MANAGESTATE A 
INNER  JOIN E_BDS_HRM_ORG b 
ON a.orgcode2=b.orgcode
AND a.orgcode2 <> ''HO''
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
GROUP BY b.code
ORDER BY b.code
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
     'OR04003',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--最近4个季度省级分公司及以下分支机构销售、承保、保全部门离职员工人数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04005'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
        SELECT ''OR04005'',
            T.CODE_1,
           DECODE(T.CODE_2,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',T.CODE_2) CHANNEL ,
           SUM(A3),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM (
 SELECT DISTINCT A3,C.CODE AS CODE_1 ,B.CODE AS CODE_2
 FROM E_BDS_HRM_EMPLOYEESTATE A
  JOIN E_BDS_HRM_ORGANIZATION B
    ON A.ORGCODE4 = B.CODE
     AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') --PR 续期 AD 多元 BD 银保 GD 团险 CS 客服 ID IDSD IDSS IDSUD IDT SFD SUD 个险
  LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
   AND B.C_NAME IS NOT NULL
 WHERE  TYPEID=''4''
  AND A.PAYRID=''30''
  AND  a.orgcode2 <> ''HO''
  AND SUBSTR(A.DATA_DATE,1,6) BETWEEN SUBSTR('''||V_EVAL_ENDTIME||''',1,6) AND '''||V_EVAL_DATE||''') T
 GROUP BY T.CODE_1,T.CODE_2
 ORDER BY T.CODE_1 
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
     'OR04005',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--前4个季度初省级分公司及以下分支机构销售、承保、保全部门员工人数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04006'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE ;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04006'',
            T.CODE_2,
           DECODE(T.CODE_1,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',T.CODE_1) CHANNEL,
           SUM(A),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM (
  SELECT DISTINCT A,B.CODE AS CODE_1,C.CODE AS CODE_2           
  FROM E_BDS_HRM_EMPLOYEESTATE A
  JOIN E_BDS_HRM_ORGANIZATION B
    ON A.ORGCODE4 = B.CODE
  AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') --PR 续期 AD 多元 BD 银保 GD 团险 CS 客服 ID IDSD IDSS IDSUD IDT SFD SUD 个险
  LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
 WHERE TYPEID = ''4''
 AND A.PAYRID = ''30''
AND  a.orgcode2 <> ''HO''
  AND SUBSTR(A.DATA_DATE,1,6) = '''||V_EVAL_DATE||''') T
 GROUP BY T.CODE_2, T.CODE_1
 ORDER BY T.CODE_2
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
     'OR04006',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--最近4个季度省级分公司及以下分支机构销售、承保、保全部门增加员工人数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04007'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04007'',
            T.CODE_1,
           DECODE(T.CODE_2,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',T.CODE_2) CHANNEL,
           SUM(A2),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM(
  SELECT DISTINCT A2,C.CODE AS CODE_1,B.CODE AS CODE_2          
  FROM E_BDS_HRM_EMPLOYEESTATE A
  JOIN E_BDS_HRM_ORGANIZATION B
    ON A.ORGCODE4 = B.CODE
    AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') --PR 续期 AD 多元 BD 银保 GD 团险 CS 客服 ID IDSD IDSS IDSUD IDT SFD SUD 个险
  LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
   AND B.C_NAME IS NOT NULL
 WHERE  TYPEID=''4''
 AND A.PAYRID = ''30''
 AND  a.orgcode2 <> ''HO''
 AND SUBSTR(A.DATA_DATE,1,6) BETWEEN SUBSTR('''||V_EVAL_ENDTIME||''',1,6) AND '''||V_EVAL_DATE||''') T
 GROUP BY T.CODE_1, T.CODE_2
 ORDER BY T.CODE_1
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
     'OR04007',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--评估期期末公司与销售人员签订有效的劳动合同、代理合同份数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04009'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID <> 'FC' ;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04009'',
            T.CODE_1,
           DECODE(T.CODE_2,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',T.CODE_2) CHANNEL,
           SUM(A1),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
SELECT DISTINCT A1,C.CODE AS CODE_1, B.CODE AS CODE_2           
FROM E_BDS_HRM_EMPLOYEESTATE A 
INNER  JOIN   E_BDS_HRM_ORGANIZATION B 
ON a.orgcode4=b.code
AND a.orgcode2 <> ''HO''
AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'')
AND b.typeid=''4''
AND A.PAYRID = ''30''
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
WHERE SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)) T
GROUP BY T.CODE_2,T.CODE_1
ORDER BY T.CODE_1
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
     'OR04009',
     'SP_MDS_PLATINUM_HRM',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--销售人员总人数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04010'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
     AND CHANNEL_ID <> 'FC' ;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04010'',
            T.CODE_2,
           DECODE(T.CODE_1,''GD'',''GP'',''BD'',''BK'',''RP_BRD'',''RP'',T.CODE_1) CHANNEL,
           NVL(SUM(A1),0),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM(           
SELECT DISTINCT A1,B.CODE AS CODE_1,C.CODE AS CODE_2
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN   E_BDS_HRM_ORGANIZATION b 
ON a.orgcode4=b.code
AND a.orgcode2 <> ''HO''
AND B.CODE IN (''RP_BRD'',''AD'',''BD'',''GD'',''CS'')
AND b.typeid=''4''
AND A.PAYRID = ''30''
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
WHERE SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6) ) T
GROUP BY T.CODE_2,T.CODE_1
ORDER BY T.CODE_1
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
     'OR04010',
     'SP_MDS_PLATINUM_HRM',
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
/*EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
      V_END     := SYSDATE;
      \**************************************** 开始加载日志信息 ****************************************\
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
         'SP_MDS_PLATINUM_HRM',
         'E_MDS_IRR_INDEX_VALUE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
      \**************************************** 加载日志信息完成 ****************************************\
    END;*/
END SP_MDS_PLATINUM_HRM;

/

