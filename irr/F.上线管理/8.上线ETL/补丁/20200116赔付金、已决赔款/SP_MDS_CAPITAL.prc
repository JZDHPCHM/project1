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
  V_EVAL_STARTTIME VARCHAR2(10);   --�����ڿ�ʼʱ��
  V_EVAL_ENDTIME   VARCHAR2(10);   --�����ڽ���ʱ��
  V_START          DATE;           --ִ�п�ʼʱ��
  V_END            DATE;           --ִ�н���ʱ��
  V_RECORD_NUM     NUMBER;         --��¼����
  V_SQL            VARCHAR2(2000); --��̬���
/***********************************************************************/
BEGIN
   V_EVAL_STARTTIME := TO_CHAR(ADD_MONTHS(TO_DATE(FUN_ETL_Q_DATE(V_INDATE), 'yyyymmdd') + 1, -3),'yyyymmdd');
   V_EVAL_ENDTIME :=  FUN_ETL_Q_DATE(V_INDATE);
--------------------------------ɾ������-------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR13007''
   AND TO_CHAR(LOAD_DT,''yyyymmdd'') = '|| V_INDATE ||'';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
--------------------------------��������--------------------------------
  V_START := SYSDATE;
  --���������˱���
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
 where  a.transcode = ''1988'' --����
   AND A.PAYMADEDATE BETWEEN to_date('''||V_EVAL_STARTTIME||'''||''000000'',''yyyymmddhh24miss'') AND
      to_date( '''||V_EVAL_ENDTIME||'''||''235959'',''yyyymmddhh24miss'') -- �·�
   and a.transstate = ''2'' --֧���ɹ�
   and a.transsource = ''CUSTOMER SERVICE''
   and a.reqreserved18 in (''301'', -- �����˱�
                           ''302'', -- ͨ���˱�
                           ''304'', --  �������˱� 
                           ''305'', --  ��ԥ�����˱�  
                           ''306'', --  ��������CFI 
                           ''307'' -- ���������˱�
                          )
 GROUP BY PROVINCECOMCODE,REQRESERVED13
 ORDER BY PROVINCECOMCODE,REQRESERVED13
';
dbms_output.put_Line(V_SQL);
  EXECUTE IMMEDIATE V_SQL;
  -----------------------------��ʼ������־��Ϣ-------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END        := SYSDATE;
  V_RTNCODE    :='0';
  V_RTNMSG     :='ִ�гɹ�';
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
 --------------------------------ɾ������--------------------------------
  V_SQL := 'DELETE FROM E_MDS_IRR_INDEX_VALUE WHERE INDEX_ID = ''OR13006''
  AND TO_CHAR(LOAD_DT,''yyyymmdd'') = '|| V_INDATE ||'';
  EXECUTE IMMEDIATE V_SQL;
  COMMIT;
  --------------------------------��������--------------------------------
    --���������⸶��
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
 WHERE  a.transcode = ''1988''          -- ����
  AND A.PAYMADEDATE BETWEEN to_date('''||V_EVAL_STARTTIME||'''||''000000'',''yyyymmddhh24miss'') AND
      to_date( '''||V_EVAL_ENDTIME||'''||''235959'',''yyyymmddhh24miss'') -- �·�
   AND a.transstate = ''2''             -- ֧���ɹ�
   AND (a.transsource IN (''GR'', ''CUSTOMER CLAIM'',''STIS'',''MID'') OR
       (a.transsource = ''CUSTOMER SERVICE'' AND
       a.reqreserved18 NOT IN (''301'',     -- �����˱�
                               ''302'',     -- ͨ���˱�
                               ''304'',     -- �������˱�
                               ''305'',     -- ��ԥ�����˱�
                               ''306'',     -- ��������CFI
                               ''307'')      -- ���������˱�
                           ))
  GROUP BY PROVINCECOMCODE,REQRESERVED13
 ORDER BY PROVINCECOMCODE,REQRESERVED13';
 dbms_output.put_Line(V_SQL);
  EXECUTE IMMEDIATE V_SQL;
  -------------------------------������־��Ϣ���----------------------------------
  -------------------------------��ʼ������־��Ϣ----------------------------------
  V_RECORD_NUM := SQL%ROWCOUNT;
  V_END := SYSDATE;
  V_RTNCODE :='0';
  V_RTNMSG :='ִ�гɹ�';
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
  --------------------------------�쳣����-----------------------------------------
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
  -------------------------------������־��Ϣ���----------------------------------
  END;
END SP_MDS_CAPITAL;
/
