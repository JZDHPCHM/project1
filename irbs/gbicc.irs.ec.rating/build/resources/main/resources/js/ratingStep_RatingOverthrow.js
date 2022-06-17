//评级步骤 ——签署意见
//模型标尺字段定义
	//信用级别

var ModelScaleFields ={
	grid: [
			{name:'id',						primaryKey:true,hidden:true},
			{name:'modelConfig',			hidden:true},
			{width:100,		name:'sNum',					type: 'number',						},
			{width:120,		name:'lowerLimit',				type: 'text',					    },
			{width:120,		name:'upperLimit',				type: 'text',					    },
			{width:120,		name:'pdLevel',					type: 'text',					    },
			{width:120,		name:'pdLevelSP',				type: 'text',					    },
			{width:'*',		name:'remark',					type: 'text',					    }
	]
};

//标尺数据源
isc.FwRestDataSource.create({
	ID: 'modelScaleDataSource',
    fields: ModelScaleFields.grid,
    fetchDataURL: FrameworkUiInterface.webContextPath+'/irs/ratingLevelAdjustRange/getAdjustRangeLevel?scaleId='+rateWindow.RatingObject.ratingConfig.modelScaleId+'&pdLevel='+rateWindow.RatingObject.modelLevel+'&modelCode='+rateWindow.RatingObject.modelCode+'',  			//用于指定自定义查询链接
});


// 意见说明
var adjReasonVal = "";

//获取处理人已经上传的调查报告
var lastFileName = null;
var lastFileFullPath = null;
var ratingOverturns = rateWindow.RatingObject.ratingOverturns;
// 意见历史时间降序
if(ratingOverturns){
	ratingOverturns.sort(function(a,b){
		return a.createDate < b.createDate ?1:-1
	})
}
// 展示最新客户经理提交的信息
for(var i = 0;i<ratingOverturns.length;i++){
	var tempFile = ratingOverturns[i];
	if(tempFile.roleCode== "CUSTOMER_MANAGER" && tempFile.userCode== rateWindow.RatingObject.accountManagerCode){
		if(tempFile.fileUrl){
			var lastFileFullPath= tempFile.fileUrl;
			lastFileName = lastFileFullPath.substring(lastFileFullPath.lastIndexOf("/")+1,lastFileFullPath.length);
			adjReasonVal = tempFile.adjReason;
			break;
		}
		
	}
}


//评级建议等级字段定义
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
			    {name:'operationalAction'	},
			    {name:'createDate'	}
		 ],
		 grid:[
				{widht:'*',		name:'roleName',		title:"建议角色"},
			    {widht:'*',		name:'userCode',		title:"建议人",type:"select",valueMap:users},
			    {widht:'*',		name:'orgId',			title:"机构",type:"select",valueMap:orgs},
			    {widht:'*',		name:'operationalAction',title:"操作动作"},
			    {widht:'*',		name:'suggLevel',		title:"建议级别"},
			    {widht:'*',		name:'adjReason',		title:"意见",type:"textArea"},
			    {widht:'*',		name:'createDate',		title:"提交时间"},
		 ]
}

//评级建议等级数据源定义
isc.FwRestDataSource.create({
	ID: 'RatingOverturnDS',
  	fields:RatingOverturnFields.DS,
  	dataURL: FrameworkUiInterface.webContextPath+'/irs/ratingOverturn/isc',
  	fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingOverturn/getRatingOverturnList'  //用于指定自定义控制器方法属性
});
isc.HTMLFlow.create({
	ID:'uploadIframe',
	contents:'<iframe name="uploadIframe" id="uploadIframe" style="width:0px;height:0px;border:0px;"></iframe>'
});
//评级推翻表单
isc.FwDynamicForm.create({
    ID:"overthrowForm",
    width:"100%",
    height:200,
	canSubmit:true,
	action: FrameworkUiInterface.webContextPath+ '/irs/ratingStep/fileUpload2',
	method:'POST',
	encoding:'multipart',
	target: 'uploadIframe',
	numCols:4,
	colWidths:[150,'*'],
    fields:[
			{ 
				name: "stepId",
				hidden:true,
				value:rateWindow.RatingObject.currentStep.id
			},
			{
				name: "ratingId",
				hidden:true,
				value:rateWindow.RatingObject.id
			},
			{
				name: "file", 
				title: "上传调查报告", 
				type:"upload",
				height: 50,
				endRow:true
			},
			{
				title:"历史调查报告",
				name:"downFile",
				type:"link",
				value:lastFileName,
				defaultValue:lastFileFullPath,
				target:"javascript",
				click:function(form, item){
					window.open(FrameworkUiInterface.webContextPath + "/commom/fileManager/downloadFile?filePath="+item.defaultValue+"&fileName="+item.value);
				}
			},
			{ 
		    	name: "adjReason",
		        type: "textArea",
		        value:adjReasonVal,
		        showHintInField :true,
		        required:true,
		        colSpan: "3",
		        width:'*',
		        canEdit:rateWindow.editable,
		        title: "意见说明",
		        validators:[
	                    {type:"lengthRange", min:1, max:150,errorMessage: "字数过多"}
		        ],
		        hint:"<nobr>填写的字数不能超过150</nobr>"
		    },
		    {
				width:'*', 
				name:'suggLevels',					
				title:'<b>建议历史</b>',	
				type:'editGrid',
				showIf:function(item, value, form, values){
					if(value){
						return true;
					}else{
						return false;
					}
					
				},
				editGridProperties:{
					height: 200,
					autoFetchData:false,
					dataSource: RatingOverturnDS,
					defaultFields:RatingOverturnFields.grid
				}
			}
	]
});

