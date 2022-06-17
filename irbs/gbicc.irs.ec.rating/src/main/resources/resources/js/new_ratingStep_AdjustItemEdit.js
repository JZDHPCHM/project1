//评级步骤 ——调整项选择页面

//存储定性指标结果Map 
var adjustItemParamMap = new Map();

//字段定义
var RatingAdjItemFields ={
	grid: [
			{name : "id",					primaryKey:true,		canEdit : false,hidden:true},
			{name : "indexCategory",		title : "类别",			canEdit : false,hidden:true}, 
			{name : "indexName",			title : "定性指标名称",	width : 1700,canEdit : false,canSave : false}, 
			{
				name : "paramValue_title",
				title : "选项"
			}, 
			{name : "indexWeight",			title : "权重",			width : 100,hidden:true,canEdit : false,canSave : false}
		]
};

isc.FwRestDataSource.create({
	ID: 'ratingIndexByAdjItemDS1',
	fields: RatingAdjItemFields.grid,
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingIndex/getRatingIndexsByStepId'  //用于指定自定义控制器方法属性
});

isc.FwListGrid.create({
		ID : "adjustItemGrid11",
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
		dataSource : ratingIndexByAdjItemDS1,
		fields: RatingAdjItemFields.grid,
		initialCriteria : {
            "stepId" : projectLoanWindow.RatingObject.currentStep.id
        },
		getExpansionComponent : function (record) {
			var paramValueForm = isc.DynamicForm.create({
					    name: "paramValueForm",
					    width:"100%",
					    titleWidth:200,
					    fields: [
							        {
							        	showTitle:false,
							        	name:record.indexCode,
							        	type:"radioGroup",
							        	vertical:false, //水平显示选项
							        	width:500,
							        	required:true,
							        	canEdit:projectLoanWindow.editable,
							        	valueMap:eval("("+record.options+")"),
							        	value:record.indexValue,
							        	changed:function(form, item, value){
							        		adjustItemParamMap.set(item.name,[record.paramName ,value]);
							        	}
							        }
					    ]
					});
			return paramValueForm;
	    }
});

//加载数据，展开行，存储定性选项
adjustItemGrid11.fetchData(adjustItemGrid11.getCriteria(),function(optional){
	for(var i = 0 ; i<optional.data.length ; i++){
		adjustItemParamMap.set(optional.data[i].indexCode,[optional.data[i].indexName,optional.data[i].indexValue]);
	}
	adjustItemGrid11.expandRecords(optional.data);
});

isc.IButton.create({
	ID:'preBtn_AdjItem1',
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
    ID : 'nextBtn_AdjItem1',
    title: '<b>下一步</b>',
    autoDraw : false,
    width: 80,
    click : function(){
    	isc.ask("请检查填写数据的正确性，完成调整项信息填写后将无法再次修改调整项信息！",function(v){
    		if(v){
    			var requestMap = {};
    	    	//检查定性必填项
    	    	for (var [key, value] of adjustItemParamMap) {
    	    		if(!value[1]){
    	    			isc.say("请选择调整项："+value[0]);
    	    			return;
    	    		}else{
    	    			requestMap[key] = value[1];
    	    		}
    	    	}
    			//执行下一步模型计算
    			isc.FwRPCManager.post({
    		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/saveAdjustItemAndnextStep",
    		       	urlParameters:{stepId:projectLoanWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
    		        data:requestMap,
    		        callback: function(response,rawData,request){
    		        	projectLoanWindow.RatingObject = rawData.response.data[0];
    					rateMenu.refreshStep();
    					rateFrameHL.changePage(projectLoanWindow.RatingObject.currentStep.stepResources);
    					rightPanel.setTitle(projectLoanWindow.RatingObject.currentStep.stepName);
    		        }
    	    	});
    		}
    	})
    }
});

isc.HLayout.create({
        ID : "btnHL1",
        autoDraw : false,
        width:"100%",
        height:40,
        membersMargin: 10,
        align: "center",
        members:[preBtn_AdjItem1,nextBtn_AdjItem1]
});
  
if(!projectLoanWindow.editable){
	btnHL1.hide();
}

isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 20,
    members:[
    	adjustItemGrid11,
        btnHL1
    ]
});
