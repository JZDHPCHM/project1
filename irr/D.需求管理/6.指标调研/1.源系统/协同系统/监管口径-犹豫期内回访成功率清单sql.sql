--犹豫期内回访成功率-（监管口径）承保的保单清单
SELECT BASE.POLICYNO  ,BASE.OCCDATE
                 FROM CCREPORT_POLICYS_BASE BASE 
                INNER JOIN CCREPORT_POLICYS_INDICATOR INDI 
                   ON BASE.POLICYNO = INDI.POLICYNO 
                INNER JOIN CCREPORT_CRM_CASE3 CASE3 
                   ON BASE.POLICYNO = CASE3.POLICYNO 
                WHERE BASE.OCCDATE >= '20180801'  --时间范围
                  AND BASE.OCCDATE <= '20180831'  --时间范围
                  AND INDI.ISCFI = '0' 
                  AND INDI.ISWD = '0' 
                  AND BASE.ZACKDTE IS NOT NULL 
                  AND BASE.ZACKDTE != '99999999' 
                  AND BASE.NEEDCALLBACK = '1' 
                  AND BASE.CNTTYPE NOT LIKE 'PA%' 
                  AND CASE3.BACRS NOT IN ('1', '10') 
                  AND NOT (CASE3.DATIME LIKE '% 0:00AM' OR CASE3.DATIME LIKE '%000000') ;
--犹豫期内回访成功率-（监管口径）犹豫期内完成新契约回访的保单清单
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
                        WHERE BASE.OCCDATE >= '20180801' --时间范围
                          AND BASE.OCCDATE <= '20180831' --时间范围
                          AND INDI.ISCFI = '0' 
                          AND INDI.ISWD = '0' 
                          AND BASE.ZACKDTE IS NOT NULL 
                          AND BASE.ZACKDTE != '99999999' 
                          AND BASE.NEEDCALLBACK = '1' 
                          AND BASE.CNTTYPE NOT LIKE 'PA%' 
                          AND NOT (CASE3.DATIME LIKE '% 0:00AM' OR CASE3.DATIME LIKE '%000000') 
                          AND CASE3.BACRS NOT IN ('0', '1') 
                          AND CBLIST.CALLBACKRESULT IN 
                              ('电话回访完全干净件','电话回访问题件','电话回访-CS干净件','电话回访CS干净件', 
                               '电话回访完全干净件非投操作','电话回访CS干净件非投操作','电话回访问题件非投操作', 
                               '亲访信回访干净件','亲访信回访问题件非投操作','问题件-投保未双录') 
			                  GROUP BY BASE.COMPANYNO, 
			                           BASE.BRANCH, 
			                           BASE.CHANNEL, 
			                           BASE.ENDHESDATE, 
			                           BASE.CHDRNUM, 
			                           BASE.AGENTNO, 
			                           BASE.ARACDE1) T 
			          WHERE T.ENDHESDATE >= SUBSTR(T.CALLBACKDATE, 0, 4) || SUBSTR(T.CALLBACKDATE, 6, 2) || SUBSTR(T.CALLBACKDATE, 9, 2) ;