
//模型列表(从模型系统获取)
var ModelList = [(${@frSystemDictionaryService.getDictionaryMapJsonString('MODEL_TYPE',#locale)})];
//选择路径状态
var selectorStatus = [(${@frJsonObjectMapper.enumerate(T(gbicc.irs.ec.rating.support.CommonVersionStatus),#locale)})];  
//运行模式
var isRunInDevelopmentMode = [(${T(org.wsp.framework.core.Environment).isRunInDevelopmentMode()})]

//选择路径测试案例字段定义
var ModelSelectorTestCaseFields = {
		dataSource:[
			{name:'id',primaryKey:true},
			{name:'modelSelector'},
			{name:'inputParameter'},
		    {name:'expectationVal'},
		    {name:'executionVal'},
		    {name:'matched'},
		],
		grid:[
			{width:'*', name:'inputParameter',	title:"[(#{inputParameter})]"},
		    {width:150, name:'expectationVal',	title:"[(#{expectationVal})]"},
		    {width:150, name:'executionVal',	title:"[(#{executionVal})]"},
		    {
		    	width:150, 
		    	name:'matched',			 
		    	title:"[(#{matched})]",
		    	formatCellValue: function (value) {
		    		if(value!=null){
		    			if (value) {
		                    return "是";
		                }else{
		                	 return "否";
		                }
		    		}
	                return null;
	            }
		    }
		],
		edit:[
			{name:'id',primaryKey:true,hidden:true},
			{name:'modelSelector',hidden:true},
			{width:200, name:'expectationVal',	title:"[(#{expectationVal})]"},
			{
				width:200, 
				name:'inputParameter',	
				title:"[(#{inputParameter})]",
				type:"textArea",
				hint: "{\n parameter1:value1,\n parameter2:value2\n}",
		        showHintInField: true,
			}
		    
		]
}
//选择路径测试案例数据源
isc.FwRestDataSource.create({
	ID: 'modelSelectorTestCaseDS',
    fields: ModelSelectorTestCaseFields.dataSource,
    dataURL: '[(@{/irs/modelSelectorTestCase/isc})]'
});

//字段定义
var ModelSelectorConfigFields ={
	grid: [
			{width:80,		name:'id',							title:'[(#{id})]',type: 'text',primaryKey:true,hidden:true},
			{width:200,		name:'selectorCode',				title:'[(#{selectorCode})]'},
			{width:200,		name:'selectorName',				title:'[(#{selectorName})]'},
		    {width:200,		name:'selectorVersion',				title:'[(#{selectorVersion})]'},
		    {width:100,		name:'selectorEnabled',				title:'[(#{selectorEnabled})]',type:'boolean'},
		    {
		    	width:200,	
		    	name:'selectorStatus',				
		    	title:'[(#{selectorStatus})]',
		    	type:'select',
		    	valueMap:selectorStatus
		    },
		    {width:'*',		name:'selectorDescribe',				title:'[(#{selectorDescribe})]'},
		    {name:"createDate",hidden:true}
	],
	edit:[
			{name:'id',							title:'[(#{id})]',				primaryKey:true,hidden:true},
			{name:'selectorCode',				title:'[(#{selectorCode})]',	required:true},
			{name:'selectorName',				title:'[(#{selectorName})]',	required:true},
			{name:'selectorDescribe',			title:'[(#{selectorDescribe})]',type:"textArea"}
	]
};

//模型选择路径数据源
isc.FwRestDataSource.create({
	ID: 'modelSelectorConfigDS',
    fields: ModelSelectorConfigFields.grid,
    dataURL: '[(@{/irs/modelSelector/isc})]'
});

//模型选择路径列表
isc.FwListGrid.create({
	ID: 'modelSelectorConfigListGrid',
	title: '<b>模型选择路径列表</b>',
	dataSource: modelSelectorConfigDS,
	sortField: 'createDate',
	groupStartOpen:"all",
	groupByField: 'selectorCode',
	fields: ModelSelectorConfigFields.grid,
	editWindow: {
        form: {
            dataSource: modelSelectorConfigDS,
            fields: ModelSelectorConfigFields.edit
        },
        afterShow: function(action){
        	this.Super('afterShow',arguments);
        },
        actionMembers:[
            'ok','close'
        ]
	},
	actionMembers:[
		'refresh','seperator','add','update',{
			title:'<span class="fa fa-files-o"></span> 复制',
			name:"COPY_SELECTOR",
			click:function(){
				var selectorRecord = modelSelectorConfigListGrid.getSelectedRecord();
				if(selectorRecord){
					//复制模型选择路径
	         		isc.RPCManager.sendRequest({
  				            containsCredentials: true,
  				            actionURL: "[(@{/irs/modelSelector/isc/copySelector})]",
  				            httpMethod : "POST",
  				            showPrompt: false,
  				            willHandleError:true,  //指定由回调函数处理服务器500错误
  				            params : {
  				            	"id":selectorRecord.id
  				            },
  				            callback : function (rpcResponse, data, rpcRequest) { 
  				            	if(rpcResponse.httpResponseCode == 500){
  				            		isc.warn("保存失败，服务器内部错误!");
  				            	}else if(rpcResponse.httpResponseCode == 200){
  				            		// 刷新列表
  				            		modelSelectorConfigListGrid.invalidateCache();
  				            		isc.say("复制模型选择路径成功!");
  				            	}
  				            }
  				        });
				}else{
					isc.say("请先选择要修改的数据!");
				}				
			}
		},'remove','seperator',
		{
			title:'<span class="fa fa-cubes"></span> 设计/查看',
			name:"DESIGN_SELECTOR",
			click:function(){
				var selectorRecord = modelSelectorConfigListGrid.getSelectedRecord();
				isc.FwWindow.create({
					title:"设计",
					maximized:true,
					showActionBar:false,
					showFooter:false,
					items:[
						isc.HTMLPane.create({
							width:'100%',
				            contentsURL:"[(@{/irs/modelSelector/showEditSelector})]/"+selectorRecord.id,
				            contentsType:"page"
				          })
					]
				}).show();
			}
		},'seperator',
		{
			title:'<span class="fa fa-check-square"></span> 发布',
			name:"RELEASE_SELECTOR",
			click:function(){
				var selectorRecord = modelSelectorConfigListGrid.getSelectedRecord();
				if(selectorRecord){
    	    		//发布模型选择路径
	         		isc.RPCManager.sendRequest({
  				            containsCredentials: true,
  				            actionURL: "[(@{/irs/modelSelector/isc/releaseSelector})]",
  				            httpMethod : "POST",
  				            showPrompt: false,
  				            willHandleError:true,  //指定由回调函数处理服务器500错误
  				            params : {
  				            	"id":selectorRecord.id
  				            },
  				            callback : function (rpcResponse, data, rpcRequest) { 
  				            	if(rpcResponse.httpResponseCode == 500){
  				            		isc.warn("保存失败，服务器内部错误!");
  				            	}else if(rpcResponse.httpResponseCode == 200){
  				            		// 刷新列表
  				            		modelSelectorConfigListGrid.invalidateCache();
  				            		isc.say("发布模型选择路径成功!");
  				            	}
  				            }
  				        });
				}
			}
		},{
			title:'<span class="fa fa-paper-plane"></span> 启用',
			name:"START_USING",
			click:function(){
				var selectorRecord = modelSelectorConfigListGrid.getSelectedRecord();
				if(selectorRecord){
    	    		//发布模型选择路径
	         		isc.RPCManager.sendRequest({
  				            containsCredentials: true,
  				            actionURL: "[(@{/irs/modelSelector/startUsingSelector})]",
  				            httpMethod : "POST",
  				            showPrompt: false,
  				            willHandleError:true,  //指定由回调函数处理服务器500错误
  				            params : {
  				            	"id":selectorRecord.id
  				            },
  				            callback : function (rpcResponse, data, rpcRequest) { 
  				            	if(rpcResponse.httpResponseCode == 500){
  				            		isc.warn("保存失败，服务器内部错误!");
  				            	}else if(rpcResponse.httpResponseCode == 200){
  				            		// 刷新列表
  				            		modelSelectorConfigListGrid.invalidateCache();
  				            		isc.say("发布模型选择路径成功!");
  				            	}
  				            }
  				        });
				}
			}
		},'seperator',
		{
			title:'<span class="fa fa-list"></span>测试案例',
			name:"TEST_SELECTOR",
			click:function(){
				var selectorRecord = modelSelectorConfigListGrid.getSelectedRecord();
				isc.FwWindow.create({
					title:"测试案例",
					items:[
						isc.FwListGrid.create({
							ID: 'modelSelectorTestCaseListGrid',
							title: '<b>测试案例列表</b>',
							width:750,
							height:350,
							dataSource: modelSelectorTestCaseDS,
							fields: ModelSelectorTestCaseFields.grid,
							initialCriteria:{
								"modelSelector.id": selectorRecord.id
							},
							canPageable:false,
							editWindow: {
						        form: {
						            dataSource: modelSelectorTestCaseDS,
						            fields: ModelSelectorTestCaseFields.edit
						        },
						        afterShow: function(action){
						        	this.getForm().setValue('modelSelector',selectorRecord);
						        	this.Super('afterShow',arguments);
						        },
						        actionMembers:[
						               		'ok','close'
						        ]
							},
							getCellCSSText: function (record, rowNum, colNum) {
						        if (this.getFieldName(colNum) == "matched") {
						            if (record.matched) {
						                return "font-weight:bold; color:green;";
						            }else{
						            	return "font-weight:bold; color:red;";
						            }
						        }
						    },
							actionMembers:[
								'refresh','seperator','add','update','remove','seperator',
								{
									title:"<span  class='fa fa-wrench'></span>执行案例",
									name:"EXECUTE_CASE",
									click:function(){
										isc.RPCManager.sendRequest({
								            containsCredentials: true,
								            actionURL: "[(@{/irs/modelSelector/executeTestCase})]?selectorId="+selectorRecord.id,
								            httpMethod : "GET",
								            showPrompt: false,
								            willHandleError:true,  //指定由回调函数处理服务器500错误
								            callback : function (rpcResponse, data, rpcRequest) { 
								            	if(rpcResponse.httpResponseCode == 500){
								            		isc.warn("测试失败，服务器内部错误!");
								            	}else if(rpcResponse.httpResponseCode == 200){
								            		// 刷新列表
								            		modelSelectorTestCaseListGrid.invalidateCache();
								            		isc.say("案例执行完毕!");
								            	}
								            }
								    	});
									}
								}
							],
							changeActionMemberButtonStatus: function(record, recordList){
								
							}
						})
					]
				}).show();
			}
		}
	],
	selectionUpdated: function(record, recordList){
		this.Super('selectionUpdated',arguments);
	},
	changeActionMemberButtonStatus: function(record, recordList){
		
		var status =FwUtil.isNull(record)?'disable':'enable';
		var actionStatus = 'disable';
		var startUsingStatus = 'disable';
		if(!FwUtil.isNull(record)){
			if(record.selectorStatus == "DRAFT"){
				actionStatus = 'enable';
			}
			if(record.selectorStatus == "RELEASE"){
				startUsingStatus = 'enable';
			}
		}
		
		this.setActionMemberButtonStatus(status,['COPY_SELECTOR']);
		this.setActionMemberButtonStatus(status,['DETAIL_SELECTOR']);
		this.setActionMemberButtonStatus(status,['TEST_SELECTOR']);
		
		this.setActionMemberButtonStatus(actionStatus,['DESIGN_SELECTOR']);
		this.setActionMemberButtonStatus(actionStatus,['EDIT_SELECTOR']);
		this.setActionMemberButtonStatus(actionStatus,['remove']);
		this.setActionMemberButtonStatus(actionStatus,['update']);
		this.setActionMemberButtonStatus(actionStatus,['RELEASE_SELECTOR']);
		
		this.setActionMemberButtonStatus(startUsingStatus,['START_USING']);
		
		if(isRunInDevelopmentMode){
			this.setActionMemberButtonStatus('enable',['remove']);
		}
    }
});

isc.HLayout.create({
			members:[
				modelSelectorConfigListGrid
			]
});
