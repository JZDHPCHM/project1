/**
 * 客户评级 框架页面（审批页面）
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
				selected : rateWindow.RatingObject.currentStep.sno == ratingSteps[i].sno,
				height:30,
				enabled:rateWindow.leftBtnDis,
				showSelectedIcon :true,
				step : ratingSteps[i],
				url : ratingSteps[i].stepResources,
				click : function(){
				    this.selected = true; 
				    	 rateWindow.RatingObject.currentStep = this.step;
				    	 rateSetpLoader.setViewURL(FrameworkUiInterface.webContextPath + this.url);
				    	 rightPanel.setTitle(rateWindow.RatingObject.currentStep.stepName);
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
	layoutAlign : "center",
	layoutMargin:10
});
//初始化导航
rateMenu.refreshStep();

//右边页面加载组件
isc.ViewLoader.create({
    ID : "rateSetpLoader",
    autoDraw : false,
    loadingMessage:"正在查询......",
    redrawOnStateChange : true,
    viewURL : FrameworkUiInterface.webContextPath + rateWindow.RatingObject.currentStep.stepResources
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
	ID: 'modelScaleDataSource',
	fields: MainScaleFields.grid,
    fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/companyRating/getMainScaleLevel?type=010&pdLevel='+rateWindow.RatingObject.modelLevel,
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
	ID: 'RatingOverturnDS',
  	fields:RatingOverturnFields.DS,
  	dataURL: FrameworkUiInterface.webContextPath+'/irs/ratingOverturn/isc',
  	fetchDataURL: FrameworkUiInterface.webContextPath + '/irs/ratingOverturn/getRatingOverturnListOpinion?ratingId='+rateWindow.RatingObject.id  //用于指定自定义控制器方法属性
});

//意见说明
var adjReasonVal = "";

//获取处理人已经上传的调查报告
var lastFileName = null;
var lastFileFullPath = null;
var ratingOverturns = rateWindow.RatingObject.ratingOverturns;
for(var i = 0;i<ratingOverturns.length;i++){
	var tempFile = ratingOverturns[i];
	if(tempFile.roleCode== "CUSTOMER_MANAGER" && tempFile.userCode== rateWindow.RatingObject.accountManagerCode){
		if(tempFile.fileUrl){
			var lastFileFullPath= tempFile.fileUrl;
			lastFileName = lastFileFullPath.substring(lastFileFullPath.lastIndexOf("/")+1,lastFileFullPath.length);
		}
		adjReasonVal = tempFile.adjReason;
	}
}

// 保存建议等级
function doFileAfter(url){
	var values = approvalOpinionForm.getValues();
	values["fileUrl"]=url;
	delete values["file"];
	isc.FwRPCManager.post({
       	url:FrameworkUiInterface.webContextPath + "/irs/companyRating/saveRatingOverturn",
    	urlParameters:values, 
        callback: function(response,rawData,request){
        	
        }
	});
}


isc.FwDynamicForm.create({
	ID : "approvalOpinionForm",
	canSubmit:true,
	action: FrameworkUiInterface.webContextPath + '/irs/ratingStep/fileUpload',
	method:'POST',
	encoding:'multipart',
	target: 'approvalRatinguploadIframe',
	fields : [
				{
					name : "suggestionGrade",
					title : "人工调整等级",
					editorType: "SelectItem", 
					optionDataSource : modelScaleDataSource,
					displayField : "scaleLevel",
					valueField : "scaleLevel",
					value:rateWindow.defaultLevel,
					defaultValue :rateWindow.defaultLevel,
					pickListFields: [
			              { name:"scaleLevel",title:"信用等级",width:100}
			        ],
			     	required : true,
			     	canEdit : rateWindow.levelFlag,
			     	changed : function(form,item, value) {
						setTimeout(function(){
							drawFXP();
						},500);
			     		// 实时展示按钮
						var forms = this.form;
						if(value != null){
							ApprovalButton.buildMembers();
							ApprovalButton.reloadButton(value);
						}
					}
				},
	          	{
	          		name:'approvalOpinion',   
	          		title:"意见说明",
	          		type:'textArea',
	          		defaultValue:"同意",
	          		width:'*',	
	          		required : true,
	          		validators:[{type:'lengthRange',max:500}],
	          		canEdit : rateWindow.levelFlag
	          	},
				{
					name: "file", 
					title: "上传报告", 
					type:"upload",
					height: 50,
					endRow:true,
					canEdit : rateWindow.levelFlag
				},
				{
					name: "ratingId",
					hidden:true,
					value:rateWindow.RatingObject.id
				}
	]
});

//当前角色为为分发员和秘书，则无需填写调整等级
if(roleCode == "HEAD_OFFICE_DISTRIBUTOR" || roleCode == "CREDIT_COMMITTEE_COMPREHENSIVE_SECRETARY"){
	approvalOpinionForm.getItem("suggestionGrade").hide();
}

//评级步骤
isc.FwPanel.create({
	ID:"leftSideLayout",
	title:"评级步骤",
	width:120,
	items:[rateMenu]
});
//评级步骤ViewLoder面板
isc.FwPanel.create({
	ID:"rightPanel",
	width:'100%',
	title:rateWindow.RatingObject.currentStep.stepName,
	items:[rateSetpLoader]
});


//评级得分表单
isc.FwReadOnlyDynamicForm.create({
	ID : "rateScoreInfoViewForm1",
	width : "100%",
	numCols : 2,
	height:20,
	colWidths : [ 100, 100, 120, 100],
	autoDraw : false,
	titleAlign:"left",
	fields : [
				{
					name : "qualitativeScore",
					title : "定性得分",
					value:rateWindow.ProjectLoanRating.qualitativeScore,
					type : "float",
					format:"0.00"
				},
				{
					name : "score",
					title : "系统得分",
					value:rateWindow.ProjectLoanRating.score,
					type : "float",
					format:"0.00"
				},
				{
					name : "modelLevel",
					title : "系统初始等级",
					value:rateWindow.ProjectLoanRating.modelLevel
				},
				{
					name : "adjLevel",
					title : "调整项等级",
					value:rateWindow.ProjectLoanRating.adjLevel
				}
	]
});


//审批信息面板
isc.FwPanel.create({
	ID:"approvePanel",
	width:350,
	title:"审批信息",
	items:[
		isc.VLayout.create({
		    autoDraw : false,
		    width:"100%",
		    height:"100%",
		    members : [
        		    	isc.FwListGrid.create({
        		    		ID:"overturnHisList",
        		    		title: '<b>意见历史列表</b>',
        		    		height:190,
        		    		canPageable:false,
        		    		dataSource: RatingOverturnDS,
        		    		fields: RatingOverturnFields.grid,
        		    		rowDoubleClick: function(record, recordNum,fieldNum){
        		    			if(record && record.fileUrl){
        		    				var urlName = record.fileUrl.substring(record.fileUrl.indexOf("upload")+6);
        		    				window.open(FrameworkUiInterface.webContextPath + "/commom/fileManager/downloadFile?filePath="+record.fileUrl+"&fileName="+urlName);
        		    			}
        		    		}
        		    	}),
        		    	isc.HTMLFlow.create({
        		    		ID:'approvalRatinguploadIframe',
        		    		contents:'<iframe name="approvalRatinguploadIframe" id="approvalRatinguploadIframe" style="width:0px;height:0px;border:0px;"></iframe>'
        		    	}),
		    			approvalOpinionForm,
		    			isc.FwPanel.create({
		    				ID:"approvePanel1",
		    				width:340,
		    				title:"房地产-贷款项目信息",
		    				items:[
		    					rateScoreInfoViewForm1,
		    					isc.IButton.create({
		    						title : "查看详情",
		    						height:22,
		    						showSelectedIcon :true,
		    						click : function(){
		    							projectLoanWindow.RatingObject = rateWindow.ProjectLoanRating;//评级对象
//		    							projectLoanWindow.ProjectLoanRating = projectLoanWindow.RatingObject;
		    							projectLoanWindow.setTitle("客户评级 ："+projectLoanWindow.RatingObject.custName+" 模型为 ："+ModelList[projectLoanWindow.RatingObject.modelCode]);
		    							projectLoanWindow.editable=false;		//设置不可修改
		    							projectLoanWindow.leftBtnDis=true;	//左侧按钮可用 
		    							projectLoanWindow.changePage(FrameworkUiInterface.webContextPath + "/js/new_rating_Frame_For_App.js");
										projectLoanWindow.show();	
		    						}
		    					})
		    				]
		    			})
		    ]
		})
	]
});
if(!rateWindow.isProjectLoan){
	approvePanel1.hide();
}

//水平布局 内容是 左边步骤列表和右边评级界面
isc.HLayout.create({
    ID : "rateFrameHL",
    width : "100%",
    height:"100%",
    layoutMargin : 20,
    membersMargin : 20,
    layoutBottomMargin :0,
    changePage:function(url){
        rateSetpLoader.setViewURL(FrameworkUiInterface.webContextPath + url);
    },
    members : [ leftSideLayout, rightPanel,approvePanel]
});

//垂直布局  整体内容与审批按钮
isc.VLayout.create({
    ID : "approveRateFrameHL",
    width : "100%",
    members : [
    	rateFrameHL,
    	isc.HLayout.create({
    		width:'100%',
    		layoutTopMargin:15,
    		layoutBottomMargin:10,
    		members:[
    			isc.LayoutSpacer.create({width:"100%"}),  //间距
    			isc.UserWorkflowAction.create({
    				ID:"ApprovalButton",
    				taskId:rateWindow.taskId,
    				suggLevel:approvalOpinionForm.getValue("suggestionGrade"),
    				comPanyId:rateWindow.RatingObject.id,
    	    		actionUrl:FrameworkUiInterface.webContextPath+"/irs/companyRating/completeRatingTask?ratingId="+rateWindow.RatingObject.id,
    	    		getData: function(){
    	    			return {
    	    	        	transientVariables	: {
    	    	        		taskId:rateWindow.RatingObject.taskId,
    	    	        		ratingId:rateWindow.RatingObject.id,
    	    	        		first_task_assignee:userAccout,
    	    	        		suggestionGrade:approvalOpinionForm.getValue("suggestionGrade"),
    	    	        		approvalOpinion:approvalOpinionForm.getValue("approvalOpinion")
    	    	        	},
    	    	        	autoCompleteFirstTask			: true
    	    			}
    	    		},
    	    		doSubmit:function(){
    	    			// 保存操作动作，保存建议等级时使用
    	    			approvalOpinionForm.values.goback = this.transientVariables.goback; 
    	    			var values = approvalOpinionForm.getValues();
    	    	    	if(values["approvalOpinion"] == null || values["approvalOpinion"].length == 0){
    	    	    		isc.say("必须填写意见说明")
    	    	    		return false;
    	    	    	}
    	    	    	if((values["suggestionGrade"] == null || values["suggestionGrade"].length == 0) 
    	    	    			&& !(roleCode == "HEAD_OFFICE_DISTRIBUTOR" || roleCode == "CREDIT_COMMITTEE_COMPREHENSIVE_SECRETARY")){
    	    	    		isc.say("必须填写调整等级")
    	    	    		return false;
    	    	    	}
    	    	    	// 调用流程跳转方法
    	    			this.Super("doSubmit", arguments);
    	    		},
    	    		successCallback: function(){
    	    			// 保存建议等级
    	    	    	approvalOpinionForm.submit();
    	    			isc.say('操作成功！');
    	    			ratingTaskListGrid.refresh();
    	        		rateWindow.close();
    	    		}
    	    	}),
    	    	isc.LayoutSpacer.create({width:"100%"})  //间距
    		]
    	})
    ]
});

// 发起页面屏蔽，审批页面的按钮
if(!rateWindow.levelFlag){
	ApprovalButton.hide();
}else{
	ApprovalButton.show();
}



function drawFXP(){
	isc.FwRPCManager.post({
       	url:FrameworkUiInterface.webContextPath + "/commom/index/custScore2",
       	urlParameters:{ratingId: rateWindow.RatingObject.id, custNo:rateWindow.RatingObject.ratingCustomer.custNo}, //传入当前步骤ID和定性选择项
        data:{},
        callback: function(response,rawData,request){
        	$(document).ready(function(){
        		FXPJData = rawData;
        		drawPanel(rawData);
        	});
        }
	});
}
function drawPanel(data){
var html = "<span style='font-size: 16px;font-weight: bold;margin-left: 35%;'>风险关注点</span>";
for(var i=0; i<data.risk.length;i++){
	html+='<div class="ibox-txt">'+data.risk[i]+'</div>';
}
$("#fxContent").html(html);
var r= rateWindow.RatingObject.processStatus!= '010'?70:128;
var DLIndexOption ;
if(data.dl.indexNames.length!=0)
	DLIndexOption= 
{		
		title:{
			text:'定量分析',
			left: '40%'},
		tooltip: {
			 position: function(point, params, dom, rect, size){
			        //其中point为当前鼠标的位置，size中有两个属性：viewSize和contentSize，分别为外层div和tooltip提示框的大小
			        var x = point[0];//
			        var y = point[1];
			        var viewWidth = size.viewSize[0];
			        var viewHeight = size.viewSize[1];
			        var boxWidth = size.contentSize[0];
			        var boxHeight = size.contentSize[1];
			        var posX = 0;//x坐标位置
			        var posY = 0;//y坐标位置
			        
			        if(x<boxWidth){//左边放不开
			            posX = 5;    
			        }else{//左边放的下
			            posX = x-boxWidth; 
			        }
			        
			        if(y<boxHeight){//上边放不开
			            posY = 5; 
			        }else{//上边放得下
			            posY = y-boxHeight;
			        }
			        
			        return [posX,posY];
			 
			 }
		},
		legend: {
			data: ['目标企业', '行业中位值'],
			bottom: 0
		},
		radar: {
			name: {
				textStyle: {
					color: '#333',
					fontSize:10
			   }
			},
			indicator: splitTitle(data.dl),
			radius: r
		},
		series: [{
			name: '',
			type: 'radar',
			data : [ {
				value : data.dl.custScore,
				name : '目标企业'
			}, {
				value : data.dl.insScore,
				name : '行业中位值'
			} ]
		}]
	};
else
	DLIndexOption = {};

var DLIndex = echarts.init(document.getElementById('DLIndex'),
		'light');
DLIndex.setOption(DLIndexOption,true);

var DXIndexOption ;
if(data.dx.indexNames.length !=0)
 DXIndexOption = {
		title:{
			text:'定性分析',
			left: '40%'},
	tooltip: {
		 position: function(point, params, dom, rect, size){
		        //其中point为当前鼠标的位置，size中有两个属性：viewSize和contentSize，分别为外层div和tooltip提示框的大小
		        var x = point[0];//
		        var y = point[1];
		        var viewWidth = size.viewSize[0];
		        var viewHeight = size.viewSize[1];
		        var boxWidth = size.contentSize[0];
		        var boxHeight = size.contentSize[1];
		        var posX = 0;//x坐标位置
		        var posY = 0;//y坐标位置
		        
		        if(x<boxWidth){//左边放不开
		            posX = 5;    
		        }else{//左边放的下
		            posX = x-boxWidth; 
		        }
		        
		        if(y<boxHeight){//上边放不开
		            posY = 5; 
		        }else{//上边放得下
		            posY = y-boxHeight;
		        }
		        
		        return [posX,posY];
		 
		 }
	},
	legend: {
		data: ['目标企业', '行业均值'],
		bottom: 0
	},
	radar: {
		name: {
			textStyle: {
				color: '#333',
				fontSize:10
		   }
		},
		indicator: splitTitle(data.dx),
		radius: r
	},
	series: [{
		name: '',
		type: 'radar',
		data : [ {
			value : data.dx.custScore,
			name : '目标企业'
		}, {
			value : data.dx.insScore,
			name : '行业均值'
		} ]
	}]
};
else
	DXIndexOption = {};
var DXIndex = echarts.init(document.getElementById('DXIndex'),
		'light');
DXIndex.setOption(DXIndexOption,true);
}

function splitTitle(data){
	if(data.length==0)
		return [""];
	var arr=[];
	var mx=0;
	for(var i=0;i<data.indexNames.length;i++){
		var obj = {};
		var tmp;
		var t = data.indexNames[i];
		var name ='';
		while(t.name.length>4){
			name+= t.name.substring(0,4)+"\n";
			t.name = t.name.substring(4);
		}
		name +=t.name;
		var a = typeof data.custScore[i]=='undefined'?0:Number(data.custScore[i]);
		var b = typeof data.insScore[i]=='undefined'?0:Number(data.insScore[i]);
		tmp = a>b?a:b;
		mx = mx > tmp?mx:tmp;
		obj={"name":name,"max":tmp};
		arr.push(obj);
	}
	for(var i=0;i<arr.length;i++){
		arr[i].max = mx+1;
	}
	return arr;
}
