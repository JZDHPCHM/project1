--2013-12-05 宋 修改 添加 'C1114'
--2013-05-09 宋 修改 添加 'C1113'
--加入四级机构
--D_PSNNATURE s_m_sms_dw_D_PSNNATURE  2010-9-15 添加ADD_BKCOD 
--2011-11-04 宋 修改 添加 'C1112'
--srg 20120822 MAK_AGENT_ATTR 修改为  bidw.la_f_agent
-- by lw add 20140916 增加渠道为RP的WHEN SUBSTR(A.DID, 6, 1) = '5' THEN 'RP'
SELECT BN.PID,
             CASE
               WHEN SUBSTR(A.DID, 6, 1) = '2' THEN
                'BK'
               WHEN SUBSTR(A.DID, 6, 1) = '1' THEN
                'FC'
	            WHEN SUBSTR(A.DID, 6, 1) = '5' THEN
                'RP'
               ELSE
                'OT'
             END CHNLCODE,
             A.PSNNAME PNAME,
             BN.SEXNO,
             BN.STUDYLEVELNO,
             ' ' IDTYPE, --证件号码
             BN.PCARDID,
             BN.HOMEADDR,
             ' ' HOMEZIPCODE, --家庭邮编
             BN.PHONE,
             TO_CHAR(BN.BIRTHDAY, 'YYYYMMDD') BIRTHDAY,
             BN.MARRAYSTATE,
             BN.ACCT,
             BN.AGAINFLAG,
             BN.NATIONNO,
             BN.RANKID,
             BN.PEID,
             TO_CHAR(BN.WORKDATE, 'YYYYMMDD') WORKDATE,
             TO_CHAR(BN.ASSSTARTDATE, 'YYYYMMDD') EFFECTDATE,
             CASE
               WHEN (RK.ISNORMAL IS NULL OR  BN.WORKDATE >RK.ISNORMAL) THEN
                '99990101'
               ELSE
                TO_CHAR(RK.ISNORMAL, 'YYYYMMDD')
             END BENORMAL,
             TO_CHAR(A.ENDDATE, 'YYYYMMDD') DIMISSDATE,
             BN.DUTYALLOWANCEFLAG,
             BN.SERIES,

             BN.PSNNAMEEX,
             BN.STRONGSUIT,
             BN.FAVORITE,
             BN.CONTACTMODE,
             BN.PSNSOURCE,
             A.PSNTYPENO,
             A.STATUS,
             'SMS' SRCSYSTEM,
             TO_CHAR(SYSTIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF') MODIFYDATE,
             TO_CHAR(SYSTIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF') LOADDATE,
             SUBSTR(A.DID,1,3) ADD_ORGCOD,
             A.DID ,
             attr.ADD_BKCOD as ADD_BKCOD
       

        FROM SSA.T_PERSON A, SSA.T_NATUREGX BN
        LEFT JOIN (SELECT MAX(STARTDATE) ISNORMAL, PID,chgfrtrankid
                     FROM SSA.T_CHGRANK RL, SSA.T_RANKDEF RD
                    WHERE RL.CHGBAKRANKID = RD.RANKID
                      AND RD.RANKLEVEL > '101'
                    GROUP BY PID,chgfrtrankid) RK ON RK.PID = BN.PID
             --        and RK.chgfrtrankid IN ('C1110','C1111','C1112')
           --        and RK.chgfrtrankid IN ('C1110','C1111','C1112','C1113')
                       and RK.chgfrtrankid IN ('C1110','C1111','C1112','C1113','C1114')
    left join bidw.la_f_agent attr on attr.AGNTNUM=bn.PID   ---srg 20120822 MAK_AGENT_ATTR
       WHERE A.PID = BN.PID
         AND A.STARTDATE = BN.STARTDATE
         --20904
         UNION ALL

      
      SELECT TP.PID PID, --销售人员ID
             'BK' CHNLCODE, --销售渠道代码
             TRIM(CN.SURNAME) || TRIM(CN.GIVNAME) PNAME, --姓名
             CN.CLTSEX SEXNO, --性别
             TN.STUDYLEVELNO STUDYLEVELNO, --学历
             '1' IDTYPE, --证件 类型为身份证
             CN.SECUITYNO PCARDID, --身份证号
             TRIM(CN.CLTADDR01) || TRIM(CN.CLTADDR02) || TRIM(CN.CLTADDR03) ||
             TRIM(CN.CLTADDR04) || TRIM(CN.CLTADDR05) HOMEADDR, --家庭住址
             CN.CLTPCODE HOMEZIPCODE, --邮编
             TRIM(CN.CLTPHONE01) || ',' || TRIM(CN.CLTPHONE02) PHONE, --电话号码
             TO_CHAR(CN.CLTDOB) BIRTHDAY, --出生日期
             CN.MARRYD MARRYSTATE, --婚姻状态
             TN.ACCT ACCT, --个人账户
             1 AGAINFLAG, --TN.AGAINFLAG, --二次入司标记
             TN.NATIONNO NATIONNO, --民族
             TN.RANKID RANKID, --当前职级
             TN.PEID PEID, --PE代码
             TO_CHAR(TN.WORKDATE, 'YYYYMMDD') WORKDATE, --入司日期
             TO_CHAR(TN.ASSSTARTDATE, 'YYYYMMDD') EFFECTDATE, --考核起期
             TO_CHAR(TN.ASSSTARTDATE, 'YYYYMMDD') BENORMAL, --转正日期 银保目前没有抽取转正日期
             TO_CHAR(TP.ENDDATE, 'YYYYMMDD') DIMISSDATE, --离司日期
             0 DUTYALLOWANCEFLAG, --津贴浮动标记
             AG.AGTYPE SERIES, --人员系列
             TRIM(CN.LSURNAME) || TRIM(CN.LGIVNAME) PSNNAMEEX, --英文名
             TN.FAVORITE STRONGSUIT, --特长
             TN.STRONGSUIT FAVORITE, --爱好
             TN.CONTACTMODE CONTACTMODE, --联系方式
             TN.PSNSOURCE PSNSOURCE, --人员来源
             TP.PSNTYPENO PSNTYPENO, --人员类型
             TP.STATUS STATUS, --人员状态
             'SMS',
             TO_CHAR(SYSTIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF'),
             TO_CHAR(SYSTIMESTAMP, 'YYYY-MM-DD HH24:MI:SS.FF'),
             SUBSTR(TP.DID,1,3),
             TP.DID,
             al.ARACDE ADD_BKCOD

        FROM LADB.AGNTPF  AG,
             LADB.AGLFPF  AL,
             LADB.CLNTPF  CN,
             SSA.T_PERSON TP,
             SSA.T_NATURE TN
       WHERE AG.AGNTCOY = AL.AGNTCOY
         AND AG.AGNTNUM = AL.AGNTNUM
         AND AG.CLNTCOY = CN.CLNTCOY
         AND AG.CLNTNUM = CN.CLNTNUM
         AND AG.AGNTNUM = TP.PID
         AND TP.PID = TN.PID
         AND SUBSTR(TP.DID, 6, 1) = '2'