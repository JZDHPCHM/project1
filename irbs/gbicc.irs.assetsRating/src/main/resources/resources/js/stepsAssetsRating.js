$.validator.setDefaults({
    submitHandler: function() {
    	var calculateFlag = true;
    	swal({
    	    title: "",
    	    text: "请确认修改信息，系统将生成评级报告。",
    	    type: "warning",
    	    showCancelButton: true,
    	    cancelButtonColor: "#ed5565",
    	    confirmButtonColor: "#1c84c6",
    	    cancelButtonText: "否",
    	    confirmButtonText: "是",
    	    closeOnConfirm: false
    	}, function () {
    		var map = {};
    		map=$("#dxDl").serializeObject();
    		var type=$("#tempType option:selected").val();
    		var obj={
    				type:$("#tempType option:selected").val(),
    				map:map,
    				mainId:mainId,
    		};
    		
    		swal.close();
    		$.ajax({
    			url : "/irs/assetsRating/testAssetsModel?type="+type+"&mainId="+mainId,
    			type:'GET',
    			data:{"map":JSON.stringify(map)},
    			dataType:'json',
    			contentType:"application/json",
    			async : false,
    			success : function(result) {
    				console.log('xxxxx',result);
    				
    				if(result){
    					if(result==null){
    						swal({
    			                title: "提示",
    			                text: "评级失败，计算异常！",
    			                type: "info"
    			            });
    					}
    					var text1=result.response.data[0];
    					//finalLevel 评级等级；
    					//finalSco 评级分数
    					//finalPd  保障倍数
//    					alert('评级等级:'+text1.finalLevel+';评级分数:'+text1.finalSco+';保障倍数'+text1.finalPd);
    					postOpenWindow('/irs/assetsRating/assetsRatingReport',{custNo:text1.id},"_self") ;
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
    		});
    	});

    }
});


/**
 * @打开评级试算页
 * @returns
 */
var rating=""; //上一步下一步的Id传递
var statusAll="";
var ratingIDGlobal="";
var ratingStep = "";
var version="";
var mainId='';
var dataArray=[];
function showProgress(ratingId,cus,tem,status){
	rating=ratingId;
	ratingIDGlobal=rating;
	custNo=cus;
	tempType=tem;
	statusAll=status;
		swal({
	        title: "",
	        text: "<div class='row'><div class='col-lg-12'><form class='form-horizontal' id='modelChange'><div class='form-group'><label class='col-sm-4 control-label' style='padding: 0;'>主体评级模版</label><div class='col-sm-6'><select class='chosen-select' id='' name='modelSteps' style='width: 100%;'><option value='UNIVERSAL_S_SODEL2'>生产型</option><option value='UNIVERSAL_S_SODEL'>服务型</option><option value='NEW_BUILD'>新建型</option></select></div></div><div class='form-group'><label class='col-sm-4 control-label' style='padding: 0;'>主体评级版本</label><div class='col-sm-6'><select class='chosen-select' id='' name='version' style='width: 100%;'><option value='1.0'>评级1.0</option><option value='2.0'>评级2.0</option></select></div></div></form></div></div>",
	        html: true,
	        //type: "prompt",
	        showCancelButton: true,
	        cancelButtonColor: "#ed5565",
	        confirmButtonColor: "#1c84c6",
	        cancelButtonText: "取消",
	        confirmButtonText: "确定",
	        closeOnConfirm: false
	    }, function () {
	  	var obj = $("#modelChange").serializeObject();
	  	version = obj.version;
	  	if(obj.modelSteps){
	  	  swal.close();
	  		tempType=obj.modelSteps;
		  	  initSteps(rating);
	  	}else{
	  		 swal.close();
	  	}
});

}

/**
 * @初始化评级步骤菜单
 * @returns
 */
function initSteps(rating,tempType,csCode,prCode){
	$.ajax({
		url : "/irs/assetsRating/getAssetsRatingById",
		type:'GET',
		data : {
			ratingId : rating,
			tempType:tempType,
		},
		async : false,
		success : function(result) { 
			$("#dxAnalysis").html('');
			$('#filter-content').html('');
			dataArray=[];
			   if(!result){
				   $("#myModal").modal("hide");
					$.ajax({
						url : "/irs/assetsRating/createAssetsRating",
						data : {
							bpCode : csCode,
							tempType:tempType,
							proCode:prCode,
							ratingid:rating,
						},
						async : false,
						success : function(ratingIdRe) { 
							mainId=ratingIdRe;
							$.ajax({
								url : "/irs/assetsRating/getAssetsRatingById",
								type:'GET',
								data : {
									ratingId : rating,
									tempType:tempType,
								},
								async : false,
								success : function(result) {
									  $("#myModal").modal("hide");	
									  $("#myModal2").modal("show");	
									  console.log(222,result);
									  qualitative1(result);
								}
							})
							
							
						}})
			}else{
//						$("#myModal").modal();
//		//				$("#dingliang").show();
//		//				$("#dingxing").hide();
//						$("#report").hide();
//		//				$("#footer1").show();
//		//				$("#footer2").hide();
//						$("#footer3").hide();
//						$("#scoreInfo").hide();
//		//				$("#dlTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
//		//				$("#dxTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
//						$("#dxTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
//						$("#reportTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
//						//定量分析
//		//				quantitative(result.steps[0].id);
//						qualitative(result.steps[1].id);
//						ratingStep = result.steps[1].id;
						
				
				
				  $("#myModal").modal("hide");	
				  $("#myModal2").modal("show");	
				  qualitative1(result);
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
			$("#dlListof").html("");
			var html="<tr class='table-head'><td width='90%'>名称</td><td width='10%' align='right'>指标值</td></tr>";
				html+="<tr align='left' class='parent' id='row1'><td colspan='3' class='index-name'><img src='/image/group_opened.png' />"+result.response.data[0].indexCategory+"</td></tr>";
				$.each(result.response.data,function(i,row){
					var valueIndex = "";
					if(row.indexValue!=null){
						valueIndex=row.indexValue;
					}
				html+="<tr class='child_row1'><td>"+row.indexName+"</td><td><input type='text' name='"+row.indexCode+"' value='"+(parseFloat(valueIndex).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g, '$1,').replace("NaN","")+"' /></td></tr>";
				})
			$("#dlListof").html(html);
			
		}
	})
	
}

function openOrClose(indexCategory,parentId){
	var display = $("#"+indexCategory+"div").css("display");
	if(display=="none"){
		$("#"+parentId).find("img").attr("src","/image/group_opened.png");
		$("#"+indexCategory+"div").css("display","block");
	}else{
		$("#"+parentId).find("img").attr("src","/image/group_closed.png");
		$("#"+indexCategory+"div").css("display","none");
	}
}

/**
 * @加载定量列表页
 * @returns
 */

function qualitative(steps){
	$.ajax({
		url : "/irs/assetsIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			$("#dxAnalysis").html("");
			$("#dxAnalysis").append('<h5 class="progress-title">评级信息录入  </h5>');

			// 左侧评级步骤 条目列表
			var optionList = "";

			var html="";
			var lastParentId="";
			var sn = 1;
			var number=0;
				$.each(result.response.data,function(i,row){
				if(!row.parentId){
					return;
				}
					if(lastParentId==row.parentId){
						html+="<div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory'>"+sn+"、"+row.indexName+"</div>";
						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label class='selected'><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio'  required name='"+row.indexCode+"' value='"+key+"'/>"+dxText[key]+"</label>";
							}
					    }
					}else{
						if(html!=''){
							html += "</div>";
							$("#dxAnalysis").append(html);
							html = '';
							sn = 1;
						}
						
						html+="<div class='parent index-name' onclick='openOrClose(\""+row.indexCategory+"\",\""+row.parentId+"\")' id='"+row.parentId+"'><img src='/image/group_opened.png' />"+row.indexCategory+"</div>";
						html+="<div id='"+row.indexCategory+"div'><div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory' >"+sn+"、"+row.indexName+"</div>";

						// 左侧评级步骤 条目列表循环
						optionList += "<label><input type='checkbox' data-id='" + row.parentId + "' checked>" + row.indexCategory + "</label>";

						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label class='selected'><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio'  name='"+row.indexCode+"' value='"+key+"' />"+dxText[key]+"</label>";
							}
					    }
					}
					
					if(i==result.response.data.length-1){
						$("#dxAnalysis").append(html);
						html = '';
						sn = 1;
						// 左侧评级步骤 条目列表选择功能
						$('#filter-content').html(optionList);
						$('#filter-content').on('click', 'input', function () {
							var optionId = '#' + $(this).attr('data-id');
							if ($(this).is(':checked')) {
								// 选中
								$(optionId).slideDown();
								$(optionId).next('div').slideDown();
								$(optionId).next('div').attr('data-status', 'show');
							} else {
								// 取消选中
								$(optionId).slideUp();
								$(optionId).next('div').slideUp();

								// 隐藏后清除列表选中状态
								$(optionId).next('div').attr('data-status', 'hide');
								$(optionId).next('div').find('label.selected').removeClass('selected');
								$(optionId).next('div').find('input[type="radio"]').prop('checked', false);
							}
						})
					}
					lastParentId=row.parentId;
					sn+=1;
				})
		}
	})
}
function qualitative1(result1){
	var id='';
	var id2='';
	if(result1.steps){
		result1.steps.map(item=>{
			if(item.stepType=='QUANTITATIVE'){
				id=item.id;
			}else if(item.stepType=='QUALITATIVE_EDIT'){
				id2=item.id;
			}
		})
	}
	$.ajax({
		url : "/irs/assetsIndex/getRatingIndexsByStepId",
		data : {
			stepId : id
		},
		async : false,
		success : function(result) {
			result.response.data.map(item=>{
				dataArray.push(item);
			});
			qualitative2(id2);
				
		}
	})
}
function qualitative2(steps){
	$.ajax({
		url : "/irs/assetsIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			result.response.data.map(item=>{
				dataArray.push(item);
			});
			console.log(dataArray);
			$("#dxAnalysis").html("");
			$("#dxAnalysis").append('<h5 class="progress-title">评级信息录入  </h5>');
			// 左侧评级步骤 条目列表
			var optionList = "";
			var html="";
			var lastParentId="";
			var sn = 1;
			var number=0;
			var is_true=false;
				$.each(dataArray,function(i,row){
					
					if(!row.parentId){
	                     // 左侧评级步骤 条目列表循环
						optionList += "<label><input type='checkbox' data-id='" + row.indexId + "' checked>" + row.indexCategory + "</label>";
					}					
					if(lastParentId==row.parentId&&row.indexType=='QUALITATIVE_EDIT'){
						if(!is_true){
							
						}else{
							html+="<div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory'>"+sn+"、"+row.indexName+"</div>";
							
							var dxText=JSON.parse(row.dxText);
							for (var key in dxText){
								if(row.indexValue==key){
									html+="<label class='selected'><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
								}else{
									html+="<label><input type='radio' required  name='"+row.indexCode+"' value='"+key+"'/>"+dxText[key]+"</label>";
								}
						    }
						}
						
						
					}else{					
						if(html!=''){
							html += "</div>";
							$("#dxAnalysis").append(html);
							html = '';
							sn = 1;
						}
						if(row.indexType=='QUANTITATIVE'){
							html+="<div class='parent index-name'  onclick='openOrClose(\""+row.indexCategory+"\",\""+row.indexId+"\")' id='"+row.indexId+"'><img src='/image/group_opened.png' />"+row.indexCategory+"</div>";
							
							html+="<div id='"+row.indexCategory+"div'><div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory options-directory1' ><input class='form-control' required value='"+row.indexValue+"' name='"+row.indexCode+"'/></div>";
							is_true=false;
						}else {
							is_true=true;
							if(!row.parentId){
								return;
							}
							html+="<div class='parent index-name' onclick='openOrClose(\""+row.indexCategory+"\",\""+row.parentId+"\")' id='"+row.parentId+"'><img src='/image/group_opened.png' />"+row.indexCategory+"</div>";
							html+="<div id='"+row.indexCategory+"div'><div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory ' >"+sn+"、"+row.indexName+"</div>";
							var dxText=JSON.parse(row.dxText);
							for (var key in dxText){
								if(row.indexValue==key){
									html+="<label class='selected'><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
								}else{
									html+="<label><input type='radio'  required name='"+row.indexCode+"' value='"+key+"' />"+dxText[key]+"</label>";
								}
						    }
						}
						
                       
						
					}
					if(i==dataArray.length-1){
						$("#dxAnalysis").append(html);
						html = '';
						sn = 1;
						// 左侧评级步骤 条目列表选择功能
						$('#filter-content').html(optionList);
						$('#filter-content').on('click', 'input', function () {
							var optionId = '#' + $(this).attr('data-id');
							if ($(this).is(':checked')) {
								// 选中
								$(optionId).slideDown();
								$(optionId).next('div').slideDown();
								$(optionId).next('div').attr('data-status', 'show');
							} else {
								// 取消选中
								$(optionId).slideUp();
								$(optionId).next('div').slideUp();

								// 隐藏后清除列表选中状态
//								$(optionId).next('div').attr('data-status', 'hide');
//								$(optionId).next('div').find('label.selected').removeClass('selected');
//								$(optionId).next('div').find('input[type="radio"]').prop('checked', false);
							}
						})
					}
					
					lastParentId=row.parentId;
					sn+=1;
				})
		}
	})
}



