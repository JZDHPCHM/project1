CREATE PROCEDURE SP_ADS_CONT_CHARGE_RATE(V_INDATE IN VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_ADS_CONT_CHARGE_RATE  续期收费                 */
  /* Developed By   : WH                                                */
  /* Developed Date : 2019-04-13                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_ADS_CONTINUE_CHARGE_RATE                        */
  /* Source table   : E_MDS_CONTINUE_CHARGE_RATE                        */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10);    --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10);    --评估期结束时间
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
  V_RECORD_NUM     NUMBER;          --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                 'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL :='TRUNCATE TABLE E_ADS_CONTINUE_CHARGE_RATE';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_ADS_CONTINUE_CHARGE_RATE(
 uuid           
,month         
,sec_org       
,channel       
,receipts_amt  
,receivable_amt
,result_rate   
,score  )
SELECT
 uuid           
,month         
,sec_org       
,channel       
,receipts_amt  
,receivable_amt
,CASE WHEN result_rate IS NULL THEN NULL 
      WHEN result_rate = 0 THEN NULL  ELSE result_rate*100||''%'' END  
,CASE WHEN result_rate IS NULL THEN NULL
      WHEN result_rate = 0 THEN NULL
      WHEN result_rate>0.9 then 3
      when result_rate>0.8 then 1.5 end score
FROM E_MDS_CONTINUE_CHARGE_RATE';
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
     '',
     'SP_ADS_CONT_CHARGE_RATE',
     'E_ADS_CONTINUE_CHARGE_RATE',
     V_START,
     V_END,
     to_char(SYSDATE,'yyyymmdd'),
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------------异常处理-----------------------------------------
  EXCEPTION
    WHEN OTHERS THEN
      BEGIN
        ROLLBACK;
        V_END     := SYSDATE;
        V_RTNCODE := '1';
        V_RTNMSG  := SQLERRM;
  -----------------------------------加载日志信息———--------------------------------
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
         'SP_ADS_CONT_CHARGE_RATE',
         'E_ADS_CONTINUE_CHARGE_RATE',
         V_START,
         V_END,
         to_char(SYSDATE,'yyyymmdd'),
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_ADS_CONT_CHARGE_RATE;

/
CREATE PROCEDURE SP_ADS_CONT_VISIT_RATE(V_INDATE IN VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_ADS_CONT_VISIT_RATE  新契约回访                */
  /* Developed By   : WH                                                */
  /* Developed Date : 2019-04-13                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_ADS_CONTRACT_VISIT_RATE                         */
  /* Source table   : E_MDS_CONTRACT_VISIT_RATE                         */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10);    --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10);    --评估期结束时间
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
  V_RECORD_NUM     NUMBER;          --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                 'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL :='TRUNCATE TABLE E_ADS_CONTRACT_VISIT_RATE';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_ADS_CONTRACT_VISIT_RATE(
   uuid        
  ,channel     
  ,org_name    
  ,visit_num   
  ,policy_num  
  ,result_rate 
  ,score)
SELECT
   uuid        
  ,channel     
  ,org_name    
  ,visit_num   
  ,policy_num  
  ,CASE WHEN result_rate IS NULL THEN NULL  ELSE result_rate*100||''%'' END
  ,case WHEN result_rate IS NULL THEN NULL
        WHEN result_rate = 0 THEN NULL
        when result_rate>=0.95 then 2
        when result_rate>=0.9 then 1 END SCORE
FROM E_MDS_CONTRACT_VISIT_RATE';
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
     '',
     'SP_ADS_CONT_VISIT_RATE',
     'E_ADS_CONTRACT_VISIT_RATE',
     V_START,
     V_END,
     to_char(SYSDATE,'yyyymmdd'),
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------------异常处理-----------------------------------------
  EXCEPTION
    WHEN OTHERS THEN
      BEGIN
        ROLLBACK;
        V_END     := SYSDATE;
        V_RTNCODE := '1';
        V_RTNMSG  := SQLERRM;
  -----------------------------------加载日志信息———--------------------------------
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
         'SP_ADS_CONT_VISIT_RATE',
         'E_ADS_CONTRACT_VISIT_RATE',
         V_START,
         V_END,
         to_char(SYSDATE,'yyyymmdd'),
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_ADS_CONT_VISIT_RATE;

/
CREATE PROCEDURE SP_ADS_EMPLOYEE_LOSS(V_INDATE IN VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_ADS_EMPLOYEE_LOSS  员工流失率                  */
  /* Developed By   : WH                                                */
  /* Developed Date : 2019-04-13                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_ADS_EMPLOYEE_LOSS                               */
  /* Source table   : E_MDS_EMPLOYEE_LOSS                               */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10);    --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10);    --评估期结束时间
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
  V_RECORD_NUM     NUMBER;          --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                 'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL :='TRUNCATE TABLE E_ADS_EMPLOYEE_LOSS';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_ADS_EMPLOYEE_LOSS(
             UUID,
             CHANNEL,
             ORG_NAME,
             QUITNUM,
             EMPLOYEE_NUM,
             ADD_NUM,
             QUIT_RATE,
             SCORE)
SELECT
             UUID,
             CHANNEL,
             ORG_NAME,
             QUITNUM,
             EMPLOYEE_NUM,
             ADD_NUM,
             CASE WHEN EMPLOYEE_NUM+ADD_NUM IS NULL THEN NULL 
                  WHEN EMPLOYEE_NUM+ADD_NUM = 0 THEN NULL ELSE round(QUITNUM/(EMPLOYEE_NUM+ADD_NUM)*100,2)||''%'' END ,
             CASE WHEN EMPLOYEE_NUM+ADD_NUM = 0 THEN NULL
                  WHEN EMPLOYEE_NUM+ADD_NUM IS NULL THEN NULL
                  WHEN QUITNUM/(EMPLOYEE_NUM+ADD_NUM)<=0.15 THEN 3
                  WHEN QUITNUM/(EMPLOYEE_NUM+ADD_NUM)<=0.3 THEN 1.5  END  AS score
FROM E_MDS_EMPLOYEE_LOSS';
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
     '',
     'SP_ADS_EMPLOYEE_LOSS',
     'E_ADS_EMPLOYEE_LOSS',
     V_START,
     V_END,
     to_char(SYSDATE,'yyyymmdd'),
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------------异常处理-----------------------------------------
  EXCEPTION
    WHEN OTHERS THEN
      BEGIN
        ROLLBACK;
        V_END     := SYSDATE;
        V_RTNCODE := '1';
        V_RTNMSG  := SQLERRM;
  -----------------------------------加载日志信息———--------------------------------
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
         'SP_ADS_EMPLOYEE_LOSS',
         'E_ADS_EMPLOYEE_LOSS',
         V_START,
         V_END,
         to_char(SYSDATE,'yyyymmdd'),
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_ADS_EMPLOYEE_LOSS;

/
CREATE PROCEDURE SP_ADS_HESI_VISIT_RATE(V_INDATE IN VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_ADS_HESI_VISIT_RATE  犹豫期内电话回访          */
  /* Developed By   : WH                                                */
  /* Developed Date : 2019-04-13                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_ADS_HESITATE_VISIT_RATE                         */
  /* Source table   : E_MDS_HESITATE_VISIT_RATE                         */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10);    --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10);    --评估期结束时间
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
  V_RECORD_NUM     NUMBER;          --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                 'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL :='TRUNCATE TABLE E_ADS_HESITATE_VISIT_RATE';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_ADS_HESITATE_VISIT_RATE(
 uuid       
,channel    
,org_name   
,visit_num  
,policy_num 
,result_rate 
,score )
SELECT
 uuid       
,channel    
,org_name   
,visit_num  
,policy_num 
,CASE WHEN result_rate IS NULL THEN NULL ELSE result_rate*100||''%''  END 
,case WHEN result_rate IS NULL THEN NULL
      WHEN result_rate = 0 THEN NULL
	    when result_rate>=0.9 then 2
      when result_rate>=0.8 then 1
      WHEN result_rate >=0.7 THEN 0.5  end score
FROM E_MDS_HESITATE_VISIT_RATE ';
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
     '',
     'SP_ADS_HESI_VISIT_RATE',
     'E_ADS_HESITATE_VISIT_RATE',
     V_START,
     V_END,
     to_char(SYSDATE,'yyyymmdd'),
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------------异常处理-----------------------------------------
  EXCEPTION
    WHEN OTHERS THEN
      BEGIN
        ROLLBACK;
        V_END     := SYSDATE;
        V_RTNCODE := '1';
        V_RTNMSG  := SQLERRM;
  -----------------------------------加载日志信息———--------------------------------
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
         'SP_ADS_HESI_VISIT_RATE',
         'E_ADS_HESITATE_VISIT_RATE',
         V_START,
         V_END,
         to_char(SYSDATE,'yyyymmdd'),
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_ADS_HESI_VISIT_RATE;

/
CREATE PROCEDURE SP_ADS_INDEX(V_INDATE  IN VARCHAR2,
                                  V_RTNCODE OUT VARCHAR2,
                                  V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_ADS_INDEX                                         */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-11-19                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_ADS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(30000); --动态语句
  /***********************************************************************/
BEGIN
  --清除旧数据
  DELETE FROM E_ADS_IRR_INDEX_VALUE
   WHERE  TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL:= 'INSERT INTO E_ADS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
--明细
SELECT INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT
  FROM E_MDS_IRR_INDEX_VALUE
 WHERE TO_CHAR(LOAD_DT, ''yyyymmdd'') = '''||V_INDATE||'''
 UNION 
--总公司分渠道
SELECT INDEX_ID, ORG_ID, CHANNEL_ID, SUM(INDEX_VALUE), EVAL_DATE, LOAD_DT
  FROM E_MDS_IRR_INDEX_VALUE
 WHERE TO_CHAR(LOAD_DT, ''yyyymmdd'') = '''||V_INDATE||'''
 AND CHANNEL_ID IS NOT NULL
 AND ORG_ID=''86''
 GROUP BY INDEX_ID, ORG_ID, CHANNEL_ID, EVAL_DATE, LOAD_DT
UNION 
--总公司机构汇总
SELECT INDEX_ID, ORG_ID, '''', SUM(INDEX_VALUE), EVAL_DATE, LOAD_DT
  FROM E_MDS_IRR_INDEX_VALUE
 WHERE TO_CHAR(LOAD_DT, ''yyyymmdd'') = '''||V_INDATE||'''
 AND ORG_ID=''86''
 GROUP BY INDEX_ID, ORG_ID, '''',EVAL_DATE, LOAD_DT
 UNION 
 --分公司渠道汇总
SELECT INDEX_ID, '''', CHANNEL_ID, SUM(INDEX_VALUE), EVAL_DATE, LOAD_DT
  FROM E_MDS_IRR_INDEX_VALUE
 WHERE  TO_CHAR(LOAD_DT, ''yyyymmdd'') = '''||V_INDATE||'''
 AND ORG_ID <> ''86''
 GROUP BY  INDEX_ID, '''', CHANNEL_ID, EVAL_DATE, LOAD_DT
 UNION 
 --分公司机构汇总
SELECT INDEX_ID, ORG_ID, '''', SUM(INDEX_VALUE), EVAL_DATE, LOAD_DT
  FROM E_MDS_IRR_INDEX_VALUE
 WHERE  TO_CHAR(LOAD_DT, ''yyyymmdd'') = '''||V_INDATE||'''
  AND ORG_ID <> ''86''
 GROUP BY  INDEX_ID, ORG_ID, '''', EVAL_DATE, LOAD_DT
 ';
  EXECUTE IMMEDIATE V_SQL;
  dbms_output.put_line(V_SQL);
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
     'SP_ADS_INDEX',
     'E_ADS_IRR_INDEX_VALUE',
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
         'SP_ADS_INDEX',
         'E_ADS_IRR_INDEX_VALUE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
      /**************************************** 加载日志信息完成 ****************************************/
    END;
END SP_ADS_INDEX;

/
CREATE PROCEDURE SP_ADS_RETREAT_RATE(V_INDATE IN VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_ADS_RETREAT_RATE  退撤保率                     */
  /* Developed By   : WH                                                */
  /* Developed Date : 2019-04-13                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_ADS_RETREAT_RATE                                */
  /* Source table   : E_MDS_RETREAT_RATE                                */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10);    --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10);    --评估期结束时间
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
  V_RECORD_NUM     NUMBER;          --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                 'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL :='TRUNCATE TABLE E_ADS_RETREAT_RATE';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_ADS_RETREAT_RATE(
 uuid          
,branch_com    
,channel       
,retreat_amt   
,revoke_amt    
,receipts_amt  
,receivable_amt
,result_rate  
,score   )
SELECT
 uuid          
,branch_com    
,channel       
,retreat_amt   
,revoke_amt    
,receipts_amt  
,receivable_amt
,CASE WHEN (RETREAT_AMT+REVOKE_AMT) IS NULL THEN NULL
      WHEN (RETREAT_AMT+REVOKE_AMT) = 0  THEN NULL 
        ELSE round((RETREAT_AMT+REVOKE_AMT)/(RECEIPTS_AMT+RECEIVABLE_AMT)*100,2)||''%''  END  
,case WHEN (RETREAT_AMT+REVOKE_AMT) IS NULL THEN NULL
      WHEN (RETREAT_AMT+REVOKE_AMT) = 0 THEN NULL
      when round((RETREAT_AMT+REVOKE_AMT)/(RECEIPTS_AMT+RECEIVABLE_AMT),2)>0.1 then 0
      when round((RETREAT_AMT+REVOKE_AMT)/(RECEIPTS_AMT+RECEIVABLE_AMT),2)>0.05 then 1.5
      else 3
 end score
FROM E_MDS_RETREAT_RATE';
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
     '',
     'SP_ADS_RETREAT_RATE',
     'E_ADS_RETREAT_RATE',
     V_START,
     V_END,
     to_char(SYSDATE,'yyyymmdd'),
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------------异常处理-----------------------------------------
  EXCEPTION
    WHEN OTHERS THEN
      BEGIN
        ROLLBACK;
        V_END     := SYSDATE;
        V_RTNCODE := '1';
        V_RTNMSG  := SQLERRM;
  -----------------------------------加载日志信息———--------------------------------
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
         'SP_ADS_RETREAT_RATE',
         'E_ADS_RETREAT_RATE',
         V_START,
         V_END,
         to_char(SYSDATE,'yyyymmdd'),
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_ADS_RETREAT_RATE;

/
CREATE PROCEDURE SP_JUDGE_FIN_DATA(V_INDATE  VARCHAR2,
                                              V_RTNCODE OUT VARCHAR2,
                                              V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_JUDGE_FIN_DATA                                             */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-11-22                                        */
  /* 判断财务接口表是否有输入日期的上季度末数据                         */
  /********************** Variable Section ******************************/
  V_COUNT      NUMBER;
  V_DATE       VARCHAR2(10);
  V_START      DATE;
  V_END        DATE;
  V_RECORD_NUM NUMBER;
BEGIN
  --备份财务接口表中数据
INSERT INTO E_BDS_FIN_GL_HIS
(
ORG_NO,
ORG_NAME,
ITEM_CODE_LVL1,
ITEM_CODE_LVL2,
ITEM_NAME,
FIN_AMT,
GL_DATE,
CHANNEL_CODE,
CHANNEL_NAME,
BACKDATE 
)
SELECT ORG_NO,
ORG_NAME,
ITEM_CODE_LVL1,
ITEM_CODE_LVL2,
ITEM_NAME,
FIN_AMT,
GL_DATE,
CHANNEL_CODE,
CHANNEL_NAME,
TO_CHAR(SYSDATE,'YYYYMMDD') FROM E_BDS_FIN_GL A  /*WHERE NOT EXISTS 
(SELECT 1 FROM E_BDS_FIN_GL_HIS B WHERE A.GL_DATE=B.GL_DATE)*/;

COMMIT;

  --清除ETL日志表中判断财务数据是否导入结果的数据
  DELETE FROM ETL_LOG_DETAIL WHERE SP_NAME = 'SP_JUDGE_FIN_DATA';
  
  --DELETE FROM  ETL_TASK_INFO ;

  --获取输入日期的上季度末日期
  V_DATE := FUN_ETL_Q_DATE(V_INDATE);

  V_START := SYSDATE;

  --获取财务接口表中输入日期的上季度末数据量
  SELECT COUNT(1)
    INTO V_COUNT
    FROM E_BDS_FIN_GL
   WHERE GL_DATE = TO_CHAR(TO_DATE(V_DATE, 'yyyymmdd'), 'yyyy-mm');
  V_END        := SYSDATE;
  V_RECORD_NUM := SQL%ROWCOUNT;

  IF V_COUNT != 0 THEN
    V_RTNCODE := '2';
    V_RTNMSG  := '上季度末数据已导入';
  ELSIF V_COUNT =0 THEN
    V_RTNCODE := '0';
    V_RTNMSG  := '上季度末数据未导入';  
  END IF;
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
       'SP_JUDGE_FIN_DATA',
       '',
       V_START,
       V_END,
       TO_CHAR(SYSDATE,'YYYYMMDD'),
       V_RECORD_NUM,
       V_RTNCODE,
       V_RTNMSG);
    COMMIT;
/**************************************** 加载日志信息完成 ****************************************/
  --------------------------------异常处理-------------------------------------
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
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
       'SP_JUDGE_FIN_DATA',
       '',
       V_START,
       V_END,
       TO_CHAR(SYSDATE,'YYYYMMDD'),
       V_RECORD_NUM,
       V_RTNCODE,
       V_RTNMSG);
    COMMIT;
    END;
    /**************************************** 加载日志信息完成 ****************************************/
END SP_JUDGE_FIN_DATA;

