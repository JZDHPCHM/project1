select  (select o.C_name
          from e_bds_hrm_organization o
         where o.typeid = 2
           and o.code = t.orgcode2
           and rownum < 2) as "T2����",
       (select o.C_name
          from e_bds_hrm_organization o
         where o.typeid = 3
           and o.code = t.orgcode3
           and rownum < 2) as "T3����",
       (select o.C_name
          from e_bds_hrm_organization o
         where o.typeid = 4
           and o.code = t.orgcode4
           and rownum < 2) as "T4����"
       
       ,
       (case
         when (t.payrid = '17') then
          '����н��'
         when (t.payrid = '30') then
          '����н��'
       
         else
          'δ֪'
       end) as "н����ϵ",
       t.data_date,
       t.a as "ͳ�ƴ�רѧ�����ϵ���ְԱ��"
  from e_bds_hrm_employeedegree t
 where t.data_date = '201906'
