var hidde = false;
if(rateWindow.RatingObject.modelCode == "S3"){
	hidde = true;
}

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
	ID:"customerVieweForm",
	numCols : 4,
	width:'100%',
	height:'100%',
	autoDraw : true,
	overflow : "auto",
	autoFocus : false,
	fields:EnterpriseCustomerFields.edit
});
customerVieweForm.setValues(rateWindow.RatingObject.ratingCustomer);

function formatterPercent(value,row,index){
	 if(!value){
	  value=0;
	 }
	 value=value*100;
	 value=value.toFixed(2);
	 return value+"%";
	}

//---------------------------------- 财报信息----------------------

//字段定义
var FinAccountDataFields ={
	grid: [
			{width:'*',		name:'id',                          title:'[[#{id}]]' ,				primaryKey:true,	canEdit:false,		hidden:true},
			{width:'*',		name:'financia',               		hidden:true	},
			{width:'*',		name:'accountCategory',             title:'科目分类',type:'select',	valueMap:FinanciaAccountCategory},
			{width:'*',		name:'dataitemName',        		title:'数据名称'},
			{width:'*',		name:'dataitemCode',    			hidden:true	},
			{width:'*',		name:'beginValue',        			title:'期初值'},
			{width:'*',		name:'endValue',        			title:'期末值'}
	]
};

//客户财报科目数据源
isc.FwRestDataSource.create({
	ID: 'finAccountDataDS',
    fields: FinAccountDataFields.grid,
    dataURL : FrameworkUiInterface.webContextPath + '/irs/ratingFinAccountData/isc'
});

//客户财报科目数据列表
isc.FwListGrid.create({
	ID: 'finAccountDataListGrid',
	//title: '<b>科目数据</b>',
	dataSource: finAccountDataDS,
	autoFetchData:false,
	canPageable:false,
    autoSaveEdits: false,
    sortField:'dataitemCode',
    listEndEditAction: "done",
    groupByField: 'accountCategory',
	fields: FinAccountDataFields.grid,
	selectionUpdated: function(record, recordList){
		this.Super('selectionUpdated',arguments);
	}

});


