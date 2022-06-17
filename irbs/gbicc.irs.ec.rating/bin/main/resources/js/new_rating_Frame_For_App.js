/**
 * 客户评级 框架页面（审批页面）
 */

//自定义导航栏，加入刷新步骤函数，供其他页面的上一步、下一步调用
isc.defineClass("RateStepMenu1","VStack").addProperties({
	refreshStep : function (){
		if(this.getMembers()){
			this.removeMembers(this.getMembers());
		}
		var ratingSteps = projectLoanWindow.RatingObject.steps;
		//创建评级步骤按钮
		for ( var i = 0; i < ratingSteps.length; i++) {
			if(ratingSteps[i].stepType=="QUANTITATIVE"){ //定量步骤
				projectLoanWindow.quanStepId = ratingSteps[i].id;
			}else if(ratingSteps[i].stepType=="QUALITATIVE_EDIT"){//定性步骤
				projectLoanWindow.qualStepId = ratingSteps[i].id;
			}
			var setpUrl = ratingSteps[i].stepResources;
			if("/js/ratingStep_Customer.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_Customer.js";
			}
			if("/js/ratingStep_Additional.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_Additional.js";
			}
			if("/js/ratingStep_AdjustItemEdit.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_AdjustItemEdit.js";
			}
			if("/js/ratingStep_QualitativeEdit.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_QualitativeEdit.js";
			}
			if("/js/ratingStep_QualitativeResult.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_QualitativeResult.js";
			}
			if("/js/ratingStep_QuantitativeResult.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_QuantitativeResult.js";
			}
			if("/js/ratingStep_RatingOverthrow.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_RatingOverthrow.js";
			}
			if("/js/ratingStep_RatingReport.js" == setpUrl){
				setpUrl = "/js/new_ratingStep_RatingReport.js";
			}
			rateMenu1.addMember(isc.IButton.create({
				title : ratingSteps[i].stepName,
				id : ratingSteps[i].id,
				sno : ratingSteps[i].sno,
				selected : projectLoanWindow.RatingObject.currentStep.sno == ratingSteps[i].sno,
				height:30,
				enabled:projectLoanWindow.leftBtnDis,
				showSelectedIcon :true,
				step : ratingSteps[i],
				url : setpUrl,
				click : function(){
				    this.selected = true; 
				    if(this.step.stepType == "ADDITIONAL"){ //补录步骤
				    	//获取补录步骤
			    		isc.FwRPCManager.get({
			    	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/getAdditionalStepByRatingId",
			    	       	urlParameters:{ratingId:projectLoanWindow.RatingObject.id}, //传入当前步骤ID
			    	        callback: function(response,rawData,request){
			    	        	projectLoanWindow.RatingObject.currentStep = rawData.response.data[0];
			    	        	rateSetpLoader1.setViewURL(FrameworkUiInterface.webContextPath + setpUrl);
			    	        	rightPanel1.setTitle(projectLoanWindow.RatingObject.currentStep.stepName);
			    	        }
			        	});
				    }else{
				    	 projectLoanWindow.RatingObject.currentStep = this.step;
				    	 rateSetpLoader1.setViewURL(FrameworkUiInterface.webContextPath + this.url);
				    	 rightPanel1.setTitle(projectLoanWindow.RatingObject.currentStep.stepName);
				    }
				}
			}));
		}
	}
});

//步骤导航
isc.RateStepMenu1.create({
	ID : "rateMenu1",
	autoDraw : false,
	membersMargin : 10,
	layoutAlign : "center",
	layoutMargin:10
});
//初始化导航
rateMenu1.refreshStep();

console.info(projectLoanWindow.RatingObject.currentStep.stepResources);
//右边页面加载组件
isc.ViewLoader.create({
    ID : "rateSetpLoader1",
    autoDraw : false,
    loadingMessage:"正在查询......",
    redrawOnStateChange : true,
    viewURL : FrameworkUiInterface.webContextPath + "/js/new_ratingStep_Customer.js"
});

var ModelScaleFields ={
		grid: [
				{name:'id',						primaryKey:true,hidden:true},
				{name:'modelConfig',			hidden:true},
				{width:100,		name:'sNum',					type: 'number'						},
				{width:120,		name:'lowerLimit',				type: 'text'					    },
				{width:120,		name:'upperLimit',				type: 'text'					    },
				{width:120,		name:'pdLevel',					type: 'text'					    },
				{width:120,		name:'pdLevelSP',				type: 'text'					    },
				{width:'*',		name:'remark',					type: 'text'					    }
		]
};

var MainScaleFields ={
		grid: [
				{name:'id',						primaryKey:true,hidden:true},
				{width:100,		name:'sort',					type: 'number'						},
				{width:120,		name:'scaleLevel',				type: 'text'					    }
		]
};

