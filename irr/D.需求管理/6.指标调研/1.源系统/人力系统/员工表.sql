select 
        (select o.C_name
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
           and rownum < 2) as "T4机构",
       (case
         when (t.payrid = '17') then
          '内勤薪酬'
         when (t.payrid = '30') then
          '销售薪酬'
       
         else
          '未知'
       end) as "薪酬体系",
       t.data_date,
       t.a as "月初在职人数",
       t.a1 as "月末在职人数",
       t.a2 as "月当月入职人数",
       t.a3 as "月当月离职人数"
  from e_bds_hrm_employeestate t
 where t.data_date = '201906'
