--�˱��� 
 select substr(a.applyentity, 1, 1) ����,
       sum(a.amount) ���,
       count(a.transseq) ����
  from ats_choudan_tmp a
 where  a.transcode = '1988' --����
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-08' --�·�
   and a.transstate = '2' --֧���ɹ�
   and a.transsource = 'CUSTOMER SERVICE' 
   and a.reqreserved18 in ('301', -- �����˱�
                           '302', -- ͨ���˱�
                           '304', --  �������˱� 
                           '305', --  ��ԥ�����˱�  
                           '306', --  ��������CFI 
                           '307' -- ���������˱�
                          )
 group by substr(a.applyentity, 1, 1)
 order by substr(a.applyentity, 1, 1)


--���ս�
select substr(a.applyentity, 1, 1) ����,
       sum(a.amount) ���,
       count(a.transseq) ����
  from ats_choudan_tmp a
 where  a.transcode = '1988' --����
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-08' --�·�
   and a.transstate = '2' --֧���ɹ�
	 and (a.transsource in ('GR', 'CUSTOMER CLAIM','STIS') or
       (a.transsource = 'CUSTOMER SERVICE' and
       a.reqreserved18 not in ('301', -- �����˱�
                           '302', -- ͨ���˱�
                           '304', --  �������˱� 
                           '305', --  ��ԥ�����˱�  
                           '306', --  ��������CFI 
                           '307' -- ���������˱�
                           )))
 group by substr(a.applyentity, 1, 1)
 order by substr(a.applyentity, 1, 1)
 
 --����
 select count(a.transseq) ����,
       sum(a.amount) ���
  from ats_choudan_tmp a
 where  a.transcode = '1988' --����
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-07' --�·�
   and a.transstate = '2' --֧���ɹ�
	 and a.transsource <> 'HEC'
	 
	 
	 select a.transsource ϵͳ,
       a.transdate ��������,
			 to_char(a.paymadedate, 'yyyymmdd') ��������,
       a.applyentity ����,
       a.oppbankcode ���д���,
       a.amount ���,
       a.sourcenotecode ҵ���,
       a.reqreserved12 ������,
       (select max(b.directaccesscode)
          from t_payments b
         where b.originnote = a.rdseq
           and b.corpaccesssystemsid = a.transsource
					 and b.returnstate='1') ͨ��,
       a.reqreserved18 ҵ������

  from ats_choudan_tmp a
 where a.transcode = '1988' --����
   and a.transstate = '2' --����ɹ�
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-07' --�·�
   and a.transsource <> 'HEC'


