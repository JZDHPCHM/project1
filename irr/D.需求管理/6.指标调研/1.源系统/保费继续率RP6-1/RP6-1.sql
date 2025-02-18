SELECT
  Table__2.INNERORGCODE,
  Table__2.PROVINCEENGNAME,
  Table__2.BRANCHENGNAME,
  DM2_LA_F_RENEWAL_RP_FY.MON13_REALPREM_MON_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON13_INSTPREM_MON_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON13_REALPREM_MON_LAST1,
  DM2_LA_F_RENEWAL_RP_FY.MON13_INSTPREM_MON_LAST1,
  DM2_LA_F_RENEWAL_RP_FY.MON13_REALPREM_MON_LAST2,
  DM2_LA_F_RENEWAL_RP_FY.MON13_INSTPREM_MON_LAST2,
  DM2_LA_F_RENEWAL_RP_FY.MON13_REALPREM_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON13_INSTPREM_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON25_REALPREM_MON_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON25_INSTPREM_MON_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON25_REALPREM_MON_LAST1,
  DM2_LA_F_RENEWAL_RP_FY.MON25_INSTPREM_MON_LAST1,
  DM2_LA_F_RENEWAL_RP_FY.MON25_REALPREM_MON_LAST2,
  DM2_LA_F_RENEWAL_RP_FY.MON25_INSTPREM_MON_LAST2,
  DM2_LA_F_RENEWAL_RP_FY.MON25_REALPREM_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON25_INSTPREM_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON37_REALPREM_MON_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON37_INSTPREM_MON_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON37_REALPREM_MON_LAST1,
  DM2_LA_F_RENEWAL_RP_FY.MON37_INSTPREM_MON_LAST1,
  DM2_LA_F_RENEWAL_RP_FY.MON37_REALPREM_MON_LAST2,
  DM2_LA_F_RENEWAL_RP_FY.MON37_INSTPREM_MON_LAST2,
  DM2_LA_F_RENEWAL_RP_FY.MON37_REALPREM_CUR,
  DM2_LA_F_RENEWAL_RP_FY.MON37_INSTPREM_CUR,
  D_CHNL.CHNLCOD,
  D_MANAGECOMPANY1.PROVINCEENGNAME
