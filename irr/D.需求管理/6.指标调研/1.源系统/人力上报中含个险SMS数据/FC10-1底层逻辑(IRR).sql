CREATE OR REPLACE VIEW DW_IRR.V_DM2_KPI_GUILORGPSN_IRR AS
select dateid, --ͳ��ʱ��
       innerorgcode, --�������
       rankid,
       0 curpsnnum,
       sum(NewAddPsnNum) as NewAddPsnNum, --����˾����
       sum(DivisionPsnNum) as DivisionPsnNum, --��˾����
       sum(CurrEPsnNum) as endpsnnum, --��ĩ����
       TID,
       0 AS ActivPsnNum,
       0 ExcellencePsnNum  ,add_bkcod
    from (select tt.dateid, --ͳ��ʱ��
                 tt.innerorgcode, --�������
                 tt.rankid,
                 TT.TID,
                 sum(NewAddPsnNum) as NewAddPsnNum, --����˾����
                 sum(DivisionPsnNum) as DivisionPsnNum, --��˾����
                 sum(CurrEPsnNum) as CurrEPsnNum, --��ĩ����
                 SUM(ActivPsnNum) AS ActivPsnNum,
                 '' ExcellencePsnNum ,ADD_BKCOD
            from (
                  -----------------����˾����
                  SELECT dateid,
                          innerorgcode,
                          rankid,
                          tid,
                          SUM(NewAddPsnNum) as NewAddPsnNum,
                          DivisionPsnNum,
                          CurrEPsnNum,
                          ActivPsnNum ,add_bkcod
                    FROM (select b.dateid, --ͳ��ʱ��
                                  innerorgcode, --�������
                                  r.rankid,
                                  re.tid AS tid,
                                  count(distinct ti.pid) as NewAddPsnNum, --����˾����
                                  0 as DivisionPsnNum, --��˾����
                                  0 as CurrEPsnNum, --��ĩ����
                                  0 as ActivPsnNum ,
                                  ti.add_bkcod  --4������
                             from BIDW.D_PSNNATURE ti
                            inner join BIDM.d_date b on ti.workdate = b.dateid
                                               --AND B.DATEID between to_char(current_date-1,'yyyy')||'0101' and to_char(current_date-1,'yyyyMMdd')
                            --inner join bidm_etl_incrflg_inc et on b.DATEID between to_char(add_months(to_date(ET.SDATE,'YYYYMMDD'),-1),'yyyymm')||'01' and et.edate
                             left join BIDW.RE_psn_Group re on TI.PID = RE.PID
                                                           AND RE.STARTDATE <=
                                                               b.dateid
                                                           AND RE.ENDDATE >=
                                                               b.dateid,
                            BIDM.d_orgmap YU,
                            bidw.d_rank r
                            where substr(ti.Did, 4, 3) = '001'
                              and r.pid=ti.pid
                              and b.dateid between r.startdate and r.enddate
                              and ti.chnlcode = 'FC'
                               and ti.psntypeno != 'D'
                              and SUBSTR(ti.DID, 1, 3) = YU.ORIGORGCODE
                              and ti.pname not like '%ֱ��%'
                            group by b.dateid,
                                     innerorgcode,
                                     r.rankid,
                                     TI.PID,
                                     re.tid   ,ti.add_bkcod
                            )
                   GROUP BY dateid,
                             innerorgcode,
                             rankid,
                             tid,
                             DivisionPsnNum,
                             CurrEPsnNum,
                             ActivPsnNum  ,add_bkcod

                  union all
                  ---------------------��˾����
                  SELECT dateid,
                         innerorgcode,
                         rankid,
                         tid,
                         NewAddPsnNum,
                         SUM(DivisionPsnNum) as DivisionPsnNum,
                         CurrEPsnNum,
                         ActivPsnNum ,add_bkcod
                    FROM (select b.dateid, --ͳ��ʱ��
                                 yu.innerorgcode, --�������
                                 r.rankid,
                                 re.tid AS tid,
                                 0 as NewAddPsnNum, --����˾����
                                  count( distinct ti.pid)as DivisionPsnNum, --��˾����
                                 0 as CurrEPsnNum, --��ĩ����
                                 0 as ActivPsnNum ,ti.add_bkcod
                            from BIDW.D_PSNNATURE ti
                           inner join BIDM.d_date b on b.dateid = ti.dimissdate
                                              --AND B.DATEID between to_char(current_date-1,'yyyy')||'0101' and to_char(current_date-1,'yyyyMMdd')
                           --inner join bidm_etl_incrflg_inc et on b.DATEID between to_char(add_months(to_date(ET.SDATE,'YYYYMMDD'),-1),'yyyymm')||'01' and et.edate
                            left join BIDW.RE_psn_Group re on TI.PID = RE.PID
                                                          AND RE.STARTDATE <=
                                                              b.dateid
                                                          AND RE.ENDDATE >=
                                                              b.dateid,
                           BIDM.d_orgmap YU,
                           bidw.d_rank r

                           where substr(ti.Did, 4, 3) = '001'
                             and r.pid=ti.pid
                             and b.dateid between r.startdate and r.enddate
                             and ti.chnlcode = 'FC'
                             and ti.psntypeno != 'D'
                             and SUBSTR(ti.DID, 1, 3) = YU.ORIGORGCODE
                             and ti.pname not like '%ֱ��%'
                          ------���һ��״̬Ϊ2��

                           group by b.dateid,
                                    yu.innerorgcode,
                                    r.rankid,
                                    TI.PID,
                                    re.tid,ti.add_bkcod  )
                   GROUP BY dateid,
                            innerorgcode,
                            rankid,
                            tid,
                            NewAddPsnNum,
                            CurrEPsnNum,
                            ActivPsnNum ,add_bkcod

                  union all
                  select dateid,
                         innerorgcode,
                         rankid,
                         tid,
                         NewAddPsnNum,
                         DivisionPsnNum,
                         SUM(CurrEPsnNum) as CurrEPsnNum,
                         ActivPsnNum,add_bkcod
                    from (
                    select c.dateid, --ͳ��ʱ��
                                 yu.innerorgcode, --�������
                                 r.rankid,
                                 re.tid AS tid,
                                 0 as NewAddPsnNum, --����˾����
                                 0 as DivisionPsnNum, --��˾����
                                 COUNT(DISTINCT Y.PID) as CurrEPsnNum, --��ĩ����
                                 0 as ActivPsnNum ,y.add_bkcod
                            from bidw.D_PSNNATURE y
                           inner join BIDM.d_date c on y.dimissdate > c.dateid
                                              and y.workdate<=c.dateid
                                              --AND c.DATEID between to_char(current_date-1,'yyyy')||'0101' and to_char(current_date-1,'yyyyMMdd')
                           --inner join bidm_etl_incrflg_inc et on c.DATEID between to_char(add_months(to_date(ET.SDATE,'YYYYMMDD'),-1),'yyyymm')||'01' and et.edate
                            left join BIDW.RE_psn_Group re on Y.PID = RE.PID
                                                          AND RE.STARTDATE <=
                                                              c.dateid
                                                          AND RE.ENDDATE >=
                                                              c.dateid,
                           BIDM.d_orgmap YU,
                           bidw.d_rank r
                           where substr(y.Did, 4, 3) = '001'
                             and r.pid=y.pid
                             and c.dateid between r.startdate and r.enddate
                             and y.chnlcode = 'FC'
                             and SUBSTR(y.DID, 1, 3) = YU.ORIGORGCODE
                             and y.psntypeno != 'D'
                             and y.pname not like '%ֱ��%'
                           group by c.dateid, --ͳ��ʱ��
                                    yu.innerorgcode, --�������
                                    r.rankid,
                                    y.pid,
                                    re.tid,y.add_bkcod  )
                   group by dateid,
                            innerorgcode,
                            rankid,
                            tid,
                            NewAddPsnNum,
                            DivisionPsnNum,
                            ActivPsnNum,add_bkcod ) tt
           group by tt.dateid, --ͳ��ʱ��
                    tt.innerorgcode, --�������
                    tt.rankid, tt.add_bkcod  ,
                    tt.tid)
   group by dateid, --ͳ��ʱ��
            innerorgcode, --�������
            rankid,
            TID,add_bkcod;
