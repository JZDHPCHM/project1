--20171012增加申请来源 
SELECT T.*, ("支付成功送盘时间" - "结案日期") AS "结案支付时效"   FROM (
--以保单为单位
SELECT DISTINCT M.*,
       CASE
         WHEN NOTE.NOTE_DAYS IS NULL THEN
          ''
         ELSE
          to_char(NOTE.NOTE_DAYS)
       END AS "照会时效",
       T_RES.SEARCH_DAYS "调查时效",
       T_COUNSEL.COUNSEL_DAYS AS "协谈时效",
       CA.CONCLUSION AS "赔案核赔结论",
      CASE WHEN "是否拒赔" ='拒赔' THEN "结案日期"
         WHEN CA.CONCLUSION='养育金给付' THEN "结案日期"
           WHEN CA.CONCLUSION='保费豁免' THEN "结案日期"
             WHEN NOT_PAY.REPORT_ID IS NOT NULL THEN "结案日期"
         ELSE PAYEE.PAY_DATE END AS FINAL_DATE,
             CASE WHEN "是否拒赔" ='拒赔' THEN "结案日期"
         WHEN CA.CONCLUSION='养育金给付' THEN "结案日期"
           WHEN CA.CONCLUSION='保费豁免' THEN "结案日期"
             WHEN NOT_PAY.REPORT_ID IS NOT NULL THEN "结案日期"
         ELSE PAYEE.PAY_TIME END AS "支付成功送盘时间",
         (CASE WHEN SAC.REPORT_ID IS NULL THEN '否' ELSE '是' END ) AS "是否小额自动理赔"/*,
          (SELECT P.PAR_VALUE
          FROM TC_PARAM P
         WHERE P.PAR_TYPE = 'application_source'
           AND P.PAR_KEY = claims.APPLICATION_SOURCE) AS "申请来源"*/

  FROM (SELECT DISTINCT claims.org_code as org,
                        (SELECT ORG.ORG_VALUE
                           FROM TC_ORG ORG
                          WHERE ORG.ORG_CODE =
                                (SUBSTR(CLAIMS.ORG_CODE, 1, 1))) AS ORG_CODE,
                        CLAIMS.REPORT_ID,
                        CLAIMS.CLAIMS_ID,

                      /*  REG.POLICY_NO,*/
                        CLAIM_TYPE.CLAIM_TYPE_NAME AS "理赔类型",
                       CLAIMS.ALL_PAY AS "赔案赔付金额",
                      ( CASE
                       CLAIMS.HAS_SURVEY
                       WHEN '0' THEN '无'
                       WHEN '1' THEN '有'
                       END ) AS "是否有调查",
                        (CASE
                          WHEN INSTR(CLAIM_TYPE.CLAIM_TYPE_NAME, '住院', 1, 1) > 0 THEN
                           '健康险'
                          WHEN INSTR(CLAIM_TYPE.CLAIM_TYPE_NAME, '医疗', 1, 1) > 0 THEN
                           '健康险'
                          WHEN INSTR(CLAIM_TYPE.CLAIM_TYPE_NAME, '津贴', 1, 1) > 0 THEN
                           '健康险'
                          ELSE
                           '非健康险'
                        END) AS CLAIMTYPE,
                        CLAIMS.EFF_DATE_BEGIN_BAOXIE AS "保协起始日",
                        CLAIMS.ACROSS_TIME as "出险时间",
                        ZY.ZY_DATE  AS "住院日期",
                         MZ.MZ_DATE AS "门诊日期",
                         CLAIMS.DEATH_DATE AS "死亡日期",
                         CLAIMS.CON_DIA_DATE AS "重疾确诊日期",
(CASE
         WHEN ZY.ZY_DATE IS NOT NULL THEN
          ZY.ZY_DATE
         WHEN MZ.MZ_DATE IS NOT NULL THEN
          MZ.MZ_DATE
         WHEN CLAIMS.DEATH_DATE IS NOT NULL THEN
          CLAIMS.DEATH_DATE
         WHEN CLAIMS.CON_DIA_DATE IS NOT NULL THEN
          CLAIMS.CON_DIA_DATE
         ELSE
          CLAIMS.ACROSS_TIME
       END) AS "系统调整后出险时间" ,
       
                        --  MENZHEN.MENZHEN_END_TIME AS "门诊时间",
                        --  HOSPITAL.HOSPITAL_END_TIME AS "出院时间",
                        --  CLAIMS.CON_DIA_DATE AS "确诊时间",
                        --   CLAIMS.DEATH_DATE AS "死亡日期",
                        CLAIMS.EFF_DATE_BEGIN_BAOXIE - CLAIMS.ACROSS_TIME + 1 "出险时间差",
                        TRUNC(T.ACCPT_GET_TIME) AS "受理日期",
                           (T.ACCPT_GET_TIME) AS "受理时间",
                      
                        TRUNC(T.ACCPT_GET_TIME) -
                        CLAIMS.EFF_DATE_BEGIN_BAOXIE + 1 "受理时间差",
                        --修改结案时间 fix by cxl 20180801
                        TRUNC(T.END_TIME) AS "结案日期",
                        (T.END_TIME) AS "结案时间",
                        TRUNC(((T.END_TIME-t.acCPT_GET_TIME) * 1440 ),2)AS "结案-受理(分钟)",
                        TRUNC(T.END_TIME) -
                        CLAIMS.EFF_DATE_BEGIN_BAOXIE + 1 "结案时间差",
                        (trunc(T.END_TIME) - TRUNC(T.ACCPT_GET_TIME) + 1) as "处理时效",
                        /*REG.HAS_REJ*/
                        (CASE REG.HAS_REJ
                          WHEN '07' THEN
                           '拒赔'
                          WHEN '08' THEN
                           '拒赔'
                          WHEN '09' THEN
                           '拒赔'
                          WHEN '10' THEN
                           '拒赔'
                          ELSE
                           '给付'
                        END) AS "是否拒赔",
                        ACC_REG.ACC_REG_DAYS AS "受理-立案时效",
                        FGS_HEPEI.FGS_HEPEI_DAYS AS "分公司核赔时效",
                        FGS_SHENPI.FGS_SHENPI_DAYS AS "分公司审批时效",
                        APP_END.APP_END_DAYS AS "审批-结案时效",
                        --claims.application_source
                         (SELECT P.PAR_VALUE
          FROM TC_PARAM P
         WHERE P.PAR_TYPE = 'application_source'
           AND P.PAR_KEY = claims.APPLICATION_SOURCE) AS "申请来源"
           
          FROM TC_CLAIMS CLAIMS
          
                    
          LEFT JOIN (SELECT BT.REPORT_ID, MIN(BT.START_TIME) AS ZY_DATE
               FROM HASLCLAIMS.TC_BILL_TOTAL BT,
                    HASLCLAIMS.TC_INSURANCE  INSURANCE
              WHERE BT.TREAT_TYPE = '2'
                AND BT.REPORT_ID = INSURANCE.REPORT_ID
                AND INSURANCE.INSURANCE_CODE IN
                    (SELECT T.INSURANCE_CODE
                       FROM HASLCLAIMS.TC_LIABILITY T
                      WHERE T.PRODUCT_TYPE = '健康险'
                        AND T.DUTY_TYPE <> '重疾')
                AND INSURANCE.ASS_CLAIMS_FLAG = '1'
              GROUP BY BT.REPORT_ID) ZY
    ON ZY.REPORT_ID = CLAIMS.REPORT_ID
  LEFT JOIN (SELECT BT.REPORT_ID, MIN(BT.START_TIME) AS MZ_DATE
               FROM HASLCLAIMS.TC_BILL_TOTAL BT,
                    HASLCLAIMS.TC_INSURANCE  INSURANCE
              WHERE BT.TREAT_TYPE = '1'
                AND BT.REPORT_ID = INSURANCE.REPORT_ID
                AND INSURANCE.INSURANCE_CODE IN
                    (SELECT T.INSURANCE_CODE
                       FROM HASLCLAIMS.TC_LIABILITY T
                      WHERE T.PRODUCT_TYPE = '健康险'
                        AND T.DUTY_TYPE <> '重疾')
                AND INSURANCE.ASS_CLAIMS_FLAG = '1'
              GROUP BY BT.REPORT_ID) MZ
    ON MZ.REPORT_ID = CLAIMS.REPORT_ID
    
    
          LEFT JOIN (SELECT T1.REPORT_ID, T1.ACCPT_GET_TIME, TASK.END_TIME
                      FROM (SELECT T.REPORT_ID,
                                   MIN(T.START_TIME) AS ACCPT_GET_TIME
                              FROM TC_TASKTIME T
                             WHERE T.CODE = 'ClaimWf001_03'
                             GROUP BY T.REPORT_ID) T1
                      LEFT JOIN (SELECT T.REPORT_ID,
                                       MAX(T.END_TIME) AS END_TIME
                                  FROM TC_TASKTIME T
                                 WHERE T.CODE = 'ClaimWf001_08'
                                 GROUP BY T.REPORT_ID) TASK ON TASK.REPORT_ID =
                                                               T1.REPORT_ID) T ON T.REPORT_ID =
                                                                                  CLAIMS.REPORT_ID
          LEFT JOIN (select t.report_id, max(t.end_time) as hospital_end_time
                      from tc_bill_total t
                     where t.treat_type = '2' --住院为2，门诊为1
                     group by report_id) HOSPITAL ON HOSPITAL.REPORT_ID =
                                                     CLAIMS.REPORT_ID
          LEFT JOIN (SELECT REPORT_ID,
                           WM_CONCAT(CLAIM_TYPE_NAME) AS CLAIM_TYPE_NAME
                      FROM (SELECT cc.*,
                                   (SELECT PAR_VALUE
                                      FROM TC_PARAM PARAM
                                     WHERE PARAM.PAR_TYPE = 'claim_type'
                                       AND PARAM.PAR_KEY = CC.CLAIMS_KEY) AS CLAIM_TYPE_NAME
                              FROM TC_CLAIMS_AND_CLAIMSTYPE CC
                             WHERE CC.HAS_CHECKED = '1') T
                     GROUP BY REPORT_ID) CLAIM_TYPE ON CLAIM_TYPE.REPORT_ID =
                                                       CLAIMS.REPORT_ID
          LEFT JOIN (select t.report_id, max(t.end_time) as MENZHEN_end_time
                      from tc_bill_total t
                     where t.treat_type = '1' --住院为2，门诊为1
                     group by report_id) MENZHEN ON MENZHEN.REPORT_ID =
                                                    CLAIMS.REPORT_ID
        --受理-立案时效
          LEFT JOIN (
                       SELECT T.REPORT_ID,
                                    SUM(TRUNC(T.END_TIME) - trunc(T.START_TIME)) AS ACC_REG_DAYS
                               FROM TC_TASKTIME T
                              WHERE (T.CODE = 'ClaimWf001_03' OR
                                    T.CODE = 'ClaimWf001_04')
                              GROUP BY T.REPORT_ID) ACC_REG ON ACC_REG.REPORT_ID =
                                                                   CLAIMS.REPORT_ID
          --分公司核赔时效
          LEFT JOIN (
                       SELECT T.REPORT_ID,
                                    SUM(TRUNC(T.END_TIME) - trunc(T.START_TIME)) AS FGS_HEPEI_DAYS
                               FROM TC_TASKTIME T
                              WHERE (T.CODE = 'ClaimWf001_05'  AND
                               T.LOCKUSER NOT IN (SELECT PAR_KEY FROM TC_PARAM
                               P WHERE P.PAR_TYPE='HQ_USER' AND P.IS_VALID='1' )
                               )
                              GROUP BY T.REPORT_ID) FGS_HEPEI ON FGS_HEPEI.REPORT_ID =
                                                                   CLAIMS.REPORT_ID
           --分公司审批时效
          LEFT JOIN (
                       SELECT T.REPORT_ID,
                                    SUM(TRUNC(T.END_TIME) - trunc(T.START_TIME)) AS FGS_SHENPI_DAYS
                               FROM TC_TASKTIME T
                              WHERE (T.CODE = 'ClaimWf001_06'  AND
                               T.LOCKUSER NOT IN (SELECT PAR_KEY FROM TC_PARAM
                               P WHERE P.PAR_TYPE='HQ_USER' AND P.IS_VALID='1' )
                               )
                              GROUP BY T.REPORT_ID) FGS_SHENPI ON FGS_SHENPI.REPORT_ID =
                                                                   CLAIMS.REPORT_ID

        --结案时效  结案- 审批时间差
          LEFT JOIN (SELECT T1.REPORT_ID,
                           T1.START_DATE,
                           T2.END_DATE,
                           T2.END_DATE - T1.START_DATE AS APP_END_DAYS
                      FROM (SELECT T.REPORT_ID,
                                   TRUNC(MAX(T.END_TIME)) AS START_DATE
                              FROM TC_TASKTIME T
                             WHERE T.CODE = 'ClaimWf001_06'
                             GROUP BY T.REPORT_ID) T1,
                           (SELECT T.REPORT_ID,
                                   TRUNC(MAX(T.END_TIME)) AS END_DATE
                              FROM TC_TASKTIME T
                             WHERE T.CODE = 'ClaimWf001_08'
                             GROUP BY T.REPORT_ID) T2
                     WHERE T1.REPORT_ID = T2.REPORT_ID) APP_END ON APP_END.REPORT_ID =
                                                                   CLAIMS.REPORT_ID,
         (SELECT DISTINCT REPORT_ID,
                                                                                    POLICY_NO
                                                                      FROM TC_INSURANCE INSURANCE
                                                                     WHERE INSURANCE.ASS_CLAIMS_FLAG = '1')
         INSURANCE,
         (SELECT DISTINCT T.REPORT_ID,
                                                                                    T.POLICY_NO,
                                                                                    T.CONCLUSION AS HAS_REJ
                                                                      FROM TC_CLAIMSAUDIT T
                                                                     WHERE --T.CONCLUSION IN ('07', '08', '09', '10')
                                                                    -- AND
                                                                     T.HIS_TAG = '0') REG
         WHERE CLAIMS.REPORT_ID = INSURANCE.REPORT_ID
           AND CLAIMS.STATUS in ('8')
           --已结案件子状态 fix by cxl 20180801
           AND CLAIMS.SUB_STATUS in ('0')
           AND REG.REPORT_ID = CLAIMS.REPORT_ID) M
--照会时效
  LEFT JOIN (SELECT T.REPORT_ID, SUM(T.NOTE_DAYS) AS NOTE_DAYS
               FROM (SELECT SUM(TRUNC(T.NOTE_RETIME) - TRUNC(T.NOTE_STIME)) AS NOTE_DAYS,
                            T.REPORT_ID,
                            T.NOTE_STIME
                       FROM TC_DATA_NOTE T
                      GROUP BY REPORT_ID, T.NOTE_STIME) T
              GROUP BY T.REPORT_ID) NOTE ON NOTE.REPORT_ID = M.REPORT_ID
--调查时效
  LEFT JOIN (SELECT T.REPORT_ID, SUM(SEARCH_DAYS) AS SEARCH_DAYS
               FROM (SELECT T.REPORT_ID,
                            T.ASKER_TIME,
                            TRUNC(MAX(T.COMPLETE_TIME)) -
                            TRUNC(MIN(T.ASKER_TIME)) AS SEARCH_DAYS
                       FROM TC_RESEARCH T
                      WHERE T.TYPE = '2'
                      GROUP BY T.REPORT_ID, T.ASKER_TIME) T
              GROUP BY T.REPORT_ID) T_RES ON T_RES.REPORT_ID = M.REPORT_ID
--协谈时效
  LEFT JOIN (SELECT T.REPORT_ID, SUM(T.COUNSEL_DAYS) AS COUNSEL_DAYS
               FROM (SELECT T.REPORT_ID,
                            T.START_TIME,
                            TRUNC(MAX(T.FINISH_TIME)) -
                            TRUNC(MIN(T.START_TIME)) AS COUNSEL_DAYS
                       FROM TC_COUNSEL T
                      WHERE T.STATUS = '2'
                      GROUP BY T.REPORT_ID, T.START_TIME) T
              GROUP BY T.REPORT_ID) T_COUNSEL ON T_COUNSEL.REPORT_ID =
                                                 M.REPORT_ID
 --赔付结论
LEFT JOIN (SELECT CA.REPORT_ID,WM_CONCAT(RES) AS CONCLUSION FROM (
     select DISTINCT  ca.report_id,
        (SELECT T.PAR_VALUE
          FROM TC_PARAM T
         WHERE T.PAR_TYPE = 'pa_claims_conclusion'
           AND T.PAR_KEY = CA.CONCLUSION) AS RES
  from tc_claimsaudit ca
 where ca.his_tag = '0') CA
 GROUP BY  REPORT_ID) CA ON CA.REPORT_ID=M.REPORT_ID
 --支付号
 --修改财务发送时间 fix by cxl 20180808
 LEFT JOIN (SELECT REPORT_ID,
               trunc(MAX(pay_time)) AS PAY_DATE,
               trunc(MAX(pay_time)) AS pay_time 　FROM (SELECT DISTINCT TC_PAYMENT.REPORT_ID,
                                                                       SUBSTR(ENTITY,
                                                                              1,
                                                                              1) ||
                                                                       REQNO AS PAY_NO,
                                                                       NVL(FPAY.PAY_SEND_TIME,
                                                                           TO_DATE(RESPONSE.PAYSENDDATE,'YYYYMMDDHH24MISS')) AS pay_time
                                                         FROM TC_PAYMENT
                                                         LEFT JOIN TC_FINANCIAL_PAY FPAY
                                                           ON SUBSTR(ENTITY,
                                                                     1,
                                                                     1) ||
                                                              REQNO =
                                                              FPAY.PAY_NO
                                                         LEFT JOIN TC_TASK_TO_FINANCE TASK
                                                           ON TC_PAYMENT.SERNO =
                                                              TASK.PAYMENT_SERNO
                                                          AND TASK.STATUS = '2'
                                                         LEFT JOIN TC_FINANCE_RESPONSE RESPONSE
                                                           ON TASK.BIDUIID =
                                                              RESPONSE.BIDUIID
                                                          AND RESPONSE.RTNCODE = 'success'
                                                          AND RESPONSE.TRANSSTATE = '2'
                                                        WHERE REQNO IS NOT NULL) PAYEE GROUP BY REPORT_ID
        ) PAYEE
 ON PAYEE.REPORT_ID=M.REPORT_ID
 LEFT JOIN TC_LOAN_NOT_PAY_REPORTS NOT_PAY
 ON NOT_PAY.REPORT_ID=M.REPORT_ID
 --20170901增加是否小额自动理赔
 LEFT JOIN TC_SMALL_AUTO_CLAIMS SAC
 ON SAC.REPORT_ID=M.REPORT_ID
 AND SAC.IS_VALID='1'

 WHERE "结案日期" BETWEEN TO_DATE('2017-01-01', 'YYYY-MM-DD') AND
       TO_DATE('2020-12-31', 'YYYY-MM-DD')

 ORDER BY org, CLAIMTYPE, M.REPORT_ID) T
 WHERE t.final_date BETWEEN TO_DATE('2020-03-01','YYYY-MM-DD')
 AND  TO_DATE('2020-03-31','YYYY-MM-DD')