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
	ID : "allscoreForm1",
	numCols : 2,
	width : "100%",
	height:30,
	titleAlign:"left",
	fields : [ 
	/*	{
			name : "quantifyScore",
			title : "定量得分",
			value:projectLoanWindow.RatingObject.quantifyScore,
			type : "float",
			format:"0.00"
		},*/
		{
			name : "qualitativeScore",
			title : "定性得分",
			value:projectLoanWindow.RatingObject.qualitativeScore,
			type : "float",
			format:"0.00"
		},
		{
			name : "score",
			title : "系统评级得分",
			value:projectLoanWindow.RatingObject.score,
			type : "float",
			format:"0.00"
		},{
			name : "modelLevel",
			title : "系统初始等级",
			value:projectLoanWindow.RatingObject.modelLevel,
			type : "text"
		}
	]
});

isc.FwRestDataSource.create({
	ID: 'ratingLogByResNoFinDS1',
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingIndex/getRatingIndexsByStepId'  //用于指定自定义控制器方法属性
});

// 定性列表
isc.FwListGrid.create({
		ID : "nonFinGrid_Res1",
		dataSource : ratingLogByResNoFinDS1,
		width : "100%",
		border:0,
		height : "85%",
		canPageable:false,
		showGridSummary:true,
		showAllRecords : true,
		groupStartOpen:"all",
		groupByField: 'indexCategory',
		initialCriteria : {
			"stepId" : projectLoanWindow.RatingObject.currentStep.lastStep
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
//			hidden:!projectLoanWindow.riskShow,
			width : 180,
			formatCellValue:formatterPercent
		}, {
			name : "indexWeight",
			title : "权重",
			width : 120,
			hidden:!projectLoanWindow.riskShow,
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

if(!projectLoanWindow.headOfficeShow){
	nonFinGrid_Res1.hide();
}

isc.IButton.create({
	ID:'preBtn_NoFin_Res1',
	title: '<b>上一步</b>',
	autoDraw : false,
	disabled : !projectLoanWindow.editable,
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

preBtn_NoFin_Res1.hide();

isc.IButton.create({
    ID : 'nextBtn_NoFin_Res1',
    title: '<b>下一步</b>',
    autoDraw : false,
    disabled : !projectLoanWindow.editable,
    width: 80,
    click : function(){
		//执行下一步模型计算
		isc.FwRPCManager.get({
	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/nextStep",
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


isc.HLayout.create({
        ID : "btnHL1",
        autoDraw : false,
        width:"100%",
        height:40,
        membersMargin: 10,
        align: "center",
        members:[preBtn_NoFin_Res1,nextBtn_NoFin_Res1]
});

if(!projectLoanWindow.editable){
	btnHL1.hide();
}

isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 5,
    members:[
    	nonFinGrid_Res1,
        allscoreForm1,
        btnHL1
    ]
});