//标尺数据源
isc.FwRestDataSource.create({
	ID: 'modelScaleDataSource1',
	fields: MainScaleFields.grid,
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/companyRating/getMainScaleLevel?type=010&pdLevel='+projectLoanWindow.RatingObject.modelLevel,
});

//评级建议等级历史字段定义
var RatingOverturnFields = {
		 DS: [
				{name:'id',			primaryKey:true},
				{name:'ratingMain'	},
				{name:'roleName'	},
				{name:'roleCode'	},
				{name:'userCode'	},
			    {name:'userName'	},
			    {name:'suggLevel'	},
			    {name:'adjReason'	},
			    {name:'orgId'	},
			    {name:'operationalAction'},
			    {name:'createDate'	},
			    {name:'fileUrl'	}
			    
		 ],
		 grid:[
				{width:120,		name:'roleName',		title:"建议角色"},
			    {width:120,		name:'userCode',		title:"建议人",type:"select",valueMap:users},
			    {width:150,		name:'orgId',			title:"机构",type:"select",valueMap:orgs},
			    {width:120,		name:'operationalAction',title:"操作动作"},
			    {width:100,		name:'suggLevel',		title:"建议级别"},
			    {width:140,		name:'createDate',		title:"提交时间"},
			    {width:200,		name:'adjReason',		title:"意见",type:"textArea"},
			    {width:300,		name:'fileUrl',			title:"文件路径"},
		 ]
}

//评级建议等级历史数据源定义
isc.FwRestDataSource.create({
	ID: 'RatingOverturnDS1',
  	fields:RatingOverturnFields.DS,
  	dataURL: FrameworkUiInterface.webContextPath+'/irs/ratingOverturn/isc',
  	fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingOverturn/getRatingOverturnListOpinion?ratingId='+projectLoanWindow.RatingObject.id  //用于指定自定义控制器方法属性
});

//意见说明
var adjReasonVal = "";

//获取处理人已经上传的调查报告
var lastFileName = null;
var lastFileFullPath = null;
var ratingOverturns = projectLoanWindow.RatingObject.ratingOverturns;
for(var i = 0;i<ratingOverturns.length;i++){
	var tempFile = ratingOverturns[i];
	if(tempFile.roleCode== "CUSTOMER_MANAGER" && tempFile.userCode== projectLoanWindow.RatingObject.accountManagerCode){
		if(tempFile.fileUrl){
			var lastFileFullPath= tempFile.fileUrl;
			lastFileName = lastFileFullPath.substring(lastFileFullPath.lastIndexOf("/")+1,lastFileFullPath.length);
		}
		adjReasonVal = tempFile.adjReason;
	}
}



//评级步骤
isc.FwPanel.create({
	ID:"leftSideLayout1",
	title:"评级步骤",
	width:120,
	items:[rateMenu1]
});
//评级步骤ViewLoder面板
isc.FwPanel.create({
	ID:"rightPanel1",
	width:'100%',
	title:"客户信息",
	items:[rateSetpLoader1]
});




//水平布局 内容是 左边步骤列表和右边评级界面
isc.HLayout.create({
    ID : "rateFrameHL1",
    width : "100%",
    height:"100%",
    layoutMargin : 20,
    membersMargin : 20,
    layoutBottomMargin :0,
    changePage:function(url){
        rateSetpLoader1.setViewURL(FrameworkUiInterface.webContextPath + url);
    },
    members : [ leftSideLayout1, rightPanel1]
});

//垂直布局  整体内容与审批按钮
isc.VLayout.create({
    ID : "approveRateFrameHL1",
    width : "100%",
    members : [
    	rateFrameHL1,
    	isc.HLayout.create({
    		width:'100%',
    		layoutTopMargin:15,
    		layoutBottomMargin:10,
    		members:[
    			isc.LayoutSpacer.create({width:"100%"}),  //间距
    			isc.UserWorkflowAction.create({
    				ID:"ApprovalButton1",
    				taskId:projectLoanWindow.taskId,
    				comPanyId:projectLoanWindow.RatingObject.id,
    	    		actionUrl:FrameworkUiInterface.webContextPath+"/irs/companyRating/completeRatingTask?ratingId="+projectLoanWindow.RatingObject.id,
    	    		getData: function(){
    	    			return {
    	    	        	transientVariables	: {
    	    	        		taskId:projectLoanWindow.RatingObject.taskId,
    	    	        		ratingId:projectLoanWindow.RatingObject.id,
    	    	        		first_task_assignee:userAccout,
    	    	        	},
    	    	        	autoCompleteFirstTask			: true
    	    			}
    	    		},
    	    		doSubmit:function(){
    	    	    	// 调用流程跳转方法
    	    			this.Super("doSubmit", arguments);
    	    		},
    	    		successCallback: function(){
    	    			ratingTaskListGrid.refresh();
    	        		projectLoanWindow.close();
    	    		}
    	    	}),
    	    	isc.LayoutSpacer.create({width:"100%"})  //间距
    		]
    	})
    ]
});



