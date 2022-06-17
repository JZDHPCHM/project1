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