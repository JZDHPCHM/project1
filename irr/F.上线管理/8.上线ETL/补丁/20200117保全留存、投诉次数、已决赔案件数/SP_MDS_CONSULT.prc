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
  V_EVAL_STARTTIME VARCHAR2(10); --�����ڿ�ʼʱ��
  V_EVAL_ENDTIME   VARCHAR2(10); --�����ڽ���ʱ��
  V_START          DATE; --ִ�п�ʼʱ��
  V_END            DATE; --ִ�н���ʱ��
  V_RECORD_NUM     NUMBER; --��¼����
  V_SQL            VARCHAR2(20000); --��̬���
  /***********************************************************************/
BEGIN
  V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
  V_EVAL_ENDTIME   := FUN_ETL_Q_DATE(V_INDATE);
  --------------------------------ɾ������-------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR06004'' AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------��������--------------------------------
  V_START := SYSDATE;
  --Ͷ�ߴ���ƽ��ʱ��
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
                  ROUND(SUM(to_date(CLOSURE_TIME,''yyyymmdd'')-to_date(COMPLAINT_DATE,''yyyymmdd''))   --�᰸����-��������
                  /COUNT(ID),4),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
                  FROM E_BDS_CCCMP_COMPLAINTSRISK
                  WHERE MAIN_STATUSNAME = ''Ͷ�߼�'' --��������
                  AND TO_DATE(CLOSURE_TIME,''yyyy-mm-dd'') BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'')';--�᰸��������
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------��ʼ������־��Ϣ-------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := 'ִ�гɹ�';
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
  --------------------------------ɾ������--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR04012''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------��������--------------------------------
  --�����ڹ�˾�������ЧͶ�߼�������
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
                DECODE(TYPENAME,''����'',''FC'',''��Ԫ'',''AD'',''����'',''GP'',''����'',''BK'',''RP''),
                COUNT(1),
                CASE WHEN TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1 =''0''
                    THEN (TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')-1)||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')+3)
                      ELSE TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''yyyy'')||''Q''||(TO_CHAR(to_date('''|| V_INDATE ||''',''yyyy-mm-dd''),''q'')-1) END,
                 TO_DATE( '''|| V_INDATE ||''',''YYYYMMDD'')
  FROM E_BDS_CCCMP_COMPLAINTSRISK A
  LEFT JOIN E_BDS_BIDM_MGECMP B
    ON B.LAORIGORGCODE = A.CHDRCOY
 WHERE A.MAIN_STATUSNAME = ''Ͷ�߼�'' --��������
   AND TO_DATE(A.CLOSURE_TIME,''yyyy-mm-dd'') BETWEEN to_date('''||V_EVAL_STARTTIME||''',''yyyy-mm-dd'') AND
      to_date( '''||V_EVAL_ENDTIME||''',''yyyy-mm-dd'') --�᰸��������
 GROUP BY B.PROVINCECOMCODE,DECODE(TYPENAME,''����'',''FC'',''��Ԫ'',''AD'',''����'',''GP'',''����'',''BK'',''RP'')';
 DBMS_OUTPUT.PUT_LINE(V_SQL);
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------------��ʼ������־��Ϣ--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := 'ִ�гɹ�';
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
  --------------------------------ɾ������--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR06005''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------��������--------------------------------
  --�����ڱ��չ�˾�������⡢��ȫҵ���ߵ�Ͷ�ߴ���
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
            AND REGEXP_REPLACE(NAME, ''[0-9]+��'')=''�������''--Ͷ��ԭ��
            AND MAIN_STATUSNAME = ''Ͷ�߼�''--��������'; 

  EXECUTE IMMEDIATE V_SQL;
   DBMS_OUTPUT.PUT_LINE(V_SQL);
  -----------------------------------��ʼ������־��Ϣ--------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := 'ִ�гɹ�';
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
  --------------------------------ɾ������--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR02011''AND TO_CHAR(LOAD_DT,''yyyymmdd'') = ' || V_INDATE || '';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------��������--------------------------------
  --�����ڱ��չ�˾���ڳб�������ҵ���ߵ�Ͷ�ߴ���
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
       AND  MAIN_STATUSNAME = ''Ͷ�߼�'' --��������
            AND REGEXP_REPLACE(NAME, ''[0-9]+��'')--Ͷ��ԭ��
     IN (''�󵼿ͻ�'',''��ǩ��'',''��ʵ��֪'',''չҵ���淶'',''�б�����'',''һվʽ��д����'',''�������շѡ����ս�'')';
  EXECUTE IMMEDIATE V_SQL;
   DBMS_OUTPUT.PUT_LINE(V_SQL);
  --------------------------------������־��Ϣ���-----------------------------
  --------------------------------��ʼ������־��Ϣ-----------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    := '0';
  V_RTNMSG     := 'ִ�гɹ�';
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
  --------------------------------�쳣����-------------------------------------
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
  --------------------------------������־��Ϣ���-----------------------------
    END;
END SP_MDS_CONSULT;
/
