/*空判断*/
function isEmpty(obj){
    if(typeof obj == "undefined" || obj == null || obj == ""){
        return true;
    }else{
        return false;
    }
}

/*非空判断*/
function isNotEmpty(obj){
	return !isEmpty(obj);
}

/*下拉框空判断*/
function isSelect(obj){
    if(typeof obj == "undefined" || obj == null || obj == "请选择" || obj == ''){
        return true;
    }else{
        return false;
    }
}

/*下拉框非空判断*/
function isNotSelect(obj){
	return !isSelect(obj);
}

/*IF转换成可计算的eval公式*/
function ifFormulaTransEvalFormula(obj){
	if(isEmpty(obj)){
		return obj;
	}
	if(!(obj.indexOf('IF') > -1)){
		return obj;
	}
	var resu = '';
	obj = obj.replace('IF','').replace('(','').replace(')','');
	var arr = obj.split(',');
	for(var i=0;i<arr.length;i++){
		resu += arr[i];
		if(i == arr.length-1){
			break;
		}
		if(i%2==0){
			resu += '?';
		}else{
			resu += ':';
		}
	}
	return resu;
}

/* 指标编码简单规则
 * 规则： 直接取机构编码，不足位数在前面补0
 * */
function indexCodeSimpleRule(orgCode,num){
	var res = '';
	if(isEmpty(orgCode)){
		for(var i=0;i<num;i++){
			res += '0';
		}
		return res;
	}
	var length = orgCode.length;
	res += orgCode;
	for(var i=0;i<num-length;i++){
		res += '0';
	}
	return res;
}

/*配置表格
 * tableId 表格ID（jquery格式）
 * pageId  分页ID（jquery格式）
 * url	       请求URL
 * colNames 表头
 * colModel 列属性
 * */
function jqGridConfig(tableId,pageId,url,colNames,colModel){
	$(tableId).jqGrid('GridUnload');
	$(tableId).jqGrid({
		url : url,
		recordtext: "显示：{0} - {1}，总数：{2} ",
	    emptyrecords: "本次查询无数据！",
	    loadtext: "读取中...",
	    loadui:"block",
	    pgtext : "第 {0}页， 共{1}页",
		mtype : "get",
        datatype: "json",
        height: 350,
        autowidth: true,
        shrinkToFit: false,
        prmNames: {//表示请求页码的参数名称
            page: "page",
            rows: "size"
        },
        page : 0,
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames : colNames,
        colModel : colModel,
        pager: pageId,
        viewrecords: true,
        caption: "",
        add: true,
        edit: true,
        addtext: 'Add',
        edittext: 'Edit',
        rownumbers: true,
        hidegrid: true,
        jsonReader : {
        	root:"response.data",
        	records: "response.totalElements",
        	total:"response.totalPages",
        	page:"response.number"
        }
    });
    $(tableId).jqGrid('navGrid', pageId,
            {edit: false, add: false, del: false, search: false},
            {height: 200, reloadAfterSubmit: true}
    );
    $(window).bind('resize', function () {
        var width = $('.jqGrid_wrapper').width();
        $(tableId).setGridWidth(width);
    });
}

/*
 * 还原表单(包括hidden)
 * formId 表单ID
 * */
function defineResetForm(formId){
	$(formId)[0].reset();
	$(formId+' input[type="hidden"]').val('');
}

/*
 * IRR流程任务的请求处理
 * 传参数的格式为json，后台需要用@RequestBody注解
 * */
function processAjaxHandleJson(url, param) {
	$.ajax({
		url : url,
		type : "post",
		dataType : "json",
		data : JSON.stringify(param),
		contentType : "application/json;charset=utf-8",
		success : function(data,textStatus) {
			if(data.flag == true){
				swal({
					title : "提示",
					type : "success",
					text : data.data
				}, function() {
					window.parent.opener.location.reload();
					window.parent.close();
					/*//调用刷新列表方法
					window.parent.close();
					var oDiv = window.parent.opener.document.getElementById('todo'); //获取元素div
					oDiv.click(); //执行点击事件，这样就模拟出了自动执行点击事件。*/
				});
			}else{
				swal('提示',data.data,'error');
			}
		},
		error : function(data, textStatus){
			swal('提示','请求失败！','error');
		}
	});
}

/*
 * jquery的ajax的请求
 * url 请求地址
 * param 参数，json对象
 * */
function ajaxRequest(url, param) {
	$.ajax({
		url : url,
		type : "post",
		dataType : "json",
		data : param,
		success : function(data,textStatus) {
			if(data.flag == true){
				swal('提示',data.data,'success');
			}else{
				swal('提示',data.data,'error');
			}
		},
		error : function(data, textStatus){
			swal('提示','请求失败！','error');
		}
	});
}

/*
 * 任务界面刷新
 * */
function taskInterfaceRefresh(){
	window.parent.opener.location.reload();
	window.parent.close();
}