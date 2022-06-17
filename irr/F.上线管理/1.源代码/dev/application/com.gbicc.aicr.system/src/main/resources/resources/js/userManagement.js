var flagVal;
var applicableLinkVal;
var createdateStartVal;
var createdateEndVal;
var param;
$(document).ready(function(){
	$("#rule_btn_query").click(function() {
		param= new Object();
		flagVal=$("#formSearch select[name='flag_query']").val();
		applicableLinkVal=$("#formSearch select[name='applicableLink_query']").val();
		createdateStartVal=$("#formSearch input[name='createdate_start']").val();
		createdateEndVal=$("#formSearch input[name='createdate_end']").val();
		param=getQueryParam();
		$("#report_table").jqGrid('setGridParam',{
			 datatype:'json',  
		     postData:{"param":JSON.stringify(param)},
		     page:0
			 }).trigger("reloadGrid");
	});
	// 清空
	$("#rule_btn_clear").click(function() {
		$("#formSearch input").val("");
        $("#formSearch select").val("");
        $("#formSearch select").trigger("chosen:updated");
	});
     // 清空
    	$("#btn_reset").click(function() {
    		$("#addForm input").val("");
            $("#addForm select").val("");
            $("#addForm select").trigger("chosen:updated");
            
       
	});
$("#report_table").jqGrid({
		url : "/reportModel/reportModel_query.action",
		recordtext: "显示：{0} - {1}，总数：{2} ",
	    emptyrecords: "本次查询无数据！",
	    loadtext: "读取中...",
	    loadui:"block",
	    beforeRequest:function(){
	    	document.getElementById("table_zz_div").style.display="";
	    	document.getElementById("table_bg_div").style.display="none";
	    },
	    loadComplete:function(){
	    	document.getElementById("table_zz_div").style.display="none";
	    	document.getElementById("table_bg_div").style.display="";
	    },
	    pgtext : "第 {0}页， 共{1}页",
		mtype : "post",
        datatype: "json",
        height: 350,
        autowidth: true,
        shrinkToFit: true,
        prmNames: {
            page: "page"   // 表示请求页码的参数名称
        },
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames:['id','姓名','UM账号','是否可用','是否过期','是否被锁定','最后修改人','最后修改日期'],
        colModel:[
            {name:'id',index:'id',hidden:true },
            {name:'mbno',index:'mbno', editable: true, width:60,search:true},
            {name:'bgname',index:'bgname', editable: true, width:90},
            {name:'applicableLink',index:'applicableLink', editable: true, width:90},
            {name:'createperson',index:'createperson', editable: true, width:90},
            {name:'createdate',index:'createdate', editable: true, width:90},
            {name:'createdate',index:'createdate', editable: true, width:90},
            {name:'flag',index:'flag', editable: true, width:100}
        ],
        pager: "#pager_list_2",
        viewrecords: true,
        caption: "",
        add: true,
        edit: true,
        addtext: 'Add',
        edittext: 'Edit',
        rownumbers: true,
        hidegrid: false,
        jsonReader : {
        	root:"data",
        	records: "recordsTotal",
        	repeatitems: false,
        	id: "0"
        }
    });

    // Setup buttons
    $("#report_table").jqGrid('navGrid', '#pager_list_2',
            {edit: true, add: true, del: true, search: true},
            {height: 200, reloadAfterSubmit: true}
    );

    // Add responsive to jqGrid
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#report_table').setGridWidth(width);
    });


    setTimeout(function(){
        $('.wrapper-content').removeClass('animated fadeInRight');
    },700);
    
	//保存按钮单击事件 
    $("#btn_submit").click(function(){
    	$("#addForm").ajaxSubmit({
    		type:'post',
    		datatype:'json',  
    		url:'/reportModel/reportModel_save.action',
    		success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
    			if (data.msg == true) {
        			$('#myModal').modal('hide');
	            	swal({
	                    title: "提示",
	                    text: "保存成功！",
	                    type: "success"
	                });
	            	refresh();
	            }else{
	            	swal({
	                    title: "错误",
	                    text: "操作出错，请联系管理员！",
	                    type: "error"
	                });
	            }
            }
    	});

        $("#addForm input").val("");
        $("#addForm select").val("");
        $("#addForm select").trigger("chosen:updated");
     
    });
    
  //保存按钮单击事件 模板章节
    $("#button_submit").click(function(){
    	console.log(editor2.getData());
    	$("#contentInfo").val(editor2.getData());
    	$("#model_form").ajaxSubmit({
    		type:'post',
    		datatype:'json',  
    		url:'/reportModelChapters/reportModelChapters_save.action',
    		success: function(data) { // data 保存提交后返回的数据，一般为 json 数据
    			if (data.msg == true) {
    				/*var contentInfo = $("#p_contentInfo").val();
    				console.log(contentInfo);*/
        			$('#compileModal').modal('hide');
	            	swal({
	                    title: "提示",
	                    text: "保存成功！",
	                    type: "success"
	                });
	            	//refresh();
	            }else{
	            	swal({
	                    title: "错误",
	                    text: "操作出错，请联系管理员！",
	                    type: "error"
	                });
	            }
            }
    	});

        $("#model_form input").val("");
        $("#model_form select").val("");
        $("#model_form select").trigger("chosen:updated");
     
    });
    
    
  //删除
    $("#btn_delete").click(function() {
    	var rowId=$('#report_table').jqGrid('getGridParam','selrow');
    	if(!rowId){
    		swal({
                title: "提示",
                text: "请先选择要删除的数据！"
            });
    		return;
    	}
    	var rowData=$('#report_table').jqGrid('getRowData',rowId);
    	swal({
    		title: "确定要删除吗？",
            text: "删除后数据无法恢复！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            cancelButtonText:"取消",
            confirmButtonText: "确定",
            closeOnConfirm: false
        }, function(){
        	$.ajax({
    	    	url:"/reportModel/reportModel_del.action",
    	    	data : {
    	    		id:rowData.id
    	    	},
    	    	type:"post",
    	    	dataType : "json",
    	    	cache : false,
    	        success : function(response, textStatus) {
    	            if (response.msg == true) {
    	            	$("#report_table").jqGrid('setGridParam',{
    	            		datatype:'json',  
    	            		postData:{"param":"{}"},
    	            		page:0
    	            	}).trigger("reloadGrid");
    	            	swal({
    	                    title: "提示",
    	                    text: "删除成功！",
    	                    type: "success"
    	                });
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
        });
    });
    

    
  //绑定修改按钮单击事件
    $("#btn_update").click(function(){
    	$("#myModalLabel").html("修改模板");
		var selectedRowId = $("#report_table").jqGrid("getGridParam", "selrow");
		var rowData = $("#report_table").jqGrid("getRowData",selectedRowId);
		if(!selectedRowId){
    		swal({
                title: "提示",
                text: "请先选择要修改的数据！"
            });
    		return;
		}else{
        	//根据选中的行，打开模态窗口并赋值 
			$("#myModal").modal('show');
        	$("#ids").val(rowData.id);
        	$("#bgname").val(rowData.bgname);
        	$("#applicableLink").val(rowData.applicableLink);
        	$("#applicableLink").trigger("chosen:updated");
        }
        	
    });
   
    
    
});
$("#insert").click(function() {
	$("#myModalLabel").html("新增模板");
	$("#addForm input").val("");
    $("#addForm select").val("");
    $("#addForm select").trigger("chosen:updated");    
});
$("#addChapter").click(function() {
	$("#titleinfo").val("");
	//window.editor2.setData("");
	var obj = CKEDITOR.replace("content");
	obj.setData("");
	$("#sectionId").val("");
	//$("#compileModal").modal();
	$("#modal_title").html("新增章节");   
});

$("#btn_update").click(function(){
	$("#myModalLabel").html("修改模板");
	var selectedRowId = $("#report_table").jqGrid("getGridParam", "selrow");
	var rowData = $("#report_table").jqGrid("getRowData",selectedRowId);
	if(!selectedRowId){
		swal({
            title: "提示",
            text: "请先选择要修改的数据！"
        });
		return;
	}else{
    	//根据选中的行，打开模态窗口并赋值 
		$("#myModal").modal('show');
    	$("#ids").val(rowData.id);
    	$("#bgname").val(rowData.bgname);
    	$("#applicableLink").val(rowData.applicableLink);
    	$("#applicableLink").trigger("chosen:updated");
    }
    	
});
$("#modify").click(function() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getSelectedNodes(),
	treeNode = nodes[0];
	var selectedRowId = $("#report_table").jqGrid("getGridParam", "selrow");
	var rowData = $("#report_table").jqGrid("getRowData",selectedRowId);
	if(nodes.length == 0){
		swal({
            title: "提示",
            text: "请先选择要修改的章节。"
        });
		return;
	}else{
		getNodeDataById(treeNode);
    	//根据选中的行，打开模态窗口并赋值
		$("#compileModal").modal('show');
		$("#modal_title").html("修改章节");
    	/*$("#ids").val(rowData.id);
    	$("#bgname").val(rowData.bgname);
    	$("#applicableLink").val(rowData.applicableLink);
    	$("#applicableLink").trigger("chosen:updated");*/
    }
	/*if (nodes.length == 0) {
		alert("请先选择要修改的章节。");
		return;
	}*/
	   
});
$("#remove").click(function(){
	var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
	nodes = zTree.getSelectedNodes(),
	treeNode = nodes[0];
	if(nodes.length == 0){
		swal({
            title: "提示",
            text: "请先选择要删除的章节。"
        });
		return;
	}else{
		
		swal({
    		title: "确定要删除吗？",
            text: "删除后数据无法恢复！",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            cancelButtonText:"取消",
            confirmButtonText: "确定",
            closeOnConfirm: false
        }, function(){
        	$.ajax({
    	    	url:"/reportModelChapters/reportModelChapters_del.action",
    	    	data : {
    	    	    id:treeNode.id
    	    	},
    	    	type:"post",
    	    	dataType : "json",
    	    	cache : false,
    	        success : function(response, textStatus) {
    	            if (response.msg == true) {
    	        		zTree.removeNode(treeNode);
    	            	swal({
    	                    title: "提示",
    	                    text: "删除成功！",
    	                    type: "success"
    	                });
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
        });
    }	
});
function refresh(){
	$("#report_table").jqGrid('setGridParam',{
		 datatype:'json',  
	     page:0
		 }).trigger("reloadGrid");
}

function getQueryParam(){
	if(!isSelect(flagVal)){
		param.flag=flagVal;
	}
	if(!isSelect(applicableLinkVal)){
		param.applicableLink=applicableLinkVal;
	}
	if(!isEmpty(createdateStartVal)){
		param.createdate=createdateStartVal;
	}
	if(!isEmpty(createdateEndVal)){
		param.createdate=createdateEndVal;
	}
	return param;
}
  //非空判断
    function isEmpty(obj){
        if(typeof obj == "undefined" || $.trim(obj) == null || $.trim(obj) == ""){
            return true;
        }else{
            return false;
        }
    }
    //下拉非空判断
    function isSelect(obj){
        if(typeof obj == "undefined" || $.trim(obj) == null || $.trim(obj) == "请选择"){
            return true;
        }else{
            return false;
        }
    }