isc.IButton.create({
	ID : "nextBtn",
	title : '<b>下一步</b>',
	autoDraw : false,
	width : 90,
	click : function() {
		if(!hidde){
			productionOperation.setValue("ratingId",rateWindow.RatingObject.id);
			//productionOperation.fetchData({ratingId:rateWindow.RatingObject.id});
			
			if((productionOperation.getValue("cust1") == null || productionOperation.getValue("cust1") == "undefined")
					&& (productionOperation.getValue("cust2") == null || productionOperation.getValue("cust2") == "undefined") 
					&& (productionOperation.getValue("cust3") == null || productionOperation.getValue("cust3") == "undefined")){
				isc.say("请填写公司上游客户信息");
				return ;
			}
			
			if((productionOperation.getValue("custA") == null || productionOperation.getValue("custA") == "undefined")
					&& (productionOperation.getValue("custB") == null || productionOperation.getValue("custB") == "undefined") 
					&& (productionOperation.getValue("custC") == null || productionOperation.getValue("custC") == "undefined")){
				isc.say("请填写公司下游客户信息");
				return ;
			}
			
			if((productionOperation.getValue("type1") == null || productionOperation.getValue("type1") == "undefined")
					&& (productionOperation.getValue("type2") == null || productionOperation.getValue("type2") == "undefined") 
					&& (productionOperation.getValue("type3") == null || productionOperation.getValue("type3") == "undefined")){
				isc.say("请填写业务构成情况");
				return ;
			}
			
			for(var a=1;a<4;a++){
				if(((productionOperation.getValue("upper"+a) != null && productionOperation.getValue("upper"+a) != "undefined") && (productionOperation.getValue("cust"+a) == null || productionOperation.getValue("cust"+a) == "undefined"))
						|| ((productionOperation.getValue("upper"+a) == null || productionOperation.getValue("upper"+a) == "undefined") && (productionOperation.getValue("cust"+a) != null && productionOperation.getValue("cust"+a) != "undefined"))){
					isc.say("公司上游客客户名称和占比需对应填写");
					return ;
				}
				if(((productionOperation.getValue("type"+a) != null && productionOperation.getValue("type"+a) != "undefined") && (productionOperation.getValue("typeNumber"+a) == null || productionOperation.getValue("typeNumber"+a) =="undefined")) 
						|| ((productionOperation.getValue("type"+a) == null || productionOperation.getValue("type"+a) == "undefined") && (productionOperation.getValue("typeNumber"+a) != null && productionOperation.getValue("typeNumber"+a) !="undefined"))){
					isc.say("业务构成情况行业和占比需对应填写");
					return ;
				}
			}
			
			if(((productionOperation.getValue("custA") != null && productionOperation.getValue("custA") != "undefined") && (productionOperation.getValue("lower1") == null || productionOperation.getValue("lower1") =="undefined")) 
					|| ((productionOperation.getValue("custA") == null || productionOperation.getValue("custA") == "undefined") && (productionOperation.getValue("lower1") != null && productionOperation.getValue("lower1") !="undefined"))){
				isc.say("公司下游客客户名称和占比需对应填写");
				return ;
			}
			
			if(((productionOperation.getValue("custB") != null && productionOperation.getValue("custB") != "undefined") && (productionOperation.getValue("lower2") == null || productionOperation.getValue("lower2") =="undefined")) 
					|| ((productionOperation.getValue("custB") == null || productionOperation.getValue("custB") == "undefined") && (productionOperation.getValue("lower2") != null && productionOperation.getValue("lower2") !="undefined"))){
				isc.say("公司下游客客户名称和占比需对应填写");
				return ;
			}
			
			if(((productionOperation.getValue("custC") != null && productionOperation.getValue("custC") != "undefined") && (productionOperation.getValue("lower3") == null || productionOperation.getValue("lower3") =="undefined")) 
					|| ((productionOperation.getValue("custC") == null || productionOperation.getValue("custC") == "undefined") && (productionOperation.getValue("lower3") != null && productionOperation.getValue("lower3") !="undefined"))){
				isc.say("公司下游客客户名称和占比需对应填写");
				return ;
			}
			
			if((productionOperation.getValue("custA") != null && productionOperation.getValue("lower1") == null) || (productionOperation.getValue("custA") == null && productionOperation.getValue("lower1") != null)){
				isc.say("公司下游客客户名称和占比需对应填写");
				return ;
			}
			
			if((productionOperation.getValue("custB") != null && productionOperation.getValue("lower2") == null) || (productionOperation.getValue("custB") == null && productionOperation.getValue("lower2") != null)){
				isc.say("公司下游客客户名称和占比需对应填写");
				return ;
			}
			
			if((productionOperation.getValue("custC") != null && productionOperation.getValue("lower3") == null) || (productionOperation.getValue("custC") == null && productionOperation.getValue("lower3") != null)){
				isc.say("公司下游客客户名称和占比需对应填写");
				return ;
			}
			
			var number=0;
			for(var a=1;a<4;a++){
				if(productionOperation.getValue("upper"+a)){
					number+=productionOperation.getValue("upper"+a);
				}
				
				if(productionOperation.getValue("upper"+a) != null && productionOperation.getValue("upper"+a) != "undefined" && parseFloat(productionOperation.getValue("upper"+a)).toString() == "NaN"){
					isc.say("上游客户占比只能输入数字");
					return ;
				}
				if(productionOperation.getValue("lower"+a) != null && productionOperation.getValue("lower"+a) != "undefined" && parseFloat(productionOperation.getValue("lower"+a)).toString() == "NaN"){
					isc.say("下游客户占比只能输入数字");
					return ;
				}
				if(productionOperation.getValue("typeNumber"+a) != null && productionOperation.getValue("typeNumber"+a) != "undefined" && parseFloat(productionOperation.getValue("typeNumber"+a)).toString() == "NaN"){
					isc.say("行业占比只能输入数字");
					return ;
				}
			}
			if(number>100){
				isc.say("公司上游客户占比不可超过百分之百！");
				return ;
			}
			number=0;
			for(var a=1;a<4;a++){
				if(productionOperation.getValue("lower"+a)){
					number+=productionOperation.getValue("lower"+a);
				}
			}
			if(number>100){
				isc.say("公司下游客户占比不可超过百分之百！");
				return ;
			}
			number=0;
			for(var a=1;a<4;a++){
				if(productionOperation.getValue("typeNumber"+a)){
					number+=productionOperation.getValue("typeNumber"+a);
				}
			}
			if(number>100){
				isc.say("主营业务构成占比不可超过百分之百！");
				return ;
			}
		}
			
		if(hidde){
			if(additionalForm.validate()){
    			//执行上一步模型计算
    			isc.FwRPCManager.post({
    		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/saveAdditionalAndNextStep",
    		       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
    		        data:additionalForm.getValues(),
    		        callback: function(response,rawData,request){
    		        	rateWindow.RatingObject = rawData.response.data[0];
    					rateMenu.refreshStep();
    					rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
    					rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
    		        }
    	    	});
    		}
		} else{
	    	isc.ask("您填列的信息将影响客户评级报告的展示效果和客户评级的准确性，是否重新填写?",function(v){
	    		if(!v){
	    			//经营情况
	    			isc.FwRPCManager.post({
	    		       	url:FrameworkUiInterface.webContextPath + "/irs/productionOperation/isc/updatePro",
	    		       	urlParameters:{
	    		       		ratingId:rateWindow.RatingObject.id,
	    		       		cust1:productionOperation.getValue("cust1") == 'undefined' || productionOperation.getValue("cust1") == null ? "":productionOperation.getValue("cust1"),
	    		       		cust2:productionOperation.getValue("cust2") == 'undefined' || productionOperation.getValue("cust2") == null ? "":productionOperation.getValue("cust2"),
	    		       		cust3:productionOperation.getValue("cust3") == 'undefined' || productionOperation.getValue("cust3") == null ? "":productionOperation.getValue("cust3"),
	    		       		
	    		       		upper1:productionOperation.getValue("upper1") == 'undefined' || productionOperation.getValue("upper1") == null ? "":productionOperation.getValue("upper1"),
	    		       		upper2:productionOperation.getValue("upper2") == 'undefined' || productionOperation.getValue("upper2") == null ? "":productionOperation.getValue("upper2"),
	    		       		upper3:productionOperation.getValue("upper3") == 'undefined' || productionOperation.getValue("upper3") == null ? "":productionOperation.getValue("upper3"),
	    		       		
	    		       		custA:productionOperation.getValue("custA") == 'undefined' || productionOperation.getValue("custA") == null ? "":productionOperation.getValue("custA"),
	    		       		custB:productionOperation.getValue("custB") == 'undefined' || productionOperation.getValue("custB") == null ? "":productionOperation.getValue("custB"),
	    		       		custC:productionOperation.getValue("custC") == 'undefined' || productionOperation.getValue("custC") == null ? "":productionOperation.getValue("custC"),
	    		       		
	    		       		lower1:productionOperation.getValue("lower1") == 'undefined' || productionOperation.getValue("lower1") == null ? "":productionOperation.getValue("lower1"),
	    		       		lower2:productionOperation.getValue("lower2") == 'undefined' || productionOperation.getValue("lower2") == null ? "":productionOperation.getValue("lower2"),
	    		       		lower3:productionOperation.getValue("lower3") == 'undefined' || productionOperation.getValue("lower3") == null ? "":productionOperation.getValue("lower3"),
	    		       		
	    		       		type1:productionOperation.getValue("type1") == 'undefined' || productionOperation.getValue("type1") == null ? "":productionOperation.getValue("type1"),
	    		       		type2:productionOperation.getValue("type2") == 'undefined' || productionOperation.getValue("type2") == null ? "":productionOperation.getValue("type2"),
	    		       		type3:productionOperation.getValue("type3") == 'undefined' || productionOperation.getValue("type3") == null ? "":productionOperation.getValue("type3"),
	    		       		
	    		       		typeNumber1:productionOperation.getValue("typeNumber1") == 'undefined' || productionOperation.getValue("typeNumber1") == null ? "":productionOperation.getValue("typeNumber1"),
	    		       		typeNumber2:productionOperation.getValue("typeNumber2") == 'undefined' || productionOperation.getValue("typeNumber2") == null ? "":productionOperation.getValue("typeNumber2"),
	    		       		typeNumber3:productionOperation.getValue("typeNumber3") == 'undefined' || productionOperation.getValue("typeNumber3") == null ? "":productionOperation.getValue("typeNumber3"),
	    		       		
	    		       	}, //传入当前步骤ID和定性选择项
	    		        callback: function(response,rawData,request){
	    		        	if(rawData==false){
	    		        		isc.say("经营情况保存失败！");
	    		        		return ;
	    		        	}
	    		        	//执行上一步模型计算
	    		        	if(additionalForm.validate()){
	    		    			//执行上一步模型计算
	    		    			isc.FwRPCManager.post({
	    		    		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/saveAdditionalAndNextStep",
	    		    		       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
	    		    		        data:additionalForm.getValues(),
	    		    		        callback: function(response,rawData,request){
	    		    		        	rateWindow.RatingObject = rawData.response.data[0];
	    		    					rateMenu.refreshStep();
	    		    					rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
	    		    					rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
	    		    		        }
	    		    	    	});
	    		    		}
	    		        	
	    		        }
	    	    	});
	    		}
	    	});
		}

		}
});

