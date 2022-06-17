//字段定义
var EnterpriseCustomerFields ={
	edit:[
	{width:'20%',		name:'custNo',              title:'客户编号'},
	{width:'20%',		name:'custName',            title:'客户名称'},
	{width:'20%',		name:'ctmCoreNo',           title:'核心客户编号'},
	{width:'20%',		name:'ctmType',     		title:'客户类型',	type: 'select',valueMap:ctmTyps},
	{width:'20%',		name:'enterpriseScale',     title:'企业规模',	type: 'select',valueMap:EnterpriseScales},
	{width:'20%',		name:'gbIndustry',     		title:'国标行业分类', type: 'select',valueMap:GBindustrys},
	{width:'20%',		name:'intraIndustry',     	title:'行内行业分类', type: 'select',valueMap:industrys},
	{width:'20%',		name:'certType',    		title:'证件类型',	type:'select',	valueMap:certTypes},
	{width:'20%',		name:'legalRep',     		title:'法人代表姓名'},
	{width:'20%',		name:'establishmentTime',   title:'成立日期'},
	{width:'20%',		name:'certidNo',      		title:'登记注册证件号'},
	{width:'20%',		name:'empCount',      		title:'职工人数'},
	{width:'20%',		name:'annualIncome',      	title:'营业收入（元）',type:'float',format:"0.00"},
	{width:'20%',		name:'totalAssets',      	title:'资产总额（元）',type:'float',format:"0.00"},
	{width:'20%',		name:'FinancialStatementsType',title:'财务报表类型',type:'select',	valueMap:ReportTypes},
	{width:'20%',		name:'org.name',      		title:'登记机构'},
	{width:'20%',		name:'inputDt',      		title:'登记日期'},
	{width:'20%',		name:'updateOrg.name',      title:'更新机构'},
	{width:'20%',		name:'updateDt',      		title:'更新日期'},
	{width:'20%',		name:'busScope',      		title:'经营范围',type:'textarea',rowSpan:2},
	{width:'20%',		name:'bankShareholders',    title:'是否本行股东',	 type:'boolean'},
	{width:'20%',		name:'newTechnology',       title:'是否政府认定高新技术企业',type:'boolean'},
	{width:'20%',		name:'technology',       	title:'是否科技企业',	 type:'boolean'},
	{width:'20%',		name:'isFinancingGuarantee',title:'是否融资担保机构',type:'boolean'},
	{width:'20%',		name:'isElseWhere',      	title:'是否异地客户',   type:'boolean'},
	{width:'20%',		name:'isAbnormalOperation', title:'经营异常名录',type:'boolean'},
	{width:'20%',		name:'isBlankBlackList', 	title:'人行联合惩戒名单',type:'boolean'},
	{width:'20%',		name:'litigation', 			title:'重大涉诉',type:'boolean'}
	]
};

isc.FwReadOnlyDynamicForm.create({
	ID:"customerVieweForm1",
	numCols : 6,
	width:'100%',
	height:'100%',
	autoDraw : true,
	overflow : "auto",
	autoFocus : false,
	fields:EnterpriseCustomerFields.edit
});
customerVieweForm1.setValues(projectLoanWindow.RatingObject.ratingCustomer);

function formatterPercent(value,row,index){
	 if(!value){
	  value=0;
	 }
	 value=value*100;
	 value=value.toFixed(2);
	 return value+"%";
	}


isc.VLayout.create({
	width : "100%",
	heigth : '100%',
	autoDraw : false,
	membersMargin : 20,
	members : [
		isc.HLayout.create({
			width : "100%",
			height:'70%',
			members:[
				isc.VLayout.create({
					width : "70%",
					height:'100%',
					members:[
						customerVieweForm1
					]
				})
			]
		})
		]
});