/
CREATE PROCEDURE SP_MDS_CAPITAL(V_INDATE  IN  VARCHAR2,
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
   AND (a.transsource IN (''GR'', ''CUSTOMER CLAIM'',''STIS'') OR
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
CREATE PROCEDURE SP_MDS_CONSULT(V_INDATE  IN VARCHAR2,
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
            AND SUBSTR(REGEXP_REPLACE(NAME, ''[0-9]+、''),1,instr(REGEXP_REPLACE(NAME, ''[0-9]+、''),'','',1)-1)=''理赔纠纷''--投诉原因
            AND MAIN_STATUSNAME = ''投诉件''--案件类型'; 

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
            AND SUBSTR(REGEXP_REPLACE(NAME, ''[0-9]+、''),1,instr(REGEXP_REPLACE(NAME, ''[0-9]+、''),'','',1)-1)--投诉原因
     IN (''误导客户'',''代签名'',''不实告知'',''展业不规范'',''承保纠纷'',''一站式手写保单'')';
  EXECUTE IMMEDIATE V_SQL;
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
CREATE PROCEDURE SP_MDS_COORDINATION(V_INDATE  IN VARCHAR2,
                                            V_RTNCODE OUT VARCHAR2,
                                            V_RTNMSG  OUT VARCHAR2) 
                                            AUTHID CURRENT_USER AS

  /**********************************************************************/
  /* Procedure Name : SP_MDS_COORDINATION                               */
  /* Developed By   : SY                                                */
  /* Developed Date : 2018-10-31                                        */
  /* MODIFIED Date : WH 2018-12-27                                      */
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
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                 'yyyymmdd') + 1,
                                         -3),
                              'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR04018'' AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' ||
           V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  --评估期内承保的保单件数
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
           SELECT ''OR04018'',
                  B.PROVINCECOMCODE,
                  DECODE(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL),
                  COUNT(NVL(BASE.POLICYNO,0)),
                   CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
                  FROM E_BDS_CCCMP_POLICYS_BASE BASE
                INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
                   ON BASE.POLICYNO = INDI.POLICYNO
                INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
                   ON BASE.POLICYNO = CASE3.POLICYNO
                LEFT JOIN E_BDS_BIDM_MGECMP B
                 ON B.LAORIGORGCODE = BASE.COMPANYNO
                WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN to_date(''' ||
           V_EVAL_STARTTIME || ''',''yyyy-mm-dd'') AND
      to_date( ''' || V_EVAL_ENDTIME ||
           ''',''yyyy-mm-dd'')
                  AND INDI.ISCFI = ''0''
                  AND INDI.ISWD = ''0''
                  AND BASE.ZACKDTE IS NOT NULL
                  AND BASE.ZACKDTE != ''99999999''
                  AND BASE.NEEDCALLBACK = ''1''
                  AND BASE.CNTTYPE NOT LIKE ''PA%''
                  AND CASE3.BACRS NOT IN (''1'', ''10'')
                  AND NOT (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
                  GROUP BY B.PROVINCECOMCODE,DECODE(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL)';
  EXECUTE IMMEDIATE V_SQL;
  DBMS_OUTPUT.PUT_LINE(V_SQL);
  COMMIT;
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
     'OR04018',
     'SP_MDS_COORDINATION',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  ----------------------------------加载日志信息完成---------------------------------
   --评估期内承保的保单件数
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
           SELECT ''OR04072'',
                  B.PROVINCECOMCODE,
                  DECODE(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL),
                  COUNT(NVL(BASE.POLICYNO,0)),
                   CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
                  FROM E_BDS_CCCMP_POLICYS_BASE BASE
                INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
                   ON BASE.POLICYNO = INDI.POLICYNO
                INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
                   ON BASE.POLICYNO = CASE3.POLICYNO
                LEFT JOIN E_BDS_BIDM_MGECMP B
                 ON B.LAORIGORGCODE = BASE.COMPANYNO
                WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN to_date(''' ||
           V_EVAL_STARTTIME || ''',''yyyy-mm-dd'') AND
      to_date( ''' || V_EVAL_ENDTIME ||
           ''',''yyyy-mm-dd'')
                  AND INDI.ISCFI = ''0''
                  AND INDI.ISWD = ''0''
                  AND BASE.ZACKDTE IS NOT NULL
                  AND BASE.ZACKDTE != ''99999999''
                  AND BASE.NEEDCALLBACK = ''1''
                  AND BASE.CNTTYPE NOT LIKE ''PA%''
                  AND CASE3.BACRS NOT IN (''1'', ''10'')
                  AND NOT (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
                  GROUP BY B.PROVINCECOMCODE,DECODE(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL)';
  EXECUTE IMMEDIATE V_SQL;
    DBMS_OUTPUT.PUT_LINE(V_SQL);
  COMMIT;
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
     'OR04072',
     'SP_MDS_COORDINATION',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  ----------------------------------加载日志信息完成---------------------------------
  
  
 --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR04017''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' ||
           V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期内通过电话回访方式在犹豫期内完成新契约回访的保单件数
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR04017'',
                 PROVINCECOMCODE,
                 DECODE(CHANNEL,''GB'',''GP'',CHANNEL),
                 COUNT(NVL(POLICYNO, 0)),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM  (SELECT BASE.AGENTNO,
               B.PROVINCECOMCODE,
               BASE.BRANCH,
               BASE.CHANNEL,
               BASE.ENDHESDATE,
               BASE.CHDRNUM,
               BASE.POLICYNO,
               MIN(CBLIST.CALLBACKDATE) CALLBACKDATE,
               BASE.ARACDE1
          FROM E_BDS_CCCMP_POLICYS_BASE BASE
         INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
            ON BASE.POLICYNO = INDI.POLICYNO
         INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
            ON BASE.POLICYNO = CASE3.POLICYNO
         INNER JOIN E_BDS_CCCMP_CRM_CALL_LIST CBLIST
            ON CBLIST.POLICYNO = BASE.POLICYNO
         LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = BASE.COMPANYNO
         WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN to_date(''' ||
             V_EVAL_STARTTIME || ''',''yyyy-mm-dd'') AND
      to_date( ''' || V_EVAL_ENDTIME ||
             ''',''yyyy-mm-dd'')
           AND INDI.ISCFI = ''0'' --剔除CFI
           AND INDI.ISWD = ''0'' --剔除WD
           AND BASE.ZACKDTE IS NOT NULL --回执日期
           AND BASE.ZACKDTE != ''99999999''
           AND BASE.NEEDCALLBACK = ''1'' --是否需要回访
           AND BASE.CNTTYPE NOT LIKE ''PA%''
           AND NOT
                (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
           AND CASE3.BACRS NOT IN (''0'', ''1'') --回访结果 0重复创建     1未回电已CFI
           AND CBLIST.CALLBACKRESULT IN
               (''电话回访完全干净件'',
                ''电话回访问题件'',
                ''电话回访-CS干净件'',
                ''电话回访CS干净件'',
                ''电话回访完全干净件非投操作'',
                ''电话回访CS干净件非投操作'',
                ''电话回访问题件非投操作'',
                ''亲访信回访干净件'',
                ''亲访信回访问题件非投操作'',
                ''问题件-投保未双录'')
         GROUP BY B.PROVINCECOMCODE,
                  BASE.BRANCH,
                  BASE.CHANNEL,
                  BASE.ENDHESDATE,
                  BASE.CHDRNUM,
                  BASE.AGENTNO,
                  BASE.ARACDE1,
                  BASE.POLICYNO) T
 WHERE T.ENDHESDATE >=
       SUBSTR(T.CALLBACKDATE, 0, 4) || SUBSTR(T.CALLBACKDATE, 6, 2) ||
       SUBSTR(T.CALLBACKDATE, 9, 2)
 GROUP BY PROVINCECOMCODE, DECODE(CHANNEL,''GB'',''GP'',CHANNEL)';
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
     'OR04017',
     'SP_MDS_COORDINATION',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR04020''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' ||
           V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期内承保的保单中完成回访的保单件数
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
 SELECT ''OR04020'',
       B.PROVINCECOMCODE ,
        DECODE(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL) CHANNEL,
         count(BASE.CHDRNUM),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM E_BDS_CCCMP_POLICYS_BASE BASE
  LEFT JOIN E_BDS_CCNT_CRM_CASE3 A
    ON A.CHDRCOYID = BASE.COMPANYNO || BASE.BRANCH
   AND A.CHDRNUM = BASE.CHDRNUM
 INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
    ON BASE.POLICYNO = INDI.POLICYNO
 INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
    ON BASE.POLICYNO = CASE3.POLICYNO
    LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = BASE.COMPANYNO 
 WHERE (TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') >= to_date(''' ||
             V_EVAL_STARTTIME || ''',''yyyy-mm-dd'')
   AND TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') <=to_date( ''' || V_EVAL_ENDTIME || ''',''yyyy-mm-dd''))
   AND INDI.ISCFI = ''0''
   AND INDI.ISWD = ''0''
   AND BASE.ZACKDTE IS NOT NULL
   AND BASE.ZACKDTE != ''99999999''
   AND BASE.NEEDCALLBACK = ''1''
   AND BASE.CNTTYPE NOT LIKE ''PA%''
   AND CASE3.BACRS NOT IN (''1'', ''10'')
   AND NOT (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
      AND BASE.CHANNEL IN (''FC'', ''BK'', ''AD'', ''RP'', ''GB'')
group BY B.PROVINCECOMCODE,DECODE(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL)';
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
     'OR04020',
     'SP_MDS_COORDINATION',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR02009''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' ||
           V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --评估期电话回访成功的保单件数
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR02009'',
                 ''86'',
                 decode(CHANNEL,''GB'',''GP'',CHANNEL),
                 COUNT(NVL(POLICYNO, 0)),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM  (SELECT BASE.AGENTNO,
               B.PROVINCECOMCODE,
               BASE.BRANCH,
               BASE.CHANNEL,
               BASE.ENDHESDATE,
               BASE.CHDRNUM,
               BASE.POLICYNO,
               MIN(CBLIST.CALLBACKDATE) CALLBACKDATE,
               BASE.ARACDE1
          FROM E_BDS_CCCMP_POLICYS_BASE BASE
         INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
            ON BASE.POLICYNO = INDI.POLICYNO
         INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
            ON BASE.POLICYNO = CASE3.POLICYNO
         INNER JOIN E_BDS_CCCMP_CRM_CALL_LIST CBLIST
            ON CBLIST.POLICYNO = BASE.POLICYNO
         LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = BASE.COMPANYNO
         WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN to_date(''' ||
             V_EVAL_STARTTIME || ''',''yyyy-mm-dd'') AND
      to_date( ''' || V_EVAL_ENDTIME ||
             ''',''yyyy-mm-dd'')
           AND INDI.ISCFI = ''0'' --剔除CFI
           AND INDI.ISWD = ''0'' --剔除WD
           AND BASE.ZACKDTE IS NOT NULL --回执日期
           AND BASE.ZACKDTE != ''99999999''
           AND BASE.NEEDCALLBACK = ''1'' --是否需要回访
           AND BASE.CNTTYPE NOT LIKE ''PA%''
           AND NOT
                (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
           AND CASE3.BACRS NOT IN (''0'', ''1'') --回访结果 0重复创建     1未回电已CFI
           AND CBLIST.CALLBACKRESULT IN
               (''电话回访完全干净件'',
                ''电话回访问题件'',
                ''电话回访-CS干净件'',
                ''电话回访CS干净件'',
                ''电话回访完全干净件非投操作'',
                ''电话回访CS干净件非投操作'',
                ''电话回访问题件非投操作'',
                ''亲访信回访干净件'',
                ''亲访信回访问题件非投操作'',
                ''问题件-投保未双录'')
         GROUP BY B.PROVINCECOMCODE,
                  BASE.BRANCH,
                  BASE.CHANNEL,
                  BASE.ENDHESDATE,
                  BASE.CHDRNUM,
                  BASE.AGENTNO,
                  BASE.ARACDE1,
                  BASE.POLICYNO) T
 WHERE T.ENDHESDATE >=
       SUBSTR(T.CALLBACKDATE, 0, 4) || SUBSTR(T.CALLBACKDATE, 6, 2) ||
       SUBSTR(T.CALLBACKDATE, 9, 2)
 GROUP BY decode(CHANNEL,''GB'',''GP'',CHANNEL)';
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
     'OR02009',
     'SP_MDS_COORDINATION',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR02010''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' ||
           V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
    --------------------------------插入数据--------------------------------
    --评估期开展电话回访的保单件数
    V_START := SYSDATE;
    V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
               INDEX_ID,
               ORG_ID,
               CHANNEL_ID,
               INDEX_VALUE,
               EVAL_DATE,
               LOAD_DT)
              SELECT ''OR02010'',
                  ''86'',
                  decode(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL),
                  COUNT(NVL(BASE.POLICYNO,0)),
                   CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
                  FROM E_BDS_CCCMP_POLICYS_BASE BASE
                INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
                   ON BASE.POLICYNO = INDI.POLICYNO
                INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
                   ON BASE.POLICYNO = CASE3.POLICYNO
                LEFT JOIN E_BDS_BIDM_MGECMP B
                 ON B.LAORIGORGCODE = BASE.COMPANYNO
                WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN to_date(''' ||
           V_EVAL_STARTTIME || ''',''yyyy-mm-dd'') AND
      to_date( ''' || V_EVAL_ENDTIME ||
           ''',''yyyy-mm-dd'')
                  AND INDI.ISCFI = ''0''
                  AND INDI.ISWD = ''0''
                  AND BASE.ZACKDTE IS NOT NULL
                  AND BASE.ZACKDTE != ''99999999''
                  AND BASE.NEEDCALLBACK = ''1''
                  AND BASE.CNTTYPE NOT LIKE ''PA%''
                  AND CASE3.BACRS NOT IN (''1'', ''10'')
                  AND NOT (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
                  GROUP BY decode(BASE.CHANNEL,''GB'',''GP'',BASE.CHANNEL)';
    EXECUTE IMMEDIATE V_SQL;
    -----------------------------------加载日志信息完成--------------------------------
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
     'OR02010',
     'SP_MDS_COORDINATION',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
    --------------------------------异常处理-----------------------------------------
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
         'SP_MDS_COORDINATION',
         'E_MDS_IRR_INDEX_VALUE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_MDS_COORDINATION;

/
CREATE PROCEDURE SP_MDS_DOCUMENT(V_INDATE  IN  VARCHAR2,
                                        V_RTNCODE OUT VARCHAR2,
                                        V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_DOCUMENT                                   */
  /* Developed By   : SY                                                */
  /* Developed Date : 2018-10-31                                        */
  /* Modified  Date : 2019-01-21  WH             添加银保部分           */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(20); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(20); --评估期结束时间
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -12),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
 DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = 'OR12003'
  AND CHANNEL_ID='GP' 
  AND TO_CHAR(LOAD_DT,'yyyymmdd') =  V_INDATE ;
  COMMIT;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  
  /*--------------------------------------------------------------------------
  -----------------------------老团险部分-------------------------------------
  --------------------------------------------------------------------------*/
  
  --最近4个季度内空白单证发放的数量
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
           SELECT ''OR12003'',
                  ''86'',
                  ''GP'',
                  NVL(SUM(A.SUMCOUNT),0),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM E_BDS_LISHASL_LZCARDTRACK A
  LEFT JOIN E_BDS_BIDM_MGECMP B
    ON A.MANAGECOM = B.INNERORGCODE
 WHERE OPERATEFLAG = ''4'' --接受确认
  AND RECEIVECOM LIKE ''B%'' --T3机构
   AND TO_CHAR(A.MAKEDATE, ''YYYYMMDD'') 
   BETWEEN '''||V_EVAL_STARTTIME||''' 
   AND '''||V_EVAL_ENDTIME||'''';
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
     'OR12003',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
   --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR12002''
  AND CHANNEL_ID=''GP'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内已发放空白单证缺失的数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR12002'',
                 ''86'',
                 ''GP'',
                 NVL(COUNT(A.PRTNO),0),
               CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM E_BDS_LISHASL_LZCARDSTATE A
            LEFT JOIN E_BDS_BIDM_MGECMP B
            ON A.MANAGECOM = B.INNERORGCODE
            WHERE A.BACKSTATE = ''2''
            AND A.USESTATE= ''1''
            AND TO_CHAR(A.BACKDATE,''YYYYMMDD'') BETWEEN '''||V_EVAL_STARTTIME||''' 
                  AND '''||V_EVAL_ENDTIME||'''';            
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
     'OR12002',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据-------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = ''OR13012'' 
   AND CHANNEL_ID=''GP'' 
   AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  --最近4个季度内空白单证发放的数量
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
 SELECT ''OR13012'',
                  B.PROVINCECOMCODE,
                  ''GP'',
                  NVL(SUM(A.SUMCOUNT),0),
                 CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
                  FROM E_BDS_LISHASL_LZCARDTRACK A
  RIGHT JOIN E_BDS_BIDM_MGECMP B
    ON A.MANAGECOM = B.INNERORGCODE
 AND OPERATEFLAG = ''4'' --接受确认
  AND RECEIVECOM LIKE ''B%'' --T3机构
   AND( TO_CHAR(A.MAKEDATE, ''YYYYMMDD'') 
   BETWEEN '''||V_EVAL_STARTTIME||''' 
   AND '''||V_EVAL_ENDTIME||''')
   group by B.PROVINCECOMCODE
   ';
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
     'OR13012',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR13011''
  AND CHANNEL_ID=''GP'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内已发放空白单证缺失的数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR13011'',
                 B.PROVINCECOMCODE,
                 ''GP'',
                 NVL(COUNT(A.PRTNO),0),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM E_BDS_LISHASL_LZCARDSTATE A
            RIGHT JOIN E_BDS_BIDM_MGECMP B
            ON A.MANAGECOM = B.INNERORGCODE
            AND A.BACKSTATE = ''2''
            AND A.USESTATE= ''1''
            AND TO_CHAR(A.BACKDATE,''YYYYMMDD'') BETWEEN '''||V_EVAL_STARTTIME||''' 
                  AND '''||V_EVAL_ENDTIME||'''  
            GROUP BY B.PROVINCECOMCODE';
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
     'OR13011',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR13014''
  AND CHANNEL_ID=''GP'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内已回销的有价单证数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR13014'',
                 B.PROVINCECOMCODE,
                 ''GP'',
                 NVL(SUM(A.sumcount),0),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM ( select z.managecom AS managecom, z.sumcount AS sumcount
                        from E_BDS_LISHASL_LZCARDTRACK z
                       where z.sendoutcom = z.receivecom
                         and z.operateflag = ''7''
                        AND( TO_CHAR(makedate, ''YYYYMMDD'') 
                         BETWEEN '''||V_EVAL_STARTTIME||''' 
                         AND '''||V_EVAL_ENDTIME||''')
                      union all
                      select z.managecom AS managecom, z.sumcount AS sumcount
                        from E_BDS_LISHASL_LZCARDTRACK z
                       where z.sendoutcom <> z.receivecom
                         and z.operateflag = ''1''
                         and z.payflag = ''1''
                          
                        AND( TO_CHAR(makedate, ''YYYYMMDD'') 
                         BETWEEN '''||V_EVAL_STARTTIME||''' 
                         AND '''||V_EVAL_ENDTIME||''')
                      union all
                      select z.managecom AS managecom, z.sumcount AS sumcount
                        from E_BDS_LISHASL_LZCARDTRACK z
                       where z.sendoutcom <> z.receivecom
                         and z.operateflag = ''5''
                        AND( TO_CHAR(makedate, ''YYYYMMDD'') 
                         BETWEEN '''||V_EVAL_STARTTIME||''' 
                         AND '''||V_EVAL_ENDTIME||''')) A
            RIGHT JOIN E_BDS_BIDM_MGECMP B
            ON A.managecom = B.INNERORGCODE
            GROUP BY B.PROVINCECOMCODE';
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
     'OR13014',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR13015''
  AND CHANNEL_ID=''GP'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内按公司规定时限内应回销的有价单证数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR13015'',
                 B.PROVINCECOMCODE,
                 ''GP'',
                 NVL(SUM(A.sumcount),0),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
            FROM ( select z.managecom AS managecom, z.sumcount AS sumcount
                        from E_BDS_LISHASL_LZCARDTRACK z
                       where z.sendoutcom = z.receivecom
                         and z.operateflag = ''7''
                        AND( TO_CHAR(makedate, ''YYYYMMDD'') 
                         BETWEEN '''||V_EVAL_STARTTIME||''' 
                         AND '''||V_EVAL_ENDTIME||''')
                      union all
                      select z.managecom AS managecom, z.sumcount AS sumcount
                        from E_BDS_LISHASL_LZCARDTRACK z
                       where z.sendoutcom <> z.receivecom
                         and z.operateflag = ''1''
                         and z.payflag = ''1''
                          
                        AND( TO_CHAR(makedate, ''YYYYMMDD'') 
                         BETWEEN '''||V_EVAL_STARTTIME||''' 
                         AND '''||V_EVAL_ENDTIME||''')
                      union all
                      select z.managecom AS managecom, z.sumcount AS sumcount
                        from E_BDS_LISHASL_LZCARDTRACK z
                       where z.sendoutcom <> z.receivecom
                         and z.operateflag = ''5''
                        AND( TO_CHAR(makedate, ''YYYYMMDD'') 
                         BETWEEN '''||V_EVAL_STARTTIME||''' 
                         AND '''||V_EVAL_ENDTIME||''')) A
            RIGHT JOIN E_BDS_BIDM_MGECMP B
            ON A.managecom = B.INNERORGCODE
            GROUP BY B.PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
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
     'OR13015',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  
  /*--------------------------------------------------------------------------
  -----------------------------银保部分---------------------------------------
  --------------------------------------------------------------------------*/
  
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmddhh24miss') + 1, -12),'yyyymmddhh24miss');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE)||'235959';
   --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR13012''
  AND CHANNEL_ID=''BK'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内空白单证发放的数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR13012'',
                 B.PROVINCECOMCODE,
                 ''BK'',
                 count(DISTINCT docno),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')    
 FROM E_BDS_AFMS_AFMS_AFMS_HISTORY A 
 RIGHT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 AND STATUS IN (''2'',''4'')--2 T2发放、 4 T3发放
 AND PFLAG_H=''1''
 AND to_date(updatetime,''yyyymmddhh24miss'')
 BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
 AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')
 GROUP BY B.PROVINCECOMCODE';
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
     'OR13012',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
 --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR13011''
  AND CHANNEL_ID=''BK'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内已发放空白单证缺失的数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
  SELECT ''OR13011'',
                 B.PROVINCECOMCODE,
                 ''BK'',
                 count(DISTINCT docno),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM E_BDS_AFMS_AFMS_AFMS_HISTORY A
 RIGHT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 AND  STATUS=''9''     --遗失
   AND SUBSTR(team,1,1)=''4''
   AND TO_DATE(UPDATETIME, ''yyyymmddhh24miss'') 
   BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
   AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')
   AND PFLAG_H =''1''
   AND docno IN (
 SELECT DOCNO  
 FROM E_BDS_AFMS_AFMS_AFMS_HISTORY A 
 LEFT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 WHERE STATUS IN (''2'',''4'')--2 T2发放、 4 T3发放
 AND PFLAG_H=''1''
 AND to_date(updatetime,''yyyymmddhh24miss'')
 BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
 AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')
 )
 GROUP BY B.PROVINCECOMCODE';
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
     'OR13011',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT; 
--------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR13014''
  AND CHANNEL_ID=''BK'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内已回销的有价单证数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR13014'',
                PROVINCECOMCODE,
                 ''BK'',
                 COUNT(docno),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
SELECT * FROM (SELECT 
ROW_NUMBER()OVER(PARTITION BY DOCNO ORDER BY UPDATETIME desc,PFLAG_H DESC) RN,docno,updatetime,pflag_h,team,status
FROM (SELECT *
FROM E_BDS_AFMS_AFMS_AFMS_HISTORY t 
WHERE  status IN (''6'',''8'',''9'',''12'',''11'',''2'',''4'')--2 T2发放  4  T3发放 6 出单回销 8 作废 9 遗失 11 空白退回T2  12 空白退回T3  
)) t WHERE rn=1
and  status IN (''6'',''8'',''9'',''12'',''11'')--6 出单回销 8 作废 9 遗失 11 空白退回T2  12 空白退回T3
AND PFLAG_H=''1''
AND  to_date(updatetime,''yyyymmddhh24miss'')
 BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
 AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')) a 
 RIGHT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 GROUP BY PROVINCECOMCODE
';
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
     'OR13014',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = ''OR13015''
   AND CHANNEL_ID=''BK'' 
   AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内按公司规定时限内应回销的有价单证数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR13015'',
                 PROVINCECOMCODE,
                 ''BK'',
                 sum(docno),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')

from (
--已回销
SELECT PROVINCECOMCODE,COUNT(docno) docno  
FROM (
SELECT * FROM (SELECT 
ROW_NUMBER()OVER(PARTITION BY DOCNO ORDER BY UPDATETIME desc,PFLAG_H DESC) RN,docno,updatetime,pflag_h,team,status
FROM (SELECT *
FROM E_BDS_AFMS_AFMS_AFMS_HISTORY t 
WHERE  status IN (''6'',''8'',''9'',''12'',''11'',''2'',''4'')--2 T2发放  4  T3发放 6 出单回销 8 作废 9 遗失 11 空白退回T2  12 空白退回T3  
)) t WHERE rn=1
and  status IN (''6'',''8'',''9'',''12'',''11'')--6 出单回销 8 作废 9 遗失 11 空白退回T2  12 空白退回T3
AND PFLAG_H=''1''
AND  to_date(updatetime,''yyyymmddhh24miss'')
 BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
 AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')) a 
 LEFT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 GROUP BY PROVINCECOMCODE
UNION ALL
--超期未回销
SELECT PROVINCECOMCODE,COUNT(DISTINCT docno)
  FROM (SELECT  DOCNO,PROVINCECOMCODE,LEAST(ADD_MONTHS(TO_DATE(UPDATETIME, ''yyyymmddhh24miss''), 12),
           to_date(DEADLINE,''yyyymmdd'')) AS ctime  ,to_date(DEADLINE,''yyyymmdd'') AS DEADLINE
                   FROM E_BDS_AFMS_AFMS_AFMS_HISTORY a 
                    LEFT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
                  WHERE STATUS IN (''2'', ''4'') 
                  ) t
 WHERE CTIME > DEADLINE
 GROUP BY PROVINCECOMCODE) t       
 GROUP BY PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
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
     'OR13015',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT; 
    --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR12003''
  AND CHANNEL_ID=''BK'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内空白单证发放的数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR12003'',
                 ''86'',
                 ''BK'',
                 count(DISTINCT docno),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')    
 FROM E_BDS_AFMS_AFMS_AFMS_HISTORY A 
 LEFT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 WHERE STATUS IN (''2'',''4'')--2 T2发放、 4 T3发放
 AND PFLAG_H=''1''
 AND to_date(updatetime,''yyyymmddhh24miss'')
 BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
 AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')
';
  EXECUTE IMMEDIATE V_SQL;
  DBMS_OUTPUT.put_line(V_SQL);
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
     'OR12003',
     'SP_MDS_DOCUMENT',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
 --------------------------------删除数据--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE 
  WHERE INDEX_ID = ''OR12002''
  AND CHANNEL_ID=''BK'' 
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  --最近4个季度内已发放空白单证缺失的数量
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE(
             INDEX_ID,
             ORG_ID,
             CHANNEL_ID,
             INDEX_VALUE,
             EVAL_DATE,
             LOAD_DT)
          SELECT ''OR12002'',
                 ''86'',
                 ''BK'',
                 count(DISTINCT docno),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM E_BDS_AFMS_AFMS_AFMS_HISTORY A
 LEFT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 WHERE  STATUS=''9''     --遗失
   AND SUBSTR(team,1,1)=''4''
   AND TO_DATE(UPDATETIME, ''yyyymmddhh24miss'') 
   BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
   AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')
   AND PFLAG_H =''1''
   AND docno IN (
 SELECT DOCNO  
 FROM E_BDS_AFMS_AFMS_AFMS_HISTORY A 
 LEFT JOIN E_BDS_BIDM_MGECMP B
 ON A.TEAM = B.LAORIGORGCODE
 WHERE STATUS IN (''2'',''4'')--2 T2发放、 4 T3发放
 AND PFLAG_H=''1''
 AND to_date(updatetime,''yyyymmddhh24miss'')
 BETWEEN to_date('''||V_EVAL_STARTTIME||''' ,''yyyymmddhh24miss'')
 AND to_date('''||V_EVAL_ENDTIME||''',''yyyymmddhh24miss'')
 )';
   DBMS_OUTPUT.put_line(V_SQL);
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
     'OR12002',
     'SP_MDS_DOCUMENT',
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
     'SP_MDS_DOCUMENT',
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
END SP_MDS_DOCUMENT;

/
CREATE PROCEDURE SP_MDS_DTL_CONT_CHARGE_RATE(V_INDATE  IN VARCHAR2,
                                  V_RTNCODE OUT VARCHAR2,
                                  V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_DTL_CONT_CHARGE_RATE  续期收费             */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-11-28                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_CONTINUE_CHARGE_RATE                        */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10);   	--评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10);   --评估期结束时间
  V_START          DATE;           --执行开始时间
  V_END            DATE;           --执行结束时间
  V_RECORD_NUM     NUMBER;         --记录条数
  V_SQL            VARCHAR2(30000);--动态语句
  /***********************************************************************/
BEGIN
DBMS_OUTPUT.ENABLE(1000000);
  V_EVAL_STARTTIME := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1,
                                         -3),
                              'yyyymmdd'),1,6);
  V_EVAL_ENDTIME   := SUBSTR(FUN_ETL_Q_DATE(V_INDATE),1,6);
  --清除旧数据
  V_SQL :='TRUNCATE TABLE E_MDS_CONTINUE_CHARGE_RATE';
  EXECUTE IMMEDIATE V_SQL;
   
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_CONTINUE_CHARGE_RATE
  (uuid
  ,month
  ,sec_org
  ,channel
  ,receipts_amt
  ,receivable_amt
  ,result_rate)
  select sys_guid(),FINMONTH,PROVINCECOMCODE,CHNLCOD, sum(factvalue),
  sum(accountvalue),ROUND(DECODE(sum(accountvalue),0,0,sum(factvalue)/sum(accountvalue)),2)
    from
  (
  --实收
  select PROVINCECOMCODE,CHNLCOD,substr(FINMONTH,-1,1) FINMONTH,0 accountvalue,
  sum(MON13_REALPREM_MON_CUR)+sum(MON25_REALPREM_MON_CUR)+sum(MON37_REALPREM_MON_CUR)  factvalue
   from
  (
  SELECT
    Table__2.PROVINCECOMCODE PROVINCECOMCODE,
    E_BDS_BIDM_RENEWAL_RP_FY.FINMONTH,
     case when E_BDS_BIDM_RENEWAL_RP_FY.MON13_REALPREM_MON_CUR is null then 0
         else E_BDS_BIDM_RENEWAL_RP_FY.MON13_REALPREM_MON_CUR
    end MON13_REALPREM_MON_CUR,
    case when E_BDS_BIDM_RENEWAL_RP_FY.MON25_REALPREM_MON_CUR is null then 0
         else E_BDS_BIDM_RENEWAL_RP_FY.MON25_REALPREM_MON_CUR
    end MON25_REALPREM_MON_CUR,
    case when E_BDS_BIDM_RENEWAL_RP_FY.MON37_REALPREM_MON_CUR is null then 0
         else E_BDS_BIDM_RENEWAL_RP_FY.MON37_REALPREM_MON_CUR
    end MON37_REALPREM_MON_CUR,
    D_CHNL.CHNLCOD CHNLCOD
  FROM
    (
    select * from E_BDS_BIDM_MGECMP  a where a.innerorgcode !=''86''
    )  Table__2,
    (
    select s.FINMONTH,
         s.INNERORGCODE,
         s.ADD_CHNLCOD,
  /*       y.MON13_INSTPREM_MON_cur,*/
         y.MON13_REALPREM_MON_cur,
  /*       y.MON13_INSTPREM_cur,*/
         y.MON13_REALPREM_cur,
  /*       y.MON25_INSTPREM_MON_cur,*/
         y.MON25_REALPREM_MON_cur,
  /*       y.MON25_INSTPREM_cur,*/
         y.MON25_REALPREM_cur,
   /*      y.MON37_INSTPREM_MON_cur,*/
         y.MON37_REALPREM_MON_cur,
  /*       y.MON37_INSTPREM_cur,*/
         y.MON37_REALPREM_cur/*,
         y.MON13_INSTPREM_MON_last1,
         y.MON13_REALPREM_MON_last1,
         y.MON25_INSTPREM_MON_last1,
         y.MON25_REALPREM_MON_last1,
         y.MON37_INSTPREM_MON_last1,
         y.MON37_REALPREM_MON_last1,
         y.MON13_INSTPREM_MON_last2,
         y.MON13_REALPREM_MON_last2,
         y.MON25_INSTPREM_MON_last2,
         y.MON25_REALPREM_MON_last2,
         y.MON37_INSTPREM_MON_last2,
         y.MON37_REALPREM_MON_last2*/

    from (
          SELECT To_Char(FINMONTH) as FINMONTH,
                  E_BDS_BIDM_RENEWAL_RP_FY.innerorgcode as INNERORGCODE,
                  d_chnl.chnlcod AS ADD_CHNLCOD
            from E_BDS_BIDM_RENEWAL_RP_FY,
                 /* (select distinct innerorgcode from E_BDS_BIDM_RENEWAL_RP_FY) E_BDS_BIDM_MGECMP,*/
                  (select ''FC'' chnlcod
                     from dual
                   union all
                   select ''BK'' chnlcod
                     from dual
                   union all
                   select ''AD'' chnlcod from dual
                   union all
                   select ''RP'' chnlcod from dual) d_chnl
           group by To_Char(FINMONTH),
                     E_BDS_BIDM_RENEWAL_RP_FY.innerorgcode,
                     d_chnl.chnlcod) s,
         (select FINMONTH,

                 INNERORGCODE,
                 ADD_CHNLCOD,
                 /*sum(MON13_INSTPREM_MON_cur) as MON13_INSTPREM_MON_cur,*/
                 sum(MON13_REALPREM_MON_cur) as MON13_REALPREM_MON_cur,
                /* sum(MON13_INSTPREM_cur) as MON13_INSTPREM_cur,*/
                 sum(MON13_REALPREM_cur) as MON13_REALPREM_cur,
                -- sum(MON25_INSTPREM_MON_cur) as MON25_INSTPREM_MON_cur,
                 sum(MON25_REALPREM_MON_cur) as MON25_REALPREM_MON_cur,
                 -- sum(MON25_INSTPREM_cur) as MON25_INSTPREM_cur,
                 sum(MON25_REALPREM_cur) as MON25_REALPREM_cur,
                --  sum(MON37_INSTPREM_MON_cur) as MON37_INSTPREM_MON_cur,
                 sum(MON37_REALPREM_MON_cur) as MON37_REALPREM_MON_cur,
                --- sum(MON37_INSTPREM_cur) as MON37_INSTPREM_cur,
                 sum(MON37_REALPREM_cur) as MON37_REALPREM_cur/*,
                 sum(MON13_INSTPREM_MON_last1) as MON13_INSTPREM_MON_last1,
                 sum(MON13_REALPREM_MON_last1) as MON13_REALPREM_MON_last1,
                 sum(MON25_INSTPREM_MON_last1) as MON25_INSTPREM_MON_last1,
                 sum(MON25_REALPREM_MON_last1) as MON25_REALPREM_MON_last1,
                 sum(MON37_INSTPREM_MON_last1) as MON37_INSTPREM_MON_last1,
                 sum(MON37_REALPREM_MON_last1) as MON37_REALPREM_MON_last1,
                 sum(MON13_INSTPREM_MON_last2) as MON13_INSTPREM_MON_last2,
                 sum(MON13_REALPREM_MON_last2) as MON13_REALPREM_MON_last2,
                 sum(MON25_INSTPREM_MON_last2) as MON25_INSTPREM_MON_last2,
                 sum(MON25_REALPREM_MON_last2) as MON25_REALPREM_MON_last2,
                 sum(MON37_INSTPREM_MON_last2) as MON37_INSTPREM_MON_last2,
                 sum(MON37_REALPREM_MON_last2) as MON37_REALPREM_MON_last2*/
            from (SELECT To_Char(Add_Months(To_Date(FINMONTH, ''yyyymm''), +2),
                                 ''yyyymm'') as FINMONTH,
                         INNERORGCODE,
                         ADD_CHNLCOD,
                        -- MON13_INSTPREM_MON as MON13_INSTPREM_MON_cur,
                         MON13_REALPREM_MON as MON13_REALPREM_MON_cur,
                        -- MON13_INSTPREM as MON13_INSTPREM_cur,
                         MON13_REALPREM as MON13_REALPREM_cur,
                        -- MON25_INSTPREM_MON as MON25_INSTPREM_MON_cur,
                         MON25_REALPREM_MON as MON25_REALPREM_MON_cur,
                        -- MON25_INSTPREM as MON25_INSTPREM_cur,
                         MON25_REALPREM as MON25_REALPREM_cur,
                        -- MON37_INSTPREM_MON as MON37_INSTPREM_MON_cur,
                         MON37_REALPREM_MON as MON37_REALPREM_MON_cur,
                       --  MON37_INSTPREM as MON37_INSTPREM_cur,
                         MON37_REALPREM as MON37_REALPREM_cur/*,
                         0 MON13_INSTPREM_MON_last1,
                         0 MON13_REALPREM_MON_last1,
                         0 MON25_INSTPREM_MON_last1,
                         0 MON25_REALPREM_MON_last1,
                         0 MON37_INSTPREM_MON_last1,
                         0 MON37_REALPREM_MON_last1,
                         0 MON13_INSTPREM_MON_last2,
                         0 MON13_REALPREM_MON_last2,
                         0 MON25_INSTPREM_MON_last2,
                         0 MON25_REALPREM_MON_last2,
                         0 MON37_INSTPREM_MON_last2,
                         0 MON37_REALPREM_MON_last2*/

                    from E_BDS_BIDM_RENEWAL_RP_FY
                 ) t
           GROUP BY FINMONTH, INNERORGCODE, ADD_CHNLCOD) y
   where s.FINMONTH = y.FINMONTH(+)
     and s.INNERORGCODE = y.INNERORGCODE(+)
     and s.ADD_CHNLCOD = y.ADD_CHNLCOD(+)
    )  E_BDS_BIDM_RENEWAL_RP_FY,
    (
    select * from E_BDS_BIDM_CHNLCOD t where t.flg=''0'' and t.chnlcod<>''GR''
    )  D_CHNL,
    /*(
    select * from E_BDS_BIDM_MGECMP  a where a.innerorgcode !=''86''
    )  E_BDS_BIDM_MGECMP1,*/
    E_BDS_BIDM_MGECMP_MAP,
    (
    select * from E_BDS_BIDM_CHNL_MAP t
  where t.FLG=''0''
  and t.ADD_CHNLCOD<>''GR''
    )  E_BDS_BIDM_CHNL_MAP
  WHERE
    ( Table__2.INNERORGCODE=E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE  )
    AND  ( E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE=Table__2.INNERORGCODE(+)  )
  /*  AND  ( E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE=E_BDS_BIDM_MGECMP1.INNERORGCODE(+)  )*/
    AND  ( D_CHNL.ADD_CHNLCOD=E_BDS_BIDM_RENEWAL_RP_FY.ADD_CHNLCOD  )
    AND  ( E_BDS_BIDM_CHNL_MAP.ADD_CHNLCOD=D_CHNL.ADD_CHNLCOD  )
    AND
    (
     E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE  =  ''86''
     AND
     to_date(E_BDS_BIDM_RENEWAL_RP_FY.FINMONTH,''yyyymm'')  
     BETWEEN to_date(''' || V_EVAL_STARTTIME ||
               ''',''yyyymm'') 
     AND to_date(''' || V_EVAL_ENDTIME || ''',''yyyymm'')
     AND E_BDS_BIDM_CHNL_MAP.CHNLCOD  =  ''ALL'')
  )t
  GROUP BY PROVINCECOMCODE,CHNLCOD,substr(FINMONTH,-1,1)
  UNION ALL
  ----应收
  SELECT 
         PROVINCECOMCODE,
         CHNLCOD,
         substr(FINMONTH,-1,1) FINMONTH,
         SUM(MON13_INSTPREM_MON_CUR) + SUM(MON25_INSTPREM_MON_CUR) +
         SUM(MON37_INSTPREM_MON_CUR) ,
         0 
    FROM (SELECT TABLE__2.PROVINCECOMCODE PROVINCECOMCODE,
    E_BDS_BIDM_RENEWAL_RP_FY.FINMONTH,
                 CASE
                   WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON13_INSTPREM_MON_CUR IS NULL THEN
                    0
                   ELSE
                    E_BDS_BIDM_RENEWAL_RP_FY.MON13_INSTPREM_MON_CUR
                 END MON13_INSTPREM_MON_CUR,
                 CASE
                   WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON25_INSTPREM_MON_CUR IS NULL THEN
                    0
                   ELSE
                    E_BDS_BIDM_RENEWAL_RP_FY.MON25_INSTPREM_MON_CUR
                 END MON25_INSTPREM_MON_CUR,
                 CASE
                   WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON37_INSTPREM_MON_CUR IS NULL THEN
                    0
                   ELSE
                    E_BDS_BIDM_RENEWAL_RP_FY.MON37_INSTPREM_MON_CUR
                 END MON37_INSTPREM_MON_CUR,
                 D_CHNL.CHNLCOD CHNLCOD
            FROM (SELECT *
                    FROM E_BDS_BIDM_MGECMP A
                   WHERE A.INNERORGCODE != ''86'') TABLE__2,
                 (
                 SELECT S.FINMONTH,
                         S.INNERORGCODE,
                         S.ADD_CHNLCOD,
                         Y.MON13_INSTPREM_MON_CUR,
                         /*   -- Y.MON13_REALPREM_MON_CUR,
                         Y.MON13_INSTPREM_CUR,*/
                         -- Y.MON13_REALPREM_CUR,
                         Y.MON25_INSTPREM_MON_CUR,
                         -- Y.MON25_REALPREM_MON_CUR,
                         /*                  Y.MON25_INSTPREM_CUR,*/
                         -- Y.MON25_REALPREM_CUR,
                         Y.MON37_INSTPREM_MON_CUR
                  -- Y.MON37_REALPREM_MON_CUR,
                  /*        Y.MON37_INSTPREM_CUR*/
                  --  Y.MON37_REALPREM_CUR,
                  --  Y.MON13_INSTPREM_MON_LAST1,
                  --  Y.MON13_REALPREM_MON_LAST1,
                  /*       -  Y.MON25_INSTPREM_MON_LAST1,
                  Y.MON25_REALPREM_MON_LAST1,
                  Y.MON37_INSTPREM_MON_LAST1,
                  Y.MON37_REALPREM_MON_LAST1,
                  Y.MON13_INSTPREM_MON_LAST2,
                  Y.MON13_REALPREM_MON_LAST2,
                  Y.MON25_INSTPREM_MON_LAST2,
                  Y.MON25_REALPREM_MON_LAST2,
                  Y.MON37_INSTPREM_MON_LAST2,
                  Y.MON37_REALPREM_MON_LAST2*/
                  
                    FROM (
                    SELECT TO_CHAR(FINMONTH) AS FINMONTH,
                                 E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE AS INNERORGCODE,
                                 D_CHNL.CHNLCOD AS ADD_CHNLCOD
                            FROM E_BDS_BIDM_RENEWAL_RP_FY,
                                 /*(SELECT DISTINCT INNERORGCODE
                                    FROM E_BDS_BIDM_RENEWAL_RP_FY) E_BDS_BIDM_MGECMP,*/
                                 (SELECT ''FC'' CHNLCOD
                                    FROM DUAL
                                  UNION ALL
                                  SELECT ''BK'' CHNLCOD
                                    FROM DUAL
                                  UNION ALL
                                  SELECT ''AD'' CHNLCOD
                                    FROM DUAL
                                  UNION ALL
                                  SELECT ''RP'' CHNLCOD
                                    FROM DUAL) D_CHNL
                           WHERE  /*E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE=E_BDS_BIDM_MGECMP.INNERORGCODE
                            AND*/ E_BDS_BIDM_RENEWAL_RP_FY.ADD_CHNLCOD=D_CHNL.CHNLCOD
                           GROUP BY TO_CHAR(FINMONTH),
                                    E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE,
                                    D_CHNL.CHNLCOD) S,
                         (SELECT FINMONTH,
                                 
                                 INNERORGCODE,
                                 ADD_CHNLCOD,
                                 SUM(MON13_INSTPREM_MON_CUR) AS MON13_INSTPREM_MON_CUR,
                                 --SUM(MON13_REALPREM_MON_CUR) AS MON13_REALPREM_MON_CUR,
                                 /*   SUM(MON13_INSTPREM_CUR) AS MON13_INSTPREM_CUR,
                                 --  SUM(MON13_REALPREM_CUR) AS MON13_REALPREM_CUR,*/
                                 SUM(MON25_INSTPREM_MON_CUR) AS MON25_INSTPREM_MON_CUR,
                                 /*               -- SUM(MON25_REALPREM_MON_CUR) AS MON25_REALPREM_MON_CUR,
                                 SUM(MON25_INSTPREM_CUR) AS MON25_INSTPREM_CUR,*/
                                 -- SUM(MON25_REALPREM_CUR) AS MON25_REALPREM_CUR,
                                 SUM(MON37_INSTPREM_MON_CUR) AS MON37_INSTPREM_MON_CUR
                          --  SUM(MON37_REALPREM_MON_CUR) AS MON37_REALPREM_MON_CUR,
                          /*  SUM(MON37_INSTPREM_CUR) AS MON37_INSTPREM_CUR*/
                          --   SUM(MON37_REALPREM_CUR) AS MON37_REALPREM_CUR,
                          /*  SUM(MON13_INSTPREM_MON_LAST1) AS MON13_INSTPREM_MON_LAST1,
                          SUM(MON13_REALPREM_MON_LAST1) AS MON13_REALPREM_MON_LAST1,
                          SUM(MON25_INSTPREM_MON_LAST1) AS MON25_INSTPREM_MON_LAST1,
                          SUM(MON25_REALPREM_MON_LAST1) AS MON25_REALPREM_MON_LAST1,
                          SUM(MON37_INSTPREM_MON_LAST1) AS MON37_INSTPREM_MON_LAST1,
                          SUM(MON37_REALPREM_MON_LAST1) AS MON37_REALPREM_MON_LAST1,
                          SUM(MON13_INSTPREM_MON_LAST2) AS MON13_INSTPREM_MON_LAST2,
                          SUM(MON13_REALPREM_MON_LAST2) AS MON13_REALPREM_MON_LAST2,
                          SUM(MON25_INSTPREM_MON_LAST2) AS MON25_INSTPREM_MON_LAST2,
                          SUM(MON25_REALPREM_MON_LAST2) AS MON25_REALPREM_MON_LAST2,
                          SUM(MON37_INSTPREM_MON_LAST2) AS MON37_INSTPREM_MON_LAST2,
                          SUM(MON37_REALPREM_MON_LAST2) AS MON37_REALPREM_MON_LAST2*/
                            FROM (SELECT TO_CHAR(ADD_MONTHS(TO_DATE(FINMONTH,
                                                                    ''yyyymm''),
                                                            +2),
                                                 ''yyyymm'') AS FINMONTH,
                                         INNERORGCODE,
                                         ADD_CHNLCOD,
                                         MON13_INSTPREM_MON AS MON13_INSTPREM_MON_CUR,
                                         -- MON13_REALPREM_MON AS MON13_REALPREM_MON_CUR,
                                         /*    MON13_INSTPREM AS MON13_INSTPREM_CUR,*/
                                         --  MON13_REALPREM AS MON13_REALPREM_CUR,
                                         MON25_INSTPREM_MON AS MON25_INSTPREM_MON_CUR,
                                         -- MON25_REALPREM_MON AS MON25_REALPREM_MON_CUR,
                                         /*    MON25_INSTPREM AS MON25_INSTPREM_CUR,*/
                                         -- MON25_REALPREM AS MON25_REALPREM_CUR,
                                         MON37_INSTPREM_MON AS MON37_INSTPREM_MON_CUR
                                  --  MON37_REALPREM_MON AS MON37_REALPREM_MON_CUR,
                                  /*       MON37_INSTPREM AS MON37_INSTPREM_CUR*/
                                  --   MON37_REALPREM AS MON37_REALPREM_CUR
                                  /* 0 MON13_INSTPREM_MON_LAST1,
                                  0 MON13_REALPREM_MON_LAST1,
                                  0 MON25_INSTPREM_MON_LAST1,
                                  0 MON25_REALPREM_MON_LAST1,
                                  0 MON37_INSTPREM_MON_LAST1,
                                  0 MON37_REALPREM_MON_LAST1,
                                  0 MON13_INSTPREM_MON_LAST2,
                                  0 MON13_REALPREM_MON_LAST2,
                                  0 MON25_INSTPREM_MON_LAST2,
                                  0 MON25_REALPREM_MON_LAST2,
                                  0 MON37_INSTPREM_MON_LAST2,
                                  0 MON37_REALPREM_MON_LAST2*/
                                  
                                    FROM E_BDS_BIDM_RENEWAL_RP_FY) T
                           GROUP BY FINMONTH, INNERORGCODE, ADD_CHNLCOD) Y
                   WHERE S.FINMONTH = Y.FINMONTH(+)
                     AND S.INNERORGCODE = Y.INNERORGCODE(+)
                     AND S.ADD_CHNLCOD = Y.ADD_CHNLCOD(+)) E_BDS_BIDM_RENEWAL_RP_FY,
                 (SELECT CHNLCOD,ADD_CHNLCOD
                    FROM E_BDS_BIDM_CHNLCOD T
                   WHERE T.FLG = ''0''
                     AND T.CHNLCOD <> ''GR'') D_CHNL,
                 /*(SELECT *
                  FROM E_BDS_BIDM_MGECMP A
                 WHERE A.INNERORGCODE != ''86'') E_BDS_BIDM_MGECMP1,*/
                 E_BDS_BIDM_MGECMP_MAP,
                 (SELECT ADD_CHNLCOD,CHNLCOD
                    FROM E_BDS_BIDM_CHNL_MAP T
                   WHERE T.FLG = ''0''
                     AND T.ADD_CHNLCOD <> ''GR'') E_BDS_BIDM_CHNL_MAP
           WHERE (TABLE__2.INNERORGCODE = E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE)
             AND (E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE =
                 TABLE__2.INNERORGCODE(+))
                /* AND (E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE =
                E_BDS_BIDM_MGECMP1.INNERORGCODE(+))*/
             AND (D_CHNL.ADD_CHNLCOD = E_BDS_BIDM_RENEWAL_RP_FY.ADD_CHNLCOD)
             AND (E_BDS_BIDM_CHNL_MAP.ADD_CHNLCOD = D_CHNL.ADD_CHNLCOD)
             AND (E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE = ''86'' AND
                 TO_DATE(E_BDS_BIDM_RENEWAL_RP_FY.FINMONTH, ''yyyymm'')BETWEEN to_date(''' ||
               V_EVAL_STARTTIME || ''',''yyyymm'') 
     AND to_date(''' || V_EVAL_ENDTIME ||
               ''',''yyyymm'')
     AND
     E_BDS_BIDM_CHNL_MAP.CHNLCOD  =  ''ALL''
    )
  )t
  group by PROVINCECOMCODE,CHNLCOD,substr(FINMONTH,-1,1) 
  ) b
  group by FINMONTH,PROVINCECOMCODE,CHNLCOD
  order by FINMONTH,PROVINCECOMCODE
   ';
  EXECUTE IMMEDIATE V_SQL;
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
     'SP_MDS_DTL_CONT_CHARGE_RATE',
     'E_MDS_CONTINUE_CHARGE_RATE',
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
         'SP_MDS_DTL_CONT_CHARGE_RATE',
         'E_MDS_CONTINUE_CHARGE_RATE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
      /**************************************** 加载日志信息完成 ****************************************/
    END;
END SP_MDS_DTL_CONT_CHARGE_RATE;

/
CREATE PROCEDURE SP_MDS_DTL_CONT_VISIT_RATE(V_INDATE  IN  VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_MDS_DTL_CONT_VISIT_RATE  新契约回访            */
  /* Developed By   : SY                                                */
  /* Developed Date : 2018-11-28                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_CONTRACT_VISIT_RATE                         */
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
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                 'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL := 'TRUNCATE TABLE E_MDS_CONTRACT_VISIT_RATE';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_MDS_CONTRACT_VISIT_RATE(
             UUID,
             CHANNEL,
             ORG_NAME,
             VISIT_NUM,
             POLICY_NUM,
             RESULT_RATE)
SELECT SYS_GUID(),
       CHANNEL,
       PROVINCECOMCODE,
       SUM(C_CNT),
       SUM(S_CNT),
       CASE WHEN SUM(C_CNT) IS NULL THEN NULL 
            WHEN SUM(C_CNT) = 0 THEN NULL ELSE ROUND((SUM(C_CNT) / SUM(S_CNT)), 2) END 
  FROM (SELECT PROVINCECOMCODE, CHANNEL, COUNT(NVL(CHDRNUM, 0)) AS C_CNT, NULL AS S_CNT
  FROM (SELECT BASE.COMPANYNO || BASE.BRANCH CHDRCOY,
               BASE.CHANNEL,
               BASE.ENDHESDATE,
               A.CHDRCOYID, --机构号
               A.CHDRNUM, --保单号
               A.CODE, --产品代码
               A.OCCDATE, --保单生效日
               A.HOISSDTE, --保单签发日
               A.AGTYPE, --渠道
               A.STATUS, --工单状态
               A.BACRS, --回访结果
               A.VSLSTATUS, --亲访信状态
               A.VSLRESULT, --亲访信回访结果
               A.ZACKDTE, --回执签收日
               A.CBLSTATUS, --回访信状态
               B.PROVINCECOMCODE
          FROM E_BDS_CCCMP_POLICYS_BASE BASE
          LEFT JOIN E_BDS_CCNT_CRM_CASE3 A
            ON A.CHDRCOYID = BASE.COMPANYNO || BASE.BRANCH
           AND A.CHDRNUM = BASE.CHDRNUM
         INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
            ON BASE.POLICYNO = INDI.POLICYNO
         INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
            ON BASE.POLICYNO = CASE3.POLICYNO
          LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = BASE.COMPANYNO
         WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN 
               to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
               to_date('''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
           AND INDI.ISCFI = ''0''
           AND INDI.ISWD = ''0''
           AND BASE.ZACKDTE IS NOT NULL
           AND BASE.ZACKDTE != ''99999999''
           AND BASE.NEEDCALLBACK = ''1''
           AND BASE.CNTTYPE NOT LIKE ''PA%''
           AND CASE3.BACRS NOT IN (''1'', ''10'')
           AND NOT
                (CASE3.DATIME LIKE ''% 0 :00AM'' OR CASE3.DATIME LIKE ''%000000'')
           AND BASE.CHANNEL IN (''FC'', ''BK'', ''AD'', ''RP'', ''GB'')) T
 WHERE BACRS IN (''2'', ''9'')
    OR VSLRESULT = ''0''
 GROUP BY PROVINCECOMCODE, CHANNEL
        UNION ALL
        SELECT B.PROVINCECOMCODE,
               BASE.CHANNEL,
               NULL AS C_CNT,
               COUNT(NVL(BASE.POLICYNO, 0)) S_CNT
          FROM E_BDS_CCCMP_POLICYS_BASE BASE
         INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
            ON BASE.POLICYNO = INDI.POLICYNO
         INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
            ON BASE.POLICYNO = CASE3.POLICYNO
          LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = BASE.COMPANYNO
         WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN 
               to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
               to_date('''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
           AND INDI.ISCFI = ''0''
           AND INDI.ISWD = ''0''
           AND BASE.ZACKDTE IS NOT NULL
           AND BASE.ZACKDTE != ''99999999''
           AND BASE.NEEDCALLBACK = ''1''
           AND BASE.CNTTYPE NOT LIKE ''PA%''
           AND CASE3.BACRS NOT IN (''1'', ''10'')
           AND NOT
                (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
         GROUP BY B.PROVINCECOMCODE, BASE.CHANNEL) C
 GROUP BY CHANNEL,PROVINCECOMCODE
 ORDER BY PROVINCECOMCODE,CHANNEL';
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
     '',
     'SP_MDS_DTL_CONT_VISIT_RATE',
     'E_MDS_CONTRACT_VISIT_RATE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------异常处理-----------------------------------------
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
         'SP_MDS_DTL_CONT_VISIT_RATE',
         'E_MDS_CONTRACT_VISIT_RATE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_MDS_DTL_CONT_VISIT_RATE;

/
CREATE PROCEDURE SP_MDS_DTL_EMPLOYEE_LOSS(V_INDATE  IN  VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_MDS_DTL_EMPLOYEE_LOSS  员工流失率              */
  /* Developed By   : WH                                                */
  /* Developed Date : 2019-01-28                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_EMPLOYEE_LOSS                               */
  /********************** Variable Section ******************************/
  V_EVAL_ENDTIME   VARCHAR2(10);    --评估期结束时间
  V_EVAL_DATE      VARCHAR2(10);    --评估期前12个月 
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
  V_RECORD_NUM     NUMBER;          --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  V_EVAL_DATE      := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),'YYYYMMDD'),-11),'YYYYMMDD'),1,6);
  --------------------------------删除数据-------------------------------
  V_SQL :='TRUNCATE TABLE E_MDS_EMPLOYEE_LOSS';
  EXECUTE IMMEDIATE V_SQL;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_MDS_EMPLOYEE_LOSS(
             UUID,
             CHANNEL,
             ORG_NAME,
             QUITNUM,
             EMPLOYEE_NUM,
             ADD_NUM)
SELECT SYS_GUID(),
       CHANNEL,
       ORG,
       SUM(CNT1),
       SUM(CNT2),
       SUM(CNT3)
FROM (
--最近4个季度省级分公司及以下分支机构销售、承保、保全部门离职员工人数
 SELECT 
            C.CODE org,
           DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',B.CODE) CHANNEL ,
           SUM(A3) cnt1,
           0 cnt2,
           0 cnt3
 FROM E_BDS_HRM_EMPLOYEESTATE A
 INNER JOIN (SELECT DISTINCT CODE 
             FROM E_BDS_HRM_ORGANIZATION 
       WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') 
       AND C_NAME IS NOT NULL 
       AND TYPEID=''4'') B --RP_BRD 续期 AD 多元 BD 银保 GD 团险 CS 客服 ID IDSD IDSS IDSUD IDT SFD SUD 个险B
    ON A.ORGCODE4 = B.CODE
  LEFT JOIN  E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
 WHERE a.orgcode2 <> ''HO''
  AND SUBSTR(A.DATA_DATE,1,6) BETWEEN '''||V_EVAL_DATE||''' AND SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
 GROUP BY C.CODE,B.CODE
UNION ALL
--前4个季度初省级分公司及以下分支机构销售、承保、保全部门员工人数
 SELECT 
            C.CODE,
           DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',B.CODE) CHANNEL ,
           0 cnt1,
           SUM(A) cnt2,
           0 cnt3
  FROM E_BDS_HRM_EMPLOYEESTATE A
  INNER JOIN (SELECT DISTINCT CODE 
              FROM E_BDS_HRM_ORGANIZATION 
			  WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') --RP_BRD 续期 AD 多元 BD 银保 GD 团险 CS 客服 ID IDSD IDSS IDSUD IDT SFD SUD 个险
			  AND TYPEID = ''4'') B
    ON A.ORGCODE4 = B.CODE
  LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
 WHERE a.orgcode2 <> ''HO''
AND SUBSTR(A.DATA_DATE,1,6) = '''||V_EVAL_DATE||'''
 GROUP BY C.CODE,B.CODE
UNION ALL
--最近4个季度省级分公司及以下分支机构销售、承保、保全部门增加员工人数
  SELECT 
            C.CODE,
           DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',B.CODE) CHANNEL ,
           0 cnt1,
           0 cnt2,
           SUM(A2) cnt3 
  FROM E_BDS_HRM_EMPLOYEESTATE A
  INNER JOIN (SELECT DISTINCT CODE 
              FROM E_BDS_HRM_ORGANIZATION 
			  WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') --RP_BRD 续期 AD 多元 BD 银保 GD 团险 CS 客服 ID IDSD IDSS IDSUD IDT SFD SUD 个险
              AND C_NAME IS NOT NULL 
              AND TYPEID=''4'') B
    ON A.ORGCODE4 = B.CODE
  LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
 WHERE a.orgcode2 <> ''HO''
 AND SUBSTR(A.DATA_DATE,1,6) BETWEEN  '''||V_EVAL_DATE||''' AND SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
 GROUP BY C.CODE,B.CODE
) t 
 GROUP BY org,channel
   ORDER BY org
 ';
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
     '',
     'SP_MDS_DTL_EMPLOYEE_LOSS',
     'E_MDS_EMPLOYEE_LOSS',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------异常处理-----------------------------------------
  EXCEPTION
    WHEN OTHERS THEN
      BEGIN
        ROLLBACK;
        V_END     := SYSDATE;
        V_RTNCODE := '1';
        V_RTNMSG  := SQLERRM;
  -----------------------------------加载日志信息———--------------------------------
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
         'SP_MDS_DTL_EMPLOYEE_LOSS',
         'E_MDS_EMPLOYEE_LOSS',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_MDS_DTL_EMPLOYEE_LOSS;

/
CREATE PROCEDURE SP_MDS_DTL_HESI_VISIT_RATE(V_INDATE  IN  VARCHAR2,
                                                       V_RTNCODE OUT VARCHAR2,
                                                       V_RTNMSG  OUT VARCHAR2) AS

  /**********************************************************************/
  /* Procedure Name : SP_MDS_DTL_HESI_VISIT_RATE  犹豫期内电话回访      */
  /* Developed By   : SY                                                */
  /* Developed Date : 2018-11-28                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_HESITATE_VISIT_RATE                         */
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
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                'yyyymmdd') + 1,-3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------删除数据-------------------------------
  V_SQL := 'TRUNCATE TABLE E_MDS_HESITATE_VISIT_RATE';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------插入数据--------------------------------
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_MDS_HESITATE_VISIT_RATE(
             UUID,
             CHANNEL,
             ORG_NAME,
             VISIT_NUM,
             POLICY_NUM,
             RESULT_RATE)
SELECT SYS_GUID(),
       CHANNEL,
       PROVINCECOMCODE,
       SUM(C_CNT),
       SUM(S_CNT),
       CASE WHEN SUM(C_CNT) IS NULL  THEN NULL 
            WHEN SUM(C_CNT) = 0 THEN NULL ELSE ROUND((SUM(C_CNT) / SUM(S_CNT)), 2) END 
  FROM (SELECT PROVINCECOMCODE,
               CHANNEL,
               COUNT(NVL(POLICYNO, 0)) C_CNT,
               NULL AS S_CNT
          FROM (SELECT BASE.AGENTNO,
                       B.PROVINCECOMCODE,
                       BASE.BRANCH,
                       BASE.CHANNEL,
                       BASE.ENDHESDATE,
                       BASE.CHDRNUM,
                       BASE.POLICYNO,
                       MIN(CBLIST.CALLBACKDATE) CALLBACKDATE,
                       BASE.ARACDE1
                  FROM E_BDS_CCCMP_POLICYS_BASE BASE
                 INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
                    ON BASE.POLICYNO = INDI.POLICYNO
                 INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
                    ON BASE.POLICYNO = CASE3.POLICYNO
                 INNER JOIN E_BDS_CCCMP_CRM_CALL_LIST CBLIST
                    ON CBLIST.POLICYNO = BASE.POLICYNO
                  LEFT JOIN E_BDS_BIDM_MGECMP B
                    ON B.LAORIGORGCODE = BASE.COMPANYNO
                 WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN 
                       to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
                       to_date('''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
                   AND INDI.ISCFI = ''0'' --剔除CFI
                   AND INDI.ISWD = ''0'' --剔除WD
                   AND BASE.ZACKDTE IS NOT NULL --回执日期
                   AND BASE.ZACKDTE != ''99999999''
                   AND BASE.NEEDCALLBACK = ''1'' --是否需要回访
                   AND BASE.CNTTYPE NOT LIKE ''PA%''
                   AND NOT (CASE3.DATIME LIKE ''% 0:00AM'' OR
                            CASE3.DATIME LIKE ''%000000'')
                   AND CASE3.BACRS NOT IN (''0'', ''1'') --回访结果 0重复创建     1未回电已CFI
                   AND CBLIST.CALLBACKRESULT IN
                       (''电话回访完全干净件'',
                        ''电话回访问题件'',
                        ''电话回访-CS干净件'',
                        ''电话回访CS干净件'',
                        ''电话回访完全干净件非投操作'',
                        ''电话回访CS干净件非投操作'',
                        ''电话回访问题件非投操作'',
                        ''亲访信回访干净件'',
                        ''亲访信回访问题件非投操作'',
                        ''问题件-投保未双录'')
                 GROUP BY B.PROVINCECOMCODE,
                          BASE.BRANCH,
                          BASE.CHANNEL,
                          BASE.ENDHESDATE,
                          BASE.CHDRNUM,
                          BASE.AGENTNO,
                          BASE.ARACDE1,
                          BASE.POLICYNO) T
         WHERE T.ENDHESDATE >=
               SUBSTR(T.CALLBACKDATE, 0, 4) || SUBSTR(T.CALLBACKDATE, 6, 2) ||
               SUBSTR(T.CALLBACKDATE, 9, 2)
         GROUP BY PROVINCECOMCODE, CHANNEL
        UNION ALL
        SELECT B.PROVINCECOMCODE,
               BASE.CHANNEL,
               NULL AS C_CNT,
               COUNT(NVL(BASE.POLICYNO, 0)) S_CNT
          FROM E_BDS_CCCMP_POLICYS_BASE BASE
         INNER JOIN E_BDS_CCCMP_POL_INDICATOR INDI
            ON BASE.POLICYNO = INDI.POLICYNO
         INNER JOIN E_BDS_CCCMP_CRM_CASE3 CASE3
            ON BASE.POLICYNO = CASE3.POLICYNO
          LEFT JOIN E_BDS_BIDM_MGECMP B
            ON B.LAORIGORGCODE = BASE.COMPANYNO
         WHERE TO_DATE(BASE.OCCDATE,''yyyy-mm-dd'') BETWEEN 
               to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
               to_date('''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')
           AND INDI.ISCFI = ''0''
           AND INDI.ISWD = ''0''
           AND BASE.ZACKDTE IS NOT NULL
           AND BASE.ZACKDTE != ''99999999''
           AND BASE.NEEDCALLBACK = ''1''
           AND BASE.CNTTYPE NOT LIKE ''PA%''
           AND CASE3.BACRS NOT IN (''1'', ''10'')
           AND NOT
                (CASE3.DATIME LIKE ''% 0:00AM'' OR CASE3.DATIME LIKE ''%000000'')
         GROUP BY B.PROVINCECOMCODE, BASE.CHANNEL) C
 GROUP BY CHANNEL,PROVINCECOMCODE
 ORDER BY PROVINCECOMCODE,CHANNEL';
DBMS_OUTPUT.PUT_LINE(V_SQL);
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
     '',
     'SP_MDS_DTL_HESI_VISIT_RATE',
     'E_MDS_HESITATE_VISIT_RATE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --------------------------------异常处理-----------------------------------------
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
         'SP_MDS_DTL_HESI_VISIT_RATE',
         'E_MDS_HESITATE_VISIT_RATE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
      END;
END SP_MDS_DTL_HESI_VISIT_RATE;

/
CREATE PROCEDURE SP_MDS_DTL_RETREAT_RATE(V_INDATE  IN VARCHAR2,
                                  V_RTNCODE OUT VARCHAR2,
                                  V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* PROCEDURE NAME : SP_MDS_DTL_RETREAT_RATE  退撤保率                 */
  /* DEVELOPED BY   : WH                                                */
  /* DEVELOPED DATE : 2018-11-28                                        */
  /************************ SOUCE TABLE *********************************/
  /* TARGET TABLE   : E_MDS_RETREAT_RATE                                */
  /********************** VARIABLE SECTION ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(30000); --动态语句
  /***********************************************************************/
BEGIN
DBMS_OUTPUT.ENABLE(1000000);
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'YYYYMMDD') + 1,
                                         -3),
                              'YYYYMMDD');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --清除旧数据
  V_SQL:='TRUNCATE TABLE E_MDS_RETREAT_RATE';
  EXECUTE IMMEDIATE V_SQL;
  
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL:= 'INSERT INTO E_MDS_RETREAT_RATE
(UUID	
,BRANCH_COM	
,CHANNEL	
,RETREAT_AMT	
,REVOKE_AMT	
,RECEIPTS_AMT	
,RECEIVABLE_AMT	
,RESULT_RATE
)	
select
 sys_guid()
,provincecomcode
,CHANNEL
,sum(RETREAT_AMT)	
,sum(REVOKE_AMT)	
,sum(RECEIPTS_AMT)	
,sum(RECEIVABLE_AMT)	
,ROUND(DECODE(sum(RECEIPTS_AMT)+sum(RECEIVABLE_AMT),0,0,(sum(RETREAT_AMT)+sum(REVOKE_AMT))/(sum(RECEIPTS_AMT)+sum(RECEIVABLE_AMT))),2)
from (
--退保金
SELECT 
           provincecomcode,
           CHANNEL_CODE CHANNEL,
           SUM(FIN_AMT) RETREAT_AMT,
           0 REVOKE_AMT,
           0 RECEIPTS_AMT,
           0 RECEIVABLE_AMT
FROM E_BDS_FIN_GL
LEFT JOIN E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
WHERE  ITEM_CODE_LVL1=''4411''
AND   replace(GL_DATE,''-'','''')   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''GROUP BY provincecomcode,CHANNEL_CODE
UNION ALL
--撤保金
SELECT 
           M.PROVINCECOMCODE,
           T.ADD_CHNLCOD,
           0,
           SUM(T.AMOUNT) * (-1),
           0,
           0
      FROM E_BDS_BIDM_LA_F_BUSINESS T, E_BDS_BIDM_MGECMP M
     WHERE T.TRANSTYP = ''P004''
       AND T.EDORTYPECODE = ''T646''
       AND T.INNERORGCODE = M.LAORIGORGCODE
       AND T.STATDATE BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''GROUP BY M.PROVINCECOMCODE,T.ADD_CHNLCOD
union all
--实收
SELECT 
           provincecomcode,
           CHANNEL_CODE,
           0,
           0,
           SUM(FIN_AMT)* (-1),
           0
FROM E_BDS_FIN_GL
LEFT JOIN E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
WHERE  ITEM_CODE_LVL1=''4101''
AND   replace(GL_DATE,''-'','''')   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''GROUP BY provincecomcode,CHANNEL_CODE
union all
--预收
SELECT  
           M.PROVINCECOMCODE,
           T.ADD_CHNLCOD,
           0,
           0,
           0,
           SUM(T.AMOUNT) 
  FROM E_BDS_BIDM_LA_F_BUSINESS T, E_BDS_BIDM_MGECMP M
 WHERE T.TRANSTYP = ''P001''
   AND T.INNERORGCODE = M.LAORIGORGCODE
   AND T.STATDATE BETWEEN ''' || V_EVAL_STARTTIME || ''' AND ''' ||
           V_EVAL_ENDTIME || '''GROUP BY M.PROVINCECOMCODE,T.ADD_CHNLCOD
) a 
group by PROVINCECOMCODE,CHANNEL
 ';
 DBMS_OUTPUT.PUT_LINE(V_SQL);
  EXECUTE IMMEDIATE V_SQL;
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
     'SP_MDS_DTL_RETREAT_RATE',
     'E_MDS_RETREAT_RATE',
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
         'SP_MDS_DTL_RETREAT_RATE',
         'E_MDS_RETREAT_RATE',
         V_START,
         V_END,
         V_INDATE,
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
      /**************************************** 加载日志信息完成 ****************************************/
    END;
END SP_MDS_DTL_RETREAT_RATE;

/
CREATE PROCEDURE SP_MDS_DW(V_INDATE  IN VARCHAR2,
                                  V_RTNCODE OUT VARCHAR2,
                                  V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_DW                                             */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-11-01                                        */
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
  V_EVAL_STARTTIME := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),
                                                        'yyyymmdd') + 1,
                                                -3),
                                     'yyyymmdd'),
                             1,
                             6);
  V_EVAL_ENDTIME   := SUBSTR(FUN_ETL_Q_DATE(V_INDATE), 1, 6);
  --评估期本期应收保费
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04023'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
  SELECT ''OR04023'',
       PROVINCECOMCODE,
       CHNLCOD,
       SUM(MON13_INSTPREM_MON_CUR) + SUM(MON25_INSTPREM_MON_CUR) +
       SUM(MON37_INSTPREM_MON_CUR),
       CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
          TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM (SELECT TABLE__2.PROVINCECOMCODE PROVINCECOMCODE,
               CASE
                 WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON13_INSTPREM_MON_CUR IS NULL THEN
                  0
                 ELSE
                  E_BDS_BIDM_RENEWAL_RP_FY.MON13_INSTPREM_MON_CUR
               END MON13_INSTPREM_MON_CUR,
               CASE
                 WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON25_INSTPREM_MON_CUR IS NULL THEN
                  0
                 ELSE
                  E_BDS_BIDM_RENEWAL_RP_FY.MON25_INSTPREM_MON_CUR
               END MON25_INSTPREM_MON_CUR,
               CASE
                 WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON37_INSTPREM_MON_CUR IS NULL THEN
                  0
                 ELSE
                  E_BDS_BIDM_RENEWAL_RP_FY.MON37_INSTPREM_MON_CUR
               END MON37_INSTPREM_MON_CUR,
               D_CHNL.CHNLCOD CHNLCOD
          FROM (SELECT *
                  FROM E_BDS_BIDM_MGECMP A
                 WHERE A.INNERORGCODE != ''86'') TABLE__2,
               (
               SELECT S.FINMONTH,
                       S.INNERORGCODE,
                       S.ADD_CHNLCOD,
                       Y.MON13_INSTPREM_MON_CUR,
                       /*   -- Y.MON13_REALPREM_MON_CUR,
                       Y.MON13_INSTPREM_CUR,*/
                       -- Y.MON13_REALPREM_CUR,
                       Y.MON25_INSTPREM_MON_CUR,
                       -- Y.MON25_REALPREM_MON_CUR,
                       /*                  Y.MON25_INSTPREM_CUR,*/
                       -- Y.MON25_REALPREM_CUR,
                       Y.MON37_INSTPREM_MON_CUR
                -- Y.MON37_REALPREM_MON_CUR,
                /*        Y.MON37_INSTPREM_CUR*/
                --  Y.MON37_REALPREM_CUR,
                --  Y.MON13_INSTPREM_MON_LAST1,
                --  Y.MON13_REALPREM_MON_LAST1,
                /*       -  Y.MON25_INSTPREM_MON_LAST1,
                Y.MON25_REALPREM_MON_LAST1,
                Y.MON37_INSTPREM_MON_LAST1,
                Y.MON37_REALPREM_MON_LAST1,
                Y.MON13_INSTPREM_MON_LAST2,
                Y.MON13_REALPREM_MON_LAST2,
                Y.MON25_INSTPREM_MON_LAST2,
                Y.MON25_REALPREM_MON_LAST2,
                Y.MON37_INSTPREM_MON_LAST2,
                Y.MON37_REALPREM_MON_LAST2*/
                
                  FROM (
                  SELECT TO_CHAR(FINMONTH) AS FINMONTH,
                               E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE AS INNERORGCODE,
                               D_CHNL.CHNLCOD AS ADD_CHNLCOD
                          FROM E_BDS_BIDM_RENEWAL_RP_FY,
                               /*(SELECT DISTINCT INNERORGCODE
                                  FROM E_BDS_BIDM_RENEWAL_RP_FY) E_BDS_BIDM_MGECMP,*/
                               (SELECT ''FC'' CHNLCOD
                                  FROM DUAL
                                UNION ALL
                                SELECT ''BK'' CHNLCOD
                                  FROM DUAL
                                UNION ALL
                                SELECT ''AD'' CHNLCOD
                                  FROM DUAL
                                UNION ALL
                                SELECT ''RP'' CHNLCOD
                                  FROM DUAL) D_CHNL
                         WHERE  /*E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE=E_BDS_BIDM_MGECMP.INNERORGCODE
                          AND*/ E_BDS_BIDM_RENEWAL_RP_FY.ADD_CHNLCOD=D_CHNL.CHNLCOD
                         GROUP BY TO_CHAR(FINMONTH),
                                  E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE,
                                  D_CHNL.CHNLCOD) S,
                       (SELECT FINMONTH,
                               
                               INNERORGCODE,
                               ADD_CHNLCOD,
                               SUM(MON13_INSTPREM_MON_CUR) AS MON13_INSTPREM_MON_CUR,
                               --SUM(MON13_REALPREM_MON_CUR) AS MON13_REALPREM_MON_CUR,
                               /*   SUM(MON13_INSTPREM_CUR) AS MON13_INSTPREM_CUR,
                               --  SUM(MON13_REALPREM_CUR) AS MON13_REALPREM_CUR,*/
                               SUM(MON25_INSTPREM_MON_CUR) AS MON25_INSTPREM_MON_CUR,
                               /*               -- SUM(MON25_REALPREM_MON_CUR) AS MON25_REALPREM_MON_CUR,
                               SUM(MON25_INSTPREM_CUR) AS MON25_INSTPREM_CUR,*/
                               -- SUM(MON25_REALPREM_CUR) AS MON25_REALPREM_CUR,
                               SUM(MON37_INSTPREM_MON_CUR) AS MON37_INSTPREM_MON_CUR
                        --  SUM(MON37_REALPREM_MON_CUR) AS MON37_REALPREM_MON_CUR,
                        /*  SUM(MON37_INSTPREM_CUR) AS MON37_INSTPREM_CUR*/
                        --   SUM(MON37_REALPREM_CUR) AS MON37_REALPREM_CUR,
                        /*  SUM(MON13_INSTPREM_MON_LAST1) AS MON13_INSTPREM_MON_LAST1,
                        SUM(MON13_REALPREM_MON_LAST1) AS MON13_REALPREM_MON_LAST1,
                        SUM(MON25_INSTPREM_MON_LAST1) AS MON25_INSTPREM_MON_LAST1,
                        SUM(MON25_REALPREM_MON_LAST1) AS MON25_REALPREM_MON_LAST1,
                        SUM(MON37_INSTPREM_MON_LAST1) AS MON37_INSTPREM_MON_LAST1,
                        SUM(MON37_REALPREM_MON_LAST1) AS MON37_REALPREM_MON_LAST1,
                        SUM(MON13_INSTPREM_MON_LAST2) AS MON13_INSTPREM_MON_LAST2,
                        SUM(MON13_REALPREM_MON_LAST2) AS MON13_REALPREM_MON_LAST2,
                        SUM(MON25_INSTPREM_MON_LAST2) AS MON25_INSTPREM_MON_LAST2,
                        SUM(MON25_REALPREM_MON_LAST2) AS MON25_REALPREM_MON_LAST2,
                        SUM(MON37_INSTPREM_MON_LAST2) AS MON37_INSTPREM_MON_LAST2,
                        SUM(MON37_REALPREM_MON_LAST2) AS MON37_REALPREM_MON_LAST2*/
                          FROM (SELECT TO_CHAR(ADD_MONTHS(TO_DATE(FINMONTH,
                                                                  ''yyyymm''),
                                                          +2),
                                               ''yyyymm'') AS FINMONTH,
                                       INNERORGCODE,
                                       ADD_CHNLCOD,
                                       MON13_INSTPREM_MON AS MON13_INSTPREM_MON_CUR,
                                       -- MON13_REALPREM_MON AS MON13_REALPREM_MON_CUR,
                                       /*    MON13_INSTPREM AS MON13_INSTPREM_CUR,*/
                                       --  MON13_REALPREM AS MON13_REALPREM_CUR,
                                       MON25_INSTPREM_MON AS MON25_INSTPREM_MON_CUR,
                                       -- MON25_REALPREM_MON AS MON25_REALPREM_MON_CUR,
                                       /*    MON25_INSTPREM AS MON25_INSTPREM_CUR,*/
                                       -- MON25_REALPREM AS MON25_REALPREM_CUR,
                                       MON37_INSTPREM_MON AS MON37_INSTPREM_MON_CUR
                                --  MON37_REALPREM_MON AS MON37_REALPREM_MON_CUR,
                                /*       MON37_INSTPREM AS MON37_INSTPREM_CUR*/
                                --   MON37_REALPREM AS MON37_REALPREM_CUR
                                /* 0 MON13_INSTPREM_MON_LAST1,
                                0 MON13_REALPREM_MON_LAST1,
                                0 MON25_INSTPREM_MON_LAST1,
                                0 MON25_REALPREM_MON_LAST1,
                                0 MON37_INSTPREM_MON_LAST1,
                                0 MON37_REALPREM_MON_LAST1,
                                0 MON13_INSTPREM_MON_LAST2,
                                0 MON13_REALPREM_MON_LAST2,
                                0 MON25_INSTPREM_MON_LAST2,
                                0 MON25_REALPREM_MON_LAST2,
                                0 MON37_INSTPREM_MON_LAST2,
                                0 MON37_REALPREM_MON_LAST2*/
                                
                                  FROM E_BDS_BIDM_RENEWAL_RP_FY) T
                         GROUP BY FINMONTH, INNERORGCODE, ADD_CHNLCOD) Y
                 WHERE S.FINMONTH = Y.FINMONTH(+)
                   AND S.INNERORGCODE = Y.INNERORGCODE(+)
                   AND S.ADD_CHNLCOD = Y.ADD_CHNLCOD(+)) E_BDS_BIDM_RENEWAL_RP_FY,
               (SELECT CHNLCOD,ADD_CHNLCOD
                  FROM E_BDS_BIDM_CHNLCOD T
                 WHERE T.FLG = ''0''
                   AND T.CHNLCOD <> ''GR'') D_CHNL,
               /*(SELECT *
                FROM E_BDS_BIDM_MGECMP A
               WHERE A.INNERORGCODE != ''86'') E_BDS_BIDM_MGECMP1,*/
               E_BDS_BIDM_MGECMP_MAP,
               (SELECT ADD_CHNLCOD,CHNLCOD
                  FROM E_BDS_BIDM_CHNL_MAP T
                 WHERE T.FLG = ''0''
                   AND T.ADD_CHNLCOD <> ''GR'') E_BDS_BIDM_CHNL_MAP
         WHERE (TABLE__2.INNERORGCODE = E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE)
           AND (E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE =
               TABLE__2.INNERORGCODE(+))
              /* AND (E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE =
              E_BDS_BIDM_MGECMP1.INNERORGCODE(+))*/
           AND (D_CHNL.ADD_CHNLCOD = E_BDS_BIDM_RENEWAL_RP_FY.ADD_CHNLCOD)
           AND (E_BDS_BIDM_CHNL_MAP.ADD_CHNLCOD = D_CHNL.ADD_CHNLCOD)
           AND (E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE = ''86'' AND
               TO_DATE(E_BDS_BIDM_RENEWAL_RP_FY.FINMONTH, ''yyyymm'')BETWEEN to_date(''' ||
             V_EVAL_STARTTIME || ''',''yyyymm'') 
   AND to_date(''' || V_EVAL_ENDTIME ||
             ''',''yyyymm'')
   AND
   E_BDS_BIDM_CHNL_MAP.CHNLCOD  =  ''ALL''
  )
)t
group by PROVINCECOMCODE,CHNLCOD';
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
     'OR04023',
     'SP_MDS_DW',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --评估期本期应收实收保费
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04022'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
SELECT ''OR04022'',
       PROVINCECOMCODE,
       CHNLCOD,
       SUM(MON13_REALPREM_MON_CUR) + SUM(MON25_REALPREM_MON_CUR) +
       SUM(MON37_REALPREM_MON_CUR),
      CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
         TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM (SELECT TABLE__2.PROVINCECOMCODE PROVINCECOMCODE,
               CASE
                 WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON13_REALPREM_MON_CUR IS NULL THEN
                  0
                 ELSE
                  E_BDS_BIDM_RENEWAL_RP_FY.MON13_REALPREM_MON_CUR
               END MON13_REALPREM_MON_CUR,
               CASE
                 WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON25_REALPREM_MON_CUR IS NULL THEN
                  0
                 ELSE
                  E_BDS_BIDM_RENEWAL_RP_FY.MON25_REALPREM_MON_CUR
               END MON25_REALPREM_MON_CUR,
               CASE
                 WHEN E_BDS_BIDM_RENEWAL_RP_FY.MON37_REALPREM_MON_CUR IS NULL THEN
                  0
                 ELSE
                  E_BDS_BIDM_RENEWAL_RP_FY.MON37_REALPREM_MON_CUR
               END MON37_REALPREM_MON_CUR,
               D_CHNL.CHNLCOD CHNLCOD
          FROM (SELECT *
                  FROM E_BDS_BIDM_MGECMP A
                 WHERE A.INNERORGCODE != ''86'') TABLE__2,
               (SELECT S.FINMONTH,
                       S.INNERORGCODE,
                       S.ADD_CHNLCOD,
                       /*       y.MON13_INSTPREM_MON_cur,*/
                       Y.MON13_REALPREM_MON_CUR,
                       /*       y.MON13_INSTPREM_cur,*/
                       Y.MON13_REALPREM_CUR,
                       /*       y.MON25_INSTPREM_MON_cur,*/
                       Y.MON25_REALPREM_MON_CUR,
                       /*       y.MON25_INSTPREM_cur,*/
                       Y.MON25_REALPREM_CUR,
                       /*      y.MON37_INSTPREM_MON_cur,*/
                       Y.MON37_REALPREM_MON_CUR,
                       /*       y.MON37_INSTPREM_cur,*/
                       Y.MON37_REALPREM_CUR /*,
                       y.MON13_INSTPREM_MON_last1,
                       y.MON13_REALPREM_MON_last1,
                       y.MON25_INSTPREM_MON_last1,
                       y.MON25_REALPREM_MON_last1,
                       y.MON37_INSTPREM_MON_last1,
                       y.MON37_REALPREM_MON_last1,
                       y.MON13_INSTPREM_MON_last2,
                       y.MON13_REALPREM_MON_last2,
                       y.MON25_INSTPREM_MON_last2,
                       y.MON25_REALPREM_MON_last2,
                       y.MON37_INSTPREM_MON_last2,
                       y.MON37_REALPREM_MON_last2*/
                
                  FROM (SELECT TO_CHAR(FINMONTH) AS FINMONTH,
                               E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE AS INNERORGCODE,
                               D_CHNL.CHNLCOD AS ADD_CHNLCOD
                          FROM E_BDS_BIDM_RENEWAL_RP_FY,
                               /* (select distinct innerorgcode from E_BDS_BIDM_RENEWAL_RP_FY) E_BDS_BIDM_MGECMP,*/
                               (SELECT ''FC'' CHNLCOD
                                  FROM DUAL
                                UNION ALL
                                SELECT ''BK'' CHNLCOD
                                  FROM DUAL
                                UNION ALL
                                SELECT ''AD'' CHNLCOD
                                  FROM DUAL
                                UNION ALL
                                SELECT ''RP'' CHNLCOD
                                  FROM DUAL) D_CHNL
                         GROUP BY TO_CHAR(FINMONTH),
                                  E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE,
                                  D_CHNL.CHNLCOD) S,
                       (SELECT FINMONTH,
                               
                               INNERORGCODE,
                               ADD_CHNLCOD,
                               /*sum(MON13_INSTPREM_MON_cur) as MON13_INSTPREM_MON_cur,*/
                               SUM(MON13_REALPREM_MON_CUR) AS MON13_REALPREM_MON_CUR,
                               /* sum(MON13_INSTPREM_cur) as MON13_INSTPREM_cur,*/
                               SUM(MON13_REALPREM_CUR) AS MON13_REALPREM_CUR,
                               -- sum(MON25_INSTPREM_MON_cur) as MON25_INSTPREM_MON_cur,
                               SUM(MON25_REALPREM_MON_CUR) AS MON25_REALPREM_MON_CUR,
                               -- sum(MON25_INSTPREM_cur) as MON25_INSTPREM_cur,
                               SUM(MON25_REALPREM_CUR) AS MON25_REALPREM_CUR,
                               --  sum(MON37_INSTPREM_MON_cur) as MON37_INSTPREM_MON_cur,
                               SUM(MON37_REALPREM_MON_CUR) AS MON37_REALPREM_MON_CUR,
                               --- sum(MON37_INSTPREM_cur) as MON37_INSTPREM_cur,
                               SUM(MON37_REALPREM_CUR) AS MON37_REALPREM_CUR /*,
                                       sum(MON13_INSTPREM_MON_last1) as MON13_INSTPREM_MON_last1,
                                       sum(MON13_REALPREM_MON_last1) as MON13_REALPREM_MON_last1,
                                       sum(MON25_INSTPREM_MON_last1) as MON25_INSTPREM_MON_last1,
                                       sum(MON25_REALPREM_MON_last1) as MON25_REALPREM_MON_last1,
                                       sum(MON37_INSTPREM_MON_last1) as MON37_INSTPREM_MON_last1,
                                       sum(MON37_REALPREM_MON_last1) as MON37_REALPREM_MON_last1,
                                       sum(MON13_INSTPREM_MON_last2) as MON13_INSTPREM_MON_last2,
                                       sum(MON13_REALPREM_MON_last2) as MON13_REALPREM_MON_last2,
                                       sum(MON25_INSTPREM_MON_last2) as MON25_INSTPREM_MON_last2,
                                       sum(MON25_REALPREM_MON_last2) as MON25_REALPREM_MON_last2,
                                       sum(MON37_INSTPREM_MON_last2) as MON37_INSTPREM_MON_last2,
                                       sum(MON37_REALPREM_MON_last2) as MON37_REALPREM_MON_last2*/
                          FROM (SELECT TO_CHAR(ADD_MONTHS(TO_DATE(FINMONTH,
                                                                  ''YYYYMM''),
                                                          +2),
                                               ''YYYYMM'') AS FINMONTH,
                                       INNERORGCODE,
                                       ADD_CHNLCOD,
                                       -- MON13_INSTPREM_MON as MON13_INSTPREM_MON_cur,
                                       MON13_REALPREM_MON AS MON13_REALPREM_MON_CUR,
                                       -- MON13_INSTPREM as MON13_INSTPREM_cur,
                                       MON13_REALPREM AS MON13_REALPREM_CUR,
                                       -- MON25_INSTPREM_MON as MON25_INSTPREM_MON_cur,
                                       MON25_REALPREM_MON AS MON25_REALPREM_MON_CUR,
                                       -- MON25_INSTPREM as MON25_INSTPREM_cur,
                                       MON25_REALPREM AS MON25_REALPREM_CUR,
                                       -- MON37_INSTPREM_MON as MON37_INSTPREM_MON_cur,
                                       MON37_REALPREM_MON AS MON37_REALPREM_MON_CUR,
                                       --  MON37_INSTPREM as MON37_INSTPREM_cur,
                                       MON37_REALPREM AS MON37_REALPREM_CUR /*,
                                                       0 MON13_INSTPREM_MON_last1,
                                                       0 MON13_REALPREM_MON_last1,
                                                       0 MON25_INSTPREM_MON_last1,
                                                       0 MON25_REALPREM_MON_last1,
                                                       0 MON37_INSTPREM_MON_last1,
                                                       0 MON37_REALPREM_MON_last1,
                                                       0 MON13_INSTPREM_MON_last2,
                                                       0 MON13_REALPREM_MON_last2,
                                                       0 MON25_INSTPREM_MON_last2,
                                                       0 MON25_REALPREM_MON_last2,
                                                       0 MON37_INSTPREM_MON_last2,
                                                       0 MON37_REALPREM_MON_last2*/
                                
                                  FROM E_BDS_BIDM_RENEWAL_RP_FY) T
                         GROUP BY FINMONTH, INNERORGCODE, ADD_CHNLCOD) Y
                 WHERE S.FINMONTH = Y.FINMONTH(+)
                   AND S.INNERORGCODE = Y.INNERORGCODE(+)
                   AND S.ADD_CHNLCOD = Y.ADD_CHNLCOD(+)) E_BDS_BIDM_RENEWAL_RP_FY,
               (SELECT *
                  FROM E_BDS_BIDM_CHNLCOD T
                 WHERE T.FLG = ''0''
                   AND T.CHNLCOD <> ''GR'') D_CHNL,
               /*(
               select * from E_BDS_BIDM_MGECMP  a where a.innerorgcode !=''86''
               )  E_BDS_BIDM_MGECMP1,*/
               E_BDS_BIDM_MGECMP_MAP,
               (SELECT *
                  FROM E_BDS_BIDM_CHNL_MAP T
                 WHERE T.FLG = ''0''
                   AND T.ADD_CHNLCOD <> ''GR'') E_BDS_BIDM_CHNL_MAP
         WHERE (TABLE__2.INNERORGCODE = E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE)
           AND (E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE =
               TABLE__2.INNERORGCODE(+))
              /*  AND  ( E_BDS_BIDM_RENEWAL_RP_FY.INNERORGCODE=E_BDS_BIDM_MGECMP1.INNERORGCODE(+)  )*/
           AND (D_CHNL.ADD_CHNLCOD = E_BDS_BIDM_RENEWAL_RP_FY.ADD_CHNLCOD)
           AND (E_BDS_BIDM_CHNL_MAP.ADD_CHNLCOD = D_CHNL.ADD_CHNLCOD)
           AND (E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE = ''86'' AND
                TO_DATE(E_BDS_BIDM_RENEWAL_RP_FY.FINMONTH, ''YYYYMM'') BETWEEN
                TO_DATE(''' || V_EVAL_STARTTIME || ''', ''YYYYMM'') AND
                TO_DATE(''' || V_EVAL_ENDTIME ||
             ''',
                        ''YYYYMM'') AND E_BDS_BIDM_CHNL_MAP.CHNLCOD = ''ALL'')) T
 GROUP BY PROVINCECOMCODE, CHNLCOD ';
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
     'OR04022',
     'SP_MDS_DW',
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
         'SP_MDS_DW',
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
  
END SP_MDS_DW;

/
CREATE PROCEDURE SP_MDS_HASLCLAIMS(V_INDATE  IN  VARCHAR2,
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
  WHERE 理赔结论 NOT IN (''给付通融'', ''协议给付'', ''部分给付'', ''拒赔解约退现价、拒赔'', ''解约退保费、拒赔'',''解约不退费、拒赔'',''保费豁免'',''拒付'')
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
  WHERE 理赔结论 NOT IN (''给付通融'', ''协议给付'', ''部分给付'', ''拒赔解约退现价、拒赔'', ''解约退保费、拒赔'',''解约不退费、拒赔'',''保费豁免'',''拒付'')
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
CREATE PROCEDURE SP_MDS_HASLCSBPM(V_INDATE  IN VARCHAR2,
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
              NULL
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
       T2.POLICY_NOS AS POLICY_NOS
FROM E_BDS_BIDM_MGECMP T1
LEFT JOIN (SELECT 
           PROVINCECOMCODE,
           CHANNEL,
           POLICY_NOS
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
CREATE PROCEDURE SP_MDS_ITDW(V_INDATE  IN VARCHAR2,
                                    V_RTNCODE OUT VARCHAR2,
                                    V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_ITDW                                          */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-10-31                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                                 */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(2000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --评估期本期合计撤保金总额
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04029'
     AND to_char(LOAD_DT,'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04029'',
           M.PROVINCECOMCODE,
           DECODE(T.ADD_CHNLCOD,''GB'',''GP'',T.ADD_CHNLCOD),
           SUM(T.AMOUNT) * (-1),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
      FROM E_BDS_BIDM_LA_F_BUSINESS T, E_BDS_BIDM_MGECMP M
     WHERE T.TRANSTYP = ''P004''
       AND T.EDORTYPECODE = ''T646''
       AND T.INNERORGCODE = M.LAORIGORGCODE
       AND T.STATDATE BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''GROUP BY M.PROVINCECOMCODE,DECODE(T.ADD_CHNLCOD,''GB'',''GP'',T.ADD_CHNLCOD)';
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
     'OR04029',
     'SP_MDS_ITDW',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --评估期本期预收保费总额
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04032'
     AND TO_CHAR(LOAD_DT,'YYYYMMDD') =V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT  ''OR04032'',
           M.PROVINCECOMCODE,
          DECODE(T.ADD_CHNLCOD,''GB'',''GP'',T.ADD_CHNLCOD),
           SUM(T.AMOUNT),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM E_BDS_BIDM_LA_F_BUSINESS T, E_BDS_BIDM_MGECMP M
 WHERE T.TRANSTYP = ''P001''
   AND T.INNERORGCODE = M.LAORIGORGCODE
   AND T.STATDATE BETWEEN ''' || V_EVAL_STARTTIME || ''' AND ''' ||
           V_EVAL_ENDTIME || '''GROUP BY M.PROVINCECOMCODE,DECODE(T.ADD_CHNLCOD,''GB'',''GP'',T.ADD_CHNLCOD)';
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
     'OR04032',
     'SP_MDS_ITDW',
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
      V_END:=SYSDATE;
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
         'SP_MDS_ITDW',
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
END SP_MDS_ITDW;

/
CREATE PROCEDURE SP_MDS_ORACLE(V_INDATE  IN VARCHAR2,
                                      V_RTNCODE OUT VARCHAR2,
                                      V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_ORACLE                                         */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-11-27                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_NEXT_EVAL_FIRST_MON VARCHAR2(10);--下个季度首月月初
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
  /***********************************************************************/
BEGIN
 V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1,
                                         -3),
                              'yyyy-mm');
  V_EVAL_ENDTIME   := TO_CHAR(To_DATE(FUN_ETL_Q_DATE(V_INDATE),'yyyymmdd'),'yyyy-mm');
  V_NEXT_EVAL_FIRST_MON := TO_CHAR(To_DATE(FUN_ETL_Q_DATE(V_INDATE),'yyyymmdd')+1,'yyyy-mm');
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02007'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --评估期评估公司规模保费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02007'',
           ''86'',
           NULL,
           nvl(SUM(FIN_AMT) * (-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
    FROM E_BDS_FIN_GL
   WHERE ITEM_CODE_LVL2 IN
         (''4101000000'', ''4101000002'', ''2171010001'', ''2181010001'')
     AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''';
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
     'OR02007',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02012'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --评估期评估公司规模保费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02012'',
           ''86'',
           NULL,
           nvl(SUM(FIN_AMT) * (-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
    FROM E_BDS_FIN_GL
   WHERE ITEM_CODE_LVL2 IN
         (''4101000000'', ''4101000002'', ''2171010001'', ''2181010001'')
     AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''';
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
     'OR02012',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR12005'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1,
                                         -12), 'yyyy-mm');
  V_EVAL_ENDTIME   := TO_CHAR(To_DATE(FUN_ETL_Q_DATE(V_INDATE),'yyyymmdd'),'yyyy-mm');
  --插入数据日期数据
  --当期保费收入
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR12005'',
           ''86'',
           NULL,
           nvl(SUM(FIN_AMT),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
--保费收入
    SELECT 
           SUM(FIN_AMT)*(-1) as FIN_AMT
FROM E_BDS_FIN_GL
WHERE ITEM_CODE_LVL2 IN (''4101000000'',''4101000002'') 
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
UNION ALL
--储金
    SELECT 
           SUM(FIN_AMT)*(-1) 
FROM E_BDS_FIN_GL
WHERE ITEM_CODE_LVL2 IN (''2171010001'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
UNION ALL
--负债
    SELECT 
           SUM(FIN_AMT)*(-1) 
FROM E_BDS_FIN_GL
WHERE ITEM_CODE_LVL2 IN (''2181010001'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||''')';
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
     'OR12005',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
 /**************************************** 加载日志信息完成 ****************************************/
   --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04030'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1,
                                         -3),
                              'yyyy-mm');
  V_EVAL_ENDTIME   := TO_CHAR(To_DATE(FUN_ETL_Q_DATE(V_INDATE),'yyyymmdd'),'yyyy-mm');
  --插入数据日期数据
  --评估期本期合计退保金总额
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04030'',
           provincecomcode,
         DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP''),
           nvl(SUM(FIN_AMT),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM  E_BDS_FIN_GL
LEFT JOIN  E_BDS_BIDM_MGECMP b 
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
AND MNGCOMLEVEL=''2''
WHERE  ITEM_CODE_LVL1=''4411''
AND CHANNEL_CODE IN (''10'',''20'',''30'',''40'',''60'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
             GROUP BY provincecomcode,DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'')
             ORDER BY  provincecomcode,DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'')';
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
     'OR04030',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
   --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04031'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --评估期本期合计实收保费金额
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04031'',
           provincecomcode,
          DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'',CHANNEL_CODE),
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM   E_BDS_FIN_GL
LEFT JOIN E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and MNGCOMLEVEL=''2''
where  ITEM_CODE_LVL1=''4101''
--AND CHANNEL_CODE IN (''10'',''20'',''30'',''40'',''60'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
             GROUP BY provincecomcode, DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'',CHANNEL_CODE)
             ORDER BY  provincecomcode, DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'',CHANNEL_CODE)';

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
     'OR04031',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
   --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR13002'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --评估期评估公司原保费收入
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
 SELECT ''OR13002'',
           provincecomcode,
          DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'',CHANNEL_CODE),
           sum(org_amt)-SUM(acct_amt),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM (
--应收保费
SELECT SUBSTR( org_no,2,2) org,CHANNEL_CODE,nvl(SUM(FIN_AMT),0) acct_amt,0 org_amt 
FROM e_Bds_Fin_Gl
WHERE ITEM_CODE_LVL2 LIKE ''1122%''
AND GL_DATE = ''' || V_EVAL_ENDTIME || '''
GROUP BY SUBSTR( org_no,2,2),CHANNEL_CODE
UNION ALL
--原保费收入
SELECT
SUBSTR( org_no,2,2)
,CHANNEL_CODE
,0 
,nvl(SUM(FIN_AMT)*(-1),0)
FROM e_Bds_Fin_Gl
WHERE  ITEM_CODE_LVL2 IN (''4101000000'',''4101000002'')
AND   GL_DATE between ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME || '''
GROUP BY SUBSTR( org_no,2,2),CHANNEL_CODE
)t
LEFT JOIN E_BDS_BIDM_MGECMP b
ON org=SUBSTR(provincecomcode,3,2)
and MNGCOMLEVEL=''2''
GROUP BY provincecomcode,  DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'',CHANNEL_CODE)
ORDER BY provincecomcode, DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'',CHANNEL_CODE)
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
     'OR13002',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR13003'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --评估期内保户投资款本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
     SELECT ''OR13003'',
           provincecomcode,
           DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP''),
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM   E_BDS_FIN_GL
LEFT JOIN  E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and MNGCOMLEVEL=''2''
where  ITEM_CODE_LVL2=''2171010001''
AND CHANNEL_CODE IN (''10'',''20'',''30'',''40'',''60'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             ''' 
GROUP BY provincecomcode,DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'')
order by provincecomcode,DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'')';
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
     'OR13003',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
 --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR13004'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --评估期内投连险独立账户本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
     SELECT ''OR13004'',
           provincecomcode,
          DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP''),
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM    E_BDS_FIN_GL b
LEFT JOIN E_BDS_BIDM_MGECMP a
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and  MNGCOMLEVEL=''2''
where ITEM_CODE_LVL2=''2181010001''
AND CHANNEL_CODE IN (''10'',''20'',''30'',''40'',''60'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
GROUP BY provincecomcode,DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'')
ORDER BY provincecomcode,DECODE(CHANNEL_CODE,''10'',''FC'',''20'',''BK'',''30'',''GP'',''40'',''AD'',''60'',''RP'')
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
     'OR13004',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
 --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR13009'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --评估期末非寿险业务应收保费余额
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT ''OR13009'',
       PROVINCECOMCODE,
       DECODE(CHANNEL_CODE,
              ''10'',
              ''FC'',
              ''20'',
              ''BK'',
              ''30'',
              ''GP'',
              ''40'',
              ''AD'',
              ''60'',
              ''RP''),
       NVL(SUM(FIN_AMT),0),
       CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM (SELECT PROVINCECOMCODE, CHANNEL_CODE, SUM(FIN_AMT) FIN_AMT
          FROM E_BDS_FIN_GL
          LEFT JOIN E_BDS_BIDM_MGECMP B
            ON SUBSTR(ORG_NO, 2, 2) = SUBSTR(PROVINCECOMCODE, 3, 2)
           AND MNGCOMLEVEL = ''2''
         WHERE ITEM_CODE_LVL2 IN (''1122000010'', ''1122000011'', ''1122000020'')
           AND   GL_DATE = ''' || V_EVAL_ENDTIME ||
             '''
         GROUP BY PROVINCECOMCODE, CHANNEL_CODE
        UNION ALL
        SELECT MANAGECOM, ''30'', SUM(SUMMONEY)
          FROM (SELECT A.POLICYNO,
                       B.GRPNAME,
                       CASE
                         WHEN SUBSTR(B.MANAGECOM, 1, 4) = ''860B'' THEN
                          ''8610''
                         ELSE
                          SUBSTR(B.MANAGECOM, 1, 4)
                       END MANAGECOM,
                       A.OTHERNO,
                       A.MAKEDATE,
                       A.SUMDUEPAYMONEY SUMMONEY
                  FROM E_BDS_GBS_LJSPAY A, E_BDS_GBS_LCGRPAPPNT B
                 WHERE A.OTHERNOTYPE = ''03''
                   AND A.BALANCEONTIME = ''1''
                   AND A.OTHERNO LIKE ''S%''
                   AND A.POLICYNO = B.GRPCONTNO
                UNION ALL
                SELECT DISTINCT A.POLICYNO,
                                B.GRPNAME,
                                CASE
                                  WHEN SUBSTR(B.MANAGECOM, 1, 4) = ''860B'' THEN
                                   ''8610''
                                  ELSE
                                   SUBSTR(B.MANAGECOM, 1, 4)
                                END MANAGECOM,
                                A.OTHERNO,
                                A.PAYDATE,
                                A.SUMDUEPAYMONEY 
                  FROM E_BDS_GBS_LJAPAY       A,
                       E_BDS_GBS_LCGRPAPPNT   B,
                       E_BDS_GBS_LPGRPEMAIN   C,
                       E_BDS_GBS_LPBALRELA    D,
                       E_BDS_GBS_LPBALANCEAPP E
                 WHERE A.OTHERNOTYPE = ''03''
                   AND A.BALANCEONTIME = ''1''
                   AND D.BALANCEALLNO = E.BALANCEALLNO
                   AND A.OTHERNO = D.EDORAPPNO
                   AND A.POLICYNO = E.GRPCONTNO
                   AND A.OTHERNO = C.EDORAPPNO
                   AND TO_CHAR(E.FINACONFDATE, ''yyyy-mm-dd'') >= ''' || V_NEXT_EVAL_FIRST_MON ||
             '''
                   AND A.OTHERNO LIKE ''S%''
                   AND A.POLICYNO = B.GRPCONTNO) A
         GROUP BY MANAGECOM) A
      WHERE  CHANNEL_CODE IN (''10'', ''20'', ''30'', ''40'', ''60'')
 GROUP BY PROVINCECOMCODE,     
          DECODE(CHANNEL_CODE,
                 ''10'',
                 ''FC'',
                 ''20'',
                 ''BK'',
                 ''30'',
                 ''GP'',
                 ''40'',
                 ''AD'',
                 ''60'',
                 ''RP'')
 ORDER BY PROVINCECOMCODE,
          DECODE(CHANNEL_CODE,
                 ''10'',
                 ''FC'',
                 ''20'',
                 ''BK'',
                 ''30'',
                 ''GP'',
                 ''40'',
                 ''AD'',
                 ''60'',
                 ''RP'')';
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
     'OR13009',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1,
                                         -12),
                              'yyyy-mm');
  V_EVAL_ENDTIME   := to_char(to_date(FUN_ETL_Q_DATE(V_INDATE),'yyyymmdd'),'yyyy-mm');
   --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04035'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度评估公司原保费收入
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04035'',
           provincecomcode,
           NULL,
          nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM E_BDS_FIN_GL
