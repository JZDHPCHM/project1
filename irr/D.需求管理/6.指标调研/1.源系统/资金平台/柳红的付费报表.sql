--退保金 
 select substr(a.applyentity, 1, 1) 机构,
       sum(a.amount) 金额,
       count(a.transseq) 笔数
  from ats_choudan_tmp a
 where  a.transcode = '1988' --付款
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-08' --月份
   and a.transstate = '2' --支付成功
   and a.transsource = 'CUSTOMER SERVICE' 
   and a.reqreserved18 in ('301', -- 正常退保
                           '302', -- 通融退保
                           '304', --  附加险退保 
                           '305', --  犹豫期内退保  
                           '306', --  银行在线CFI 
                           '307' -- 银行在线退保
                          )
 group by substr(a.applyentity, 1, 1)
 order by substr(a.applyentity, 1, 1)


--保险金
select substr(a.applyentity, 1, 1) 机构,
       sum(a.amount) 金额,
       count(a.transseq) 笔数
  from ats_choudan_tmp a
 where  a.transcode = '1988' --付款
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-08' --月份
   and a.transstate = '2' --支付成功
	 and (a.transsource in ('GR', 'CUSTOMER CLAIM','STIS') or
       (a.transsource = 'CUSTOMER SERVICE' and
       a.reqreserved18 not in ('301', -- 正常退保
                           '302', -- 通融退保
                           '304', --  附加险退保 
                           '305', --  犹豫期内退保  
                           '306', --  银行在线CFI 
                           '307' -- 银行在线退保
                           )))
 group by substr(a.applyentity, 1, 1)
 order by substr(a.applyentity, 1, 1)
 
 --汇总
 select count(a.transseq) 笔数,
       sum(a.amount) 金额
  from ats_choudan_tmp a
 where  a.transcode = '1988' --付款
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-07' --月份
   and a.transstate = '2' --支付成功
	 and a.transsource <> 'HEC'
	 
	 
	 select a.transsource 系统,
       a.transdate 申请日期,
			 to_char(a.paymadedate, 'yyyymmdd') 付款日期,
       a.applyentity 机构,
       a.oppbankcode 银行代码,
       a.amount 金额,
       a.sourcenotecode 业务号,
       a.reqreserved12 保单号,
       (select max(b.directaccesscode)
          from t_payments b
         where b.originnote = a.rdseq
           and b.corpaccesssystemsid = a.transsource
					 and b.returnstate='1') 通道,
       a.reqreserved18 业务类型

  from ats_choudan_tmp a
 where a.transcode = '1988' --付款
   and a.transstate = '2' --付款成功
   and to_char(a.paymadedate, 'yyyy-mm') = '2018-07' --月份
   and a.transsource <> 'HEC'