var productionOperationFields ={
		grid:[
			{width:100,		name:'cust1',                   title:'公司上游客户1' },
			{width:100,		name:'upper1',                   title:'上游客户占比1' ,type:'float' },
			{width:100,		name:'cust2',                   title:'公司上游客户2' },
			{width:100,		name:'upper2',                   title:'上游客户占比2' ,type:'float'},
			{width:100,		name:'cust3',                   title:'公司上游客户3'},
			{width:100,		name:'upper3',                   title:'上游客户占比3' ,type:'float'}
		],
		
		grid1:[
			{width:100,		name:'custA',                   title:'公司下游客户1' },
			{width:100,		name:'lower1',                   title:'下游客户占比1',type:'float' },						
			{width:100,		name:'custB',                   title:'公司下游客户2'},
			{width:100,		name:'lower2',                   title:'下游客户占比2' ,type:'float'},								
			{width:100,		name:'custC',                   title:'公司下游客户3'},
			{width:100,		name:'lower3',                   title:'下游客户占比3' ,type:'float'}							
		],
		
		grid2: [
			{width:100,		name:'id',                       title:'id' ,		hidden:true,		primaryKey:true,		canEdit:false},
			{width:100,		name:'ratingId',        	       title:'ratingId' ,		hidden:true},
			
			{width:100,		name:'type1',          title:'业务类型1' },
			{width:100,		name:'typeNumber1',   	title:'业务占比1',type:'float' },
			{width:100,		name:'type2',          title:'业务类型2' },
			{width:100,		name:'typeNumber2',     title:'业务占比2',type:'float' },
			{width:100,		name:'type3',          title:'业务类型3'},
			{width:100,		name:'typeNumber3',     title:'业务占比3',type:'float'}
			
		],
	};


