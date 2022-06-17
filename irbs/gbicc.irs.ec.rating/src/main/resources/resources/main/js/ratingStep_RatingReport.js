isc.IButton.create({
	ID:"preBtn_Fin",
	title: '<b>上一步</b>',
	autoDraw : false,
	disabled : !rateWindow.editable,
	width: 80,
	click:function(){
			//执行上一步模型计算
			isc.FwRPCManager.get({
		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingMainStep/lastStep",
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
isc.HTMLFlow.create({
	width:"100%",
    height:"85%",
	 ID : 'mainRatingReport',
	contents:'<iframe src="/irs/mainRating/report?custNo='+rateWindow.RatingObject.id+'"  width="100%" height="580"></iframe>'
}),

isc.HTMLFlow.create({
	width:"100%",
    height:"85%",
	 ID : 'detailRatingReport',
	contents:'<iframe src="/irs/mainRating/ratingSubsidiary?custNo='+rateWindow.RatingObject.id+'"  width="100%" height="580"></iframe>'
}),

isc.HTMLFlow.create({
	width:"100%",
    height:"85%",
	 ID : 'scoringDetails',
	contents:'<iframe src="/irs/mainRating/scoringDetails?custNo='+rateWindow.RatingObject.id+'"  width="100%" height="580"></iframe>'
}),

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
isc.HLayout.create({
    ID : "btnHL",
    autoDraw : false,
    width:"100%",
    height:40,
    membersMargin: 10,
    align: "center",
    members : [preBtn_Fin,closeRatingWindow]
});


//选项卡组件
isc.TabSet.create({
   ID: "rightSideTabset1",
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
	    	title:"评级报告",
	    	pane:mainRatingReport
	    },
	    {
	    	title:"评级明细",
	    	pane:detailRatingReport
	    },
	    {
	    	title:"得分明细",
	    	pane:scoringDetails
	    }
    ]
});

isc.VLayout.create({
    width:"100%",
    autoDraw : false,
    membersMargin: 10,
    height : "100%",
    members:[
    	rightSideTabset1,
        btnHL
    ]
});
