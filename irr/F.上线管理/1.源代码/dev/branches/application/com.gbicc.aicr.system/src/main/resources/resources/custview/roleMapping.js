var flagVal;
var applicableLinkVal;
var createdateStartVal;
var createdateEndVal;
var param;
$(document).ready(function(){
//系统角色列表
$("#role_list").jqGrid({
		url : "/role/query_sys_action",
		recordtext: "显示：{0} - {1}，总数：{2} ",
	    emptyrecords: "本次查询无数据！",
	    loadtext: "读取中...",
	    loadui:"block",
	    pgtext : "第 {0}页， 共{1}页",
		mtype : "post",
        datatype: "json",
        height: 350,
        autowidth: true,
        shrinkToFit: true,
        onSelectRow: function(ids) { //单击选择行  
        	                if(ids == null) {   
        	                    ids=0;   
        	                    
        	                } else { 
        		var selectedRowId = $("#role_list").jqGrid("getGridParam", "selrow");
        	    //获得当前行各项属性
        	   var rowData = $("#role_list").jqGrid("getRowData",selectedRowId);
        	   var ddddd = rowData.FD_ID;
        	  
        	   console.log(ddddd);
        	  $("#core_role_list").jqGrid('setGridParam',{
	     		datatype:'json',  
	     	   postData:{"params":ddddd},
	     		page:0
	     	}).trigger("reloadGrid");
        	                }   
        	               
        	            }, 
        prmNames: {
            page: "page"   // 表示请求页码的参数名称
        },
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames:['id','代码','名称','描述'],
        colModel:[
        	 {name:'FD_ID',index:'FD_ID', editable: true, width:60,hidden:true},
            {name:'FD_CODE',index:'FD_CODE', editable: true, width:60,search:true},
            {name:'FD_NAME',index:'FD_NAME', editable: true, width:90},
            {name:'FD_DESCRIPTION',index:'FD_DESCRIPTION', editable: true, width:90}         
        ],
        pager: "#pager_list_3",
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


  $("#core_role_list").jqGrid({
	url : "/role/id_query_action",
	recordtext: "显示：{0} - {1}，总数：{2} ",
    emptyrecords: "本次查询无数据！",
    loadtext: "读取中...",
    loadui:"block",
    pgtext : "第 {0}页， 共{1}页",
	mtype : "post",
    datatype: "json",
    height: 350,
    autowidth: true,
    shrinkToFit: true,
    rowNum: 200,
    rowList: [10, 20, 30],
    colNames:['id','角色名称','角色类型','角色状态','是否可用'],
    colModel:[
        {name:'roleId',index:'roleId',hidden:true },
        {name:'roleName',index:'roleName', editable: true, width:60,sortable:false},
        {name:'roleType',index:'roleType', editable: true, width:90,sortable:false},
        {name:'roleStatus',index:'roleStatus', editable: true, width:90,sortable:false},
        {name:'isSelected',index:'isSelected', editable: true, width:90,sortable:false,
        	formatter:function(cellvalue,options,rowObject){  
        	    if(cellvalue ==0){
        	    	return '<input type="checkbox" name="checkbox">';	
        	    }
        		if(cellvalue ==1){
        			return '<input type="checkbox" name="checkbox" checked="checked">';
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
    hidegrid: false,
    
    jsonReader : {
    	root:"data",
    	records: "recordsTotal",
    	repeatitems: false,
    	id: "0"
    }
}); 

  

    // Setup buttons
    $("#role_list").jqGrid('navGrid', '#pager_list_3',
            {edit: true, add: true, del: true, search: true},
            {height: 200, reloadAfterSubmit: true}
    );

    // Add responsive to jqGrid
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $('#role_list').setGridWidth(width);
    });


    setTimeout(function(){
        $('.wrapper-content').removeClass('animated fadeInRight');
    },700);

       		
    

    //点击保存
    $(".preservation").click(function(){
    	var selectedRowId = $("#role_list").jqGrid("getGridParam", "selrow");
	    //获得当前行各项属性
	   var rowData = $("#role_list").jqGrid("getRowData",selectedRowId);
	   var params = rowData.FD_ID;
	   if(isEmpty(rowData.FD_ID)){
		   swal({
			   title:"提示",
			   text:"请先选择左边表格的行数据",
			   type:"info"
		   })
	   }else{
		   var oCar = new Object;
		   	 oCar.first=params;
		   	 
		   	 var leftid=params;
		   	 
		   	 oCar.last=[];
		   	
		   
		   	 var rightTables = $("#core_role_list td input");
		   
		   	 for(j = 0;j < rightTables.length;j++){
		   		 if($(rightTables[j]).prop("checked") == true){
		   			 var names = $(rightTables[j]).parent().parent(); 
		   			 var firstIds = $(names).children()[0];
		   			 var idValuess = $(firstIds).html();
		   			 oCar.last.push(idValuess);
		   		 }
		   	 }
		   	 
		 	var rightIds=oCar.last;
		 	
		   	 $.ajax({
					type: "post",
					url: "/role/save_action",
					async:false,
					dataType: "json",
					data:{leftId:leftid,rightIds:rightIds.toString()},
					success :function(data){
						if(data.msg==true){
							swal({
								   title:"提示",
								   text:"保存成功!",
								   type:"success"
							   })
						}else{
							swal({
								   title:"提示",
								   text:"保存失败,请联系管理员!",
								   type:"error"
							   })
							
						}
						
					}
					
			 });
		   	
	   }
   	 
    })
  
function refresh(){
	$("#role_list").jqGrid('setGridParam',{
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
})