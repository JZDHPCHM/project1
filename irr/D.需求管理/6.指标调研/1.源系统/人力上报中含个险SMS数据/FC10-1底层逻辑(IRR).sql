CREATE OR REPLACE VIEW DW_IRR.V_DM2_KPI_GUILORGPSN_IRR AS
select dateid, --统计时间
       innerorgcode, --管理机构
       rankid,
       0 curpsnnum,
       sum(NewAddPsnNum) as NewAddPsnNum, --新入司人力
       sum(DivisionPsnNum) as DivisionPsnNum, --离司人力
       sum(CurrEPsnNum) as endpsnnum, --期末人力
       TID,
       0 AS ActivPsnNum,
       0 ExcellencePsnNum  ,add_bkcod
    from (select tt.dateid, --统计时间
                 tt.innerorgcode, --管理机构
                 tt.rankid,
                 TT.TID,
                 sum(NewAddPsnNum) as NewAddPsnNum, --新入司人力
                 sum(DivisionPsnNum) as DivisionPsnNum, --离司人力
                 sum(CurrEPsnNum) as CurrEPsnNum, --期末人力
                 SUM(ActivPsnNum) AS ActivPsnNum,
                 '' ExcellencePsnNum ,ADD_BKCOD
            from (
                  -----------------新入司人力
                  SELECT dateid,
                          innerorgcode,
                          rankid,
                          tid,
                          SUM(NewAddPsnNum) as NewAddPsnNum,
                          DivisionPsnNum,
                          CurrEPsnNum,
                          ActivPsnNum ,add_bkcod
                    FROM (select b.dateid, --统计时间
                                  innerorgcode, --管理机构
                                  r.rankid,
                                  re.tid AS tid,
                                  count(distinct ti.pid) as NewAddPsnNum, --新入司人力
                                  0 as DivisionPsnNum, --离司人力
                                  0 as CurrEPsnNum, --期末人力
                                  0 as ActivPsnNum ,
                                  ti.add_bkcod  --4级机构
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
                              and ti.pname not like '%直销%'
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
                  ---------------------离司人力
                  SELECT dateid,
                         innerorgcode,
                         rankid,
                         tid,
                         NewAddPsnNum,
                         SUM(DivisionPsnNum) as DivisionPsnNum,
                         CurrEPsnNum,
                         ActivPsnNum ,add_bkcod
                    FROM (select b.dateid, --统计时间
                                 yu.innerorgcode, --管理机构
                                 r.rankid,
                                 re.tid AS tid,
                                 0 as NewAddPsnNum, --新入司人力
                                  count( distinct ti.pid)as DivisionPsnNum, --离司人力
                                 0 as CurrEPsnNum, --期末人力
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
                             and ti.pname not like '%直销%'
                          ------添加一个状态为2的

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
                    select c.dateid, --统计时间
                                 yu.innerorgcode, --管理机构
                                 r.rankid,
                                 re.tid AS tid,
                                 0 as NewAddPsnNum, --新入司人力
                                 0 as DivisionPsnNum, --离司人力
                                 COUNT(DISTINCT Y.PID) as CurrEPsnNum, --期末人力
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
                             and y.pname not like '%直销%'
                           group by c.dateid, --统计时间
                                    yu.innerorgcode, --管理机构
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
           group by tt.dateid, --统计时间
                    tt.innerorgcode, --管理机构
                    tt.rankid, tt.add_bkcod  ,
                    tt.tid)
   group by dateid, --统计时间
            innerorgcode, --管理机构
            rankid,
            TID,add_bkcod;
