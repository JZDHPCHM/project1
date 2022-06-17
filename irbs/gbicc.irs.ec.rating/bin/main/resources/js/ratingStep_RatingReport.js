
//评级步骤 ——评级报告页面
var EnterpriseCustomerFields ={
		edit: [
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
			{width:'20%',		name:'certidNo',      		title:'登记注册证件号码'},
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

//客户信息表单
isc.FwReadOnlyDynamicForm.create({
	ID : "customerVieweForm",
	width : "100%",
	numCols : 4,
	autoDraw : false,
	colWidths : [ 120, '*', 120, '*',120,'*' ],
	fields : EnterpriseCustomerFields.edit
});

customerVieweForm.setValues(rateWindow.RatingObject.ratingCustomer);

// 评级得分表单
isc.FwReadOnlyDynamicForm.create({
	ID : "rateScoreInfoViewForm",
	width : "100%",
	numCols : 4,
	colWidths : [ 120, 100, 120, "*" ],
	autoDraw : false,
	titleAlign:"left",
	fields : [
				{
					name : "quantifyScore",
					title : "定量得分",
					value:rateWindow.RatingObject.quantifyScore,
					type : "float",
					format:"0.00"
				},
				{
					name : "qualitativeScore",
					title : "定性得分",
					value:rateWindow.RatingObject.qualitativeScore,
					type : "float",
					format:"0.00"
				},
				{
					name : "score",
					title : "系统得分",
					value:rateWindow.RatingObject.score,
					type : "float",
					format:"0.00"
				},
				{
					name : "modelLevel",
					title : "系统初始等级",
					value:rateWindow.RatingObject.modelLevel
				},
				{
					name : "adjLevel",
					title : "调整项等级",
					value:rateWindow.RatingObject.adjLevel
				}
	]
});

//指标数据源
isc.FwRestDataSource.create({
	ID: 'ratingLogReportDS',
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingIndex/getRatingIndexsByStepId'  //用于指定自定义控制器方法属性
});


var productionOperationFields ={
		grid: [
			{ defaultValue:"上游供应商业务占比情况", type:"section", sectionExpanded:true,
                itemIds: ["cust1", "upper1", "cust2", "upper2","cust3", "upper3"], width:220
            },
            {width:110, name:"cust1", type:"text", title:"客户名称", required:true},
            {width:110, name:"upper1", type:"float", title:"占比",required:true},
            {width:110, name:"cust2", type:"text",  showTitle:false},
            {width:110, name:"upper2", type:"float", showTitle:false},
            {width:110, name:"cust3", type:"text",  showTitle:false},
            {width:110, name:"upper3", type:"float",showTitle:false},
            { defaultValue:"下游客户业务占比情况", type:"section", sectionExpanded:true,
                itemIds: ["custA", "custB", "custC", "lower1","lower2", "lower3"], width:220
            },
            {width:110, name:"custA", type:"text", title:"客户名称",required:true},
            {width:110, name:"lower1", type:"float", title:"占比",required:true},
            {width:110, name:"custB", type:"text", showTitle:false},
            {width:110, name:"lower2", type:"float",  showTitle:false},
            {width:110, name:"custC", type:"text", showTitle:false},
            {width:110, name:"lower3", type:"float", showTitle:false},
            { defaultValue:"业务构成情况", type:"section", sectionExpanded:true,
                itemIds: ["type1", "type2", "type3", "typeNumber1","typeNumber2", "typeNumber3"], width:220
            },
            {width:110, name:"type1", type:"text", title:"行业",required:true},
            {width:110, name:"typeNumber1", type:"float", title:"占比",required:true},
            {width:110, name:"type2", type:"text", showTitle:false},
            {width:110, name:"typeNumber2", type:"float",  showTitle:false},
            {width:110, name:"type3", type:"text", showTitle:false},
            {width:110, name:"typeNumber3", type:"float", showTitle:false}
			
		]
	};

isc.FwRestDataSource.create({
	ID: 'productionOperationDS',
	autoFetchData:false,
    fields: productionOperationFields.grid,
    dataURL: FrameworkUiInterface.webContextPath+'/irs/productionOperation/isc'
});


isc.FwReadOnlyDynamicForm.create({
	ID : "productionOperation",
	numCols : 2,
	titleOrientation: "top",
	width:"100%",
	autoDraw : false,
	//colWidths : [ 120, '*', 120, '*',120,'*' ],
	dataSource:productionOperationDS,
	fields : productionOperationFields.grid
})
productionOperation.fetchData({'ratingId.id':rateWindow.RatingObject.id});
//定量指标列表
isc.FwListGrid.create({
	ID : "finGrid",
	title:"<b>定量指标</b>",
	autoDraw : false,
	canPageable:false,
	autoFitData :"vertical",
	autoFitMaxRecords:1000,
	groupStartOpen:"all",
	groupByField: 'indexCategory',
	dataSource : ratingLogReportDS,
	alternateRecordStyles : true,
	initialCriteria : {
		"stepId" :	rateWindow.quanStepId
    },
	width : "100%",
	fields : [ 
				{
					name : "indexCategory",
					title : "类型",
					hidden:true,
					width : 120
				},{
					name : "indexName",
					title : "名称",
					width : '*'
				}, { 
					name : "indexValue",
					title : "指标值",
					hidden:!rateWindow.riskShow,
//					hidden:true,
					width : 180,
					formatCellValue:formatterPercent
					
				}, {
					name : "indexWeight",
					title : "权重",
					width : 120,
					hidden:!rateWindow.riskShow,
					canEdit : false,
					canSave : false,
					formatCellValue:formatterPercentWidth
				}, { 
					name : "indexScore",
					title : "得分",
					width : 180,
					formatCellValue:formatterDecimal
				}
	]
});


function formatterDecimal(value,row,index){
	  if(!value){
	   value=0;
	  }
	  value=value*1;
	  value = Math.floor(value * 100) / 100
	  return value;
}

function formatterPercent(value,row,index){
	if(!value){
		value=0;
	}
 value=(value*100)/100;
 value=value.toFixed(4);
 return value;
}

function formatterPercentWidth(value,row,index){
	if(!value){
		value=0;
	}
 value=value*100;
 value=value.toFixed(2);
 return value+"%";
}

// 定性指标列表
isc.FwListGrid.create({
	ID : "nonFinGrid",
	autoDraw : false,
	canPageable:false,
	title:"<b>定性指标</b>",
	autoFitData :"vertical",
	autoFitMaxRecords:1000,
	groupStartOpen:"all",
	groupByField: 'indexCategory',
	dataSource : ratingLogReportDS,
	alternateRecordStyles : true,
	initialCriteria : {
		"stepId" :	rateWindow.qualStepId
    },
	width : "100%",
	fields : [
				{
					name : "indexCategory",
					title : "类别",
					hidden:true,
					width : 200
				},
				{
					name : "indexName",
					title : "名称",
					width : '*'
				}
//				, 
//				{
//					name : "indexValue",
//					title : "选项",
//					type: "radioGroup",		
//					width : "*",
//					formatCellValue : function(value, record, rowNum, colNum){
//					    if(value){
//					        var obj = eval("("+record.options+")");
//					        return obj[value];
//					    }
//					}
//				}
				, {
					name : "indexWeight",
					title : "权重",
					width : 120,
					hidden:!rateWindow.riskShow,
					formatCellValue:formatterPercentWidth
					
				}
				,{
					name :"indexScore",
					title:"得分",
					width : 180,
					formatCellValue:formatterDecimal
				}
	]
});


if(!rateWindow.headOfficeShow){
	finGrid.hide();
	nonFinGrid.hide();
}

// 因为微型企业和项目贷款没有定量模块
if(rateWindow.RatingObject.modelCode == "W1" || rateWindow.RatingObject.modelCode == "S3"){
	finGrid.hide();
}


//客户基本信息表单容器
isc.HStack.create({
	ID : "customerVieweFormPanel",
	autoDraw : false,
	overflow : "auto",
	members : [ customerVieweForm ]
});

//补录表单
isc.FwReadOnlyDynamicForm.create({
	ID : "additionalForm",
	numCols : 4,
	autoDraw : true,
	overflow : "auto",
	autoFocus : false,
	colWidths:[240,"*","*","*"]
});


//补录表单容器
isc.HStack.create({
	ID : "addCustomerInfoViewFormPanel",
	autoDraw : false,
	overflow : "auto",
	members : [additionalForm]
});

//选项卡组件
isc.TabSet.create({
   ID: "rightSideTabset",
   height: "90%",
   width:"100%",
   border:0,
   paneContainerProperties:{
	   border:0
   },
   tabBarProperties:{
	   border:0
   },
   tabs: [
	    {
	    	title:"客户基本信息",
	    	pane:customerVieweFormPanel
	    },
	    {
	    	title:"补录信息",
	    	pane:addCustomerInfoViewFormPanel,
	    	tabSelected:function(tabNum, tabPane, ID, tab, name){
	    		//获取补录步骤
	    		isc.FwRPCManager.get({
	    	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/getAdditionalStepByRatingId",
	    	       	urlParameters:{ratingId:rateWindow.RatingObject.id}, //传入当前步骤ID
	    	        callback: function(response,rawData,request){
	    	        	additionalForm.setFields(rawData.response.data[0].formfields);
	    	        }
	        	});
	    	}
	    },
	    {
	    	title:"评级信息",
	    	pane:isc.VLayout.create({
	    		height:"100%",
	    		overflow:"auto",
	    		members:[
	    			isc.FwPanel.create({
	    				ID:"ratingSummary",
	    				title:"摘要信息",
	    				height:130,
	    				items:[rateScoreInfoViewForm]
	    			}),
	    			isc.LayoutSpacer.create({height:20}),
	    			finGrid,
//	    			quanScoForm,
	    			isc.LayoutSpacer.create({height:20}),
	    			nonFinGrid
	    		]
	    	})
	    },
	    {
	    	title:"生产经营情况",
	    	pane:productionOperation
	    },
	    {
	    	title:"客户风险分析",
	    	pane: createFXPanel(),
	    	tabSelected:function(tabNum, tabPane, ID, tab, name){
	    		showEchartFlag = true;
	    		drawFXP();
	    	}
	    }
    ]
});
//默认展示评级指标选项卡
rightSideTabset.selectTab(2);

isc.IButton.create({
	ID:'preBtn_Report',
	title: '<b>上一步</b>',
	autoDraw : false,
	disabled : !rateWindow.editable,
	width: 80,
	click:function(){
		//执行上一步模型计算
		isc.FwRPCManager.get({
	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/lastStep",
	       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID
	        callback: function(response,rawData,request){
	        	rateWindow.RatingObject = rawData.response.data[0];
				rateMenu.refreshStep();
				rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
				rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
	        }
    	});
	}
});

//关闭按钮
isc.IButton.create({
    ID : 'closeRatingWindow',
    title: '<span class="fa fa-window-close"></span> 关闭',
    autoDraw : false,
    disabled : !rateWindow.editable,
    width: 80,
    click : function(){
    	rateWindow.close();
    }
});


//按钮容器
isc.HLayout.create({
	ID : "btnHL",
	autoDraw : false,
	height:40,
	width : "100%",
	membersMargin : 10,
	align : "center",
	members:[
		isc.LayoutSpacer.create({width:'100%'}),  //间距
		preBtn_Report,
		isc.UserWorkflowAction.create({
			ID:"process_action",
			taskId:rateWindow.RatingObject.taskId,
			suggLevel:rateWindow.suggLevel,
			comPanyId:rateWindow.RatingObject.id,
    		actionUrl:FrameworkUiInterface.webContextPath+"/irs/companyRating/completeRatingTask?ratingId="+rateWindow.RatingObject.id,
    		getData: function(){
    			return {
    	        	transientVariables	: {
    	        		taskId:rateWindow.RatingObject.taskId,
    	        		ratingId:rateWindow.RatingObject.id,
    	        		first_task_assignee:userAccout,
    	        		enterpriseScale:rateWindow.RatingObject.enterpriseScale,
    	        	},
    	        	autoCompleteFirstTask			: true
    			}
    		},
    		successCallback: function(){
    			isc.say('操作成功！');
    			companyRatingListGrid.refresh();
    			rateWindow.close();
    		}
    	}),
		closeRatingWindow,
		isc.LayoutSpacer.create({width:'100%'})  //间距
	]
});

if(rateWindow.trialEnable){
	closeRatingWindow.show();
	process_action.hide();
}else{
	closeRatingWindow.hide();
	process_action.show();
}

if(!rateWindow.editable){
	btnHL.hide();
}

isc.VLayout.create({
	ID : "VLid",
	autoDraw : false,
	width : "100%",
	height : "100%",
	membersMargin : 5,
	overflow : 'auto',
	members : [rightSideTabset,btnHL ]
});


function createFXPanel(){

	var panel = isc.HLayout.create({
		ID: "FXPanel",
		showResizeBar: false,
	    width: '100%',
	    members: [isc.HTMLPane.create({
	        width:"100%",
	        height:"100%",	
	        contents:"<div id=\"DLIndex\"style=\"width:100%;height:100%;\"></div>"
	        }),isc.HTMLPane.create({
	        width:"100%",
	        height:"100%",		
	        contents:"<div id=\"DXIndex\"style=\"width:100%;height:100%;\"></div>"
	    }),isc.HTMLPane.create({
	        width:"100%",
	        height:"100%",		
	        contents:"<div id=\"fxContent\"style=\"width:100%;height:100%;\"></div>"
	    })]     
	});
	
	return panel;
}

function drawFXP(){
		isc.FwRPCManager.post({
	       	url:FrameworkUiInterface.webContextPath + "/commom/index/custScore2",
	       	urlParameters:{ratingId: rateWindow.RatingObject.id, custNo:rateWindow.RatingObject.ratingCustomer.custNo}, //传入当前步骤ID和定性选择项
	        data:{},
	        callback: function(response,rawData,request){
	        	$(document).ready(function(){debugger;
	        		FXPJData = rawData;
	        		drawPanel(rawData);
	        	});
	        }
		});
}
function drawPanel(data){
	var html = "<span style='font-size: 16px;font-weight: bold;margin-left: 35%;'>风险关注点</span>";
	for(var i=0; i<data.risk.length;i++){
		html+='<div class="ibox-txt">'+data.risk[i]+'</div>';
	}
	$("#fxContent").html(html);
	var r= rateWindow.RatingObject.processStatus!= '010'?70:128;
	var DLIndexOption ;
	if(data.dl.indexNames.length!=0)
		DLIndexOption= 
	{		
			title:{
				text:'定量分析',
				left: '40%'},
			tooltip: {
				 position: function(point, params, dom, rect, size){
				        //其中point为当前鼠标的位置，size中有两个属性：viewSize和contentSize，分别为外层div和tooltip提示框的大小
				        var x = point[0];//
				        var y = point[1];
				        var viewWidth = size.viewSize[0];
				        var viewHeight = size.viewSize[1];
				        var boxWidth = size.contentSize[0];
				        var boxHeight = size.contentSize[1];
				        var posX = 0;//x坐标位置
				        var posY = 0;//y坐标位置
				        
				        if(x<boxWidth){//左边放不开
				            posX = 5;    
				        }else{//左边放的下
				            posX = x-boxWidth; 
				        }
				        
				        if(y<boxHeight){//上边放不开
				            posY = 5; 
				        }else{//上边放得下
				            posY = y-boxHeight;
				        }
				        
				        return [posX,posY];
				 
				 }
			},
			legend: {
				data: ['目标企业', '行业中位值'],
				bottom: 0
			},
			radar: {
				name: {
					textStyle: {
						color: '#333',
						fontSize:10
				   }
				},
				indicator: splitTitle(data.dl),
				radius: r
			},
			series: [{
				name: '',
				type: 'radar',
				data : [ {
					value : data.dl.custScore,
					name : '目标企业'
				}, {
					value : data.dl.insScore,
					name : '行业中位值'
				} ]
			}]
		};
	else
		DLIndexOption = {};

	var DLIndex = echarts.init(document.getElementById('DLIndex'),
			'light');
	DLIndex.setOption(DLIndexOption,true);
	
	var DXIndexOption ;
	if(data.dx.indexNames.length !=0)
	 DXIndexOption = {
			title:{
				text:'定性分析',
				left: '40%'},
		tooltip: {
			 position: function(point, params, dom, rect, size){
			        //其中point为当前鼠标的位置，size中有两个属性：viewSize和contentSize，分别为外层div和tooltip提示框的大小
			        var x = point[0];//
			        var y = point[1];
			        var viewWidth = size.viewSize[0];
			        var viewHeight = size.viewSize[1];
			        var boxWidth = size.contentSize[0];
			        var boxHeight = size.contentSize[1];
			        var posX = 0;//x坐标位置
			        var posY = 0;//y坐标位置
			        
			        if(x<boxWidth){//左边放不开
			            posX = 5;    
			        }else{//左边放的下
			            posX = x-boxWidth; 
			        }
			        
			        if(y<boxHeight){//上边放不开
			            posY = 5; 
			        }else{//上边放得下
			            posY = y-boxHeight;
			        }
			        
			        return [posX,posY];
			 
			 }
		},
		legend: {
			data: ['目标企业', '行业均值'],
			bottom: 0
		},
		radar: {
			name: {
				textStyle: {
					color: '#333',
					fontSize:10
			   }
			},
			indicator: splitTitle(data.dx),
			radius: r
		},
		series: [{
			name: '',
			type: 'radar',
			data : [ {
				value : data.dx.custScore,
				name : '目标企业'
			}, {
				value : data.dx.insScore,
				name : '行业均值'
			} ]
		}]
	};
	else
		DXIndexOption = {};
	var DXIndex = echarts.init(document.getElementById('DXIndex'),
			'light');
	DXIndex.setOption(DXIndexOption,true);
}


function splitTitle(data){
	if(data.length==0)
		return [""];
	var arr=[];
	var mx=0;
	for(var i=0;i<data.indexNames.length;i++){
		var obj = {};
		var tmp;
		var t = data.indexNames[i];
		var name ='';
		while(t.name.length>4){
			name+= t.name.substring(0,4)+"\n";
			t.name = t.name.substring(4);
		}
		name +=t.name;
		var a = typeof data.custScore[i]=='undefined'?0:Number(data.custScore[i]);
		var b = typeof data.insScore[i]=='undefined'?0:Number(data.insScore[i]);
		tmp = a>b?a:b;
		mx = mx > tmp?mx:tmp;
		obj={"name":name,"max":tmp};
		arr.push(obj);
	}
	for(var i=0;i<arr.length;i++){
		arr[i].max = mx+1;
	}
	return arr;
}

$(document).ready(function(){
	$(window).resize(function(){
		if(showEchartFlag){
			setTimeout(function(){
				drawFXP();
			},500);
		}
	});
});

var showEchartFlag = false;
