--2013-12-05 �� �޸� ��� 'C1114'
--2013-05-09 �� �޸� ��� 'C1113'
--�����ļ�����
--D_PSNNATURE s_m_sms_dw_D_PSNNATURE  2010-9-15 ���ADD_BKCOD 
--2011-11-04 �� �޸� ��� 'C1112'
--srg 20120822 MAK_AGENT_ATTR �޸�Ϊ  bidw.la_f_agent
-- by lw add 20140916 ��������ΪRP��WHEN SUBSTR(A.DID, 6, 1) = '5' THEN 'RP'
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
             ' ' IDTYPE, --֤������
             BN.PCARDID,
             BN.HOMEADDR,
             ' ' HOMEZIPCODE, --��ͥ�ʱ�
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

      
      SELECT TP.PID PID, --������ԱID
             'BK' CHNLCODE, --������������
             TRIM(CN.SURNAME) || TRIM(CN.GIVNAME) PNAME, --����
             CN.CLTSEX SEXNO, --�Ա�
             TN.STUDYLEVELNO STUDYLEVELNO, --ѧ��
             '1' IDTYPE, --֤�� ����Ϊ���֤
             CN.SECUITYNO PCARDID, --���֤��
             TRIM(CN.CLTADDR01) || TRIM(CN.CLTADDR02) || TRIM(CN.CLTADDR03) ||
             TRIM(CN.CLTADDR04) || TRIM(CN.CLTADDR05) HOMEADDR, --��ͥסַ
             CN.CLTPCODE HOMEZIPCODE, --�ʱ�
             TRIM(CN.CLTPHONE01) || ',' || TRIM(CN.CLTPHONE02) PHONE, --�绰����
             TO_CHAR(CN.CLTDOB) BIRTHDAY, --��������
             CN.MARRYD MARRYSTATE, --����״̬
             TN.ACCT ACCT, --�����˻�
             1 AGAINFLAG, --TN.AGAINFLAG, --������˾���
             TN.NATIONNO NATIONNO, --����
             TN.RANKID RANKID, --��ǰְ��
             TN.PEID PEID, --PE����
             TO_CHAR(TN.WORKDATE, 'YYYYMMDD') WORKDATE, --��˾����
             TO_CHAR(TN.ASSSTARTDATE, 'YYYYMMDD') EFFECTDATE, --��������
             TO_CHAR(TN.ASSSTARTDATE, 'YYYYMMDD') BENORMAL, --ת������ ����Ŀǰû�г�ȡת������
             TO_CHAR(TP.ENDDATE, 'YYYYMMDD') DIMISSDATE, --��˾����
             0 DUTYALLOWANCEFLAG, --�����������
             AG.AGTYPE SERIES, --��Աϵ��
             TRIM(CN.LSURNAME) || TRIM(CN.LGIVNAME) PSNNAMEEX, --Ӣ����
             TN.FAVORITE STRONGSUIT, --�س�
             TN.STRONGSUIT FAVORITE, --����
             TN.CONTACTMODE CONTACTMODE, --��ϵ��ʽ
             TN.PSNSOURCE PSNSOURCE, --��Ա��Դ
             TP.PSNTYPENO PSNTYPENO, --��Ա����
             TP.STATUS STATUS, --��Ա״̬
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