isc.FwRestDataSource.create({
	ID: 'productionOperationDS',
	autoFetchData:false,
    //fields: productionOperationFields.grid,
    dataURL: FrameworkUiInterface.webContextPath +'/irs/productionOperation/isc'
});

var CusFinancialStatementsFields ={
		grid: [
				{width:100,		name:'id',                          title:'[[#{id}]]' ,		hidden:true,		primaryKey:true,		canEdit:false},
				{width:'*',		name:'reportBussDate',           	title:'报表日期'},
				{width:'*',		name:'reportSpecifications',    	title:'财报口径',type: 'select',valueMap:ReportSpecificationss,allowEmptyValue:true,autoFetchData:false},
			    {width:'*',		name:'reportCurrency',      		title:'财报币种',type: 'select',valueMap:ReportCurrencys,allowEmptyValue:true,autoFetchData:false},
			    {width:'*',		name:'reportCycle',					title:'财报周期',type: 'select',valueMap:ReportCycles,allowEmptyValue:true,autoFetchData:false},
			    {width:'*',		name:'reportSource',           		title:'财报来源'},
			    {width:'*',		name:'reportType',          		title:'财报类型',type: 'select',valueMap:ReportTypes,allowEmptyValue:true,autoFetchData:false},
			    {width:'*',		name:'vaild',           			title:'是否有效' ,				type: 'boolean'},
			    {width:'*',		name:'isAudit',           			title:'是否审计',				type: 'boolean'},
			    {width:'*',		name:'auditOrganization',           title:'设计单位',	hidden:true},
			    {width:'*',		name:'auditOpinion',           		title:'审计意见',	hidden:true},
			    {width:'*',		name:'isNullTable',           		title:'是否空表',			type: 'select',valueMap:isNullTabels,allowEmptyValue:true,autoFetchData:false},
			    {width:'*',		name:'isNullBalance',           	title:'是否平衡',			type: 'select',valueMap:isNullBalances,allowEmptyValue:true,autoFetchData:false},
			    {width:'*',		name:'checkPassRate',           	title:'MIS勾稽校验通过率',formatCellValue:formatterPercent}
//			    {width:'*',		name:'viewSubject',          	 	title:'操作'}
		]
	};