FROM
  ( 
  select * from d_managecompany  a where a.innerorgcode !='86'
  )  Table__2,
  ( 
  select s.FINMONTH,
       s.INNERORGCODE,
       s.ADD_CHNLCOD,
       y.MON13_INSTPREM_MON_cur,
       y.MON13_REALPREM_MON_cur,
       y.MON13_INSTPREM_cur,
       y.MON13_REALPREM_cur,
       y.MON25_INSTPREM_MON_cur,
       y.MON25_REALPREM_MON_cur,
       y.MON25_INSTPREM_cur,
       y.MON25_REALPREM_cur,
       y.MON37_INSTPREM_MON_cur,
       y.MON37_REALPREM_MON_cur,
       y.MON37_INSTPREM_cur,
       y.MON37_REALPREM_cur,
       y.MON13_INSTPREM_MON_last1,
       y.MON13_REALPREM_MON_last1,
       y.MON25_INSTPREM_MON_last1,
       y.MON25_REALPREM_MON_last1,
       y.MON37_INSTPREM_MON_last1,
       y.MON37_REALPREM_MON_last1,
       y.MON13_INSTPREM_MON_last2,
       y.MON13_REALPREM_MON_last2,
       y.MON25_INSTPREM_MON_last2,
       y.MON25_REALPREM_MON_last2,
       y.MON37_INSTPREM_MON_last2,
       y.MON37_REALPREM_MON_last2
      
  from (        
        SELECT To_Char(FINMONTH) as FINMONTH,
                d_managecompany.innerorgcode as INNERORGCODE,
                d_chnl.chnlcod AS ADD_CHNLCOD
          from DM2_LA_F_RENEWAL_RP_FY,
                (select distinct innerorgcode from DM2_LA_F_RENEWAL_RP_FY) d_managecompany,
                (select 'FC' chnlcod
                   from dual
                 union all
                 select 'BK' chnlcod
                   from dual
                 union all
                 select 'AD' chnlcod from dual
                 union all
                 select 'RP' chnlcod from dual) d_chnl
         group by To_Char(FINMONTH),
                   d_managecompany.innerorgcode,
                   d_chnl.chnlcod) s,
       (select FINMONTH,
             
               INNERORGCODE,
               ADD_CHNLCOD,
               sum(MON13_INSTPREM_MON_cur) as MON13_INSTPREM_MON_cur,
               sum(MON13_REALPREM_MON_cur) as MON13_REALPREM_MON_cur,
               sum(MON13_INSTPREM_cur) as MON13_INSTPREM_cur,
               sum(MON13_REALPREM_cur) as MON13_REALPREM_cur,
               sum(MON25_INSTPREM_MON_cur) as MON25_INSTPREM_MON_cur,
               sum(MON25_REALPREM_MON_cur) as MON25_REALPREM_MON_cur,
               sum(MON25_INSTPREM_cur) as MON25_INSTPREM_cur,
               sum(MON25_REALPREM_cur) as MON25_REALPREM_cur,
               sum(MON37_INSTPREM_MON_cur) as MON37_INSTPREM_MON_cur,
               sum(MON37_REALPREM_MON_cur) as MON37_REALPREM_MON_cur,
               sum(MON37_INSTPREM_cur) as MON37_INSTPREM_cur,
               sum(MON37_REALPREM_cur) as MON37_REALPREM_cur,
               sum(MON13_INSTPREM_MON_last1) as MON13_INSTPREM_MON_last1,
               sum(MON13_REALPREM_MON_last1) as MON13_REALPREM_MON_last1,
               sum(MON25_INSTPREM_MON_last1) as MON25_INSTPREM_MON_last1,
               sum(MON25_REALPREM_MON_last1) as MON25_REALPREM_MON_last1,
               sum(MON37_INSTPREM_MON_last1) as MON37_INSTPREM_MON_last1,
               sum(MON37_REALPREM_MON_last1) as MON37_REALPREM_MON_last1,
               sum(MON13_INSTPREM_MON_last2) as MON13_INSTPREM_MON_last2,
               sum(MON13_REALPREM_MON_last2) as MON13_REALPREM_MON_last2,
               sum(MON25_INSTPREM_MON_last2) as MON25_INSTPREM_MON_last2,
               sum(MON25_REALPREM_MON_last2) as MON25_REALPREM_MON_last2,
               sum(MON37_INSTPREM_MON_last2) as MON37_INSTPREM_MON_last2,
               sum(MON37_REALPREM_MON_last2) as MON37_REALPREM_MON_last2
          from (SELECT To_Char(Add_Months(To_Date(FINMONTH, 'yyyymm'), +2),
                               'yyyymm') as FINMONTH,
                       INNERORGCODE,
                       ADD_CHNLCOD,
                       MON13_INSTPREM_MON as MON13_INSTPREM_MON_cur,
                       MON13_REALPREM_MON as MON13_REALPREM_MON_cur,
                       MON13_INSTPREM as MON13_INSTPREM_cur,
                       MON13_REALPREM as MON13_REALPREM_cur,
                       MON25_INSTPREM_MON as MON25_INSTPREM_MON_cur,
                       MON25_REALPREM_MON as MON25_REALPREM_MON_cur,
                       MON25_INSTPREM as MON25_INSTPREM_cur,
                       MON25_REALPREM as MON25_REALPREM_cur,
                       MON37_INSTPREM_MON as MON37_INSTPREM_MON_cur,
                       MON37_REALPREM_MON as MON37_REALPREM_MON_cur,
                       MON37_INSTPREM as MON37_INSTPREM_cur,
                       MON37_REALPREM as MON37_REALPREM_cur,
                       0 MON13_INSTPREM_MON_last1,
                       0 MON13_REALPREM_MON_last1,
                       0 MON25_INSTPREM_MON_last1,
                       0 MON25_REALPREM_MON_last1,
                       0 MON37_INSTPREM_MON_last1,
                       0 MON37_REALPREM_MON_last1,
                       0 MON13_INSTPREM_MON_last2,
                       0 MON13_REALPREM_MON_last2,
                       0 MON25_INSTPREM_MON_last2,
                       0 MON25_REALPREM_MON_last2,
                       0 MON37_INSTPREM_MON_last2,
                       0 MON37_REALPREM_MON_last2
                   
                  from DM2_LA_F_RENEWAL_RP_FY
                UNION all
                SELECT To_Char(Add_Months(To_Date(FINMONTH, 'yyyymm'), +1),
                               'yyyymm') as FINMONTH,
                       INNERORGCODE,
                       ADD_CHNLCOD,
                       0 MON13_INSTPREM_MON_cur,
                       0 MON13_REALPREM_MON_cur,
                       0 MON13_INSTPREM_cur,
                       0 MON13_REALPREM_cur,
                       0 MON25_INSTPREM_MON_cur,
                       0 MON25_REALPREM_MON_cur,
                       0 MON25_INSTPREM_cur,
                       0 MON25_REALPREM_cur,
                       0 MON37_INSTPREM_MON_cur,
                       0 MMON37_REALPREM_MON_cur,
                       0 MON37_INSTPREM_cur,
                       0 MON37_REALPREM_cur,
                       MON13_INSTPREM_MON MON13_INSTPREM_MON_last1,
                       MON13_REALPREM_MON MON13_REALPREM_MON_last1,
                       MON25_INSTPREM_MON MON25_INSTPREM_MON_last1,
                       MON25_REALPREM_MON MON25_REALPREM_MON_last1,
                       MON37_INSTPREM_MON MON37_INSTPREM_MON_last1,
                       MON37_REALPREM_MON MON37_REALPREM_MON_last1,
                       0 MON13_INSTPREM_MON_last2,
                       0 MON13_REALPREM_MON_last2,
                       0 MON25_INSTPREM_MON_last2,
                       0 MON25_REALPREM_MON_last2,
                       0 MON37_INSTPREM_MON_last2,
                       0 MON37_REALPREM_MON_last2
                    
                  from DM2_LA_F_RENEWAL_RP_FY
                UNION all
                SELECT to_char(FINMONTH),
                       INNERORGCODE,
                       ADD_CHNLCOD,
                       0 MON13_INSTPREM_MON_cur,
                       0 MON13_REALPREM_MON_cur,
                       0 MON13_INSTPREM_cur,
                       0 MON13_REALPREM_cur,
                       0 MON25_INSTPREM_MON_cur,
                       0 MON25_REALPREM_MON_cur,
                       0 MON25_INSTPREM_cur,
                       0 MON25_REALPREM_cur,
                       0 MON37_INSTPREM_MON_cur,
                       0 MON37_REALPREM_MON_cur,
                       0 MON37_INSTPREM_cur,
                       0 MON37_REALPREM_cur,
                       0 MON13_INSTPREM_MON_last1,
                       0 MON13_REALPREM_MON_last1,
                       0 MON25_INSTPREM_MON_last1,
                       0 MON25_REALPREM_MON_last1,
                       0 MON37_INSTPREM_MON_last1,
                       0 MON37_REALPREM_MON_last1,
                       MON13_INSTPREM_MON MON13_INSTPREM_MON_last2,
                       MON13_REALPREM_MON MON13_REALPREM_MON_last2,
                       MON25_INSTPREM_MON MON25_INSTPREM_MON_last2,
                       MON25_REALPREM_MON MON25_REALPREM_MON_last2,
                       MON37_INSTPREM_MON MON37_INSTPREM_MON_last2,
                       MON37_REALPREM_MON MON37_REALPREM_MON_last2
                    
                  from DM2_LA_F_RENEWAL_RP_FY) t        
         GROUP BY FINMONTH, INNERORGCODE, ADD_CHNLCOD) y
 where s.FINMONTH = y.FINMONTH(+)
   and s.INNERORGCODE = y.INNERORGCODE(+)
   and s.ADD_CHNLCOD = y.ADD_CHNLCOD(+)
  )  DM2_LA_F_RENEWAL_RP_FY,
  ( 
  select * from d_chnlcod t where t.flg='0' and t.chnlcod<>'GR'
  )  D_CHNL,
  ( 
  select * from d_managecompany  a where a.innerorgcode !='86'
  )  D_MANAGECOMPANY1,
  D_MANAGECOMPANY_MAP,
  ( 
  select * from d_chnl_map t
where t.FLG='0'
and t.ADD_CHNLCOD<>'GR'
  )  D_CHNL_MAP
WHERE
  ( Table__2.INNERORGCODE=D_MANAGECOMPANY_MAP.BRANCHCOMCODE  )
  AND  ( DM2_LA_F_RENEWAL_RP_FY.INNERORGCODE=Table__2.INNERORGCODE(+)  )
  AND  ( DM2_LA_F_RENEWAL_RP_FY.INNERORGCODE=D_MANAGECOMPANY1.INNERORGCODE(+)  )
  AND  ( D_CHNL.ADD_CHNLCOD=DM2_LA_F_RENEWAL_RP_FY.ADD_CHNLCOD  )
  AND  ( D_CHNL_MAP.ADD_CHNLCOD=D_CHNL.ADD_CHNLCOD  )
  AND  
  (
   D_MANAGECOMPANY_MAP.PROVINCECOMCODE  =  '86'
   AND
   DM2_LA_F_RENEWAL_RP_FY.FINMONTH  =  '201801'
   AND
   D_CHNL_MAP.CHNLCOD  =  'ALL'
  )