RatingOverturnDS.fetchData({"ratingId":rateWindow.RatingObject.id});

//评级信息表单
isc.FwReadOnlyDynamicForm.create({
	ID:"customerRateForm",
    width:"100%",
    numCols : 4,
    //colWidths :[100,150,100,'*'],
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
					title : "系统得分",
					value:rateWindow.RatingObject.score,
					type : "float",
					format:"0.00"
				},
				{
					name : "modelLevel",
					title : "系统初始等级",
					value:rateWindow.RatingObject.modelLevel
				},
				{
					name : "adjLevel",
					title : "调整项等级",
					value:rateWindow.RatingObject.adjLevel
				}
	]
});

isc.IButton.create({
	ID:'preBtn_Overthrow',
	title: '<b>上一步</b>',
	hidden:true,
	disabled : !rateWindow.editable,
	autoDraw : false,
	width: 80,
	click:function(){
		//执行上一步模型计算
		isc.FwRPCManager.get({
	       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/lastStep",
	       	urlParameters:{stepId:rateWindow.RatingObject.currentStep.id}, //传入当前步骤ID
	        callback: function(response,rawData,request){
	        	rateWindow.RatingObject = rawData.response.data[0];
	        	rateWindow.suggLevel=overthrowForm.getValue("suggLevel");
				rateMenu.refreshStep();
				rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
				rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
	        }
    	});
	}
});
preBtn_Overthrow.hide();

isc.IButton.create({
    ID : 'nextBtn_Overthrow',
    title: '<b>下一步</b>',
    autoDraw : false,
    disabled : !rateWindow.editable,
    width: 80,
    click : function(){
    	var values = overthrowForm.getValues();
    	console.info(values);
    	if(values["adjReason"] == null || values["adjReason"].length == 0){
    		isc.say("必须填写意见说明")
    		return false;
    	}
    	if(values["file"] == undefined && values["downFile"] == undefined){
    		isc.say("必须上传调查报告")
    		return false;
    	}
    	overthrowForm.submit();
    }
});

isc.HLayout.create({
            ID : "btnHL",
            autoDraw : false,
            height:40,
            width:"100%",
            membersMargin: 10,
            align: "center",
            members:[
                     	preBtn_Overthrow,
                     	nextBtn_Overthrow
            ]
});
    
if(!rateWindow.editable){
	btnHL.hide();
}

isc.HLayout.create({
	width:"100%",
	heigth:"100%",
	members:[
		isc.LayoutSpacer.create({width:"*"}),
		isc.VLayout.create({
			ID:"vl",
			width:650,
			autoDraw : false,
			heigth:"100%",
			members:[
				isc.VLayout.create({
					height:"100%",
					members:[
						 customerRateForm,
						 isc.LayoutSpacer.create({height:30}),
						 overthrowForm,
						 uploadIframe
					]
				}),
			    btnHL
			]
		}),
		isc.LayoutSpacer.create({width:"*"})
	]
});

function doFileAfter2(url){
	var values = overthrowForm.getValues();
	values["fileUrl"]=url;
	delete values["file"];
	console.info(values);
	isc.FwRPCManager.post({
       	url:FrameworkUiInterface.webContextPath + "/irs/ratingStep/saveOverturnAndnextStep",
    	urlParameters:values, 
        callback: function(response,rawData,request){
        	rateWindow.RatingObject = rawData.response.data[0];
			rateMenu.refreshStep();
			rateFrameHL.changePage(rateWindow.RatingObject.currentStep.stepResources);
			rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
        }
	});
};
