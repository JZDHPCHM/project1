//评级步骤 ——定量结果页面

isc.FwRestDataSource.create({
	ID: 'ratingLogDS1',
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
	ID : "finGrid1",
	dataSource : ratingLogDS1,
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
        "stepId" : projectLoanWindow.RatingObject.currentStep.id
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
		hidden:!projectLoanWindow.riskShow
	}, {
		name : "indexWeight",
		title : "权重",
		width : 400,
		formatCellValue:formatterPercentWidth,
		hidden:!projectLoanWindow.riskShow
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
quanScoForm.setValues(projectLoanWindow.RatingObject);

if(!projectLoanWindow.headOfficeShow){
	finGrid1.hide();
}


isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 10,
    height : "100%",
    members:[
    	finGrid1,
    	quanScoForm
    ]
});
