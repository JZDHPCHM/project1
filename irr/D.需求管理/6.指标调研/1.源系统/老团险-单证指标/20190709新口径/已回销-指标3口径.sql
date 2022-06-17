-- 已回销
select 'OR13012', r.PROVINCECOMCODE, t1.fd_name, 'GP', r.scount, '2019Q1'
  from (SELECT 'OR13012',
               B.PROVINCECOMCODE,
               'GP',
               NVL(SUM(A.SUMCOUNT), 0) as scount,
               '2019Q1'
          FROM (select z.managecom, z.startno, z.endno, z.sumcount
                  from E_BDS_LISHASL_LZCARDTRACK z
                 where z.sendoutcom = z.receivecom
                   and z.operateflag = '7'
                      
                   AND (TO_CHAR(makedate, 'YYYYMMDD') BETWEEN '20180401' AND
                       '20190331')
                union all
                select z.managecom, z.startno, z.endno, z.sumcount
                  from E_BDS_LISHASL_LZCARDTRACK z
                 where z.sendoutcom <> z.receivecom
                   and z.operateflag = '1'
                   and z.payflag = '1'
                      
                   AND (TO_CHAR(makedate, 'YYYYMMDD') BETWEEN '20180401' AND
                       '20190331')
                union all
                select z.managecom, z.startno, z.endno, z.sumcount
                  from E_BDS_LISHASL_LZCARDTRACK z
                 where z.sendoutcom <> z.receivecom
                   and z.operateflag = '5'
                      
                   AND (TO_CHAR(makedate, 'YYYYMMDD') BETWEEN '20180401' AND
                       '20190331')) A
         RIGHT JOIN E_BDS_BIDM_MGECMP B
            ON A.MANAGECOM = B.INNERORGCODE
        
         group by B.PROVINCECOMCODE) r

  LEFT JOIN FR_AA_ORG T1
    ON r.PROVINCECOMCODE = T1.FD_CODE
      

   
   b.provincechnname,
   
   

--20190709新口径
   
   select z.managecom, z.startno, z.endno, z.sumcount
  from E_BDS_LISHASL_LZCARDTRACK z
 where z.sendoutcom = z.receivecom
   and z.operateflag = '7'
   
  AND( TO_CHAR(makedate, 'YYYYMMDD') 
   BETWEEN '20180401' 
   AND '20190331')
union all
select z.managecom, z.startno, z.endno, z.sumcount
  from E_BDS_LISHASL_LZCARDTRACK z
 where z.sendoutcom <> z.receivecom
   and z.operateflag = '1'
   and z.payflag = '1'
    
  AND( TO_CHAR(makedate, 'YYYYMMDD') 
   BETWEEN '20180401' 
   AND '20190331')
union all
select z.managecom, z.startno, z.endno, z.sumcount
  from E_BDS_LISHASL_LZCARDTRACK z
 where z.sendoutcom <> z.receivecom
   and z.operateflag = '5'
     
  AND( TO_CHAR(makedate, 'YYYYMMDD') 
   BETWEEN '20180401' 
   AND '20190331')

