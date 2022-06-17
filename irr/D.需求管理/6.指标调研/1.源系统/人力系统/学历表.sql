select  (select o.C_name
          from e_bds_hrm_organization o
         where o.typeid = 2
           and o.code = t.orgcode2
           and rownum < 2) as "T2机构",
       (select o.C_name
          from e_bds_hrm_organization o
         where o.typeid = 3
           and o.code = t.orgcode3
           and rownum < 2) as "T3机构",
       (select o.C_name
          from e_bds_hrm_organization o
         where o.typeid = 4
           and o.code = t.orgcode4
           and rownum < 2) as "T4机构"
       
       ,
       (case
         when (t.payrid = '17') then
          '内勤薪酬'
         when (t.payrid = '30') then
          '销售薪酬'
       
         else
          '未知'
       end) as "薪酬体系",
       t.data_date,
       t.a as "统计大专学历以上的在职员工"
  from e_bds_hrm_employeedegree t
 where t.data_date = '201906'
