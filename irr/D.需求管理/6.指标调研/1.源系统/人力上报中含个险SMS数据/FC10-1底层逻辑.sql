--1-------------------------------------------------------------   该sql 取出的结果集 会 insert 到 dm2_kpi_guilorgpsn，      bidw.D_PSNNATURE 表中有pname可以过滤直销号
select c.calmonth || '01' as dateid, --统计时间
         yu.innerorgcode, --管理机构
         y.pid,
         R.rankid,
         re.tid,
         count(distinct y.pid) AS CurFPsnNum --期初人力
   ,y.add_bkcod
    from bidw.D_PSNNATURE y
   inner join bidm.d_date c on c.calmonth between '200801' and to_char(current_date-1,'yyyyMM')
                      and y.dimissdate >= c.calmonth || '01'
                      and y.workdate < c.calmonth || '01'
    left join bidw.RE_psn_Group re on y.pid = re.pid
                                  AND RE.STARTDATE <= c.calmonth || '01'
                                  AND RE.ENDDATE > c.calmonth || '01',
   bidm.d_orgmap YU,
   bidw.d_rank r
   where substr(y.Did, 4, 3) = '001'
     and SUBSTR(y.DID, 1, 3) = trim(YU.ORIGORGCODE)
     and y.chnlcode = 'FC'        
     and y.psntypeno != 'D'
     and r.pid=y.pid
     and c.dateid between r.startdate and r.enddate
   group by c.calmonth || '01', --统计时间
            yu.innerorgcode, --管理机构
            y.pid,
            R.rankid,
            re.tid ,y.add_bkcod
            order by y.pid
    
    
