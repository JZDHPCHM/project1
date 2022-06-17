/**
 * @打开评级试算页
 * @returns
 */
var rating=""; //上一步下一步的Id传递
var statusAll="";
function showProgress(ratingId,cus,tem,status){
	debugger
	rating=ratingId;
	custNo=cus;
	tempType=tem;
	statusAll=status;
	//initSteps(rating);
	//$('.chosen-select').chosen({width: "100%"});
	if((rating)&&(tem)&&tem!="null"&&rating!="null"){
		 initSteps(rating);    	 
	}else{
		swal({
	        title: "",
	        text: "<div class='row'><div class='col-lg-12'><form class='form-horizontal' id='modelChange'><div class='form-group'><label class='col-sm-4 control-label' style='padding: 0;'>主体评级模版</label><div class='col-sm-6'><select class='chosen-select' id='' name='modelSteps' style='width: 100%;'><option value=''>请选择</option><option value='UNIVERSAL_S_SODEL2'>生产型</option><option value='UNIVERSAL_S_SODEL'>服务型</option><option value='NEW_BUILD'>新建型</option></select></div></div></form></div></div>",
	        html: true,
	        //type: "prompt",
	        showCancelButton: true,
	        cancelButtonColor: "#ed5565",
	        confirmButtonColor: "#1c84c6",
	        cancelButtonText: "取消",
	        confirmButtonText: "确定",
	        closeOnConfirm: false
	    }, function () {
	  	  swal.close();
	  	var obj = $("#modelChange").serializeObject();
	  	tempType=obj.modelSteps;
	  	  initSteps(rating);
	  
	    });
	}
}

/**
 * @初始化评级步骤菜单
 * @returns
 */
function initSteps(rating){
	$.ajax({
		url : "/irs/mainRating/getRatingById",
		data : {
			ratingId : rating
		},
		async : false,
		success : function(result) {
			if(!result){
				$("#myModal").modal("hide");
				$.ajax({
					url : "/irs/ratingMainStep/createRating",
					data : {
						bpCode : custNo
					},
					async : false,
					success : function(result) {
						initSteps(result);
					}})
			}else{
				$("#myModal").modal();
				$("#dingliang").show();
				$("#dingxing").hide();
				$("#report").hide();
				$("#footer1").show();
				$("#footer2").hide();
				$("#footer3").hide();
				$("#scoreInfo").hide();
				debugger
				if(statusAll!='000'&&statusAll!='010'&&statusAll!='020'){
					$("#btn_next2").attr("disabled", true);
				}
				$("#dlTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
				$("#dxTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
				$("#reportTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
				quantitative(result.steps[0].id);
				qualitative(result.steps[1].id);
			}
		}
	})
}
/**
 * @加载定量列表页
 * @returns
 */
function quantitative(steps){
	$.ajax({
		url : "/irs/ratingIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			var html="<tr class='table-head'><td width='50%'>名称</td><td width='50%'>指标值</td></tr>";
				html+="<tr align='left' class='parent' id='row1'><td colspan='3' class='index-name'><img src='/image/group_opened.png' />"+result.response.data[0].indexCategory+"</td></tr>";
				$.each(result.response.data,function(i,row){
					var valueIndex = "";
					if(row.indexValue!=null){
						valueIndex=row.indexValue;
					}
				html+="<tr class='child_row1'><td>"+row.indexName+"</td><td><input type='text' name='"+row.indexCode+"' value='"+valueIndex+"' /></td></tr>";
				})
			$("#dlListof").html(html);
		}
	})
	
}

/**
 * @加载定量列表页
 * @returns
 */
function qualitative(steps){
	$.ajax({
		url : "/irs/ratingIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			var html="";
			var lastParentId="";
				$.each(result.response.data,function(i,row){
					if(lastParentId==row.parentId){
						html+="<div class='child_row1'><div class='options-directory'>"+row.indexName+"</div>";
						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"'/>"+dxText[key]+"</label>";
							}
					    }
					}else{
						html+="<div class='parent index-name'  id='"+row.parentId+"'><img src='/image/group_opened.png' />"+row.indexCategory+"</div>";
						html+="<div class='child_row1'><div class='options-directory' >"+row.indexName+"</div>";
						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"' />"+dxText[key]+"</label>";
							}
					    }
					}
					lastParentId=row.parentId;
				})
			$("#dxAnalysis").append(html);
		}
	})
}