function qualitative(steps){
	$.ajax({
		url : "/irs/assetsIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			$("#dxAnalysis").html("");
			$("#dxAnalysis").append('<h5 class="progress-title">评级信息录入  </h5>');

			// 左侧评级步骤 条目列表
			var optionList = "";

			var html="";
			var lastParentId="";
			var sn = 1;
			var number=0;
				$.each(result.response.data,function(i,row){
			
					if(lastParentId==row.parentId){
						html+="<div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory'>"+sn+"、"+row.indexName+"</div>";
						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label class='selected'><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"'/>"+dxText[key]+"</label>";
							}
					    }
					}else{
					
						if(html!=''){
							html += "</div>";
							$("#dxAnalysis").append(html);
							html = '';
							sn = 1;
						}
						html+="<div class='parent index-name' onclick='openOrClose(\""+row.indexCategory+"\",\""+row.parentId+"\")' id='"+row.parentId+"'><img src='/image/group_opened.png' />"+row.indexCategory+"</div>";
						html+="<div id='"+row.indexCategory+"div'><div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory' >"+sn+"、"+row.indexName+"</div>";

						// 左侧评级步骤 条目列表循环
						optionList += "<label><input type='checkbox' data-id='" + row.parentId + "' checked>" + row.indexCategory + "</label>";

						var dxText=JSON.parse(row.dxText);
						for (var key in dxText){
							if(row.indexValue==key){
								html+="<label class='selected'><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
							}else{
								html+="<label><input type='radio' name='"+row.indexCode+"' value='"+key+"' />"+dxText[key]+"</label>";
							}
					    }
					}
					
					if(i==result.response.data.length-1){
						$("#dxAnalysis").append(html);
						html = '';
						sn = 1;
						// 左侧评级步骤 条目列表选择功能
						$('#filter-content').html(optionList);
						$('#filter-content').on('click', 'input', function () {
							var optionId = '#' + $(this).attr('data-id');
							if ($(this).is(':checked')) {
								// 选中
								$(optionId).slideDown();
								$(optionId).next('div').slideDown();
								$(optionId).next('div').attr('data-status', 'show');
							} else {
								// 取消选中
								$(optionId).slideUp();
								$(optionId).next('div').slideUp();

								// 隐藏后清除列表选中状态
								$(optionId).next('div').attr('data-status', 'hide');
								$(optionId).next('div').find('label.selected').removeClass('selected');
								$(optionId).next('div').find('input[type="radio"]').prop('checked', false);
							}
						})
					}
					lastParentId=row.parentId;
					sn+=1;
				})
		}
	})
}

/**
 * @评级试算按钮
 * @returns
 */
/*$("#btn_calculate").click(function(){
	
})*/
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
            o[this.name].push(this.value.replace(new RegExp(",","gm"),"").replace(new RegExp(" ","gm"),"").replace(new RegExp("，","gm"),"") || '');  
        } else {  
            o[this.name] = this.value.replace(new RegExp(",","gm"),"").replace(new RegExp(" ","gm"),"").replace(new RegExp("，","gm"),"") || '';  
        }  
    });  
    return o;  
}

function postOpenWindow(URL, PARAMS, target) {
    if(target == null) target = "_blank";
    var temp_form = document.createElement("form");
    temp_form.action = URL;
    temp_form.target = target;
    temp_form.method = "post";
    temp_form.style.display = "none";
    for(var x in PARAMS) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = PARAMS[x];
        temp_form.appendChild(opt);
    }
    document.body.appendChild(temp_form);
    temp_form.submit();
    document.body.removeChild(temp_form);
}


$("#btn_next2").click(function(){
	$("#dxDl").validate();
})
