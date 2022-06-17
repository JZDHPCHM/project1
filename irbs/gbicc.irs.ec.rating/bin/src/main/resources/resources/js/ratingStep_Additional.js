
//评级步骤 ——补录信息
//字段定义

isc.FwDynamicForm.create({
	ID : "additionalForm",
	numCols : 4,
	autoDraw : true,
	overflow : "auto",
	canEdit:rateWindow.editable,
	autoFocus : false,
	fields : rateWindow.RatingObject.currentStep.formfields
});

isc.IButton.create({
	ID:'preBtn_Addit',
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
	ID : "nextBtn_Addit",
	title : '<b>下一步</b>',
	autoDraw : false,
	width : 80,
	click : function() {
		
		if(additionalForm.validate()){
			//执行上一步模型计算
			isc.FwRPCManager.post({
		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/saveAdditionalAndNextStep",
		       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
		        data:additionalForm.getValues(),
		        callback: function(response,rawData,request){
		        	rateWindow.RatingObject = rawData.response.data[0];
					rateMenu.refreshStep();
					rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
					rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
		        }
	    	});
		}
	}
});

isc.HLayout.create({
    ID : "btnHL",
    autoDraw : false,
    hidden:true,
    width:"100%",
    membersMargin: 10,
    align: "center",
    height : "10%",
    members : [preBtn_Addit,nextBtn_Addit]
});

if(!rateWindow.editable){
	btnHL.hide();
}

isc.VLayout.create({
	width : "100%",
	heigth : '100%',
	autoDraw : false,
	membersMargin : 20,
	members : [additionalForm,btnHL ]
});
