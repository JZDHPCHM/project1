//评级步骤 ——定性选择页面

//字段定义
var RatingLogFields ={
	grid: [
			{name : "id",					primaryKey:true,		canEdit : false,hidden:true},
			{name : "indexCategory",		title : "类别",			canEdit : false,hidden:true}, 
			{name : "indexName",			title : "定性指标名称",	width : '*',canEdit : false,canSave : false}, 
			{
				name : "paramValue_title",
				title : "选项"
			}, 
			{name : "indexWeight",			title : "权重",			width : 100,hidden:true,canEdit : false,canSave : false}
	]
};

//存储定性指标结果Map 
var noFinParamMap = new Map();

isc.FwRestDataSource.create({
	ID: 'ratingLogByNoFinDS1',
	fields: RatingLogFields.grid,
	dataURL: FrameworkUiInterface.webContextPath + '/irs/ratingLog/isc',  	   //默认增删改查的方法
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingIndex/getRatingIndexsByStepId'  //用于指定自定义控制器方法属性
});

var paramValueForm;
	
isc.FwListGrid.create({
		ID : "nonFinGrid1",
		width : "100%",
		height : "100%",
		border:0,
		groupStartOpen:"all",
	    groupByField: 'indexCategory',
	    autoFetchData : false,
		showHeader:false,
		canPageable:false,
		showRollOver:false,
		canSelectText :false,
		showSelectionCanvas :false,
		dataSource : ratingLogByNoFinDS1,
		fields: RatingLogFields.grid,
		initialCriteria : {
            "stepId" : projectLoanWindow.RatingObject.currentStep.id
        },
		getExpansionComponent : function (record) {
			paramValueForm = isc.DynamicForm.create({
					    name: "paramValueForm",
					    width:"100%",
					    titleWidth:200,
					    fields: [
							        {
							        	showTitle:false,
							        	name:record.indexCode,
							        	type:"radioGroup",
							        	width:500,
							        	required:true,
							        	canEdit:projectLoanWindow.editable,
							        	valueMap:eval("("+record.dxText+")"),
							        	value:record.indexValue,
							        	changed:function(form, item, value){
							        		noFinParamMap.set(item.name,[record.paramName ,value]);
							        	}
							        }
					    ]
					});
			return paramValueForm;
	    }
});

//加载数据，展开行，存储定性选项
nonFinGrid1.fetchData(nonFinGrid1.getCriteria(),function(optional){
	for(var i = 0 ; i<optional.data.length ; i++){
		noFinParamMap.set(optional.data[i].indexCode,[optional.data[i].indexName,optional.data[i].indexValue]);
	}
	nonFinGrid1.expandRecords(optional.data);
});