LEFT JOIN E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and MNGCOMLEVEL=''2''
WHERE  ITEM_CODE_LVL2 IN (''4101000000'',''4101000002'')
AND   GL_DATE between ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME || '''
GROUP BY provincecomcode
order by provincecomcode
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
     'OR04035',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04036'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度保户投资款本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04036'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM   E_BDS_FIN_GL
LEFT JOIN E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and  MNGCOMLEVEL=''2''
 WHERE ITEM_CODE_LVL2=''2171010001''
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             ''' 
GROUP BY provincecomcode
order by provincecomcode';
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
     'OR04036',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
 --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04037'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度投连险独立账户本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04037'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM  E_BDS_FIN_GL b
LEFT JOIN E_BDS_BIDM_MGECMP a 
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and  MNGCOMLEVEL=''2''
WHERE ITEM_CODE_LVL2=''2181010001''
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
GROUP BY provincecomcode
ORDER BY provincecomcode';
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
     'OR04037',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR08013'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度评估公司原保费收入
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR08013'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM   E_BDS_FIN_GL
LEFT JOIN  E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and   MNGCOMLEVEL=''2''
WHERE  ITEM_CODE_LVL2 IN (''4101000000'',''4101000002'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
 GROUP BY provincecomcode
 order by provincecomcode';
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
     'OR08013',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR08014'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度保户投资款本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR08014'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM   E_BDS_FIN_GL
LEFT JOIN E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and  MNGCOMLEVEL=''2''
where  ITEM_CODE_LVL2=''2171010001''
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             ''' 
GROUP BY provincecomcode
order by provincecomcode';
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
     'OR08014',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR08015'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度投连险独立账户本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR08015'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM E_BDS_FIN_GL b
LEFT JOIN  E_BDS_BIDM_MGECMP a 
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and MNGCOMLEVEL=''2''
where ITEM_CODE_LVL2=''2181010001''
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
GROUP BY provincecomcode
ORDER BY provincecomcode';
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
     'OR08015',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR13016'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度评估公司原保费收入
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR13016'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM   E_BDS_FIN_GL
LEFT JOIN  E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
and MNGCOMLEVEL=''2''
where  ITEM_CODE_LVL2 IN (''4101000000'',''4101000002'')
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
 GROUP BY provincecomcode
 order by provincecomcode';
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
     'OR13016',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR13017'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度保户投资款本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR13017'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM  E_BDS_FIN_GL
LEFT JOIN  E_BDS_BIDM_MGECMP b
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
AND MNGCOMLEVEL=''2''
WHERE  ITEM_CODE_LVL2=''2171010001''
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             ''' 
GROUP BY provincecomcode
order by provincecomcode';
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
     'OR13017',
     'SP_MDS_ORACLE',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
--清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR13018'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  --最近4个季度投连险独立账户本年新增交费
   V_START := SYSDATE;
   V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR13018'',
           provincecomcode,
           NULL,
           nvl(SUM(FIN_AMT)*(-1),0),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM  E_BDS_FIN_GL b
LEFT JOIN E_BDS_BIDM_MGECMP a 
ON SUBSTR( org_no,2,2)=SUBSTR(provincecomcode,3,2)
AND MNGCOMLEVEL=''2''
where ITEM_CODE_LVL2=''2181010001''
AND   GL_DATE   BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME ||
             '''
GROUP BY provincecomcode
ORDER BY provincecomcode';
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
     'OR13018',
     'SP_MDS_ORACLE',
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
      V_END:=SYSDATE;
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
         'SP_MDS_ORACLE',
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
END SP_MDS_ORACLE;

/
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
  V_EVAL_DATE      VARCHAR2(10); --评估期前12个月
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(10000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  V_EVAL_DATE      := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),'YYYYMMDD'),-11),'YYYYMMDD'),1,6);
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
    SELECT 
    ''OR02002'',
           ''86'',
       T.CHANNEL,
       SUM(A3),
       CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM (
  SELECT 
          DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',B.CODE) AS CHANNEL,A3            
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'') AND TYPEID=''4'')  b 
ON a.orgcode4=b.code
WHERE A.PAYRID = ''30''
AND SUBSTR(A.DATA_DATE,1,6) BETWEEN  SUBSTR('''||V_EVAL_STARTTIME||''',1,6)  AND SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
) T
GROUP BY T.CHANNEL';
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
           T.CHANNEL,
           SUM(A),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'') 
FROM(
SELECT   DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',B.CODE) AS CHANNEL,A   
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'') AND TYPEID=''4'') b 
ON a.orgcode4=b.code
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
WHERE a.PAYRID=''30''--销售体系
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_STARTTIME||''',1,6)
) T
GROUP BY T.CHANNEL
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
           T.CHANNEL,
           SUM(A1),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM(SELECT DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',B.CODE) AS CHANNEL,A1       
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'') AND TYPEID=''4'')  b 
ON a.orgcode4=b.code
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
WHERE a.payrid=''30''--销售体系
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
) T
GROUP BY T.CHANNEL
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
           T.CHANNEL,
           SUM(A1),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')          
