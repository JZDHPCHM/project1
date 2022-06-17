/**
 * 
 */
function jqGridNoRecords(tableId){
	var re_records = $("#"+ tableId).getGridParam("records");
	if(re_records == 0 || re_records == null){
		if($("#"+tableId+"_norecords").html() == null){
			$("#"+tableId).parent().append("<div id=\""+tableId+"_norecords\" class=\"norecords\">本次查询无数据！</div>");
			$("#"+tableId+"_norecords").show();
		}else{
			$("#"+tableId+"_norecords").show();
		}
	}else{
		$("#"+tableId+"_norecords").hide();
	}
}
