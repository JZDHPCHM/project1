/**
 * 客户评级 框架页面
 */

//自定义导航栏，加入刷新步骤函数，供其他页面的上一步、下一步调用
isc.defineClass("RateStepMenu","VStack").addProperties({
	refreshStep : function (){
		if(this.getMembers()){
			this.removeMembers(this.getMembers());
		}
		var ratingSteps = rateWindow.RatingObject.steps;
		//创建评级步骤按钮
		for ( var i = 0; i < ratingSteps.length; i++) {
			if(ratingSteps[i].stepType=="QUANTITATIVE"){ //定量步骤
				rateWindow.quanStepId = ratingSteps[i].id;
			}else if(ratingSteps[i].stepType=="QUALITATIVE_EDIT"){//定性步骤
				rateWindow.qualStepId = ratingSteps[i].id;
			}
			rateMenu.addMember(isc.IButton.create({
				title : ratingSteps[i].stepName,
				id : ratingSteps[i].id,
				sno : ratingSteps[i].sno,
				enabled : rateWindow.leftBtnDis || (rateWindow.RatingObject.currentStep.sno == ratingSteps[i].sno),
				height:30,
				//enabled:rateWindow.leftBtnDis,
				showSelectedIcon :true,
				step : ratingSteps[i],
				url : ratingSteps[i].stepResources,
				click : function(){
				    this.selected = true; 
				    if(this.step.stepType == "ADDITIONAL"){ //补录步骤
				    	//获取补录步骤
			    		isc.FwRPCManager.get({
			    	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/getAdditionalStepByRatingId",
			    	       	urlParameters:{ratingId:rateWindow.RatingObject.id}, //传入当前步骤ID
			    	        callback: function(response,rawData,request){
			    	        	rateWindow.RatingObject.currentStep = rawData.response.data[0];
			    	        	rateSetpLoader.setViewURL(FrameworkUiInterface.webContextPath + rateWindow.RatingObject.currentStep.stepResources);
			    	        }
			        	});
				    }else{
				    	 rateWindow.RatingObject.currentStep = this.step;
				    	 rateSetpLoader.setViewURL(FrameworkUiInterface.webContextPath + this.url);
				    }
				}
			}));
		}
	}
});

//步骤导航
isc.RateStepMenu.create({
	ID : "rateMenu",
	autoDraw : false,
	membersMargin : 10,
	align:"align",
	layoutAlign : "align",
	layoutMargin:16
});
//初始化导航
rateMenu.refreshStep();

//右边页面加载组件
isc.ViewLoader.create({
    ID : "rateSetpLoader",
    autoDraw : false,
    loadingMessage:"正在查询......",
    contentsType: 'page',
    redrawOnStateChange : true,
    viewURL : FrameworkUiInterface.webContextPath + rateWindow.RatingObject.currentStep.stepResources
});

isc.FwPanel.create({
	ID:"leftSideLayout",
	title:"评级步骤",
	width:120,
	items:[rateMenu]
});

isc.FwPanel.create({
	ID:"rightPanel",
	title:rateWindow.RatingObject.currentStep.stepName,
	bodyDefaults:{
		padding:0
	},
	items:[rateSetpLoader]
});

//水平布局 内容是 左边步骤列表和右边评级界面
isc.HLayout.create({
    ID : "rateFrameHL",
    width : "100%",
    layoutMargin : 20,
    membersMargin : 20,
    changePage:function(url){
        rateSetpLoader.setViewURL(FrameworkUiInterface.webContextPath + url);
    },
    members : [ leftSideLayout, rightPanel ]
});
