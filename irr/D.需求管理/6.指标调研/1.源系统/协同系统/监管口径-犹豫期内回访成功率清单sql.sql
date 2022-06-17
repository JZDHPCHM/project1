--��ԥ���ڻطóɹ���-����ܿھ����б��ı����嵥
SELECT BASE.POLICYNO  ,BASE.OCCDATE
                 FROM CCREPORT_POLICYS_BASE BASE 
                INNER JOIN CCREPORT_POLICYS_INDICATOR INDI 
                   ON BASE.POLICYNO = INDI.POLICYNO 
                INNER JOIN CCREPORT_CRM_CASE3 CASE3 
                   ON BASE.POLICYNO = CASE3.POLICYNO 
                WHERE BASE.OCCDATE >= '20180801'  --ʱ�䷶Χ
                  AND BASE.OCCDATE <= '20180831'  --ʱ�䷶Χ
                  AND INDI.ISCFI = '0' 
                  AND INDI.ISWD = '0' 
                  AND BASE.ZACKDTE IS NOT NULL 
                  AND BASE.ZACKDTE != '99999999' 
                  AND BASE.NEEDCALLBACK = '1' 
                  AND BASE.CNTTYPE NOT LIKE 'PA%' 
                  AND CASE3.BACRS NOT IN ('1', '10') 
                  AND NOT (CASE3.DATIME LIKE '% 0:00AM' OR CASE3.DATIME LIKE '%000000') ;
--��ԥ���ڻطóɹ���-����ܿھ�����ԥ�����������Լ�طõı����嵥
SELECT T.COMPANYNO,T.BRANCH,T.CHDRNUM
                 FROM (SELECT BASE.AGENTNO, 
                              BASE.COMPANYNO, 
                              BASE.BRANCH, 
                              BASE.CHANNEL, 
                              BASE.ENDHESDATE, 
                              BASE.CHDRNUM, 
                              MIN(CBLIST.CALLBACKDATE) CALLBACKDATE, 
                              BASE.ARACDE1 
                         FROM CCREPORT_POLICYS_BASE BASE 
                        INNER JOIN CCREPORT_POLICYS_INDICATOR INDI 
                           ON BASE.POLICYNO = INDI.POLICYNO 
                        INNER JOIN CCREPORT_CRM_CASE3 CASE3 
                           ON BASE.POLICYNO = CASE3.POLICYNO 
                        INNER JOIN CCREPORT_CRM_CALLBACK_LIST CBLIST 
                           ON CBLIST.POLICYNO = BASE.POLICYNO 
                        WHERE BASE.OCCDATE >= '20180801' --ʱ�䷶Χ
                          AND BASE.OCCDATE <= '20180831' --ʱ�䷶Χ
                          AND INDI.ISCFI = '0' 
                          AND INDI.ISWD = '0' 
                          AND BASE.ZACKDTE IS NOT NULL 
                          AND BASE.ZACKDTE != '99999999' 
                          AND BASE.NEEDCALLBACK = '1' 
                          AND BASE.CNTTYPE NOT LIKE 'PA%' 
                          AND NOT (CASE3.DATIME LIKE '% 0:00AM' OR CASE3.DATIME LIKE '%000000') 
                          AND CASE3.BACRS NOT IN ('0', '1') 
                          AND CBLIST.CALLBACKRESULT IN 
                              ('�绰�ط���ȫ�ɾ���','�绰�ط������','�绰�ط�-CS�ɾ���','�绰�ط�CS�ɾ���', 
                               '�绰�ط���ȫ�ɾ�����Ͷ����','�绰�ط�CS�ɾ�����Ͷ����','�绰�ط��������Ͷ����', 
                               '�׷��Żطøɾ���','�׷��Żط��������Ͷ����','�����-Ͷ��δ˫¼') 
			                  GROUP BY BASE.COMPANYNO, 
			                           BASE.BRANCH, 
			                           BASE.CHANNEL, 
			                           BASE.ENDHESDATE, 
			                           BASE.CHDRNUM, 
			                           BASE.AGENTNO, 
			                           BASE.ARACDE1) T 
			          WHERE T.ENDHESDATE >= SUBSTR(T.CALLBACKDATE, 0, 4) || SUBSTR(T.CALLBACKDATE, 6, 2) || SUBSTR(T.CALLBACKDATE, 9, 2) ;