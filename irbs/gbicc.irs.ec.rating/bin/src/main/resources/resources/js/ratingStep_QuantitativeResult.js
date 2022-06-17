//评级步骤 ——定量结果页面

isc.FwRestDataSource.create({
	ID: 'ratingLogDS',
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingIndex/getRatingIndexsByStepId'  //用于指定自定义控制器方法属性
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


// 定量指标列表
isc.FwListGrid.create({filterButtonPrompt : '查询',
	ID : "finGrid",
	dataSource : ratingLogDS,
	width : "100%",
	border:0,
	height : "85%",
	canPageable:false,
	alternateRecordStyles : true,
	showGridSummary:true,
	showAllRecords : true,
	groupStartOpen:"all",
    groupByField:'indexCategory',
	border:0,
	width : "100%",
	height : '100%',
	initialCriteria : {
        "stepId" : rateWindow.RatingObject.currentStep.id
    },
	fields : [ {
		name : "indexCategory",
		title : "类型",
		hidden:true
	},{
		name : "indexName",
		title : "名称",
		width : 400
	}, { 
		name : "indexValue",
		title : "指标值",
		formatCellValue:formatterPercent,
		width : 400,
		hidden:!rateWindow.riskShow
	}, {
		name : "indexWeight",
		title : "权重",
		width : 400,
		formatCellValue:formatterPercentWidth,
		hidden:!rateWindow.riskShow
//		canEdit : false,
//		canSave : false,
//		type : 'float',
//		format:'0.00'
	}, { 
		name : "indexScore",
		title : "得分",
		width : '*',
		formatCellValue:formatterDecimal
//		showGridSummary:true,
//		summaryFunction:function(records, summaryField){
//			 var sumScore = 0;
//             for (var i = 0; i < records.length; i++) {
//            	 sumScore += records[i].indexScore;
//             }
//             sumScore = Math.floor(sumScore * 100) / 100;
//             return "定量得分："+sumScore.toFixed(6);
//		}
	}
]
});


//展示结果
isc.FwReadOnlyDynamicForm.create({
	ID : "quanScoForm",
	numCols : 8,
	width : "100%",
	height:30,
	titleAlign:"left",
	fields : [
		{
			name : "quantifyScore",
			title : "定量得分",
			type : "float",
			format:"0.00"
		}
	]
});
quanScoForm.setValues(rateWindow.RatingObject);

if(!rateWindow.headOfficeShow){
	finGrid.hide();
}

isc.IButton.create({
	ID:"preBtn_Fin",
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

isc.IButton.create({
    ID : "nextBtn_Fin",
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
        members : [preBtn_Fin,nextBtn_Fin]
});

if(!rateWindow.editable){
	btnHL.hide();
}

isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 10,
    height : "100%",
    members:[
    	finGrid,
    	quanScoForm,
        btnHL
    ]
});
