//规则大类change事件
function ruleAddChange(ele){
    var typeCode = $(ele).val();
    $.ajax({
    	url:"[(@{"+ruleSubTypeUrl+"})]",
        data:{"parentCode":typeCode},
        dataType : "json",
        cache : false,
        type : "post",
        success : function(response, textStatus) {
            if(response){
            	setSelect($('#ruleSubType'),response.data);
            }
        }
    });
}

function ruleQueryChange(ele){
    var typeCode = $(ele).val();
    $.ajax({
    	url:"[(@{"+ruleSubTypeUrl+"})]",
        data:{"parentCode":typeCode},
        dataType : "json",
        cache : false,
        type : "post",
        success : function(response, textStatus) {
            if(response){
            	setSelect($('#query_ruleSubType'),response.data);
            }
        }
    });
}

// 查询
$("#rule_btn_query").click(function() {
	var params={};
	params.query_ruleCode=$("#query_ruleCode").val();
	params.query_ruleName=$("#query_ruleName").val();
	params.query_ruleType=$("#query_ruleType").val();
	params.query_ruleSubType=$("#query_ruleSubType").val();
	params.query_warnLevel=$("#query_warnLevel").val();
	params.query_ruleFrequency=$("#query_ruleFrequency").val();
	params.query_isValid=$("#query_isValid").val();
    $("#rule_properties_table").jqGrid('setGridParam',{
        datatype:'json',
        postData:{"data":JSON.stringify(params)},
        page:0
    }).trigger("reloadGrid");
});
    
// 清空
$("#rule_btn_clear").click(function() {
    $("#formSearch input").val("");
    $("#formSearch select").val("");
    $("#formSearch select").trigger("chosen:updated");
});