/**
 * @评级试算按钮
 * @returns
 */
$("#btn_calculate").click(function(){
	debugger
	var map = {};
	map=$("#dxDl").serializeObject();
	$.ajax({
		url : "/irs/mainRating/startTrial?type="+tempType+"&mainId="+rating,
		type:'GET',
		data:{"map":JSON.stringify(map)},
		dataType:'json',
		contentType:"application/json",
		async : false,
		success : function(result) {
			if(result){
				$("#scoreInfo").css("display","inline-block");
				$("#btn_next2").attr("disabled", false);
				
				var dlScore=result.response.data[0].quanSco*1.0;
				dlScore=dlScore.toFixed(2); 
				$("#dlScore").text(dlScore);
				
				var dxScore=result.response.data[0].qualSco*1.0;
				dxScore=dxScore.toFixed(2);
				$("#dxScore").text(dxScore);
				
				var sumScore=result.response.data[0].sco*1.0;
				sumScore=sumScore.toFixed(2);
				$("#sumScore").text(sumScore);
				
				$("#tab_sheet_1").attr("src", "/irs/mainRating/report?custNo="+result.response.data[0].id)
				$("#tab_sheet_2").attr("src", "/irs/mainRating/ratingSubsidiary?custNo="+result.response.data[0].id)
				//$("#tab_sheet_3").attr("src", "/irs/mainRating/scoringDetails?custNo="+result.response.data[0].id)
			}else{
				swal({
	                title: "提示",
	                text: "计算参数异常！",
	                type: "info"
	            });
			}
		},error:function(){
			swal({
                title: "提示",
                text: "计算失败，程序发生异常！",
                type: "info"
            });
		}
	})
	
})
/**
 * @将form变单转化为json对象
 */
$.fn.serializeObject = function() {  
    var o = {};  
    var a = this.serializeArray();  
    $.each(a, function() {  
        if (o[this.name]) {  
            if (!o[this.name].push) {  
                o[this.name] = [ o[this.name] ];  
            }  
            o[this.name].push(this.value || '');  
        } else {  
            o[this.name] = this.value || '';  
        }  
    });  
    return o;  
}




$("#btn_next2").click(function(){
	if(statusAll!='000'&&statusAll!='010'&&statusAll!='020'){
		swal({
	        title: "",
	        text: "定量、定性指标是否复核完成？",
	        type: "warning",
	        showCancelButton: true,
	        cancelButtonColor: "#ed5565",
	        confirmButtonColor: "#1c84c6",
	        cancelButtonText: "否",
	        confirmButtonText: "是",
	        closeOnConfirm: false
	    }, function () {
	  	  swal.close();
	  	  $("#dingxing").hide();
	  	  $("#dingliang").hide();
	  	  $("#report").show();
	  	  $("#footer1").hide();
	  	  $("#footer2").hide();
	  	  $("#footer3").show();
	  	  $("#dlTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#dxTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#reportTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});    	  
	    });
	}else{
		$("#dingxing").hide();
	  	  $("#dingliang").hide();
	  	  $("#report").show();
	  	  $("#footer1").hide();
	  	  $("#footer2").hide();
	  	  $("#footer3").show();
	  	  $("#dlTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#dxTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  	  $("#reportTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});    	
	  	$("#tab_sheet_1").attr("src", "/irs/mainRating/report?custNo="+rating)
		$("#tab_sheet_2").attr("src", "/irs/mainRating/ratingSubsidiary?custNo="+rating)
		//$("#tab_sheet_3").attr("src", "/irs/mainRating/scoringDetails?custNo="+rating)
	}
	  
})
