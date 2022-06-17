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
		width : 400,
		formatCellValue:function(value,row,index){
			debugger
			if(row.indexCode.indexOf("YRZB")>-1){
				return "<input name="+row.indexCode+" value='"+value+"' class='parameterValues'/>";
			}else{
				return row.indexScore;
			}
		}
	}, {
		name : "indexWeight",
		title : "权重",
		width : 400,
		formatCellValue:formatterPercentWidth,
	}, { 
		name : "indexScore",
		title : "得分",
		width : '*',
		formatCellValue:formatterDecimal
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
			name : "quanSco",
			title : "定量得分",
			type : "float",
			format:"0.00"
		}
	]
});
quanScoForm.setValues(rateWindow.RatingObject);


isc.IButton.create({
    ID : "nextBtn_Fin",
    title: '<b>下一步</b>',
    autoDraw : false,
    width: 80,
    click : function(){
    	var requestMap = {};
    	for(var i=0;i<$(".parameterValues").length;i++){
    		var classPar=$(".parameterValues")[i];
    		requestMap[classPar.name]=classPar.value;
    	}
		//执行下一步模型计算
		isc.FwRPCManager.get({
	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingMainStep/nextStep",
	       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID
	        callback: function(response,rawData,request){
	        	rateWindow.RatingObject = rawData.response.data[0];
	        	rateWindow.requestMap=requestMap;
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
        members : [nextBtn_Fin]
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