isc.IButton.create({
	ID:'ratingEstimateBtn1',
	title: '<b>评级测算</b>',
	autoDraw : false,
	width: 80,
	click:function(){
		var requestMap = {};
    	//检查定性必填项
    	for (var [key, value] of noFinParamMap) {
    		if(!value[1]){
    			isc.say("请选择定性指标："+value[0]);
    			return;
    		}else{
    			requestMap[key] = value[1];
    		}
    	}
    	// 开始测算
    	isc.FwRPCManager.post({
	       	url:FrameworkUiInterface.webContextPath + "/irs/companyRating/startTrial",
	       	urlParameters:{stepId:projectLoanWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
	        data:requestMap,
	        callback: function(response,rawData,request){
	        	if(!rawData.response.data[0]){
	        		isc.say("已经超过试算次数，不允许继续发起试算");
	        		return;
	        	}else{
	        		projectLoanWindow.trialObject = rawData.response.data[0];
		        	trialResultForm1.show();
		        	trialResultForm1.setValues(projectLoanWindow.trialObject);
		        	isc.say("试算成功！");
	        	}
	        }
    	});
	}
});

//展示试算结果
isc.FwReadOnlyDynamicForm.create({
	ID : "trialResultForm1",
	numCols : 8,
	width : "100%",
	height:30,
	titleAlign:"left",
	fields : [ 
		{
			name : "quanSco",
			title : "试算定量得分",
			type : "float",
			format:"0.00"
		},
		{
			name : "qualSco",
			title : "试算定性得分",
			type : "float",
			format:"0.00"
		},
		{
			name : "overall_ratings",
			title : "试算综合得分",
			type : "float",
			format:"0.00"
		},{
			name : "level",
			title : "试算等级",
			type : "text"
		}
	]
});

// 初始化隐藏、点击试算展示
trialResultForm1.hide();
// 大于试算次数则隐藏试算按钮
if(projectLoanWindow.RatingObject.processStatus == "010" && projectLoanWindow.trialObject.trialCount >= ratingTrialCount){
	ratingEstimateBtn1.hide();
}

isc.IButton.create({
	ID:'preBtn_NoFin1',
	title: '<b>上一步</b>',
	autoDraw : false,
	width: 80,
	click:function(){
		//执行上一步模型计算
		isc.FwRPCManager.get({
	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/lastStep",
	       	urlParameters:{stepId:projectLoanWindow.RatingObject.currentStep.id}, //传入当前步骤ID
	        callback: function(response,rawData,request){
	        	projectLoanWindow.RatingObject = rawData.response.data[0];
				rateMenu.refreshStep();
				rateFrameHL.changePage(projectLoanWindow.RatingObject.currentStep.stepResources);
				rightPanel.setTitle(projectLoanWindow.RatingObject.currentStep.stepName);
	        }
    	});
	}
});

isc.IButton.create({
    ID : 'nextBtn_NoFin1',
    title: '<b>下一步</b>',
    autoDraw : false,
    width: 80,
    click : function(){
    	var requestMap = {};
    	//检查定性必填项
    	for (var [key, value] of noFinParamMap) {
    		if(!value[1]){
    			isc.say("请选择定性指标："+value[0]);
    			return;
    		}else{
    			requestMap[key] = value[1];
    		}
    	}
    	isc.ask("请检查您填写数据的准确性，定性信息填写确认后将无法再次修改补录信息和定性信息！是否确认所填信息无误？",function(v){
    		if(v){
    			//执行下一步模型计算
    			isc.FwRPCManager.post({
    		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/checkQualitative",
    		       	urlParameters:{stepId:projectLoanWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
    		        data:requestMap,
    		        callback: function(response,rawData,request){
    		        	projectLoanWindow.RatingObject = rawData.response.data[0];
    		        	// 试算定量得分
    		        	var quantifyScore = rawData.response.data[0].quantifyScore * 1.5;
    		        	// 试算定性得分
    		        	var qualitativeScore = rawData.response.data[0].qualitativeScore;
    		        	// 模型编号
    		        	var modelCode = rawData.response.data[0].modelCode;
    		        	if(modelCode != "W1" && modelCode != "S3" && qualitativeScore > quantifyScore){
    		        		isc.ask("定性评价信息能否准确客观地录入，对客户评级最终结果有很大影响。是否需要重新进行定性评价？",function(v){
    		            		if(v){
    		            			return;
    		            		}else{
    		            			// 流程跳转
    		            			saveQualitativeAndNextStep(requestMap);
    		            		}
    		        		});
    		        	}else{
    		        		saveQualitativeAndNextStep(requestMap);
    		        	}
    		        	
    		        }
    	    	});
    		}
    	});
    }
});


function saveQualitativeAndNextStep(requestMap){
	// 流程跳转
	isc.FwRPCManager.post({
       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/saveQualitativeAndNextStep",
       	urlParameters:{stepId:projectLoanWindow.RatingObject.currentStep.id},
        data:requestMap,
        callback: function(response,rawData,request){
        	projectLoanWindow.RatingObject = rawData.response.data[0];
        	rateMenu.refreshStep();
			rateFrameHL.changePage(projectLoanWindow.RatingObject.currentStep.stepResources);
			rightPanel.setTitle(projectLoanWindow.RatingObject.currentStep.stepName);
        }
    });
}

// 是否开启评级测算（系统参数配置以及当前评级状态不能是退回重审）
if(!ratingTrialEnable && projectLoanWindow.RatingObject.processStatus == "030"){
	ratingEstimateBtn1.hide();
}

isc.HLayout.create({
        ID : "btnHL1",
        autoDraw : false,
        width:"100%",
        height:40,
        membersMargin: 10,
        align: "center",
        members:[ratingEstimateBtn1,preBtn_NoFin1,nextBtn_NoFin1]
});
  
if(!projectLoanWindow.editable){
	btnHL1.hide();
}

isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 20,
    members:[
    	nonFinGrid1,
    	isc.HLayout.create({
			width : "100%",
			height:'10%',
			members:[
				trialResultForm1
			]
		}),
        btnHL1
    ]
});