--2-------------------------------------------------------------  其中 dm2_kpi_guilorgpsn 来源于 上面的sql
--DM3_KPI_CHNL11_1
--添加ADD_BKCOD
--s_m_FC_DM_DM3_KPI_CHNL11_1_add
--DM3_KPI_CHNL11_1
--添加ADD_BKCOD
--s_m_FC_DM_DM3_KPI_CHNL11_1_add
--20140110修改期初人力取值，改为从期末人力 srg
select ty.dateid,
       ty.innerorgcode,
       ty.tid,
       sum(ty.curpsnnum) as curpsnnum,
       sum(ty.endpsnnum) as endpsnnum,
       sum(ty.newaddpsnnum) as newaddpsnnum,
       sum(ty.divisionpsnnum) as divisionpsnnum,
       sum(ty.mon3addpsnnum) as mon3addpsnnum,
       sum(ty.mon6addpsnnum) as mon6addpsnnum,
       sum(ty.mon12addpsnnum) as mon12addpsnnum,
       sum(ty.mon3inpass) as mon3inpass,
       sum(ty.mon6inpass) as mon6inpass,
       sum(ty.mon3retain) as mon3retain,
       sum(ty.mon6retain) as mon6retain,
       sum(ty.mon13retain) as mon13retain,
       sum(ty.mon3addpsnall) as mon3addpsnall,
       sum(ty.mon6addpsnall) as mon6addpsnall,
       sum(ty.mon13addpsnall) as mon13addpsnall,
       sum(ty.MON24RETAIN) as MON24RETAIN,
       sum(ty.MON24ADDPSNNUM) as MON24ADDPSNNUM,
       SUM(TY.mon24addpsnall) AS mon24addpsnall,
       ty.add_bkcod
  from (select substr(t.dateid, 1, 6) as dateid,
               t.innerorgcode,
               t.tid,
               0 as curpsnnum,
               0 endpsnnum,
               sum(t.newaddpsnnum) as newaddpsnnum,
               sum(t.divisionpsnnum) as divisionpsnnum,
               0 as mon3addpsnnum,
               0 as mon6addpsnnum,
               0 as mon12addpsnnum,
               0 AS MON24ADDPSNNUM,
               0 as mon3inpass,
               0 as mon6inpass,
               0 as mon3retain,
               0 as mon6retain,
               0 as mon13retain,
               0 AS MON24RETAIN,
               0 as mon3addpsnall,
               0 as mon6addpsnall,
               0 as mon13addpsnall,
               0 AS mon24addpsnall,
         t.add_bkcod
          from dm2_kpi_guilorgpsn t
         group by substr(t.dateid, 1, 6), t.innerorgcode, t.tid,t.add_bkcod
        union all
        select trim(y.dateid),
               y.innerorgcode,
               y.tid,
               0 as curpsnnum,
               0 as endpsnnum,
               0 as newaddpsnnum,
               0 as divisionpsnnum,
               y.mon3addpsnnum,
               y.mon6addpsnnum,
               y.mon12addpsnnum,
               Y.MON24ADDPSNNUM,
               y.mon3inpass,
               y.mon6inpass,
               y.mon3retain,
               y.mon6retain,
               y.mon13retain,
               Y.MON24RETAIN,
               y.mon3addpsnall,
               y.mon6addpsnall,
               y.mon13addpsnall,
               Y.mon24addpsnall  ,
         y.add_bkcod
          from bidm.dm2_kpi_AddPasRetain y
        union all
    /*    select substr(dr.dateid, 1, 6),
               dr.innerorgcode,
               dr.tid,
               dr.curfpsnnum as curpsnnum,
               0 as endpsnnum,
               0 as newaddpsnnum,
               0 as divisionpsnnum,
               0 as mon3addpsnnum,
               0 as mon6addpsnnum,
               0 as mon12addpsnnum,
               0 as MON24ADDPSNNUM,
               0 as mon3inpass,
               0 as mon6inpass,
               0 as mon3retain,
               0 as mon6retain,
               0 as mon13retain,
               0 as MON24RETAIN,
               0 as mon3addpsnall,
               0 as mon6addpsnall,
               0 as mon13addpsnall,
               0 AS mon24addpsnall,
         dr.add_bkcod
          from DM2_KPI_GUILORGPsn_cur dr*/
          
       select  to_char(add_months(to_date(substr(t.dateid,1,6),'yyyymm'), 1),'yyyymm') as dateid,
               t.innerorgcode,
               t.tid,
               t.endpsnnum  as curpsnnum, 
               0  as  endpsnnum ,
               0           as newaddpsnnum,
               0           as divisionpsnnum,
               0           as mon3addpsnnum,
               0           as mon6addpsnnum,
               0           as mon12addpsnnum,
               0           as MON24ADDPSNNUM,
               0           as mon3inpass,
               0           as mon6inpass,
               0           as mon3retain,
               0           as mon6retain,
               0           as mon13retain,
               0           as MON24RETAIN,
               0           as mon3addpsnall,
               0           as mon6addpsnall,
               0           as mon13addpsnall,
               0           AS mon24addpsnall,
         t.add_bkcod
          from dm2_kpi_guilorgpsn t
         where t.dateid = 
         case to_date(t.dateid, 'yyyy-mm-dd') 
              when  last_Day(to_date(t.dateid, 'yyyy-mm-dd')) then t.dateid 
              when to_Date(to_char(sysdate - 1, 'yyyymmdd'), 'yyyy-mm-dd') then t.dateid 
              else '0' end 
         and t.dateid <> '0'   
         
         
                
        union all
        select substr(t.dateid, 1, 6) as dateid,
               t.innerorgcode,
               t.tid,
               0 as curpsnnum,
               /*sum(*/
               t.endpsnnum /*) */     ,
               0           as newaddpsnnum,
               0           as divisionpsnnum,
               0           as mon3addpsnnum,
               0           as mon6addpsnnum,
               0           as mon12addpsnnum,
               0           as MON24ADDPSNNUM,
               0           as mon3inpass,
               0           as mon6inpass,
               0           as mon3retain,
               0           as mon6retain,
               0           as mon13retain,
               0           as MON24RETAIN,
               0           as mon3addpsnall,
               0           as mon6addpsnall,
               0           as mon13addpsnall,
               0           AS mon24addpsnall,
         t.add_bkcod
          from dm2_kpi_guilorgpsn t
         where t.dateid = 
         case to_date(t.dateid, 'yyyy-mm-dd') 
              when  last_Day(to_date(t.dateid, 'yyyy-mm-dd')) then t.dateid 
              when to_Date(to_char(sysdate - 1, 'yyyymmdd'), 'yyyy-mm-dd') then t.dateid 
              else '0' end 
         and t.dateid <> '0'
         ) ty
 where ty.dateid -- in ('201309','201310')
  BETWEEN (select to_char(add_months(to_date(SDATE,'YYYYMMDD'),-1),'yyyymm')from bidm_etl_incrflg_inc ) 
   and  (select substr(edate, 1, 6) from bidm_etl_incrflg_inc)
 group by ty.dateid, ty.innerorgcode, ty.tid,ty.add_bkcod
 
 ----3  DM3_KPI_CHNL11_1 来源于上面的sql
 
 ---4----------------------------------------------------------------
 create or replace view v_dm2_kpi_chnl11_1 as
select
  DATEID,
  INNERORGCODE,
  TID,
  CURPSNNUM,
  ENDPSNNUM,
  NEWADDPSNNUM,
  DIVISIONPSNNUM,
  MON3ADDPSNNUM,
  MON6ADDPSNNUM,
  MON12ADDPSNNUM,
  MON3INPASS,
  MON6INPASS,
  MON3RETAIN,
  MON6RETAIN,
  MON13RETAIN,
	MON3ADDPSNALL,
	MON6ADDPSNALL,
	MON13ADDPSNALL,
  MON24RETAIN,
  MON24ADDPSNNUM,
  MON24ADDPSNALL,trim(ADD_BKCOD)ADD_BKCOD
from
  DM3_KPI_CHNL11_1;