$(document).ready(function(){
	//规则大类
	var ruletypesel;
	//规则小类
	var rulesubtypesel;
	 //绑定规则大类
	$.ajax({
		url:"[(@{"+ruleTypeUrl+"})]",
        cache : false,
        type : "post",
        async:false,
		success:function(response,textStatus){
			ruletypesel = response.data;
			setSelect($("select[name='query_ruleType']"),ruletypesel);
			setSelect($('#ruleType'),ruletypesel);
		}
	});
	$.ajax({
		url:"[(@{"+ruleSubTypeUrl+"})]",
		dataType : "json",
	    cache : false,
	    type : "post",
	    async:false,
	    success : function(response, textStatus) {
	    	if(response){
	    		rulesubtypesel = response.data;
	    		setSelect($('#ruleSubType'),rulesubtypesel);
	    		setSelect($('#query_ruleSubType'),rulesubtypesel);
	    	}
	    }
	});
	
	//绑定回车事件
	$(document).keydown(function(event){
		if(event.keyCode==13){
			$("#rule_btn_query").click();
		}
	});
	
    $("#rule_properties_table").jqGrid({
        url : "[(@{/batchrule/after_rule_list.action})]",
        recordtext: "显示：{0} - {1}，总数：{2} ",
        emptyrecords: "",
        loadtext: "读取中...",
        loadui:"block",
        beforeRequest:function(){
            document.getElementById("table_zz_div").style.display="";
            document.getElementById("table_bg_div").style.display="none";
        },
        loadComplete:function(){
            document.getElementById("table_zz_div").style.display="none";
            document.getElementById("table_bg_div").style.display="";
            jqGridNoRecords("rule_properties_table");
        },
        onSelectRow: function(id){
        	var rowData = $("#rule_properties_table").jqGrid("getRowData",id);
        	if(rowData){
        		$("#rule_params_table").jqGrid('setGridParam',{
                    datatype:'json',
                    postData:{"modelId":rowData.model_id},
                }).trigger("reloadGrid");
        	}
        },
        pgtext : "第 {0}页， 共{1}页",
        mtype : "post",
        datatype: "json",
        height: 200,
        autowidth: true,
        shrinkToFit: false,
        //postData:{"param":JSON.stringify(param)},
        prmNames: {//表示请求页码的参数名称
            page: "page",
            rows: "size"
        },
        page:0,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames:['id','model_id','规则编号','规则名称','规则大类隐藏值','规则大类','规则小类隐藏值','规则小类','适用关联人类型隐藏值','适用关联人类型','预警等级隐藏值','预警等级','规则跑批频率隐藏值','规则跑批频率','触发机制描述','是否生效隐藏值','是否生效','最后修改人','最后修改日期'],
        colModel:[
            {name:'id',index:'id',hidden:true},
            {name:'model_id',index:'model_id',hidden:true},
            {name:'fd_code',index:'fd_code', editable: true, width:80},
            {name:'fd_name',index:'fd_name', editable: true, width:250},
            {name:'rule_type',index:'rule_type', editable: true,hidden:true},
            {name:'rule_type_name',index:'rule_type_name', editable: true, width:100,
            	formatter:function(value,index,row){
            		if(value == null || value == '' || value == undefined){
            			return '';
            		}
            		return [(${@frSystemDictionaryService.getDictionaryMapJsonString('WARN_CATE',#locale)})][value];
            	}
            },
            {name:'rule_sub_type',index:'rule_sub_type', editable: true,hidden:true},
            {name:'rule_sub_type_name',index:'rule_sub_type_name', editable: true, width:100,
            	formatter:function(value,index,row){
            		if(value == null || value == '' || value == undefined){
            			return '';
            		}
            		var cateCode = row.rule_type;
            		if(cateCode == 'R001'){
            			return R001[value];
            		}else if(cateCode == 'R002'){
            			return R002[value];
            		}else if(cateCode == 'R003'){
            			return R003[value];
            		}
            		return '';
            	}
            },
            {name:'rele_type',index:'rele_type', editable: true,hidden:true},
            {name:'rele_type_name',index:'rele_type_name', editable: true, width:100,
            	formatter:function(value,index,row){
                    if(row.rele_type==null || row.rele_type==""){
                        return "";
                    }else{
                        var rele_type = row.rele_type.split(',')
                        var rele_type_arr = new Array();
                        $.each(ruleReleType,function(k,v){
                            for (var i = 0; i < rele_type.length; i++) {
                                if(k==rele_type[i]){
                                	rele_type_arr.push(v);
                                }
                            }
                        })
                        return rele_type_arr.toString();
                    }
                }
            },
            {name:'warn_level',index:'warn_level', editable: true, hidden:true},
            {name:'warn_level_name',index:'warn_level_name', editable: true, width:80,
            	formatter:function(value,index,row){
                    if(row.warn_level==null || row.warn_level==""){
                        return "";
                    }else{
                    	var result="";
                        $.each(warnLevelVal,function(k,v){
                        	if(k==row.warn_level){
                        		result=v;
                            }
                        })
                        return result;
                    }
                }
            },
            {name:'frequency',index:'frequency', editable: true, hidden:true},
            {name:'frequencyName',index:'frequencyName', editable: true, width:90,
            	formatter:function(value,index,row){
                    if(row.frequency==null||row.frequency==""){
                        return "";
                    }else{
                    	var result="";
                        $.each(ruleFrequency,function(k,v){
                        	if(k==row.frequency){
                        		result=v;
                            }
                        })
                        return result;
                    }
                }
            },
            {name:'trig_desc',index:'trig_desc', editable: true, width:250},
            {name:'is_valid',index:'is_valid', editable: true,hidden:true},
            {name:'is_valid_name',index:'is_valid_name', editable: true, width:80,
            	formatter:function(value,index,row){
                    if(row.is_valid==null || row.is_valid==""){
                        return "";
                    }else{
                    	var result="";
                        if(row.is_valid=="Y"){
                        	result="是";
                        }else{
                        	result="否";
                        }
                        return result;
                    }
                }
            },
            {name:'fd_last_modifier',index:'fd_last_modifier', editable: true, width:100},
            {name:'fd_last_modifydate',index:'fd_last_modifydate', editable: true, width:100}
        ],
        pager: "#pager_list_1",
        viewrecords: true,
        caption: "",
        add: true,
        edit: true,
        addtext: 'Add',
        edittext: 'Edit',
        rownumbers: true,
        rownumWidth: 50,
        hidegrid: false,
        jsonReader : {
        	root:"data",
        	page:"page",
        	total:"total",
        	records: "recordsTotal",
        	repeatitems: false,
        	id: "fd_code"
        }
    });
    $("#rule_properties_table").jqGrid('setLabel','rn','序号',{'text-align':'center','vertical-align':'middle'},'');
    
    //租后规则列表修改
    $("#btn_update").click(function(){
        var selectedRowId = $("#rule_properties_table").jqGrid("getGridParam", "selrow");
        //获得当前行各项属性
        var rowData = $("#rule_properties_table").jqGrid("getRowData",selectedRowId);
        if(!rowData.fd_code){
            swal({
                title: "提示",
                text: "请先选择要修改的数据",
                type: "info"
            });
            return;
        }else{
            $('#myModal').modal('show');
            $("#ruleId").val(rowData.id);
            $("#ruleCode").val(rowData.fd_code);
            $("#ruleName").val(rowData.fd_name);
            
            $("#ruleFrequency").val(rowData.frequency);
            $("#ruleFrequency").trigger("chosen:updated");
            $("#ruleType").val(rowData.rule_type);
            $("#ruleType").trigger("chosen:updated");
            $("#ruleSubType").val(rowData.rule_sub_type);
            $("#ruleSubType").trigger("chosen:updated");
            $("#warnLevel").val(rowData.warn_level);
            $("#warnLevel").trigger("chosen:updated");
            
            if(rowData.rele_type!=null && rowData.rele_type!=""){
            	var ruleReleTypeArr=[];
            	if(rowData.rele_type.indexOf(",")>-1){
            		var tmpArr=rowData.rele_type.split(",");
            		for(var i=0;i<tmpArr.length;i++){
            			ruleReleTypeArr.push(tmpArr[i]);
            		}
            	}else{
            		ruleReleTypeArr.push(rowData.rele_type);
            	}
            	$("#ruleReleType").val(ruleReleTypeArr);
                $("#ruleReleType").trigger("chosen:updated");
            }
            
            $("#trigDesc").val(rowData.trig_desc);
        }
    });

    //保存按钮单击事件
    $("#btn_submit").click(function(){
    	$('#addForm').data('bootstrapValidator').validate();
        //是否通过校验
        if(!$('#addForm').data('bootstrapValidator').isValid()){
        	swal({
        		title: "提示",
                text: "表单校验未通过！",
                type: "info"
            });
        	return;
        }else{
        	var ruleId=$("#ruleId").val();
        	
        	//将要保存的数据准备好
    		var data={};
    	    data.ruleCode=$("#ruleCode").val();
    	    data.ruleName=$("#ruleName").val();
    	    data.ruleType=$("#ruleType").val();
    	    data.ruleSubType=$("#ruleSubType").val();
    	    var ruleReleTypeArr=$("#ruleReleType").val();
    	    var ruleReleTypeStr="";
    	    if(ruleReleTypeArr!=null && ruleReleTypeArr.length>0){
    	    	for(var i=0;i<ruleReleTypeArr.length;i++){
    	    		ruleReleTypeStr=ruleReleTypeStr+ruleReleTypeArr[i]+",";
    	    	}
    	    	ruleReleTypeStr=ruleReleTypeStr.substring(0,ruleReleTypeStr.length-1);
    	    }
    	    data.releType=ruleReleTypeStr;
    	    data.warnLevel=$("#warnLevel").val();
    	    data.trigDesc=$("#trigDesc").val();
    	    data.frequency=$("#ruleFrequency").val();
    	    var iscRequest={};
    	    iscRequest.data=data;
    	    
    	    var saveUrl="/batchrule/isc/add";
        	if(ruleId!=null && ruleId!=""){
        		var oldValues={};
        		oldValues.id=ruleId;
        		iscRequest.oldValues=oldValues;
        		
        		saveUrl="/batchrule/isc/update";
        	}
        	$.ajax({
    	    	url:saveUrl,
    	    	data : JSON.stringify(iscRequest),
    	    	contentType: 'application/json; charset=utf-8',//这句一定要加，否则后端无法处理请求
    	    	type:"post",
    	    	dataType : "json",
    	    	cache : false,
    	        success : function(response, textStatus) {
    	            if (response.response.status == 0) {
    	            	swal({
    	                    title: "提示",
    	                    text: "保存成功！",
    	                    type: "success"
    	                });
    	            	$('#myModal').modal('hide');
    	            	$("#rule_properties_table").jqGrid('setGridParam',{
    	                    datatype:'json',
    	                    //postData:{"param":JSON.stringify(param)},
    	                    page:0
    	                }).trigger("reloadGrid");
    	            }else{
    	            	swal({
    	                    title: "错误",
    	                    text: "操作出错，请联系管理员！",
    	                    type: "error"
    	                });
    	            }
    	        },
    		    error : function(textStatus, e) {
    		    	swal({
    	                title: "错误",
    	                text: "操作出错，请联系管理员！",
    	                type: "error"
    	            });
    		    }
    	    });
        }
    });
    
    //修改状态
    $("#btn_upd_status").click(function(){
    	var selectedRowId = $("#rule_properties_table").jqGrid("getGridParam", "selrow");
        //获得当前行各项属性
        var rowData = $("#rule_properties_table").jqGrid("getRowData",selectedRowId);
        if(!selectedRowId){
        	swal({
                title: "提示",
                text: "请选择要修改状态的记录！",
                type: "info"
            });
            return ;
        }
        if(!rowData.id){
            swal({
                title: "提示",
                text: "规则未配置基本信息无法修改状态！",
                type: "info"
            });
            return ;
        }else{
        	var valid="N";
        	if(!rowData.is_valid || rowData.is_valid=="N"){
        		valid="Y";
        	}
    		$.ajax({
    	    	url:"/batchrule/update_isvalid.action",
    	    	data : {
    	    		id:rowData.id,
    	    		isValid:valid
    	    	},
    	    	type:"post",
    	    	dataType : "json",
    	    	cache : false,
    	        success : function(response, textStatus) {
    	            if (response && response.success==true) {
    	            	swal({
    	                    title: "提示",
    	                    text: "保存成功！",
    	                    type: "success"
    	                });
    	            	$("#rule_properties_table").jqGrid('setGridParam',{
    	                    datatype:'json',
    	                    page:0
    	                }).trigger("reloadGrid");
    	            }else{
    	            	swal({
    	                    title: "错误",
    	                    text: "操作出错，请联系管理员！",
    	                    type: "error"
    	                });
    	            }
    	        },
    		    error : function(textStatus, e) {
    		    	swal({
    	                title: "错误",
    	                text: "操作出错，请联系管理员！",
    	                type: "error"
    	            });
    		    }
    	    });
        }
    });
    
    //删除规则
    $("#btn_delete").click(function(){
    	var selectedRowId = $("#rule_properties_table").jqGrid("getGridParam", "selrow");
        //获得当前行各项属性
        var rowData = $("#rule_properties_table").jqGrid("getRowData",selectedRowId);
        if(!selectedRowId){
        	swal({
                title: "提示",
                text: "请选择要删除的记录！",
                type: "info"
            });
            return ;
        }
        if(!rowData.id){
            swal({
                title: "提示",
                text: "规则当前未生效，跑批时也不会跑该规则，无法删除！",
                type: "info"
            });
            return ;
        }else{
        	$.ajax({
    	    	url:"/batchrule/delete_rule.action",
    	    	data : {
    	    		ruleCode:rowData.fd_code,
    	    	},
    	    	type:"post",
    	    	dataType : "json",
    	    	cache : false,
    	        success : function(response, textStatus) {
    	            if (response && response.success==true) {
    	            	swal({
    	                    title: "提示",
    	                    text: "保存成功！",
    	                    type: "success"
    	                });
    	            	$("#rule_properties_table").jqGrid('setGridParam',{
    	                    datatype:'json',
    	                    page:0
    	                }).trigger("reloadGrid");
    	            }else{
    	            	swal({
    	                    title: "错误",
    	                    text: "操作出错，请联系管理员！",
    	                    type: "error"
    	                });
    	            }
    	        },
    		    error : function(textStatus, e) {
    		    	swal({
    	                title: "错误",
    	                text: "操作出错，请联系管理员！",
    	                type: "error"
    	            });
    		    }
    	    });
        }
    });
    
    //租后规则参数列表
    $("#rule_params_table").jqGrid({
        url : "[(@{/batchrule/get_rule_parameter_list.action})]",
        recordtext: "显示：{0} - {1}，总数：{2} ",
        emptyrecords: "",
        loadtext: "读取中...",
        loadui:"block",
        beforeRequest:function(){
            document.getElementById("table_zz_div2").style.display="";
            document.getElementById("table_bg_div2").style.display="none";
        },
        loadComplete:function(){
            document.getElementById("table_zz_div2").style.display="none";
            document.getElementById("table_bg_div2").style.display="";
            jqGridNoRecords("rule_params_table");
        },
        pgtext : "第 {0}页， 共{1}页",
        mtype : "post",
        datatype: "local",
        height: 160,
        autowidth: true,
        shrinkToFit: false,
        rowNum: 0,
        colNames:['fd_id','参数代码','参数名称','参数描述','参数类型','值类型','默认值','结果值定义','最后修改人','最后修改时间','操作'],
        colModel:[
            {name:'fd_id',index:'fd_id',hidden:true},
            {name:'fd_code',index:'fd_code', editable: true, width:120},
            {name:'fd_name',index:'fd_name', editable: true, width:200},
            {name:'fd_description',index:'fd_description', editable: true, width:200},
            {name:'fd_type',index:'fd_type', editable: true, width:100,
            	formatter:function(value,index,row){
                    if(ParameterType && value){
                    	var result="";
                        $.each(ParameterType,function(k,v){
                        	if(k==value){
                        		result=v;
                            }
                        })
                        return result;
                    }else{
                    	return "";
                    }
                }
            },
            {name:'fd_value_type',index:'fd_value_type', editable: true, width:100,
            	formatter:function(value,index,row){
                    if(ParameterValueType && value){
                    	var result="";
                        $.each(ParameterValueType,function(k,v){
                        	if(k==value){
                        		result=v;
                            }
                        })
                        return result;
                    }else{
                    	return "";
                    }
                }
            },
            {name:'fd_default_value',index:'fd_default_value', editable: true, width:100},
            {name:'fd_script',index:'fd_script', editable: true,hidden:true},
            {name:'fd_last_modifier',index:'fd_last_modifier', editable: true, width:100},
            {name:'fd_last_modifydate',index:'fd_last_modifydate', editable: true, width:100},
            {name:'op',index:'op', editable: true, width:80,
            	formatter:function(value,index,row){
                    if(row && row.fd_type=="OUT"){
                    	return '<a style="color:#00CCFF" href="javascript:void(0);" onclick="javascript:paramOpClickFun(\''+row.fd_id+'\',\'view\');">查看</a>';
                    }else{
                    	return '<a style="color:#00CCFF" href="javascript:void(0);" onclick="javascript:paramOpClickFun(\''+row.fd_id+'\',\'update\');">修改</a>';
                    }
                }
            }
        ],
        viewrecords: true,
        caption: "",
        add: true,
        edit: true,
        addtext: 'Add',
        edittext: 'Edit',
        rownumbers: true,
        rownumWidth: 50,
        hidegrid: false,
        jsonReader : {
            root:"data",
            records: "recordsTotal",
            repeatitems: false,
            id: "fd_id"
        }
    });
    $("#rule_params_table").jqGrid('setLabel','rn','序号',{'text-align':'center','vertical-align':'middle'},'');
    
    //参数修改页面，保存按钮单击事件
    $("#btn_param_save").click(function(){
        //是否通过校验
        if(!$('#defaultValue').val()){
        	swal({
        		title: "提示",
                text: "默认值不允许为空！",
                type: "info"
            });
        	return;
        }else{
        	var paramId=$("#paramId").val();
        	var defaultValue=$("#defaultValue").val();
        	//将要保存的数据准备好
        	$.ajax({
    	    	url:"/batchrule/update_rule_parameter.action",
    	    	data : {paramId:paramId,defaultValue:defaultValue},
    	    	type:"post",
    	    	dataType : "json",
    	    	cache : false,
    	        success : function(response, textStatus) {
    	        	if (response && response.success==true) {
    	            	swal({
    	                    title: "提示",
    	                    text: "保存成功！",
    	                    type: "success"
    	                });
    	            	$('#paramsModal').modal('hide');
    	            	var selectedRowId = $("#rule_properties_table").jqGrid("getGridParam", "selrow");
    	            	$("#rule_params_table").jqGrid('setGridParam',{
    	                    datatype:'json',
    	                    postData:{"modelId":selectedRowId.model_id},
    	                }).trigger("reloadGrid");
    	            }else{
    	            	swal({
    	                    title: "错误",
    	                    text: "操作出错，请联系管理员！",
    	                    type: "error"
    	                });
    	            }
    	        },
    		    error : function(textStatus, e) {
    		    	swal({
    	                title: "错误",
    	                text: "操作出错，请联系管理员！",
    	                type: "error"
    	            });
    		    }
    	    });
        }
    });
});

function paramOpClickFun(id,op){
	var rowData = $("#rule_params_table").jqGrid("getRowData",id);
	if(op=="view"){
		$('#params_win_title').html("查看规则结果值");
		$("#paramScript").val(rowData.fd_script);
		document.getElementById("div_paramScript").style.display="";
		$("#btn_param_save").hide();
	}else{
		$('#params_win_title').html("修改参数");
		document.getElementById("div_paramScript").style.display="none";
		$("#btn_param_save").show();
	}
	$('#paramsModal').modal('show');
	
	$("#paramId").val(id);
	$("#paramCode").val(rowData.fd_code);
	$("#paramName").val(rowData.fd_name);
	$("#paramDesc").val(rowData.fd_description);
	$("#paramType").val(rowData.fd_type);
	$("#paramValueType").val(rowData.fd_value_type);
	$("#defaultValue").val(rowData.fd_default_value);
}