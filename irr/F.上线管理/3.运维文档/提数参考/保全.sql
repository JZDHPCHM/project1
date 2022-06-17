SELECT PROVINCECOMCODE org,count(T2.policy_nos) custno

FROM E_BDS_CSBPM_CUSTNO_TEMP t1 JOIN (

SELECT B.PROVINCECOMCODE, CLTNUM, STATUS, POLICY_NOS,
  SERVICE_NAME,CHANNEL,START_DATE, START_TIME,MINDATE
    FROM   E_BDS_BIDM_MGECMP B
   RIGHT JOIN  (SELECT
          --������
           SUBSTR(T.DID, 0, 1) COMPANY,
           --��ȫ��
         T.CHANGE_ID,
           T.FIN,
           T.BUS_SOURCE,
           --״̬
           T.STATUS,
           T.CHANNEL,
           T.SERVICE_NAME,
          CASE WHEN  PAYLIST.MINDATE
             IS NULL THEN FIN
               ELSE  PAYLIST.MINDATE
          END MINDATE,
           TO_CHAR(T.START_DATE,'YYYYMMDD')  START_DATE,
           --��ʼʱ��
         TO_CHAR(T.START_DATE, 'HH24:MI:SS') START_TIME,
           --������
           T.POLICY_NOS,
           --�ͻ���
           (CASE T.SERVICE_ID
             WHEN '105' THEN
              (SELECT M.CUSTOMER_ID
                 FROM E_BDS_CSBPM_CS_105_MAIN M
                WHERE M.CHANGE_ID = T.CHANGE_ID
                  AND M.SUB_CHANGE_ID = T.SUB_CHANGE_ID)
             WHEN '107' THEN
              (SELECT M.CUSTOMER_ID
                 FROM E_BDS_CSBPM_CS_107_MAIN M
                WHERE M.CHANGE_ID = T.CHANGE_ID
                  AND M.SUB_CHANGE_ID = T.SUB_CHANGE_ID)
             ELSE
               --NULL
              (SELECT CUSTOMER_ID FROM E_BDS_CSBPM_POLICY_SERVICE T1 WHERE T1.CHANGE_ID = T.CHANGE_ID AND ROWNUM <2 )
           END) AS CLTNUM,
           --���ͨ������
           TRUNC((CASE
                   WHEN SERVICE_ID = '804' THEN
                    CHECKER_SUBMIT_TIME
                   WHEN SERVICE_ID IN (SELECT T.PARAM_CODE
                                         FROM E_BDS_CSBPM_PARAM T
                                        WHERE T.PARAM_VALUE = 'AUTO'
                                          AND T.USING = '1') THEN
                    T.START_DATE
                   WHEN SERVICE_ID = '101' THEN
                    (CASE
                      WHEN P_ACC.OPERATOR_TIME > T.FIN THEN
                       T.FIN
                      ELSE
                       P_ACC.OPERATOR_TIME
                    END)
                   WHEN SERVICE_ID = '917' THEN
                    DD.SEND_DD_TIME
                   WHEN DD.SEND_DD_TIME IS NOT NULL THEN
                    (SELECT MAX(OPERATOR_TIME) AS OPERATOR_TIME
                       FROM E_BDS_CSBPM_PROCESSSTEP VER_DD
                      WHERE VER_DD.STEP_NAME = 'verify'
                        AND VER_DD.CHANGE_ID = T.CHANGE_ID
                        AND VER_DD.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                        AND VER_DD.OPERATOR_TIME <= DD.SEND_DD_TIME)
                 --20180913 ��ʱ����
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
                           WHERE P.PARAM_TYPE = 'busSourceAll'
                             AND P.PARAM_CODE = T2.BUS_SOURCE) BUS_SOURCE,
                         (SELECT MIN(OPERATOR_TIME)
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE CHANGE_ID = T1.CHANGE_ID
                             AND STEP_NAME = 'accept') ACC,
                         (SELECT MAX(OPERATOR_TIME)
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE CHANGE_ID = T1.CHANGE_ID
                             AND STEP_NAME = 'accept') ACC_MAX_TIME,
                         ((CASE
                           WHEN VERIFY_RTN.OPERATOR_TIME IS NULL THEN
                            (SELECT MAX(OPERATOR_TIME)
                               FROM E_BDS_CSBPM_PROCESSSTEP
                              WHERE CHANGE_ID = T1.CHANGE_ID
                                AND STEP_NAME = 'verify'
                                AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID)
                           ELSE
                            ((SELECT MAX(OPERATOR_TIME)
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE T.CHANGE_ID = T1.CHANGE_ID
                                 AND T.STEP_NAME = 'verify'
                                 AND T.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                 AND T.OPERATOR_TIME < VERIFY_RTN.OPERATOR_TIME))
                         END)) VERIFY_MAX_TIME,
                         ((CASE
                           WHEN APP_RTN.OPERATOR_TIME IS NULL THEN
                            (SELECT MAX(OPERATOR_TIME)
                               FROM E_BDS_CSBPM_PROCESSSTEP
                              WHERE CHANGE_ID = T1.CHANGE_ID
                                AND STEP_NAME = 'approval'
                                AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID)
                           ELSE
                            ((SELECT MAX(OPERATOR_TIME)
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE T.CHANGE_ID = T1.CHANGE_ID
                                 AND T.STEP_NAME = 'approval'
                                 AND T.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                 AND T.OPERATOR_TIME < APP_RTN.OPERATOR_TIME))
                         END)) APP_MAX_TIME,
                         (SELECT MAX(OPERATOR_TIME)
                            FROM E_BDS_CSBPM_PROCESSSTEP
                           WHERE CHANGE_ID = T1.CHANGE_ID
                             AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                             AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                             AND STEP_NAME IN ('end', 'cancel')) FIN,
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
                           WHERE PARAM_TYPE = 'POST'
                             AND T1.STATUS = PARAM_CODE) STATUS,
                         T1.POLICY_NOS,
                         DECODE(T1.CHANNEL, 'GB', 'GP', T1.CHANNEL) CHANNEL,
                         CHECKER.CHECKER_SUBMIT_TIME
                    FROM E_BDS_CSBPM_POLICY_SERVICE T1
                    LEFT JOIN (SELECT T.CHANGE_ID,
                                     T.SUB_CHANGE_ID,
                                     MIN(OPERATOR_TIME) AS OPERATOR_TIME
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE (T.ACTION_NAME = '���˵ȴ����˻�' OR
                                     T.ACTION_NAME = '�Զ����񷵻�')
                                 AND T.STEP_NAME = 'verify'
                               GROUP BY T.CHANGE_ID, T.SUB_CHANGE_ID) VERIFY_RTN
                      ON VERIFY_RTN.CHANGE_ID = T1.CHANGE_ID
                     AND VERIFY_RTN.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                    LEFT JOIN (SELECT T.CHANGE_ID,
                                     T.SUB_CHANGE_ID,
                                     MIN(OPERATOR_TIME) AS OPERATOR_TIME
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE (T.ACTION_NAME = '���˵ȴ����˻�' OR
                                     T.ACTION_NAME = '�Զ����񷵻�')
                                 AND T.STEP_NAME = 'approval'
                               GROUP BY T.CHANGE_ID, T.SUB_CHANGE_ID) APP_RTN
                      ON APP_RTN.CHANGE_ID = T1.CHANGE_ID
                     AND APP_RTN.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                  --20180705���Ӻ˱�ʱ��
                    LEFT JOIN (SELECT T.CHANGE_ID,
                                     T.SUB_CHANGE_ID,
                                     MAX(T.OPERATOR_TIME) AS CHECKER_SUBMIT_TIME
                                FROM E_BDS_CSBPM_PROCESSSTEP T
                               WHERE T.STEP_NAME = 'checker'
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
                     --AND T1.STATUS IN ('end', 'cancel')
                        --�޶���ȫ��Ŀ
                     AND T1.SERVICE_ID IN
                         (SELECT T.PARAM_CODE
                            FROM E_BDS_CSBPM_PARAM T
                           WHERE T.PARAM_VALUE IN ('AUTO', 'OUTER'))) T
            LEFT JOIN (SELECT MAX(P.OPERATOR_NAME) AS OPERATOR_NAME,
                             P.CHANGE_ID,
                             MAX(P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME = 'accept'
                         --AND P.ACTION_VERSION = '1'
                       GROUP BY P.STEP_NAME, P.CHANGE_ID) P_ACC
              ON P_ACC.CHANGE_ID = T.CHANGE_ID
             AND P_ACC.OPERATOR_TIME = T.ACC_MAX_TIME
            LEFT JOIN (SELECT (P.OPERATOR_NAME) AS OPERATOR_NAME,
                             P.CHANGE_ID,
                             P.SUB_CHANGE_ID,
                             (P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME = 'verify') P_VER
              ON P_VER.CHANGE_ID = T.CHANGE_ID
             AND P_VER.SUB_CHANGE_ID = T.SUB_CHANGE_ID
             AND P_VER.OPERATOR_TIME = T.VERIFY_MAX_TIME
            LEFT JOIN (SELECT (P.OPERATOR_NAME) AS OPERATOR_NAME,
                             P.CHANGE_ID,
                             P.SUB_CHANGE_ID,
                             (P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME = 'approval') P_APP
              ON P_APP.CHANGE_ID = T.CHANGE_ID
             AND P_APP.SUB_CHANGE_ID = T.SUB_CHANGE_ID
             AND P_APP.OPERATOR_TIME = T.APP_MAX_TIME
          --20180913 ��ʱ���ӣ�������ͨ��ʱ��ϵͳ��¼���������
            LEFT JOIN (SELECT P.CHANGE_ID,
                             P.SUB_CHANGE_ID,
                             MIN(P.OPERATOR_TIME) AS OPERATOR_TIME
                        FROM E_BDS_CSBPM_PROCESSSTEP P
                       WHERE P.STEP_NAME IN
                             ('payWait', 'paymentWait', 'H355H208Wait')
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
          --����֧������ʱ��
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
      ON (COMPANY = B.LAORIGORGCODE )
   WHERE (SUBSTR(TRUNC(M.START_DATE),1,6) BETWEEN '20191001' AND  '20191231')

) t2
ON t1.APP_CUSTOMERNO = '9' || TRIM(t2.CLTNUM)
LEFT JOIN E_BDS_BIDM_MGECMP t3
ON substr(ADD_ORGCOD,1,1)=t3.LAORIGORGCODE
WHERE T2.PROVINCECOMCODE IS NULL
AND T2.STATUS NOT IN ( '����','����')
AND T2.SERVICE_NAME<>'������Զ�����'
AND (TO_DATE(to_char(MINDATE,'YYYYMMDD'),'YYYYMMDD') BETWEEN TO_DATE('20191001','YYYYMMDD')
 AND TO_DATE('20191231','YYYYMMDD')) 
 group by PROVINCECOMCODE