FROM(SELECT DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',B.CODE) AS CHANNEL,A1
FROM E_BDS_HRM_EMPLOYEESTATE a 
INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'') AND typeid=''4'') b 
ON a.orgcode4=b.code
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
WHERE a.payrid=''30''--销售体系
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
) T
GROUP BY T.CHANNEL
';
  EXECUTE IMMEDIATE V_SQL;
  dbms_output.put_line(V_SQL);
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
    SELECT  ''OR02006'',
            ''86'',
            T.CHANNEL,
            SUM(A),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')           
FROM(SELECT 
  DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',B.CODE) AS CHANNEL,A
FROM E_BDS_HRM_EMPLOYEEDEGREE a 
INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'') AND TYPEID=''4'')  b 
ON a.orgcode4=b.code
LEFT JOIN E_BDS_HRM_ORG C
ON A.ORGCODE2 = C.ORGCODE
WHERE a.payrid=''30''--销售体系
AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
) T
GROUP BY T.CHANNEL';
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
AND SUBSTR(A.DATA_DATE,1,6) BETWEEN  '''||V_EVAL_DATE||''' AND SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
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
           SUM(A1),
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
           T.CODE,
           T.CHANNEL ,
           SUM(A3),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
     FROM(
   SELECT 
   DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',B.CODE) CHANNEL,
   A3,C.CODE AS CODE  
   FROM E_BDS_HRM_EMPLOYEESTATE A
     INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') AND TYPEID=''4'' AND C_NAME IS NOT NULL) B
     ON A.ORGCODE4 = B.CODE 
     LEFT JOIN E_BDS_HRM_ORG C
     ON A.ORGCODE2 = C.ORGCODE
     WHERE a.orgcode2 <> ''HO''
     AND SUBSTR(A.DATA_DATE,1,6) BETWEEN '''||V_EVAL_DATE||''' AND SUBSTR('''||V_EVAL_ENDTIME||''',1,6)
   ) T
     GROUP BY T.CODE,T.CHANNEL
     ORDER BY T.CODE';
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
    SELECT  ''OR04006'',
            T.CODE,
            T.CHANNEL,
            SUM(A),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
             THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
          TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')  
    FROM(
  SELECT 
    DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',B.CODE) CHANNEL,
  A,C.CODE AS CODE  
    FROM E_BDS_HRM_EMPLOYEESTATE A
    INNER JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') AND TYPEID = ''4'')  B
    ON A.ORGCODE4 = B.CODE
    LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
    WHERE a.orgcode2 <> ''HO''
    AND SUBSTR(A.DATA_DATE,1,6) = '''||V_EVAL_DATE||''') T
    GROUP BY T.CODE,T.CHANNEL
    ORDER BY T.CODE';
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
      SELECT   ''OR04007'',
               T.CODE,
               T.CHANNEL,
               SUM(A2),
               CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM(     
  SELECT DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',''ID'',''FC'',''IDSD'',''FC'',''IDSS'',''FC'',''IDSUD'',''FC'',''IDT'',''FC'',''SFD'',''FC'',''SUD'',''FC'',B.CODE) CHANNEL,
  A2,C.CODE AS CODE  
  FROM E_BDS_HRM_EMPLOYEESTATE A
  INNER JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'',''CS'',''ID'',''IDSD'',''IDSS'',''IDSUD'',''IDT'',''SFD'',''SUD'') AND TYPEID=''4'' AND C_NAME IS NOT NULL) B
  ON A.ORGCODE4 = B.CODE
  LEFT JOIN E_BDS_HRM_ORG C
  ON A.ORGCODE2 = C.ORGCODE
  WHERE a.orgcode2 <> ''HO''
  AND SUBSTR(A.DATA_DATE,1,6) BETWEEN '''||V_EVAL_DATE||''' AND SUBSTR('''||V_EVAL_ENDTIME||''',1,6)) T
  GROUP BY T.CODE,T.CHANNEL
  ORDER BY T.CODE
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
           T.CODE,
           T.CHANNEL,
           SUM(A1),
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'') 
    FROM(
    SELECT   DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',B.CODE) CHANNEL,A1,C.CODE AS CODE     
    FROM E_BDS_HRM_EMPLOYEESTATE A 
    INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'') AND typeid=''4'') B 
    ON a.orgcode4=b.code
    LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
    WHERE a.orgcode2 <> ''HO''
    AND A.PAYRID = ''30''
    AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6)) T
    GROUP BY T.CODE,T.CHANNEL
    ORDER BY T.CODE';
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
           T.CODE,
           T.CHANNEL,
           SUM(A1),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
    FROM(
  SELECT NVL(A1,0) AS A1,DECODE(B.CODE,''GD'',''GP'',''BD'',''BK'',''PR'',''RP'',''RP_BRD'',''RP'',B.CODE) AS CHANNEL,C.CODE AS CODE
  FROM E_BDS_HRM_EMPLOYEESTATE a 
    INNER  JOIN (SELECT DISTINCT CODE FROM E_BDS_HRM_ORGANIZATION  WHERE CODE IN (''PR'',''RP_BRD'',''AD'',''BD'',''GD'') AND typeid=''4'') b 
    ON a.orgcode4=b.code
    LEFT JOIN E_BDS_HRM_ORG C
    ON A.ORGCODE2 = C.ORGCODE
    WHERE a.orgcode2 <> ''HO''
    AND A.PAYRID = ''30''
    AND SUBSTR(A.DATA_DATE,1,6) = SUBSTR('''||V_EVAL_ENDTIME||''',1,6) ) T
    GROUP BY T.CODE,T.CHANNEL
    ORDER BY T.CODE';
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
CREATE PROCEDURE SP_MDS_PLATINUM_HRM_SSA(V_INDATE IN VARCHAR2, V_RTNCODE OUT VARCHAR2, V_RTNMSG OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_PLATINUM_HRM_SSA                           */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-11-05                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --E_BDS_GBS_lpbalanceapp
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(10000); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --评估期末，销售人员中大专以上学历人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02006'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE
     AND CHANNEL_ID ='FC';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT  ''OR02006'',
           ''86'',
           ''FC'',
             COUNT(DISTINCT a.pid) ,
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM E_BDS_SSA_T_FNL_SALE A
LEFT JOIN (SELECT a.pid,a.studylevelno FROM  E_BDS_SSA_T_NATUREGX a  LEFT JOIN
 (SELECT pid,MAX(workdate) wdate FROM E_BDS_SSA_T_NATUREGX
GROUP BY pid
) b ON a.pid=b.pid
WHERE  a.workdate=b.wdate
) B
ON A.pid=B.pid
WHERE B.studylevelno <= ''30''';
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
     'SP_MDS_PLATINUM_HRM_SSA',
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
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE
      AND CHANNEL_ID ='FC';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT  ''OR04010'',
           SUBSTR(E_BDS_BIDM_MGECMP.INNERORGCODE, 1, 4),
           ''FC'',
             SUM(NVL(E_BDS_BIDM_KPI.ENDPSNNUM,0)) ,
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM (SELECT INNERORGCODE FROM E_BDS_BIDM_MGECMP) E_BDS_BIDM_MGECMP,
       (SELECT *
          FROM E_BDS_BIDM_KPI T
         WHERE TO_NUMBER(T.DATEID) <=
               TO_NUMBER(TO_CHAR((SYSDATE - 1), ''YYYYMM''))) E_BDS_BIDM_KPI,
       (SELECT T.COMPANYNO || T.BRANCH AS ORI,
               T.AREACODE,
               T.LONGDESC,
               O.INNERORGCODE,
               O.ORIGORGCODE,
               D.PROVINCECHNNAME,
               D.BRANCHCHNNAME
       FROM E_BDS_BIDM_FC_AREACODE T,
          E_BDS_BIDM_ORGMAP      O,
               E_BDS_BIDM_MGECMP      D
         WHERE O.ORIGORGCODE = T.COMPANYNO || T.BRANCH
           AND O.INNERORGCODE = D.INNERORGCODE     
        ) E_BDS_BIDM_FC_AREACODE,
       (SELECT * FROM E_BDS_BIDM_MGECMP_MAP) E_BDS_BIDM_MGECMP_MAP
 WHERE (E_BDS_BIDM_KPI.ADD_BKCOD(+) = E_BDS_BIDM_FC_AREACODE.AREACODE)
   AND (E_BDS_BIDM_FC_AREACODE.INNERORGCODE =
       E_BDS_BIDM_MGECMP.INNERORGCODE(+))
   AND (E_BDS_BIDM_MGECMP.INNERORGCODE =
       E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE)
   AND ((E_BDS_BIDM_KPI.DATEID =substr('''||V_EVAL_ENDTIME ||''',1,6) OR E_BDS_BIDM_KPI.DATEID IS NULL) AND
       E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE LIKE ''86'')
    GROUP BY    SUBSTR(E_BDS_BIDM_MGECMP.INNERORGCODE, 1, 4)
    ORDER BY     SUBSTR(E_BDS_BIDM_MGECMP.INNERORGCODE, 1, 4) ';
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
     'SP_MDS_PLATINUM_HRM_SSA',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  --评估期期末公司与销售人员签订有效的劳动合同、代理合同份数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04009'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE
     AND CHANNEL_ID ='FC';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
  SELECT  ''OR04009'',
           SUBSTR(E_BDS_BIDM_MGECMP.INNERORGCODE, 1, 4),
           ''FC'',
             SUM(NVL(E_BDS_BIDM_KPI.ENDPSNNUM,0)) ,
           CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM (SELECT INNERORGCODE FROM E_BDS_BIDM_MGECMP) E_BDS_BIDM_MGECMP,
       (SELECT *
          FROM E_BDS_BIDM_KPI T
         WHERE TO_NUMBER(T.DATEID) <=
               TO_NUMBER(TO_CHAR((SYSDATE - 1), ''YYYYMM''))) E_BDS_BIDM_KPI,
       (SELECT T.COMPANYNO || T.BRANCH AS ORI,
               T.AREACODE,
               T.LONGDESC,
               O.INNERORGCODE,
               O.ORIGORGCODE,
               D.PROVINCECHNNAME,
               D.BRANCHCHNNAME
       FROM E_BDS_BIDM_FC_AREACODE T,
          E_BDS_BIDM_ORGMAP      O,
               E_BDS_BIDM_MGECMP      D
         WHERE O.ORIGORGCODE = T.COMPANYNO || T.BRANCH
           AND O.INNERORGCODE = D.INNERORGCODE     
        ) E_BDS_BIDM_FC_AREACODE,
       (SELECT * FROM E_BDS_BIDM_MGECMP_MAP) E_BDS_BIDM_MGECMP_MAP
 WHERE (E_BDS_BIDM_KPI.ADD_BKCOD(+) = E_BDS_BIDM_FC_AREACODE.AREACODE)
   AND (E_BDS_BIDM_FC_AREACODE.INNERORGCODE =
       E_BDS_BIDM_MGECMP.INNERORGCODE(+))
   AND (E_BDS_BIDM_MGECMP.INNERORGCODE =
       E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE)
   AND ((E_BDS_BIDM_KPI.DATEID =substr('''||V_EVAL_ENDTIME ||''',1,6) OR E_BDS_BIDM_KPI.DATEID IS NULL) AND
       E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE LIKE ''86'')
    GROUP BY    SUBSTR(E_BDS_BIDM_MGECMP.INNERORGCODE, 1, 4)
    ORDER BY     SUBSTR(E_BDS_BIDM_MGECMP.INNERORGCODE, 1, 4) ';
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
     'SP_MDS_PLATINUM_HRM_SSA',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
 V_EVAL_STARTTIME := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd'),1,6);
  V_EVAL_ENDTIME  := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),'YYYYMMDD'),1),'YYYYMMDD'),1,6);
 --评估期初销售人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02003'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE
      AND CHANNEL_ID ='FC';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT  ''OR02003'',
           ''86'',
            ''FC'',
             CASE WHEN sum(E_BDS_BIDM_KPI.CURPSNNUM)
IS NULL THEN 0
 ELSE SUM(E_BDS_BIDM_KPI.CURPSNNUM)
   END,
         CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM
  ( 
  select * from E_BDS_BIDM_MGECMP
  )  E_BDS_BIDM_MGECMP,
  ( 
  select * from E_BDS_BIDM_KPI t where to_number(t.dateid)<=to_number(to_char((sysdate-1),''YYYYMM''))
  )  E_BDS_BIDM_KPI,
  ( 
  select t.companyno||t.branch  as ORI,
t.areacode,t.longdesc,o.innerorgcode,o.origorgcode
,d.provincechnname,d.branchchnname
from E_BDS_BIDM_FC_AREACODE t
,E_BDS_BIDM_ORGMAP o,E_BDS_BIDM_MGECMP d
where o.origorgcode = t.companyno||t.branch
and o.innerorgcode=d.innerorgcode

  )  D_FC_AREACODE,
  ( 
  select * from E_BDS_BIDM_MGECMP_map
  )  E_BDS_BIDM_MGECMP_MAP
WHERE
  ( E_BDS_BIDM_KPI.ADD_BKCOD(+)=D_FC_AREACODE.AREACODE  )
  AND  ( D_FC_AREACODE.INNERORGCODE=E_BDS_BIDM_MGECMP.INNERORGCODE(+)  )
  AND  ( E_BDS_BIDM_MGECMP.INNERORGCODE=E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE  )
  AND  
  (
   (
    E_BDS_BIDM_KPI.DATEID  =  ''' || V_EVAL_STARTTIME || '''
    OR
    E_BDS_BIDM_KPI.DATEID  Is Null  
   )
   AND
   E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE  Like  ''86''
  )';
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
     'SP_MDS_PLATINUM_HRM_SSA',
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
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE
     AND CHANNEL_ID ='FC';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT  ''OR02034'',
           ''86'',
           ''FC'',
              CASE WHEN sum(E_BDS_BIDM_KPI.Curpsnnum)
IS NULL THEN 0
 ELSE SUM(E_BDS_BIDM_KPI.Curpsnnum)
   END ,
          CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM
  ( 
  select * from E_BDS_BIDM_MGECMP
  )  E_BDS_BIDM_MGECMP,
  ( 
  select * from E_BDS_BIDM_KPI t where to_number(t.dateid)<=to_number(to_char((sysdate-1),''YYYYMM''))
  )  E_BDS_BIDM_KPI,
  ( 
  select t.companyno||t.branch  as ORI,
t.areacode,t.longdesc,o.innerorgcode,o.origorgcode
,d.provincechnname,d.branchchnname
from E_BDS_BIDM_FC_AREACODE t
,E_BDS_BIDM_ORGMAP o,E_BDS_BIDM_MGECMP d
where o.origorgcode = t.companyno||t.branch
and o.innerorgcode=d.innerorgcode

  )  D_FC_AREACODE,
  ( 
  select * from E_BDS_BIDM_MGECMP_map
  )  E_BDS_BIDM_MGECMP_MAP
WHERE
  ( E_BDS_BIDM_KPI.ADD_BKCOD(+)=D_FC_AREACODE.AREACODE  )
  AND  ( D_FC_AREACODE.INNERORGCODE=E_BDS_BIDM_MGECMP.INNERORGCODE(+)  )
  AND  ( E_BDS_BIDM_MGECMP.INNERORGCODE=E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE  )
  AND  
  (
   (
    E_BDS_BIDM_KPI.DATEID  =  ''' || V_EVAL_ENDTIME || '''
    OR
    E_BDS_BIDM_KPI.DATEID  Is Null  
   )
   AND
   E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE  Like  ''86''
  )';
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
     'SP_MDS_PLATINUM_HRM_SSA',
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
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE
     AND CHANNEL_ID ='FC';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
   SELECT  ''OR02004'',
           ''86'',
           ''FC'',
              CASE WHEN sum(E_BDS_BIDM_KPI.Curpsnnum)
IS NULL THEN 0
 ELSE SUM(E_BDS_BIDM_KPI.Curpsnnum)
   END ,
          CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM
  ( 
  select * from E_BDS_BIDM_MGECMP
  )  E_BDS_BIDM_MGECMP,
  ( 
  select * from E_BDS_BIDM_KPI t where to_number(t.dateid)<=to_number(to_char((sysdate-1),''YYYYMM''))
  )  E_BDS_BIDM_KPI,
  ( 
  select t.companyno||t.branch  as ORI,
t.areacode,t.longdesc,o.innerorgcode,o.origorgcode
,d.provincechnname,d.branchchnname
from E_BDS_BIDM_FC_AREACODE t
,E_BDS_BIDM_ORGMAP o,E_BDS_BIDM_MGECMP d
where o.origorgcode = t.companyno||t.branch
and o.innerorgcode=d.innerorgcode

  )  D_FC_AREACODE,
  ( 
  select * from E_BDS_BIDM_MGECMP_map
  )  E_BDS_BIDM_MGECMP_MAP
WHERE
  ( E_BDS_BIDM_KPI.ADD_BKCOD(+)=D_FC_AREACODE.AREACODE  )
  AND  ( D_FC_AREACODE.INNERORGCODE=E_BDS_BIDM_MGECMP.INNERORGCODE(+)  )
  AND  ( E_BDS_BIDM_MGECMP.INNERORGCODE=E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE  )
  AND  
  (
   (
    E_BDS_BIDM_KPI.DATEID  =  ''' || V_EVAL_ENDTIME || '''
    OR
    E_BDS_BIDM_KPI.DATEID  Is Null  
   )
   AND
   E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE  Like  ''86''
  )';

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
     'SP_MDS_PLATINUM_HRM_SSA',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  V_EVAL_STARTTIME := SUBSTR(TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd'),1,6);
  V_EVAL_ENDTIME  := SUBSTR(TO_CHAR(TO_DATE(FUN_ETL_Q_DATE(V_INDATE),'YYYYMMDD'),'YYYYMMDD'),1,6);

  --评估期内离职的销售人员数量
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR02002'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE
      AND CHANNEL_ID ='FC';
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR02002'',
           ''86'',
            ''FC'',
          CASE WHEN sum(E_BDS_BIDM_KPI.DIVISIONPSNNUM)
IS NULL THEN 0
 ELSE SUM(E_BDS_BIDM_KPI.DIVISIONPSNNUM)
   END,
          CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
FROM
  ( 
  select * from E_BDS_BIDM_MGECMP
  )  E_BDS_BIDM_MGECMP,
  ( 
  select * from E_BDS_BIDM_KPI t where to_number(t.dateid)<=to_number(to_char((sysdate-1),''YYYYMM''))
  )  E_BDS_BIDM_KPI,
  ( 
  select t.companyno||t.branch  as ORI,
t.areacode,t.longdesc,o.innerorgcode,o.origorgcode
,d.provincechnname,d.branchchnname
from E_BDS_BIDM_FC_AREACODE t
,E_BDS_BIDM_ORGMAP o,E_BDS_BIDM_MGECMP d
where o.origorgcode = t.companyno||t.branch
and o.innerorgcode=d.innerorgcode

  )  D_FC_AREACODE,
  ( 
  select * from E_BDS_BIDM_MGECMP_map
  )  E_BDS_BIDM_MGECMP_MAP
WHERE
  ( E_BDS_BIDM_KPI.ADD_BKCOD(+)=D_FC_AREACODE.AREACODE  )
  AND  ( D_FC_AREACODE.INNERORGCODE=E_BDS_BIDM_MGECMP.INNERORGCODE(+)  )
  AND  ( E_BDS_BIDM_MGECMP.INNERORGCODE=E_BDS_BIDM_MGECMP_MAP.BRANCHCOMCODE  )
  AND  
  (
   (
    E_BDS_BIDM_KPI.DATEID  BETWEEN  ''' || V_EVAL_STARTTIME || '''
    AND  ''' || V_EVAL_ENDTIME || '''
    OR
    E_BDS_BIDM_KPI.DATEID  Is Null  
   )
   AND
   E_BDS_BIDM_MGECMP_MAP.PROVINCECOMCODE  Like  ''86''
  )';

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
     'SP_MDS_PLATINUM_HRM_SSA',
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
         'SP_MDS_PLATINUM_HRM_SSA',
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
END SP_MDS_PLATINUM_HRM_SSA;

/
CREATE PROCEDURE SP_MDS_SMS(V_INDATE  IN VARCHAR2,
                                   V_RTNCODE OUT VARCHAR2,
                                   V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_MDS_SMS                                            */
  /* Developed By   : WH                                                */
  /* Developed Date : 2018-10-31                                        */
  /************************ Souce table *********************************/
  /* Target table   : E_MDS_IRR_INDEX_VALUE                             */
  /********************** Variable Section ******************************/
  V_EVAL_STARTTIME VARCHAR2(10); --评估期开始时间
  V_EVAL_ENDTIME   VARCHAR2(10); --评估期结束时间
  V_START          DATE; --执行开始时间
  V_END            DATE; --执行结束时间
  V_RECORD_NUM     NUMBER; --记录条数
  V_SQL            VARCHAR2(32766); --动态语句
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd')+1, -16),'yyyymmdd');
  V_EVAL_ENDTIME   := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd'), -13),'yyyymmdd');
  --评估期期末前13个月已入职且评估期在职代理制销售人员数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04014'
     AND TO_CHAR(LOAD_DT, 'yyyymmdd') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT ''OR04014'',
       PROVINCECOMCODE,
       ''FC'',
       COUNT(1),
        CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
 FROM (SELECT PSN.*
          FROM E_BDS_SSA_T_PERSON PSN
          JOIN E_BDS_SSA_T_NATUREGX NT
            ON PSN.PID = NT.PID
          JOIN E_BDS_SSA_T_MEMBERSET TM
            ON PSN.PID = TM.PID
          JOIN E_BDS_SSA_T_TEAMNO TN
            ON TM.TID = TN.TID
          JOIN (SELECT RELANO, SIGNTID
                 FROM (SELECT T1.SIGNTID,
                              T1.RELANO,
                              ROW_NUMBER() OVER(PARTITION BY SIGNTID ORDER BY T1.ENDDATE DESC) RN
                         FROM E_BDS_SSA_T_TEAM2TEAM T1
                        WHERE T1.RELANO = ''TT01'') TT
                WHERE TT.RN = 1) T2T
            ON T2T.SIGNTID = TM.TID
            AND T2T.RELANO = ''TT01''
          JOIN (SELECT RELANO, SIGNTID
                 FROM (SELECT T1.RELANO,
                              SIGNTID,
                              ROW_NUMBER() OVER(PARTITION BY SIGNTID ORDER BY T1.ENDDATE DESC) RN
                         FROM E_BDS_SSA_T_TEAM2TEAM T1
                        WHERE T1.RELANO = ''TT02'') TT
                WHERE TT.RN = 1) T2T1
            ON T2T1.SIGNTID = TM.TID
            AND  T2T1.RELANO = ''TT02''
          JOIN (SELECT PID, ARACDE, STARTDATE, LAST_DAY(ENDDATE) ENDDATE
                 FROM E_BDS_SSA_T_PSN2DEPT_GX) P2D
            ON P2D.PID = PSN.PID
          JOIN E_BDS_SSA_LA_CODEITEM CODE
            ON CODE.DESCITEM = P2D.ARACDE
          JOIN E_BDS_SSA_T_CHGRANK CR
            ON PSN.PID = CR.PID
           AND (CR.ENDDATE BETWEEN TN.STARTDATE AND TN.ENDDATE)
           AND (CR.ENDDATE BETWEEN NT.STARTDATE AND NT.ENDDATE)
           AND (CR.ENDDATE BETWEEN P2D.STARTDATE AND P2D.ENDDATE)
           AND (CR.ENDDATE BETWEEN TM.STARTDATE AND TM.ENDDATE)
         WHERE  CR.ENDDATE =
               (SELECT MIN(ENDDATE)
                  FROM E_BDS_SSA_T_CHGRANK
                 WHERE PID = CR.PID
                   AND ((PSN.STATUS = ''1'' AND ENDDATE BETWEEN PSN.STARTDATE AND
                       LAST_DAY(PSN.ENDDATE)) OR
                       (PSN.STATUS <> ''1'' AND ENDDATE BETWEEN PSN.STARTDATE AND
                       TO_DATE(''9999-09-09'', ''yyyy-mm-dd''))))
         --  AND PSN.STATUS IN (''0'')
           AND PSN.PSNTYPENO IN (''M'', ''L'')
           and TO_CHAR(psn.startdate,''yyyymmdd'')  BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME || '''   ) T
  LEFT JOIN E_BDS_BIDM_MGECMP B
    ON SUBSTR(T.DID, 1, 1) = B.LAORIGORGCODE
    WHERE trunc(months_between(enddate,startdate))>12
    GROUP BY PROVINCECOMCODE';
  EXECUTE IMMEDIATE V_SQL;
  dbms_output.put_line(V_SQL);
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
     'OR04014',
     'SP_MDS_SMS',
     'E_MDS_IRR_INDEX_VALUE',
     V_START,
     V_END,
     V_INDATE,
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  /**************************************** 加载日志信息完成 ****************************************/
  --评估期前13个月已入职代理制销售人员数
  --清除旧数据
  DELETE FROM E_MDS_IRR_INDEX_VALUE
   WHERE INDEX_ID = 'OR04015'
     AND TO_CHAR(LOAD_DT, 'YYYYMMDD') = V_INDATE;
       COMMIT;
  --插入数据日期数据
  V_START := SYSDATE;
  V_SQL   := 'INSERT INTO E_MDS_IRR_INDEX_VALUE
    (INDEX_ID, ORG_ID, CHANNEL_ID, INDEX_VALUE, EVAL_DATE, LOAD_DT)
    SELECT
    ''OR04015'',
           PROVINCECOMCODE, 
           ''FC'',
           COUNT(1),
            CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
           TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
   FROM (SELECT PSN.DID, PSN.PID
          FROM E_BDS_SSA_T_PERSON PSN
          JOIN E_BDS_SSA_T_NATUREGX NT
            ON PSN.PID = NT.PID
          JOIN E_BDS_SSA_T_MEMBERSET TM
            ON PSN.PID = TM.PID
          JOIN E_BDS_SSA_T_TEAMNO TN
            ON TM.TID = TN.TID
          JOIN (SELECT RELANO, SIGNTID
                 FROM (SELECT T1.SIGNTID,
                              T1.RELANO,
                              ROW_NUMBER() OVER(PARTITION BY SIGNTID ORDER BY T1.ENDDATE DESC) RN
                         FROM E_BDS_SSA_T_TEAM2TEAM T1
                        WHERE T1.RELANO = ''TT01'') TT
                WHERE TT.RN = 1) T2T
            ON T2T.SIGNTID = TM.TID
            AND T2T.RELANO = ''TT01''
          JOIN (SELECT RELANO, SIGNTID
                 FROM (SELECT T1.RELANO,
                              SIGNTID,
                              ROW_NUMBER() OVER(PARTITION BY SIGNTID ORDER BY T1.ENDDATE DESC) RN
                         FROM E_BDS_SSA_T_TEAM2TEAM T1
                        WHERE T1.RELANO = ''TT02'') TT
                WHERE TT.RN = 1) T2T1
            ON T2T1.SIGNTID = TM.TID
            AND  T2T1.RELANO = ''TT02''
          JOIN (SELECT PID, ARACDE, STARTDATE, LAST_DAY(ENDDATE) ENDDATE
                 FROM E_BDS_SSA_T_PSN2DEPT_GX) P2D
            ON P2D.PID = PSN.PID
          JOIN E_BDS_SSA_LA_CODEITEM CODE
            ON CODE.DESCITEM = P2D.ARACDE
          JOIN E_BDS_SSA_T_CHGRANK CR
            ON PSN.PID = CR.PID
           AND (CR.ENDDATE BETWEEN TN.STARTDATE AND TN.ENDDATE)
           AND (CR.ENDDATE BETWEEN NT.STARTDATE AND NT.ENDDATE)
           AND (CR.ENDDATE BETWEEN P2D.STARTDATE AND P2D.ENDDATE)
           AND (CR.ENDDATE BETWEEN TM.STARTDATE AND TM.ENDDATE)
         WHERE  CR.ENDDATE =
               (SELECT MIN(ENDDATE)
                  FROM E_BDS_SSA_T_CHGRANK
                 WHERE PID = CR.PID
                   AND ((PSN.STATUS = ''1'' AND ENDDATE BETWEEN PSN.STARTDATE AND
                       LAST_DAY(PSN.ENDDATE)) OR
                       (PSN.STATUS <> ''1'' AND ENDDATE BETWEEN PSN.STARTDATE AND
                       TO_DATE(''9999-09-09'', ''yyyy-mm-dd''))))
         --  AND PSN.STATUS IN (''0'')
           AND PSN.PSNTYPENO IN (''M'', ''L'')
           and TO_CHAR(psn.startdate,''yyyymmdd'')  BETWEEN ''' || V_EVAL_STARTTIME ||
             ''' AND ''' || V_EVAL_ENDTIME || '''   ) T
  LEFT JOIN E_BDS_BIDM_MGECMP B
    ON SUBSTR(T.DID, 1, 1) = B.LAORIGORGCODE
    GROUP BY PROVINCECOMCODE';
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
     'OR04015',
     'SP_MDS_SMS',
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
         'SP_MDS_SMS',
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
END SP_MDS_SMS;

