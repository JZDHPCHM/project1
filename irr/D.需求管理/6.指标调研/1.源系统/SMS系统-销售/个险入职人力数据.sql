SELECT psn.did,
       (SELECT deptname FROM ssa.t_dept WHERE did = psn.did) deptname,
       psn.startdate,
       psn.enddate,
       (select codename
          from ssa.t_codedef
         where codetypeno = 'P003'
           and codevalue = psn.psntypeno) psntype,
       nt.againflag,
       code.descitem,
       code.longdesc,
       (CASE
         WHEN groupno IS NULL AND departmentno IS NULL THEN
          majorno
         WHEN groupno IS NULL THEN
          majorno || ',' || departmentno
         ELSE
          majorno || ',' || departmentno || ',' || groupno
       END) newatid,
       (SELECT teamname
          FROM ssa.t_team
         WHERE enddate =
               (SELECT MAX(tt.enddate) FROM ssa.t_team tt WHERE tt.tid = tm.tid)
           AND tid = tm.tid) zu,
       (SELECT teamname
          FROM ssa.t_team
         WHERE enddate = (SELECT MAX(tt.enddate)
                            FROM ssa.t_team tt
                           WHERE tt.tid = t2t.relatid)
           AND tid = t2t.relatid) bu,
       (SELECT teamname
          FROM ssa.t_team
         WHERE enddate = (SELECT MAX(tt.enddate)
                            FROM ssa.t_team tt
                           WHERE tt.tid = t2t1.relatid)
           AND tid = t2t1.relatid) qu,
       (SELECT DISTINCT rankname
          FROM ssa.t_rankdef
         WHERE rankid = cr.chgbakrankid) rankname,
       TO_CHAR(cr.startdate, 'YYYY-MM-DD') rankstartdate,
       TO_CHAR(cr.enddate, 'YYYY-MM-DD') rankenddate,
       psn.pid,
       psn.psnname,
       NVL(nt.peid, '-') peid,
       (SELECT codename
          FROM ssa.t_codedef
         WHERE codetypeno = 'P007'
           AND codevalue = psn.status) status,
       (SELECT codename
          FROM ssa.t_codedef
         WHERE codetypeno = 'C002'
           AND codevalue = nt.series) series,
       nt.assstartdate
  FROM ssa.t_person psn,
       ssa.t_naturegx nt,
       ssa.t_memberset tm,
       ssa.t_teamno tn,
       ssa.t_chgrank cr,
       (select *
          from (SELECT t1.*,
                       row_number() over(partition by signtid order by t1.enddate desc) rn
                  FROM ssa.t_team2team t1
                 WHERE t1.relano = 'TT01') tt
         where tt.rn = 1) t2t,
       (select *
          from (SELECT t1.*,
                       row_number() over(partition by signtid order by t1.enddate desc) rn
                  FROM ssa.t_team2team t1
                 WHERE t1.relano = 'TT02') tt
         where tt.rn = 1) t2t1,
       ssa.t_psn2dept_gx p2d,
       ssa.la_codeitem code
 WHERE code.descitem = p2d.aracde
   AND p2d.pid = psn.pid
   and cr.enddate between p2d.startdate and last_day(p2d.enddate)
   AND psn.pid = tm.pid
   and cr.enddate between tm.startdate and tm.enddate
   and t2t.relano = 'TT01'
   and t2t.signtid = tm.tid
   and t2t1.relano = 'TT02'
   and t2t1.signtid = tm.tid
   AND tm.tid = tn.tid
   and cr.enddate between tn.startdate and tn.enddate
   AND psn.pid = nt.pid
   and cr.enddate between nt.startdate and nt.enddate
   AND psn.pid = cr.pid
   AND cr.enddate =
       (select min(enddate)
          from ssa.t_chgrank
         where pid = cr.pid
           and ((psn.status = '1' and enddate between psn.startdate and
               last_day(psn.enddate)) or
               (psn.status <> '1' and enddate between psn.startdate and
               to_date('9999-09-09', 'yyyy-mm-dd'))))
   and psn.status in ('0')
   and psn.psntypeno in ('M', 'L')
   and psn.startdate >= --入职开始日期
   and psn.startdate <= --入职结束日期