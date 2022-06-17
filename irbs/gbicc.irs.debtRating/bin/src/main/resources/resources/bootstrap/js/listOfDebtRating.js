//主体评级发起列表
var form="";
	$('#btn-query').click(function(){
		form = $('#formSearch').serializeObject();
	    $("#table_list").jqGrid('setGridParam', {postData:form}).trigger("reloadGrid");
	});
	//重置按钮
	$("#btn-reset").click(function(){
		$("#formSearch input").val("");
		$("#formSearch select").val("");
		$("#formSearch select").trigger("chosen:updated");
	});
var custNo="";
var tempType="";
$(document).ready(function(){
	$("#table_list").jqGrid({
		url : "/irs/mainRating/parameterQuery",
		recordtext: "显示：{0} - {1}，总数：{2} ",
	    emptyrecords: "本次查询无数据！",
	    loadtext: "读取中...",
	    pgtext : "第 {0}页， 共{1}页",
		mtype : "GET",
	    datatype: "json",
        height: 300,
        scrollOffset : 0,
        autowidth: true,
        shrinkToFit: true,
        prmNames: {
            page: "page"   // 表示请求页码的参数名称
        },
        rowNum: 20,
        rowList: [10, 20, 30],
        colNames:['承租人编号','承租人名称','承租人类型','项目名称','项目编号','项目经理', '项目状态', '业务部', '业务类别', '业务类型', '评级编号', '评级类型','初评等级','复核等级','总体保障倍数','评级状态','评级日期','操作'],
        colModel:[
        	//{name:'id',index:'id', editable: true,hidden:true},
            {name:'custcode',index:'custCode', editable: true},
            {name:'custname',index:'custName', editable: true},
            {name:'scoretemplateid',index:'scoretemplateid', editable: true},
            {name:'segmentindustry',index:'segmentindustry', editable: true},
            {name:'highprecision',index:'highprecision', editable: true},
            {name:'enterprisehonor',index:'enterprisehonor', editable: true},
            {name:'economic',index:'economic', editable: true},
            {name:'economic',index:'economic', editable: true},
            {name:'economic',index:'economic', editable: true},
            {name:'economic',index:'economic', editable: true},
            {name:'economic',index:'economic', editable: true},
            {name:'type',index:'type', editable: true},
            {name:'internlevel',index:'internlevel', editable: true},
            {name:'finallevel',index:'finallevel', editable: true},
            {name:'pd',index:'pd', editable: true},
            {name:'ratingstatus',index:'ratingstatus', editable: true},
            {name:'finaldate',index:'finaldate', editable: true},
            {name:'id',index:'id', editable: true,align: 'center', formatter:function(value,index,row){
            	custNo=row.custcode;
            	tempType=row.scoretemplateid;
            	return "<button class='btn btn-success btn-small' onclick='showProgress(\""+value+"\",\""+custNo+"\",\""+tempType+"\",\""+row.ratingstatus+"\");'>评级发起</button>";
            }}
        ],
        pager: "#pager_list",
        viewrecords: true,
        caption: "",
        add: true,
        edit: true,
        addtext: 'Add',
        edittext: 'Edit',
        rownumbers: true,
        rownumWidth: 60,
        hidegrid: false,
        jsonReader: {
        	root: "response.data",
        	records: "response.totalRows",
        	//page: "response.currPage", // 当前第几页
            total: "response.totalPages", // 总共多少页
        	repeatitems: false,
        	id: "0"
        }
    });	
	$("#table_list").jqGrid('setLabel','rn','序号',{'text-align':'center','vertical-align':'middle'},'');
});
$(function(){
	   $(window).resize(function(){   
	      $("#table_list").setGridWidth($(window).width());
	   });
	});
