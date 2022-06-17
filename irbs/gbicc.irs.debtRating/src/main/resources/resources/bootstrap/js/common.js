/**
 * @author liuxiaofei
 */
//承租人名称
var lesseeName = [];
$.ajax({
	url : "/bpMaster/SearchBpMasterName",
	type : 'GET',
	dataType : 'json',
	async : false,
	success : function(result) {
		lesseeName = result;
	}
});
$('#lesseeName').typeahead({
	source : lesseeName,
	minLength : 0,//键入字数多少开始补全
	showHintOnFocus : "true",//将显示所有匹配项
	fitToElement : true,//选项框宽度与输入框一致
	items : 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	autoSelect : true,//允许你决定是否自动选择第一个建议
});
var entryName = [];
$.ajax({
	  url : "/irs/assetsRating/SearchpRrojectNameOrCode",
	  type:'GET',
	  data:{
		  col:'PROJECT_NAME',
	  },
    async : false,
	  success : function(result) {
		  entryName=result;
	  }
});
$('#entryName').typeahead({
	  source: entryName,
	  minLength: 0,//键入字数多少开始补全
	  showHintOnFocus: "true",//将显示所有匹配项
	  fitToElement: true,//选项框宽度与输入框一致
	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	  autoSelect: true//允许你决定是否自动选择第一个建议
});
var entryCode = [];
$.ajax({
	  url : "/irs/assetsRating/SearchpRrojectNameOrCode",
	  type:'GET',
	  data:{
		  col:'PROJECT_NUMBER',
	  },
    async : false,
	  success : function(result) {
		  entryCode=result;
	  }
});
$('#entryCode').typeahead({
	  source: entryCode,
	  minLength: 0,//键入字数多少开始补全
	  showHintOnFocus: "true",//将显示所有匹配项
	  fitToElement: true,//选项框宽度与输入框一致
	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	  autoSelect: true//允许你决定是否自动选择第一个建议
});


var SearchBpMasterCode = [];
$.ajax({
	  url : "/bpMaster/SearchBpMasterCode",
	  type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  SearchBpMasterCode=result;
	  }
});
$('#SearchBpMasterCode').typeahead({
	  source: SearchBpMasterCode
});
//项目经理
var userSearch = [];
$.ajax({
	url : "/bpMaster/userSearch",
	type : 'GET',
	dataType : 'json',
	async : false,
	success : function(result) {
		userSearch = result;
	}
});
$('#userSearch').typeahead({
	source : userSearch,
	minLength : 0,//键入字数多少开始补全
	showHintOnFocus : "true",//将显示所有匹配项
	fitToElement : true,//选项框宽度与输入框一致
	items : 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	autoSelect : true,//允许你决定是否自动选择第一个建议
});
//资产经理
var userSearch1 = [];
$.ajax({
	url : "/bpMaster/userSearch",
	type : 'GET',
	dataType : 'json',
	async : false,
	success : function(result) {
		userSearch1 = result;
	}
});


var userSearch2 = [];
$.ajax({
	url : "/bpMaster/userSearch",
	type : 'GET',
	dataType : 'json',
	async : false,
	success : function(result) {
		userSearch2 = result;
	}
});

$('#userSearch2').typeahead({
	source : userSearch2,
	minLength : 0,//键入字数多少开始补全
	showHintOnFocus : "true",//将显示所有匹配项
	fitToElement : true,//选项框宽度与输入框一致
	items : 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	autoSelect : true,//允许你决定是否自动选择第一个建议
});


$('#userSearch1').typeahead({
	source : userSearch1,
	minLength : 0,//键入字数多少开始补全
	showHintOnFocus : "true",//将显示所有匹配项
	fitToElement : true,//选项框宽度与输入框一致
	items : 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	autoSelect : true,//允许你决定是否自动选择第一个建议
});
$('.chosen-select').chosen({
	width : "100%"
});
$('#date2').datepicker({
	  autoclose : true,
    todayHighlight : true,
    endDate : new Date()
 }).on('show',function(e){
    if ($('#date1').val() == '') {
       //$('#date2').datepicker('setEndDate', null);
    }
 }).on('changeDate',function(e){
    if (e.date) {
       $('#date1').datepicker('setStartDate', new Date(e.date.valueOf()));
    } else {
       $('#date1').datepicker('setStartDate', null);
    }
 });
 
 $('#date1').datepicker({
    autoclose : true,
    todayHighlight : true,
    endDate : new Date()
 }).on('show',function(e){
    if ($('#date2').val() == '') {
       //$('#date1').datepicker('setStartDate', null);
    }
 }).on('changeDate',function(e){
    if (e.date) {
       $('#date2').datepicker('setEndDate', new Date(e.date.valueOf()));
    } else {
       $('#date2').datepicker('setEndDate', null);
    }
 });