/
CREATE PROCEDURE SP_ALL(V_INDATE_1 IN VARCHAR2,V_RTNCODE_1 OUT VARCHAR2,V_RTNMSG_1 OUT VARCHAR2)
IS

 BEGIN
 -- 调用存储过程的 存储过程
SP_JUDGE_FIN_DATA(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_ORACLE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_CAPITAL(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_CONSULT(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_COORDINATION(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_DW(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_ORACLE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_HASLCLAIMS(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_HASLCSBPM(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_DOCUMENT(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_ITDW(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_PLATINUM_HRM(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_PLATINUM_HRM_SSA(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_SMS(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_DTL_CONT_CHARGE_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_DTL_CONT_VISIT_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_DTL_EMPLOYEE_LOSS(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_DTL_RETREAT_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_MDS_DTL_HESI_VISIT_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_ADS_CONT_CHARGE_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_ADS_CONT_VISIT_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_ADS_EMPLOYEE_LOSS(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_ADS_HESI_VISIT_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_ADS_RETREAT_RATE(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
SP_ADS_INDEX(V_INDATE => V_INDATE_1,V_RTNCODE =>V_RTNCODE_1,V_RTNMSG =>V_RTNMSG_1);
 
 END SP_ALL;

/
CREATE PROCEDURE SP_BDS_BACKUPS( V_RTNCODE OUT VARCHAR2,
 V_RTNMSG  OUT VARCHAR2)
AS
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
BEGIN

V_START:=SYSDATE;
--清除当日备份69张表
DELETE FROM E_BDS_SSA_T_TEAMNO_HIS         NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_TEAM2TEAM_HIS      NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_TEAM_HIS           NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_RANKDEF_HIS        NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_PSN2DEPT_GX_HIS    NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_PERSON_HIS         NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_NATUREGX_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_MEMBERSET_HIS      NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_DEPT_HIS           NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_CONTRACT_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_CODEDEF_HIS        NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_CHGRANK_HIS        NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_T_FNL_SALE_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_SSA_LA_CODEITEM_HIS      NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_LISHASL_LZCARDTRACK_HIS  NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_LISHASL_LZCARDSTATE_HIS  NOLOGGING WHERE BACKDATE_IRR=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_GBS_LPGRPEMAIN_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_GBS_LPBALRELA_HIS        NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_GBS_LPBALANCEAPP_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_GBS_LJSPAY_HIS           NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_GBS_LJAPAY_HIS           NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_GBS_LCGRPAPPNT_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_SERVICE_PAY_HIS    NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_SERVICE_DD_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_PROCESSSTEP_HIS    NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_POLICY_SERVICE_HIS NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_POLICY_CHANGE_HIS  NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_PAY_INFO_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_PARAM_HIS          NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_CUST_ORG_TEMP_HIS  NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_CUSTNO_TEMP_HIS    NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_CS_107_MAIN_HIS    NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CSBPM_CS_105_MAIN_HIS    NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_TASK_TO_FIN_HIS   NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_TASKTIME_HIS      NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_SMALL_CLM_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_PAYMENT_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_PARAM_HIS         NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_LOAN_NOT_PAY_HIS  NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_LIABILITY_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_INSURANCE_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_FIN_PAY_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_FIN_RESPONSE_HIS  NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_FIN_PAYMENT_HIS   NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_DUTY_DETAIL_HIS   NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_DUTY_HIS          NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_CLAIMSAUDIT_HIS   NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CLAIMS_CLAIMS_HIS        NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CCNT_CRM_CASE3_HIS       NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CCCMP_POL_INDICATOR_HIS  NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CCCMP_POLICYS_BASE_HIS   NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CCCMP_CRM_CASE3_HIS      NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CCCMP_CRM_CALL_LIST_HIS  NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_CCCMP_COMPLAINTSRISK_HIS NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDW_POL_PER_DTL_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDW_LA_F_POL_INS_HIS    NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDW_LA_F_POLICY_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_RENEWAL_RP_FY_HIS   NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_ORGMAP_HIS          NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_MGECMP_MAP_HIS      NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_MGECMP_HIS          NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_LA_F_BUSINESS_HIS   NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_KPI_HIS             NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_FC_AREACODE_HIS     NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_CHNL_MAP_HIS        NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_BIDM_CHNLCOD_HIS         NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_ATS_CHOUDAN_TMP_HIS      NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_AFMS_HISTORY_HIS         NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;
DELETE FROM E_BDS_AFMS_HIS                 NOLOGGING WHERE BACKDATE=TO_CHAR(SYSDATE,'YYYYMMDD');
COMMIT;

--插入备份数据69张表
INSERT INTO  E_BDS_SSA_T_TEAMNO_HIS         SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_TEAMNO              A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_TEAM2TEAM_HIS      SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_TEAM2TEAM           A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_TEAM_HIS           SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_TEAM                A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_RANKDEF_HIS        SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_RANKDEF             A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_PSN2DEPT_GX_HIS    SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_PSN2DEPT_GX         A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_PERSON_HIS         SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_PERSON              A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_NATUREGX_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_NATUREGX            A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_MEMBERSET_HIS      SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_MEMBERSET           A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_DEPT_HIS           SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_DEPT                A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_CONTRACT_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_CONTRACT            A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_CODEDEF_HIS        SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_CODEDEF             A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_CHGRANK_HIS        SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_CHGRANK             A ;
COMMIT;
INSERT INTO  E_BDS_SSA_T_FNL_SALE_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_T_FNL_SALE            A ;
COMMIT;
INSERT INTO  E_BDS_SSA_LA_CODEITEM_HIS      SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_SSA_LA_CODEITEM           A ;
COMMIT;
INSERT INTO  E_BDS_LISHASL_LZCARDTRACK_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_LISHASL_LZCARDTRACK       A ;
COMMIT;
INSERT INTO  E_BDS_LISHASL_LZCARDSTATE_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_LISHASL_LZCARDSTATE       A ;
COMMIT;
INSERT INTO  E_BDS_GBS_LPGRPEMAIN_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_GBS_LPGRPEMAIN            A ;
COMMIT;
INSERT INTO  E_BDS_GBS_LPBALRELA_HIS        SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_GBS_LPBALRELA             A ;
COMMIT;
INSERT INTO  E_BDS_GBS_LPBALANCEAPP_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_GBS_LPBALANCEAPP          A ;
COMMIT;
INSERT INTO  E_BDS_GBS_LJSPAY_HIS           SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_GBS_LJSPAY                A ;
COMMIT;
INSERT INTO  E_BDS_GBS_LJAPAY_HIS           SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_GBS_LJAPAY                A ;
COMMIT;
INSERT INTO  E_BDS_GBS_LCGRPAPPNT_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_GBS_LCGRPAPPNT            A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_SERVICE_PAY_HIS    SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_SERVICE_PAY         A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_SERVICE_DD_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_SERVICE_DD          A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_PROCESSSTEP_HIS    SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_PROCESSSTEP         A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_POLICY_SERVICE_HIS SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_POLICY_SERVICE      A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_POLICY_CHANGE_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_POLICY_CHANGE       A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_PAY_INFO_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_PAY_INFO            A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_PARAM_HIS          SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_PARAM               A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_CUST_ORG_TEMP_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_CUST_ORG_TEMP       A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_CUSTNO_TEMP_HIS    SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_CUSTNO_TEMP         A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_CS_107_MAIN_HIS    SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_CS_107_MAIN         A ;
COMMIT;
INSERT INTO  E_BDS_CSBPM_CS_105_MAIN_HIS    SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CSBPM_CS_105_MAIN         A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_TASK_TO_FIN_HIS   SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_TASK_TO_FINANCE    A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_TASKTIME_HIS      SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_TASKTIME           A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_SMALL_CLM_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_SMALL_AUTO_CLAIMS  A;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_PAYMENT_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_PAYMENT            A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_PARAM_HIS         SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_PARAM              A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_LOAN_NOT_PAY_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_LOAN_NOT_PAY       A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_LIABILITY_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_LIABILITY          A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_INSURANCE_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_INSURANCE          A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_FIN_PAY_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_FINANCIAL_PAY      A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_FIN_RESPONSE_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_FINANCE_RESPONSE   A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_FIN_PAYMENT_HIS   SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_FINANCE_PAYMENT    A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_DUTY_DETAIL_HIS   SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_DUTY_DETAIL        A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_DUTY_HIS          SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_DUTY               A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_CLAIMSAUDIT_HIS   SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_CLAIMSAUDIT        A ;
COMMIT;
INSERT INTO  E_BDS_CLAIMS_CLAIMS_HIS        SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CLAIMS_CLAIMS             A ;
COMMIT;
INSERT INTO  E_BDS_CCNT_CRM_CASE3_HIS       SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CCNT_CRM_CASE3            A ;
COMMIT;
INSERT INTO  E_BDS_CCCMP_POL_INDICATOR_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CCCMP_POL_INDICATOR       A ;
COMMIT;
INSERT INTO  E_BDS_CCCMP_POLICYS_BASE_HIS   SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CCCMP_POLICYS_BASE        A ;
COMMIT;
INSERT INTO  E_BDS_CCCMP_CRM_CASE3_HIS      SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CCCMP_CRM_CASE3           A ;
COMMIT;
INSERT INTO  E_BDS_CCCMP_CRM_CALL_LIST_HIS  SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CCCMP_CRM_CALL_LIST       A ;
COMMIT;
INSERT INTO  E_BDS_CCCMP_COMPLAINTSRISK_HIS SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_CCCMP_COMPLAINTSRISK      A ;
COMMIT;
INSERT INTO  E_BDS_BIDW_POL_PER_DTL_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDW_POL_PERSONER_DETAIL  A;
COMMIT;
INSERT INTO  E_BDS_BIDW_LA_F_POL_INS_HIS    SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDW_LA_F_POL_INSURANCE   A ;
COMMIT;
INSERT INTO  E_BDS_BIDW_LA_F_POLICY_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDW_LA_F_POLICY          A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_RENEWAL_RP_FY_HIS   SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_RENEWAL_RP_FY        A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_ORGMAP_HIS          SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_ORGMAP               A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_MGECMP_MAP_HIS      SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_MGECMP_MAP           A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_MGECMP_HIS          SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_MGECMP               A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_LA_F_BUSINESS_HIS   SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_LA_F_BUSINESS        A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_KPI_HIS             SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_KPI                  A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_FC_AREACODE_HIS     SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_FC_AREACODE          A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_CHNL_MAP_HIS        SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_CHNL_MAP             A ;
COMMIT;
INSERT INTO  E_BDS_BIDM_CHNLCOD_HIS         SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_BIDM_CHNLCOD              A ;
COMMIT;
INSERT INTO  E_BDS_ATS_CHOUDAN_TMP_HIS      SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_ATS_CHOUDAN_TMP           A ;
COMMIT;
INSERT INTO  E_BDS_AFMS_HISTORY_HIS         SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_AFMS_AFMS_AFMS_HISTORY    A ;
COMMIT;
INSERT INTO  E_BDS_AFMS_HIS                 SELECT A.*,TO_CHAR(SYSDATE,'YYYYMMDD') from  E_BDS_AFMS_AFMS_AFMS            A ;
COMMIT;
V_END:=SYSDATE;
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
     '',
     'SP_BDS_BACKUPS',
     'DELETE_INSERT_HRM_DATA',
     V_START,
     V_END,
     to_char(SYSDATE,'yyyymmdd'),
     '',
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
 EXCEPTION
    WHEN OTHERS THEN
      BEGIN
        ROLLBACK;
        V_END     := SYSDATE;
        V_RTNCODE := '1';
        V_RTNMSG  := SQLERRM;
  -----------------------------------加载日志信息———--------------------------------
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
     'SP_BDS_BACKUPS',
     'DELETE_INSERT_HRM_DATA',
     V_START,
     V_END,
     to_char(SYSDATE,'yyyymmdd'),
     '',
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
   -----------------------------------加载日志信息完成--------------------------------
     END;
END SP_BDS_BACKUPS;

/
CREATE PROCEDURE SP_CREATE_INDEX(
 V_RTNCODE OUT VARCHAR2,
 V_RTNMSG  OUT VARCHAR2
)
AS
V_SQL        VARCHAR2(1000);
V_RECORD_NUM VARCHAR2(500);
V_START      DATE;
V_END        DATE;
BEGIN

V_SQL:='CREATE INDEX CAU_INDEX_01 ON E_BDS_CLAIMS_CLAIMSAUDIT(REPORT_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CAU_INDEX_02 ON E_BDS_CLAIMS_CLAIMSAUDIT(REPORT_ID, POLICY_NO, INSURANCE_CODE, HIS_TAG)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_CHANGE_IDX_CHANGE_ID ON E_BDS_CSBPM_POLICY_CHANGE(CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_PROSTEP_ACTION_NAME ON E_BDS_CSBPM_PROCESSSTEP(ACTION_NAME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_PROSTEP_STEP_NAME ON E_BDS_CSBPM_PROCESSSTEP(STEP_NAME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX CSBPM_SERVICE_IDX_CHANGE_ID ON E_BDS_CSBPM_POLICY_SERVICE(CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX DBA_052009 ON E_BDS_LISHASL_LZCARDTRACK(MANAGECOM, OPERATEFLAG, MAKEDATE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX DBA_11102602 ON E_BDS_LISHASL_LZCARDTRACK(MANAGECOM, OPERATEFLAG, STATE, MAKEDATE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX DUTY_DETAIL_INDEX01 ON E_BDS_CLAIMS_DUTY_DETAIL(DUTY_SERNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_BIDM_RENEWAL_CHNLCODE ON E_BDS_BIDM_RENEWAL_RP_FY(ADD_CHNLCOD)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_BIDM_RENEWAL_ORGCODE ON E_BDS_BIDM_RENEWAL_RP_FY(INNERORGCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_CASEID_INDEX ON E_BDS_CCCMP_CRM_CALL_LIST(CASEID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_DBA_01090901 ON E_BDS_BIDM_MGECMP_MAP(PROVINCECOMCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_DBA_13051306 ON E_BDS_CCNT_CRM_CASE3(DATIME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX$$_180390002 ON E_BDS_CCNT_CRM_CASE3(SUBSTR("CHDRCOYID",0,1), CHDRNUM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX_CRM_CASE3 ON E_BDS_CCNT_CRM_CASE3(CHDRCOYID, CHDRNUM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX_D_MAN_MAP_001 ON E_BDS_BIDM_MGECMP_MAP(BRANCHCOMCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_IDX_D_ORGMAP_001 ON E_BDS_BIDM_ORGMAP(INNERORGCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_INDEX_D_ORGMAP1 ON E_BDS_BIDM_ORGMAP(ORIGORGCODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISHASL_LPMAIN_IDE1 ON E_BDS_GBS_LPGRPEMAIN(EDORSTATE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LCGRPAPPNT_IDX ON E_BDS_GBS_LCGRPAPPNT(GRPNAME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDE3 ON E_BDS_GBS_LJSPAY(POLICYNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX1 ON E_BDS_GBS_LJSPAY(OTHERNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX2 ON E_BDS_GBS_LJSPAY(OTHERNOTYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX4 ON E_BDS_GBS_LJSPAY(CONTTYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX5 ON E_BDS_GBS_LJSPAY(MANAGECOM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_NEW_LISH_LJSPAY_IDX6 ON E_BDS_GBS_LJSPAY(BALANCERELANO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_POLICYNO_INDEX ON E_BDS_CCCMP_CRM_CASE3(POLICYNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_POLICY_INDEX ON E_BDS_CCCMP_CRM_CALL_LIST(POLICYNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_CLAIMS_STATUS ON E_BDS_CLAIMS_CLAIMS(STATUS)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_DUTY_INDEX01 ON E_BDS_CLAIMS_DUTY(REPORT_ID, POLICY_NO, ASS_CLAIMS_FLAG, COMPUTE_FLAG, INSURANCE_CODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_INSURANCE_KEY1 ON E_BDS_CLAIMS_INSURANCE(REPORT_ID, POLICY_NO, ASS_CLAIMS_FLAG)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_TC_INSURANCE_KEY2 ON E_BDS_CLAIMS_INSURANCE(INSURANCE_CODE, INSURANCE_TYPE, REPORT_ID, POLICY_NO, ASS_CLAIMS_FLAG)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_T_POL_APP_CARD_ID ON E_BDS_CSBPM_POLICY_CHANGE(APPLY_CARD_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_BDS_T_PROCESSSTEP_CHANGE_ID ON E_BDS_CSBPM_PROCESSSTEP(CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_T_PAY_INFO_SUB_CHANGE_ID ON E_BDS_CSBPM_PAY_INFO(SUB_CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX E_T_POL_SER_SUB_CHANGE_ID ON E_BDS_CSBPM_POLICY_SERVICE(SUB_CHANGE_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX IDX_LZCARDTRACK01 ON E_BDS_LISHASL_LZCARDTRACK(RECEIVECOM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX IDX_LZCARDTRACK02 ON E_BDS_LISHASL_LZCARDTRACK(SENDOUTCOM)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX IDX_LZCARDTRACK03 ON E_BDS_LISHASL_LZCARDTRACK(STARTNO, ENDNO)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX01 ON E_BDS_CLAIMS_LIABILITY(INSURANCE_CODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX02 ON E_BDS_CLAIMS_LIABILITY(IS_JINTIE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX03 ON E_BDS_CLAIMS_LIABILITY(PRODUCT_TYPE, DUTY_TYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX LIABILITY_INDEX04 ON E_BDS_CLAIMS_LIABILITY(NOT_PAY_TYPE, PAY_PERCENT, IS_MEDICAL_INSURANCE, VALIDFLAG, IS_OW_DUTY)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX PARAM_INDEX_01 ON E_BDS_CLAIMS_PARAM(PAR_TYPE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX SYS_C00169891A ON E_BDS_CLAIMS_CLAIMS(REPORT_ID)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX TASK_TIME_INDEX01 ON E_BDS_CLAIMS_TASKTIME(REPORT_ID, CODE)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX TASK_TIME_INDEX02 ON E_BDS_CLAIMS_TASKTIME(END_TIME)';
 EXECUTE IMMEDIATE V_SQL;
V_SQL:='CREATE INDEX TFPAY_INDEX1 ON E_BDS_CLAIMS_FINANCE_PAYMENT(BIDUIID, BANKACCOUNT, BANKNAME, DETAILSUM)';
 EXECUTE IMMEDIATE V_SQL;
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
     '',
     'SP_CREATE_INDEX',
     '',
     V_START,
     V_END,
     '',
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  ----------------------------------????--------------------------------------
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_END     := SYSDATE;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
  --------------------------------????????-----------------------------
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
         'SP_CREATE_INDEX',
         '',
         V_START,
         V_END,
         '',
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
  --------------------------------????????-----------------------------
    END;
END SP_CREATE_INDEX;

/
CREATE PROCEDURE SP_DROP_INDEX(
 V_RTNCODE OUT VARCHAR2,
 V_RTNMSG  OUT VARCHAR2
)
AS
V_SQL        VARCHAR(1000);
V_RECORD_NUM VARCHAR(500);
V_START      DATE;
V_END        DATE;
BEGIN
V_SQL:='DROP INDEX IDX_F_POLICY_INNERPOL';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_LA_F_POLICY';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_LA_F_POLICY_001';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_1';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_2';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_3';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_4';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX LA_F_POLICY_IDX_5';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX E_BDS_DBA_01090906';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_1';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_2';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_3';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_4';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_5';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX POL_PERSONER_DETAIL_IDX_6';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_POL_INSURANCE';   
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX IDX_POL_INSURANCE2';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_APPLYENTITY';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_ATS_RETURNSTATE';   
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_DTCODE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_FAILTYPE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_REQSEQID';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_REQSERVED12';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_SOURCENOTECODE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_TRANSCODE';  
EXECUTE IMMEDIATE V_SQL;                          
V_SQL:='DROP INDEX INDEX_TRANSSTATE';  
EXECUTE IMMEDIATE V_SQL; 
V_SQL:='DROP INDEX PK_ATS_RDSEQ';  
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_ATS_SEQ';  
EXECUTE IMMEDIATE V_SQL;  
V_SQL:='DROP INDEX KEY_D_FC_AREACODE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_DBA_01090901';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX_D_MAN_MAP_001';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX_D_ORGMAP_001';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_INDEX_D_ORGMAP1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_BIDM_RENEWAL_CHNLCODE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_BIDM_RENEWAL_ORGCODE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_CASEID_INDEX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_POLICY_INDEX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_CRM_CALLBACK_LIST';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_POLICYNO_INDEX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_CRM_CASE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_POLICYS_BASE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_CCREPORT_POL_BAT_TRANS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_DBA_13051306';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX$$_180390002';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_IDX_CRM_CASE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_CLAIMS_STATUS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C00169891A';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318636';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CAU_INDEX_01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CAU_INDEX_02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318602';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_DUTY_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318638';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX DUTY_DETAIL_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318640';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318613';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX TFPAY_INDEX1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318617';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003320058';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_INSURANCE_KEY1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_TC_INSURANCE_KEY2';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318642';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX03';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX LIABILITY_INDEX04';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PARAM_INDEX_01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318632';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318609';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318606';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX TASK_TIME_INDEX01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX TASK_TIME_INDEX02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318620';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318592';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318594';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318596';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_T_PAY_INFO_SUB_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_CHANGE_IDX_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_T_POL_APP_CARD_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_SERVICE_IDX_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_T_POL_SER_SUB_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_PROSTEP_ACTION_NAME';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX CSBPM_PROSTEP_STEP_NAME';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_T_PROCESSSTEP_CHANGE_ID';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318600';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003318610';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003328123';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LCGRPAPPNT_IDX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321824';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX2';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX4';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX5';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISH_LJSPAY_IDX6';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LJSPAY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321851';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321859';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX E_BDS_NEW_LISHASL_LPMAIN_IDE1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX SYS_C003321870';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LZCARDSTATE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX DBA_052009';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX DBA_11102602';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX IDX_LZCARDTRACK01';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX IDX_LZCARDTRACK02';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX IDX_LZCARDTRACK03';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LZCARDTRACK';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_LA_CODEITEM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_CHGRANK';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_CODEDEF';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_CONTRACT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_DEPT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_MEMBERSET';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_NATUREGX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_PERSON';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_PSN2DEPT_GX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_RANKDEF';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_TEAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_TEAM2TEAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_TEAMNO';
EXECUTE IMMEDIATE V_SQL;    
V_SQL:='DROP INDEX PK_T_PERSON_1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='DROP INDEX PK_T_PERSON_2';
EXECUTE IMMEDIATE V_SQL;               
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
     '',
     'SP_DROP_INDEX',
     '',
     V_START,
     V_END,
     '',
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  ----------------------------------????--------------------------------------
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_END     := SYSDATE;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
  --------------------------------????????-----------------------------
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
         'SP_DROP_INDEX',
         '',
         V_START,
         V_END,
         '',
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
  --------------------------------????????-----------------------------
    END;
END SP_DROP_INDEX;

/
CREATE PROCEDURE SP_HRM_HANDLE(V_INDATE  VARCHAR2,
                                          V_RTNCODE OUT VARCHAR2,
                                          V_RTNMSG  OUT VARCHAR2) AS
  /**********************************************************************/
  /* Procedure Name : SP_HRM_HANDLE                                     */
  /* Developed By   : WH                                                */
  /* Developed Date : 2019-4-16                                         */
  /* Target table   : E_BDS_HRM_EMPLOYEEDEGREE    员工学历表            */
  /*                  E_BDS_HRM_EMPLOYEESTATE     员工表                */
  /*                  E_BDS_HRM_MANAGESTATE       管理层表              */
  /*                  E_BDS_HRM_ORGANIZATION      机构表                */
  /* Source table   : E_BDS_HRM_EMPLOYEEDEGREE_MIR 员工学历镜像表       */
  /*                  E_BDS_HRM_EMPLOYEESTATE_MIR  员工镜像表           */
  /*                  E_BDS_HRM_MANAGESTATE_MIR    管理层镜像表         */
  /*                  E_BDS_HRM_ORGANIZATION_MIR  机构镜像表            */
  /* AIM：获取铂金人力四张接口表中当月数据字段(A)插入目标表中           */
  /********************** Variable Section ******************************/
  V_START          DATE;            --执行开始时间
  V_END            DATE;            --执行结束时间
  V_RECORD_NUM     NUMBER;          --记录条数
  V_SQL            VARCHAR2(20000); --动态语句
BEGIN
  
  /*****************************开始清除当天备份*****************************/  --清除员工学历历史表当天备份
  --清除员工学历表当天备份
  V_SQL :='DELETE FROM E_BDS_HRM_EMPLOYEEDEGREE_HIS
  WHERE BACKDATE=to_char(SYSDATE,''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;

  --清除员工历史表当天备份
  V_SQL :='DELETE FROM E_BDS_HRM_EMPLOYEESTATE_HIS
  WHERE BACKDATE=to_char(SYSDATE,''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;

  --清除管理层历史表当天备份
  V_SQL :='DELETE FROM E_BDS_HRM_MANAGESTATE_HIS
  WHERE BACKDATE=to_char(SYSDATE,''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;

  --清除机构历史表当天备份
  V_SQL :='DELETE FROM E_BDS_HRM_ORGANIZATION_HIS
  WHERE BACKDATE=to_char(SYSDATE,''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;
  /*****************************清除当天备份结束*****************************/


  /*******************************开始备份数据*******************************/
  --备份员工学历镜像表
  INSERT INTO E_BDS_HRM_EMPLOYEEDEGREE_HIS
  SELECT payrid,orgcode2,orgcode3,orgcode4,B,TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM'),TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')
  FROM E_BDS_HRM_EMPLOYEEDEGREE_MIR;
  COMMIT;
  --备份员工镜像表
  INSERT INTO E_BDS_HRM_EMPLOYEESTATE_HIS
  SELECT payrid,orgcode2,orgcode3,orgcode4,C1,B1,B2,B3,TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM'),TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')
  FROM E_BDS_HRM_EMPLOYEESTATE_MIR;
   COMMIT;
  --备份管理层镜像表
  INSERT INTO E_BDS_HRM_MANAGESTATE_HIS
  SELECT payrid,orgcode2,orgcode3,orgcode4,C1,B1,B2,B3,TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM'),TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')
  FROM E_BDS_HRM_MANAGESTATE_MIR;
   COMMIT;
  --备份机构镜像表
  INSERT INTO E_BDS_HRM_ORGANIZATION_HIS(
  code
  ,typeid
  ,hrid
  ,e_name
  ,c_name
  ,effectivedate
  ,countrycode
  ,companycode
  ,extensioncode
  ,nondisclosureflag
  ,managerid
  ,attribute1
  ,attribute2
  ,attribute3
  ,attribute4
  ,backdate
  )
  SELECT
   code
  ,typeid
  ,hrid
  ,e_name
  ,c_name
  ,effectivedate
  ,countrycode
  ,companycode
  ,extensioncode
  ,nondisclosureflag
  ,managerid
  ,attribute1
  ,attribute2
  ,attribute3
  ,attribute4
  ,TO_CHAR(SYSDATE,'YYYYMM')
  FROM E_BDS_HRM_ORGANIZATION_MIR;
   COMMIT;
  /******************************备份数据完成*******************************/



  /*****************************开始清除上月数据*****************************/  
  --清除员工学历表上月数据
  V_SQL :='DELETE FROM E_BDS_HRM_EMPLOYEEDEGREE
  WHERE DATA_DATE = TO_CHAR(ADD_MONTHS(SYSDATE,-1),''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;
    --清除员工表上月数据
  V_SQL :='DELETE FROM E_BDS_HRM_EMPLOYEESTATE
  WHERE DATA_DATE = TO_CHAR(ADD_MONTHS(SYSDATE,-1),''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;
    --清除管理层表上月数据
  V_SQL :='DELETE FROM E_BDS_HRM_MANAGESTATE
  WHERE DATA_DATE = TO_CHAR(ADD_MONTHS(SYSDATE,-1),''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;
    --清除机构表上月数据
  V_SQL :='DELETE FROM E_BDS_HRM_ORGANIZATION
  WHERE DATA_DATE = TO_CHAR(ADD_MONTHS(SYSDATE,-1),''YYYYMM'')';
  EXECUTE IMMEDIATE V_SQL;

  /******************************开始插入数据*******************************/
  
 --员工学历表
  INSERT INTO E_BDS_HRM_EMPLOYEEDEGREE
  SELECT payrid,orgcode2,orgcode3,orgcode4,B,TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')
  FROM E_BDS_HRM_EMPLOYEEDEGREE_MIR A
  ;
   COMMIT;
  --因为取数日志修改为每个月的12号，之前取数逻辑：B1,A1,A2,A3 ,现取数逻辑：C1,B1,B2,B3 需要错开一位
  --员工表
  INSERT INTO E_BDS_HRM_EMPLOYEESTATE
  SELECT payrid,orgcode2,orgcode3,orgcode4,C1,B1,B2,B3,TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')
  FROM E_BDS_HRM_EMPLOYEESTATE_MIR;
   COMMIT;
  --管理层表
  INSERT INTO E_BDS_HRM_MANAGESTATE
  SELECT payrid,orgcode2,orgcode3,orgcode4,C1,B1,B2,B3,TO_CHAR(ADD_MONTHS(SYSDATE,-1),'YYYYMM')
  FROM E_BDS_HRM_MANAGESTATE_MIR;
   COMMIT;
  --机构表
  INSERT INTO E_BDS_HRM_ORGANIZATION(
  code
  ,typeid
  ,hrid
  ,e_name
  ,c_name
  ,effectivedate
  ,countrycode
  ,companycode
  ,extensioncode
  ,nondisclosureflag
  ,managerid
  ,attribute1
  ,attribute2
  ,attribute3
  ,attribute4
  )
  SELECT
   code
  ,typeid
  ,hrid
  ,e_name
  ,c_name
  ,effectivedate
  ,countrycode
  ,companycode
  ,extensioncode
  ,nondisclosureflag
  ,managerid
  ,attribute1
  ,attribute2
  ,attribute3
  ,attribute4
  FROM E_BDS_HRM_ORGANIZATION_MIR;
   COMMIT;
  
   /******************************插入数据完成*******************************/
   V_RTNCODE := '0';
   V_RTNMSG  := '执行成功';
   /**************************** 开始加载日志信息 ***************************/
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
       'SP_HRM_HANDLE',
       'ALL_HRM_TABLES',
       V_START,
       V_END,
       to_char(SYSDATE,'yyyymmdd'),
       V_RECORD_NUM,
       V_RTNCODE,
       V_RTNMSG);
    COMMIT;
/**************************** 加载日志信息完成 ******************************/

   /********************************异常处理*********************************/
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
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
       'SP_HRM_HANDLE',
       'ALL_HRM_TABLES',
       V_START,
       V_END,
       to_char(SYSDATE,'yyyymmdd'),
       V_RECORD_NUM,
       V_RTNCODE,
       V_RTNMSG);
    COMMIT;
    END;
    /**************************************** 加载日志信息完成 ****************************************/
END SP_HRM_HANDLE;

/
CREATE PROCEDURE SP_TRUNCATE_TABLE(
 V_RTNCODE OUT VARCHAR2,
 V_RTNMSG  OUT VARCHAR2
)
AS
V_SQL        VARCHAR(1000);
V_RECORD_NUM VARCHAR(500);
V_START      DATE;
V_END        DATE;
BEGIN
V_SQL:='TRUNCATE TABLE E_BDS_AFMS_AFMS_AFMS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_AFMS_AFMS_AFMS_HISTORY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_ATS_CHOUDAN_TMP';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_CHNLCOD';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_CHNL_MAP';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_FC_AREACODE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_KPI';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_LA_F_BUSINESS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_MGECMP';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_MGECMP_MAP';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_ORGMAP';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_RENEWAL_RP_FY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDW_LA_F_POLICY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDW_LA_F_POL_INSURANCE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDW_POL_PERSONER_DETAIL';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CCCMP_COMPLAINTSRISK';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CCCMP_CRM_CALL_LIST';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CCCMP_CRM_CASE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CCCMP_POLICYS_BASE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CCCMP_POL_INDICATOR';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CCNT_CRM_CASE3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_CLAIMS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_CLAIMSAUDIT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_DUTY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_DUTY_DETAIL';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_FINANCE_PAYMENT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_FINANCE_RESPONSE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_INSURANCE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_LIABILITY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_LOAN_NOT_PAY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_PARAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_PAYMENT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_SMALL_AUTO_CLAIMS';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_TASKTIME';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CLAIMS_TASK_TO_FINANCE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_CS_105_MAIN';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_CS_107_MAIN';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_PARAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_PAY_INFO';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_POLICY_CHANGE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_POLICY_SERVICE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_PROCESSSTEP';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_SERVICE_DD';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_GBS_LCGRPAPPNT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_GBS_LJAPAY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_GBS_LJSPAY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_GBS_LPBALANCEAPP';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_GBS_LPBALRELA';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_GBS_LPGRPEMAIN';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_LISHASL_LZCARDSTATE';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_LISHASL_LZCARDTRACK';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_LA_CODEITEM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_CHGRANK';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_CODEDEF';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_CONTRACT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_DEPT';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_MEMBERSET';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_NATUREGX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_PERSON';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_PSN2DEPT_GX';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_RANKDEF';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_TEAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_TEAM2TEAM';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_TEAMNO';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_CSBPM_SERVICE_PAY';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_KPI_1';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_KPI_2';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_KPI_3';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_BIDM_KPI_4';
EXECUTE IMMEDIATE V_SQL;
V_SQL:='TRUNCATE TABLE E_BDS_SSA_T_FNL_SALE';
EXECUTE IMMEDIATE V_SQL;
V_RTNCODE    := '0';
  V_RTNMSG     := '????';
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
     'SP_DROP_INDEX',
     '',
     V_START,
     V_END,
     '',
     V_RECORD_NUM,
     V_RTNCODE,
     V_RTNMSG);
  COMMIT;
  ----------------------------------????--------------------------------------
EXCEPTION
  WHEN OTHERS THEN
    BEGIN
      ROLLBACK;
      V_END     := SYSDATE;
      V_RTNCODE := '1';
      V_RTNMSG  := SQLERRM;
  --------------------------------????????-----------------------------
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
         'SP_DROP_INDEX',
         '',
         V_START,
         V_END,
         '',
         V_RECORD_NUM,
         V_RTNCODE,
         V_RTNMSG);
      COMMIT;
  --------------------------------????????-----------------------------
    END;
END SP_TRUNCATE_TABLE;

/