//客户财报基本信息列表
isc.FwListGrid.create({
	ID: 'cusFinancialStatementsListGrid',
	title: '<b>财报列表</b>',
	canPageable:false,
	autoFetchData :true,
	showRecordComponents: true,
    showRecordComponentsByCell: true,
	fields: CusFinancialStatementsFields.grid,
	actionMembers:[
//		'refresh',
		{
			title:'查看',
			name:'lookOver',
			click:function(){
				var record = cusFinancialStatementsListGrid.getSelectedRecord();
				if(record == null){
					isc.say("请选择要查看的财报！");
            		return false;
				}
				//查看财报详细信息
            	isc.Window.create({
            				title : "财报详细信息",
            				width : "100%",
            				height : "100%",
            				autoCenter : true,
            				isModal : true,
            				visiable : false,
            				showModalMask : true,
            				autoDraw : false,
            				headerControls : ["headerLabel", "closeButton"],
            				items : [finAccountDataListGrid]
            	}).show();
            	finAccountDataListGrid.fetchData({'financia':record.id});
				
			}
		}
	],
	changeActionMemberButtonStatus: function(record, recordList){
		
    },
	afterRemove: function(dsResponse, data, dsRequest){
		
	}
});
cusFinancialStatementsListGrid.setData(rateWindow.RatingObject.ratingCustomer.financialReports);
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
						customerVieweForm
					]
				}),
				isc.VLayout.create({
					width : "30%",
					height:'100%',
					members:[
						
						isc.FwPanel.create({
							title:"生产经营情况",
							items:[
								isc.FwDynamicForm.create({
									ID : "productionOperation",
									titleOrientation: "top",
									numCols : 2,
									width:"100%",
									height:'50%',	
									autoDraw : true,
									overflow : "auto",
									canEdit:true,
									autoFocus : false,
									dataSource:productionOperationDS,
									fields : [
										{ defaultValue:"上游供应商业务占比情况", type:"section", sectionExpanded:true,
							                itemIds: ["cust1", "upper1", "cust2", "upper2","cust3", "upper3"], width:220, hidden:hidde
							            },
							            {width:110, name:"cust1", type:"text", title:"客户名称", required:true, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"upper1", type:"float", title:"占比(%)",required:true, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"cust2", type:"text",  showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"upper2", type:"float", showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"cust3", type:"text",  showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"upper3", type:"float",showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            { defaultValue:"下游客户业务占比情况", type:"section", sectionExpanded:true,
							                itemIds: ["custA", "custB", "custC", "lower1","lower2", "lower3"], width:220, hidden:hidde
							            },
							            {width:110, name:"custA", type:"text", title:"客户名称",required:true, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"lower1", type:"float", title:"占比(%)",required:true, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"custB", type:"text", showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"lower2", type:"float",  showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"custC", type:"text", showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"lower3", type:"float", showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            { defaultValue:"业务构成情况", type:"section", sectionExpanded:true,
							                itemIds: ["type1", "type2", "type3", "typeNumber1","typeNumber2", "typeNumber3"], width:220, hidden:hidde
							            },
							            {width:110, name:"type1", type:"text", title:"行业",required:true, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"typeNumber1", type:"float", title:"占比(%)",required:true, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"type2", type:"text", showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"typeNumber2", type:"float",  showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"type3", type:"text", showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde},
							            {width:110, name:"typeNumber3", type:"float", showTitle:false, canEdit : rateWindow.levelFlag, hidden:hidde}
									]
								})
								]
						}),
						isc.FwPanel.create({
							title:"补录信息",
							items:[
								isc.FwDynamicForm.create({
									ID : "additionalForm",
									numCols : 1,
									width:"100%",
									height:'50%',
									autoDraw : true,
									overflow : "auto",
									titleOrientation: "top",
									canEdit:true,
									autoFocus : false,
									fields : rateWindow.RatingObject.currentStep.formfields
								})
							]
						})
					]
				})
			]
		}),
		isc.HLayout.create({
			width : "100%",
			height:'30%',
			members:[cusFinancialStatementsListGrid]
		}),
		isc.HLayout.create({
			ID : "btnHL",
		    autoDraw : false,
		    hidden:false,
		    width:"100%",
		    membersMargin: 10,
		    align: "center",
		    height : "10%",
			members:[nextBtn]
		}),
		]
});
		
productionOperation.fetchData({'ratingId.id':rateWindow.RatingObject.id});

if(!rateWindow.editable){
	btnHL.hide();
}