//评级步骤 ——定性计算结果展示页面

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
 value=value*100;
 value=value.toFixed(2);
 return value+"%";
}

//分数展示 
isc.FwReadOnlyDynamicForm.create({
	ID : "allscoreForm",
	numCols : 2,
	width : "100%",
	height:30,
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
			title : "系统评级得分",
			value:rateWindow.RatingObject.score,
			type : "float",
			format:"0.00"
		},{
			name : "modelLevel",
			title : "系统初始等级",
			value:rateWindow.RatingObject.modelLevel,
			type : "text"
		}
	]
});

isc.FwRestDataSource.create({
	ID: 'ratingLogByResNoFinDS',
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingIndex/getRatingIndexsByStepId'  //用于指定自定义控制器方法属性
});

// 定性列表
isc.FwListGrid.create({
		ID : "nonFinGrid_Res",
		dataSource : ratingLogByResNoFinDS,
		width : "100%",
		border:0,
		height : "85%",
		canPageable:false,
		showGridSummary:true,
		showAllRecords : true,
		groupStartOpen:"all",
		groupByField: 'indexCategory',
		initialCriteria : {
			"stepId" : rateWindow.RatingObject.currentStep.lastStep
        },
		fields : [ {
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
			hidden:true,
//			hidden:!rateWindow.riskShow,
			width : 180,
			formatCellValue:formatterPercent
		}, {
			name : "indexWeight",
			title : "权重",
			width : 120,
			hidden:!rateWindow.riskShow,
			canEdit : false,
			canSave : false,
			formatCellValue:formatterPercent
//			type : "float",
//			format:"0.00"
		}, { 
			name : "indexScore",
			title : "得分",
			width : 180,
			formatCellValue:formatterDecimal
//			showGridSummary:true,
//			summaryFunction:function(records, summaryField){
//				 var sumScore = 0;
//	             for (var i = 0; i < records.length; i++) {
//	            	 sumScore += records[i].indexScore;
//	             }
//	             return "定性得分："+sumScore.toFixed(6);
//			}
		}
	]
});

if(!rateWindow.headOfficeShow){
	nonFinGrid_Res.hide();
}

isc.IButton.create({
	ID:'preBtn_NoFin_Res',
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

preBtn_NoFin_Res.hide();

isc.IButton.create({
    ID : 'nextBtn_NoFin_Res',
    title: '<b>下一步</b>',
    autoDraw : false,
    disabled : !rateWindow.editable,
    width: 80,
    click : function(){
		//执行下一步模型计算
		isc.FwRPCManager.get({
	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/nextStep",
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


isc.HLayout.create({
        ID : "btnHL",
        autoDraw : false,
        width:"100%",
        height:40,
        membersMargin: 10,
        align: "center",
        members:[preBtn_NoFin_Res,nextBtn_NoFin_Res]
});

if(!rateWindow.editable){
	btnHL.hide();
}

isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 5,
    members:[
    	nonFinGrid_Res,
        allscoreForm,
        btnHL
    ]
});
