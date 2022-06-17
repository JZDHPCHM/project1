
//评级步骤 ——补录信息
//字段定义

isc.FwDynamicForm.create({
	ID : "additionalForm11",
	numCols : 4,
	autoDraw : true,
	overflow : "auto",
	canEdit:projectLoanWindow.editable,
	autoFocus : false,
	fields : projectLoanWindow.RatingObject.currentStep.formfields
});

isc.IButton.create({
	ID:'preBtn_Addit1',
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

isc.IButton.create({
	ID : "nextBtn_Addit111",
	title : '<b>下一步</b>',
	autoDraw : false,
	width : 80,
	click : function() {
		
		if(additionalForm1.validate()){
			//执行上一步模型计算
			isc.FwRPCManager.post({
		       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/saveAdditionalAndNextStep",
		       	urlParameters:{stepId:projectLoanWindow.RatingObject.currentStep.id}, //传入当前步骤ID和定性选择项
		        data:additionalForm1.getValues(),
		        callback: function(response,rawData,request){
		        	projectLoanWindow.RatingObject = rawData.response.data[0];
					rateMenu.refreshStep();
					rateFrameHL.changePage(projectLoanWindow.RatingObject.currentStep.stepResources);
					rightPanel.setTitle(projectLoanWindow.RatingObject.currentStep.stepName);
		        }
	    	});
		}
	}
});

isc.HLayout.create({
    ID : "btnHL1",
    autoDraw : false,
    hidden:true,
    width:"100%",
    membersMargin: 10,
    align: "center",
    height : "10%",
    members : [preBtn_Addit1,nextBtn_Addit11]
});

if(!projectLoanWindow.editable){
	btnHL1.hide();
}

isc.VLayout.create({
	width : "100%",
	heigth : '100%',
	autoDraw : false,
	membersMargin : 20,
	members : [additionalForm1,btnHL1 ]
});
