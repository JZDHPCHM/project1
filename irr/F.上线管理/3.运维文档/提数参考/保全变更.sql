SELECT * FROM (
 SELECT
--机构号
SUBSTR(T.DID, 0, 1) COMPANY,

--T3
                       T.DID,
--业务来源
                       T.BUS_SOURCE,
--受理方式
                       T.APPLY_WAY,
--受理方式
                       DECODE(T.APPLY_WAY, '亲自办理', '', ENTRUSTED_RELA) ENTRUSTED_RELA,
                       DECODE(T.APPLY_WAY, '亲自办理', '', ENTRUSTED_NAME) ENTRUSTED_NAME,
--是否电话核实
                       T.HAS_CHECK_TEL,
--开始日期
                       T.START_DATE AS START_time,
                       TO_CHAR(T.START_DATE, 'YYYY/MM/DD') STARTDATE,
--开始时间
                       TO_CHAR(T.START_DATE, 'HH24:MI:SS') STARTTIME,
--系统最早记录日期
                         TO_CHAR(T.min_recevice_time, 'YYYY/MM/DD') min_recevice_date,
--系统最早记录时间
                       TO_CHAR(T.min_recevice_time, 'HH24:MI:SS') min_recevice_time,
--受理日期
                       TO_CHAR(T.ACC, 'YYYY/MM/DD') ACCDATE,
--受理时间
                       TO_CHAR(T.ACC, 'HH24:MI:SS') ACCTIME,
--到达经办日期
                       TO_CHAR(T.HAN, 'YYYY/MM/DD') HANDATE,
--到达经办时间
                       TO_CHAR(T.HAN, 'HH24:MI:SS') HANTIME,
--结案日期
                       TO_CHAR(T.FIN, 'YYYY/MM/DD') FINDATE,
--结案时间
                       TO_CHAR(T.FIN, 'HH24:MI:SS') FINTIME,
--保全号
                       T.CHANGE_ID,
--子保全号
                       T.SUB_CHANGE_ID,
--保全项目代码
                        T.SERVICE_ID,
--保全项目名称
                       T.SERVICE_NAME,
--客户号
                         (CASE T.SERVICE_ID WHEN '105' THEN (SELECT M.CUSTOMER_ID FROM T_CS_105_MAIN M WHERE M.CHANGE_ID=T.CHANGE_ID AND M.SUB_CHANGE_ID=T.SUB_CHANGE_ID)
                                                      WHEN '107' THEN (SELECT M.CUSTOMER_ID FROM T_CS_107_MAIN M WHERE M.CHANGE_ID=T.CHANGE_ID AND M.SUB_CHANGE_ID=T.SUB_CHANGE_ID)
                                                        ELSE NULL END ) AS ""客户号"",
--客户其他信息
                         (CASE T.SERVICE_ID WHEN '105' THEN (SELECT M.NAME||'-'||m.nation||m.province||m.address FROM T_CS_105_MAIN M WHERE M.CHANGE_ID=T.CHANGE_ID AND M.SUB_CHANGE_ID=T.SUB_CHANGE_ID)
                                             WHEN '107' THEN (SELECT m.name FROM T_CS_107_MAIN M WHERE M.CHANGE_ID=T.CHANGE_ID AND M.SUB_CHANGE_ID=T.SUB_CHANGE_ID)
                                 ELSE NULL END ) AS ""客户其他信息"",
--状态
                       T.STATUS,
--保单号
                       T.POLICY_NOS,
--受理人员
                       P_ACC.OPERATOR_NAME AS ACC_PERSON,
--受理人员操作日期
                       TO_CHAR(P_ACC.OPERATOR_TIME, 'YYYY/MM/DD') AS ACC_OPERATOR_DATE,
--经办人
                       P_HAN.OPERATOR_NAME AS HAN_PERSON,
--经办人员操作日期
                       TO_CHAR(P_HAN.OPERATOR_TIME, 'YYYY/MM/DD') AS HAN_OPERATOR_DATE,
--复核人员
                       P_VER.OPERATOR_NAME AS VER_PERSON,
--复核人员操作日期
                       TO_CHAR(P_VER.OPERATOR_TIME, 'YYYY/MM/DD') AS VER_OPERATOR_DATE,
--审批人员
                       P_APP.OPERATOR_NAME AS APP_PERSON,
--审批人员操作日期
                       TO_CHAR(P_APP.OPERATOR_TIME, 'YYYY/MM/DD') AS APP_OPERATOR_DATE,
--扣款日期
                       TO_CHAR(DD.SEND_DD_TIME, 'YYYY-MM-DD') AS DD_SEND_DATE,
                        TO_CHAR(PAYLIST.MIN_INSERT_INTO_PAY_TIME, 'YYYY/MM/DD') AS ""最早进入支付队列日期"",
                        --系统最早记录时间
                       TO_CHAR(PAYLIST.MIN_INSERT_INTO_PAY_TIME, 'HH24:MI:SS') ""最早进入支付队列时间"",
                      -- PAY_SEQ.IN_PAY_SEQ_TIME,
                      TO_CHAR(PAY_SEQ.IN_PAY_SEQ_TIME, 'YYYY/MM/DD') in_pay_seq_date,
                      TO_CHAR(PAY_SEQ.IN_PAY_SEQ_TIME, 'HH24:MI:SS') in_pay_seq_time,
--审核通过日期
                       TRUNC((CASE
                      /* --20180702 撤单的审核通过日期=结案日期
                                WHEN status='cancel' THEN T.FIN*/
                                  -- 20180705补充告知审核通过日期调整为核保最后一次提交日期
                               WHEN SERVICE_ID='804' THEN CHECKER_SUBMIT_TIME
                               WHEN SERVICE_ID IN
                                    (SELECT T.PARAM_CODE
                                       FROM T_PARAM T
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
                                   FROM T_PROCESSSTEP VER_DD
                                  WHERE VER_DD.STEP_NAME = 'verify'
                                    AND VER_DD.CHANGE_ID = T.CHANGE_ID
                                    AND VER_DD.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                                    AND VER_DD.OPERATOR_TIME <= DD.SEND_DD_TIME)
                                 --20180913 临时增加
                                 WHEN (P_APP.OPERATOR_TIME IS NOT NULL) AND  (P_PAY.OPERATOR_TIME <=   P_APP.OPERATOR_TIME)
                                      THEN P_PAY.OPERATOR_TIME
                                  WHEN (P_VER.OPERATOR_TIME IS NOT NULL) AND  (P_PAY.OPERATOR_TIME <=   P_VER.OPERATOR_TIME)
                                      THEN P_PAY.OPERATOR_TIME
                               WHEN P_APP.OPERATOR_TIME IS NOT NULL THEN
                                P_APP.OPERATOR_TIME
                               WHEN P_VER.OPERATOR_TIME IS NOT NULL THEN
                                P_VER.OPERATOR_TIME
                             END)) AS AUDIT_PASS_DATE,
--金额
                       CASE T.SERVICE_ID
                         WHEN '103' THEN
                          (SELECT SUM(PAY_AMOUNT)
                             FROM T_PAY_INFO PAY
                            WHERE PAY.CHANGE_ID = T.CHANGE_ID
                              AND PAY.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                            GROUP BY PAY.CHANGE_ID, PAY.SUB_CHANGE_ID)
                         WHEN '901' THEN
                          (SELECT SUM(PAY_AMOUNT)
                             FROM T_PAY_INFO PAY
                            WHERE PAY.CHANGE_ID = T.CHANGE_ID
                              AND PAY.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                            GROUP BY PAY.CHANGE_ID, PAY.SUB_CHANGE_ID)
                         WHEN '306' THEN
                          (SELECT SUM(PAY_AMOUNT)
                             FROM T_PAY_INFO PAY
                            WHERE PAY.CHANGE_ID = T.CHANGE_ID
                              AND PAY.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                            GROUP BY PAY.CHANGE_ID, PAY.SUB_CHANGE_ID)
                         WHEN '307' THEN
                          (SELECT SUM(PAY_AMOUNT)
                             FROM T_PAY_INFO PAY
                            WHERE PAY.CHANGE_ID = T.CHANGE_ID
                              AND PAY.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                            GROUP BY PAY.CHANGE_ID, PAY.SUB_CHANGE_ID)
                         ELSE
                          (SELECT PAY_AMOUNT
                             FROM T_PAY_INFO PAY
                            WHERE PAY.CHANGE_ID = T.CHANGE_ID
                              AND PAY.SUB_CHANGE_ID = T.SUB_CHANGE_ID)
                       END AS AMOUNT,
--核保最后提交日期
                       CHECKER_SUBMIT_TIME AS ""核保最后提交日期""
                  FROM (SELECT T2.DID,
                               (SELECT P.PARAM_VALUE
                                  FROM T_PARAM P
                                 WHERE P.PARAM_TYPE = 'busSourceAll'
                                   AND P.PARAM_CODE = T2.BUS_SOURCE) BUS_SOURCE,
                               (SELECT P.PARAM_VALUE
                                  FROM T_PARAM P
                                 WHERE P.PARAM_TYPE = 'applyWay'
                                   AND P.PARAM_CODE = T2.APPLY_WAY) APPLY_WAY,
                               (SELECT P.PARAM_VALUE
                                  FROM T_PARAM P
                                 WHERE P.PARAM_TYPE = 'entrustedRela'
                                   AND P.PARAM_CODE = T2.ENTRUSTED_RELA) ENTRUSTED_RELA,
                               T2.ENTRUSTED_NAME,
                               DECODE(T2.HAS_CHECK_TEL, '1', '是', '否') HAS_CHECK_TEL,
                               (SELECT MIN(OPERATOR_TIME)
                                  FROM T_PROCESSSTEP
                                 WHERE CHANGE_ID = T1.CHANGE_ID) START_DATE,
                                  (SELECT MIN(recevice_time)
                                  FROM T_PROCESSSTEP
                                 WHERE CHANGE_ID = T1.CHANGE_ID) min_recevice_time,
                               (SELECT MIN(OPERATOR_TIME)
                                  FROM T_PROCESSSTEP
                                 WHERE CHANGE_ID = T1.CHANGE_ID
                                   AND STEP_NAME = 'accept') ACC,
                               (SELECT MAX(OPERATOR_TIME)
                                  FROM T_PROCESSSTEP
                                 WHERE CHANGE_ID = T1.CHANGE_ID
                                   AND STEP_NAME = 'accept') ACC_MAX_TIME,
                               (SELECT MAX(OPERATOR_TIME)
                                  FROM T_PROCESSSTEP
                                 WHERE CHANGE_ID = T1.CHANGE_ID
                                   AND STEP_NAME = 'handle'
                                   AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID) HAN_MAX_TIME,
                               ((CASE
                                 WHEN VERIFY_RTN.OPERATOR_TIME IS NULL THEN
                                  (SELECT MAX(OPERATOR_TIME)
                                     FROM T_PROCESSSTEP
                                    WHERE CHANGE_ID = T1.CHANGE_ID
                                      AND STEP_NAME = 'verify'
                                      AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID)
                                 ELSE
                                  ((SELECT MAX(OPERATOR_TIME)
                                      FROM T_PROCESSSTEP T
                                     WHERE T.CHANGE_ID = T1.CHANGE_ID
                                       AND T.STEP_NAME = 'verify'
                                       AND T.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                       AND T.OPERATOR_TIME <
                                           VERIFY_RTN.OPERATOR_TIME))
                               END)) VERIFY_MAX_TIME,
                               ((CASE
                                 WHEN APP_RTN.OPERATOR_TIME IS NULL THEN
                                  (SELECT MAX(OPERATOR_TIME)
                                     FROM T_PROCESSSTEP
                                    WHERE CHANGE_ID = T1.CHANGE_ID
                                      AND STEP_NAME = 'approval'
                                      AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID)
                                 ELSE
                                  ((SELECT MAX(OPERATOR_TIME)
                                      FROM T_PROCESSSTEP T
                                     WHERE T.CHANGE_ID = T1.CHANGE_ID
                                       AND T.STEP_NAME = 'approval'
                                       AND T.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                       AND T.OPERATOR_TIME <
                                           APP_RTN.OPERATOR_TIME))
                               END)) APP_MAX_TIME,
                               T1.START_TIME HAN,
                               (SELECT MAX(OPERATOR_TIME)
                                  FROM T_PROCESSSTEP
                                 WHERE CHANGE_ID = T1.CHANGE_ID
                                   AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                   AND SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                                   AND STEP_NAME IN ( 'end','cancel' )) FIN,
                               T1.CHANGE_ID,
                               T1.SUB_CHANGE_ID,
                               T1.SERVICE_NAME,
                               T1.SERVICE_ID,
                               (SELECT NVL(PARAM_VALUE, T1.STATUS)
                                  FROM T_PARAM
                                 WHERE PARAM_TYPE = 'POST'
                                   AND T1.STATUS = PARAM_CODE) STATUS,
                               T1.POLICY_NOS,
                               CHECKER.CHECKER_SUBMIT_TIME
                          FROM T_POLICY_SERVICE T1
                          LEFT JOIN (SELECT T.CHANGE_ID,
                                           T.SUB_CHANGE_ID,
                                           MIN(OPERATOR_TIME) AS OPERATOR_TIME
                                      FROM T_PROCESSSTEP T
                                     WHERE (T.ACTION_NAME = '复核等待岗退回' OR
                                           T.ACTION_NAME = '自动任务返回')
                                       AND T.STEP_NAME = 'verify'
                                     GROUP BY T.CHANGE_ID, T.SUB_CHANGE_ID) VERIFY_RTN
                            ON VERIFY_RTN.CHANGE_ID = T1.CHANGE_ID
                           AND VERIFY_RTN.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                          LEFT JOIN (SELECT T.CHANGE_ID,
                                           T.SUB_CHANGE_ID,
                                           MIN(OPERATOR_TIME) AS OPERATOR_TIME
                                      FROM T_PROCESSSTEP T
                                     WHERE (T.ACTION_NAME = '复核等待岗退回' OR
                                           T.ACTION_NAME = '自动任务返回')
                                       AND T.STEP_NAME = 'approval'
                                     GROUP BY T.CHANGE_ID, T.SUB_CHANGE_ID) APP_RTN
                            ON APP_RTN.CHANGE_ID = T1.CHANGE_ID
                           AND APP_RTN.SUB_CHANGE_ID = T1.SUB_CHANGE_ID
                           --20180705增加核保时间
                  LEFT JOIN (
                  SELECT t.change_id,t.sub_change_id,MAX(t.operator_time) AS checker_submit_time
                          FROM t_processstep t WHERE t.step_name='checker'
                               GROUP BY t.change_id,t.sub_change_id) CHECKER ON CHECKER.CHANGE_ID=T1.CHANGE_ID
                               AND CHECKER.SUB_CHANGE_ID=T1.SUB_CHANGE_ID
                               ,
                         T_POLICY_CHANGE T2, (SELECT CHANGE_ID,
                                       MIN(OPERATOR_TIME) OPERATOR_TIME
                                  FROM T_PROCESSSTEP
                                 WHERE OPERATOR_TIME IS NOT NULL
                                 GROUP BY CHANGE_ID) T3
                         WHERE T1.CHANGE_ID = T2.CHANGE_ID
                           AND T1.CHANGE_ID = T3.CHANGE_ID

                               --AND t1.status IN ( 'end','cancel' )
                               --限定保全项目
                               AND T1.SERVICE_ID
                               IN (select T.PARAM_CODE from T_PARAM t WHERE T.PARAM_VALUE IN ('AUTO','OUTER')  )
                           ) T

                  LEFT JOIN (SELECT MAX(P.OPERATOR_NAME) AS OPERATOR_NAME,
                                   P.CHANGE_ID,
                                   MAX(P.OPERATOR_TIME) AS OPERATOR_TIME
                              FROM T_PROCESSSTEP P
                             WHERE P.STEP_NAME = 'accept'
                             --20190703 重新支付会改变原来的accept handle action version 因为取max所以注销action version
                             --  AND P.ACTION_VERSION = '1'
                             GROUP BY P.STEP_NAME, P.CHANGE_ID) P_ACC
                    ON P_ACC.CHANGE_ID = T.CHANGE_ID
                   AND P_ACC.OPERATOR_TIME = T.ACC_MAX_TIME
                  LEFT JOIN (SELECT MAX(P.OPERATOR_NAME) AS OPERATOR_NAME,
                                   P.CHANGE_ID,
                                   P.SUB_CHANGE_ID,
                                   MAX(P.OPERATOR_TIME) AS OPERATOR_TIME
                              FROM T_PROCESSSTEP P
                             WHERE P.STEP_NAME = 'handle'
                              --20190703 重新支付会改变原来的accept handle action version 因为取max所以注销action version
                             --  AND P.ACTION_VERSION = '1'
                             GROUP BY P.STEP_NAME,
                                      P.CHANGE_ID,
                                      P.SUB_CHANGE_ID) P_HAN
                    ON P_HAN.CHANGE_ID = T.CHANGE_ID
                   AND P_HAN.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                   AND P_HAN.OPERATOR_TIME = T.HAN_MAX_TIME
                  LEFT JOIN (SELECT (P.OPERATOR_NAME) AS OPERATOR_NAME,
                                   P.CHANGE_ID,
                                   P.SUB_CHANGE_ID,
                                   (P.OPERATOR_TIME) AS OPERATOR_TIME
                              FROM T_PROCESSSTEP P
                             WHERE P.STEP_NAME = 'verify') P_VER
                    ON P_VER.CHANGE_ID = T.CHANGE_ID
                   AND P_VER.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                   AND P_VER.OPERATOR_TIME = T.VERIFY_MAX_TIME
                  LEFT JOIN (SELECT (P.OPERATOR_NAME) AS OPERATOR_NAME,
                                   P.CHANGE_ID,
                                   P.SUB_CHANGE_ID,
                                   (P.OPERATOR_TIME) AS OPERATOR_TIME
                              FROM T_PROCESSSTEP P
                             WHERE P.STEP_NAME = 'approval') P_APP
                    ON P_APP.CHANGE_ID = T.CHANGE_ID
                   AND P_APP.SUB_CHANGE_ID = T.SUB_CHANGE_ID
                   AND P_APP.OPERATOR_TIME = T.APP_MAX_TIME
                   --20180913 临时增加，解决审核通过时间系统记录有误的问题
                   LEFT JOIN(
                   SELECT
                                   P.CHANGE_ID,
                                   P.SUB_CHANGE_ID,
                                  MIN (P.OPERATOR_TIME) AS OPERATOR_TIME
                              FROM T_PROCESSSTEP P
                             WHERE P.STEP_NAME in('payWait','paymentWait','H355H208Wait')
                               GROUP BY P.CHANGE_ID,
                                   P.SUB_CHANGE_ID) P_PAY ON P_PAY.CHANGE_ID=T.CHANGE_ID
                                                    AND P_PAY.SUB_CHANGE_ID=T.SUB_CHANGE_ID

                  LEFT JOIN (SELECT DD.CHANGE_ID,
                                   DD.SUB_CHANGE_ID,
                                   MIN(DD.CREATE_TIME) AS SEND_DD_TIME
                              FROM T_SERVICE_DD DD
                             GROUP BY DD.CHANGE_ID, DD.SUB_CHANGE_ID) DD
                    ON DD.CHANGE_ID = T.CHANGE_ID
                   AND DD.SUB_CHANGE_ID = T.SUB_CHANGE_ID

                   --进入支付队列时间
                   LEFT JOIN (SELECT PAY.CHANGE_ID,PAY.SUB_CHANGE_ID,MIN(PAY.CREATE_TIME) AS MIN_INSERT_INTO_PAY_TIME
 FROM T_SERVICE_PAY PAY GROUP BY PAY.CHANGE_ID,PAY.SUB_CHANGE_ID) PAYLIST
 ON PAYLIST.CHANGE_ID=T.CHANGE_ID AND PAYLIST.SUB_CHANGE_ID=T.SUB_CHANGE_ID
 LEFT JOIN (SELECT PAY.CHANGE_ID,PAY.SUB_CHANGE_ID,MIN(PAY.CREATE_TIME) AS IN_PAY_SEQ_TIME
 FROM T_SERVICE_PAY PAY GROUP BY PAY.CHANGE_ID,PAY.SUB_CHANGE_ID) PAY_SEQ
 ON PAY_SEQ.CHANGE_ID=t.CHANGE_ID
 AND PAY_SEQ.SUB_CHANGE_ID=t.SUB_CHANGE_ID

 )
 M


  WHERE TRUNC(M.START_time) BETWEEN to_date('20190901','yyyymmdd') AND to_date('20191231','yyyymmdd')

        -- AND M.CHANGE_ID='19134475'
                 ORDER BY ACCDATE ASC
