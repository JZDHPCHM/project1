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
    		$.ajax({
    			url : "/irs/ratingIndex/getRatingIndexsByStepId",
    			data : {
    				stepId : ratingStep
    			},
    			async : false,
    			success : function(result) {
    				$.each(result.response.data,function(i,row){
    					var value = $("#dxAnalysis").find('input:radio[name='+row.indexCode+']:checked').val();
    					var status = $('.options-directory').parents("#" + row.indexCategory + "div").attr('data-status');

    					if(value==null && status != "hide"){
    						if($("#"+row.indexName+"Div").find("span").length==0){
    							$("#"+row.indexName+"Div").prepend("<span class='glyphicon glyphicon-exclamation-sign'>请选择"+row.indexName+"</span>");
    						}
    						if(calculateFlag){
    							calculateFlag = false;
    						}
    					}else{
    						$("#"+row.indexName+"Div").find("span").remove();
    					}
    				});
    			}
    		})
    		if(!calculateFlag){
    			swal({
    	            title: "提示",
    	            text: "计算参数异常！",
    	            type: "info"
    	        });
    			return false;
    		}
    		var map = {};
    		map=$("#dxDl").serializeObject();
    		$.ajax({
    			url : "/irs/mainRating/startTrial?type="+tempType+"&mainId="+ratingIDGlobal+"&version="+'',
    			type:'GET',
    			data:{"map":JSON.stringify(map)},
    			dataType:'json',
    			contentType:"application/json",
    			async : false,
    			success : function(result) {
    				if(result){
    					var text1=result.response.data[0];
    					if(result==null){
    						swal({
    			                title: "提示",
    			                text: "评级失败，计算异常！",
    			                type: "info"
    			            });
    					}
    					$("#scoreInfo").css("display","inline-block");
    					if(!result.response.data){
    						swal({
    			                title: "提示",
    			                text: "评级失败，计算异常！",
    			                type: "info"
    			            });
    						return;
    					}
    					swal.close();
//    					alert("违约概率:"+result.response.data[0].pd+';   总得分：  '+result.response.data[0].sco+";  评级登记："+result.response.data[0].finalLevel);
//    					window.location.href='/irs/mainRating/mainRatingReport?custNo='+text1.id;
    					postOpenWindow('/irs/mainRating/mainRatingReport',{custNo:text1.id},"_self") ;
//    					var dlScore=result.response.data[0].quanSco*1.0;
//    					dlScore=dlScore.toFixed(2); 
//    					$("#dlScore").text(dlScore);
//    					
//    					var dxScore=result.response.data[0].qualSco*1.0;
//    					dxScore=dxScore.toFixed(2);
//    					$("#dxScore").text(dxScore);
//    					
//    					var sumScore=result.response.data[0].sco*1.0;
//    					sumScore=sumScore.toFixed(2);
//    					$("#sumScore").text(sumScore);
//    					ratingIDGlobal=result.response.data[0].id;
//    					swal.close();
    	//
//    					  $("#report").show();
//    					  $("#footer1").hide();
//    					  $("#footer2").hide();
//    					  $("#footer3").show();
    	//
//    					$("#dlTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
//    					$("#dxTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
//    					$("#reportTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
//    				  $("#tab_sheet_1").attr("src", "/irs/mainRating/report?custNo="+ratingIDGlobal)
//    				  $("#tab_sheet_2").attr("src", "/irs/mainRating/ratingSubsidiary?custNo="+ratingIDGlobal)
//    					$(".filter-box").slideUp(120);
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
    	});

    }
});

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

/**
 * @打开评级试算页
 * @returns
 */
var rating=""; //上一步下一步的Id传递
var statusAll="";
var ratingIDGlobal="";
var ratingStep = "";
var version="";
var dataArray=[];
var tempType='';
var num1=0;
function showProgress(ratingId,cus,tem,status,custName){
	$.ajax({
        async: false,
        type: "GET",
        url: "/irs/mainRating/isExitMainRating",
        data: {
        	custCode:cus,
        },
        success: function (data) {
       	 var res=data;
          if(res.isNext=='1'){      	  
      	    rating=ratingId;
      		ratingIDGlobal=rating;
      		custNo=cus;
      		tempType=tem;
      		statusAll=status;
      			swal({
      		        title: custName?custName:'',
      		        text: "<div class='row'><div class='col-lg-12'><form class='form-horizontal' id='modelChange'>" +
      		        		"<div class='form-group'><label class='col-sm-4 control-label' style='padding: 0;'>主体评级模版</label><div class='col-sm-6'>" +
      		        		"<select class='chosen-select' id='' name='modelSteps' style='width: 100%;'>" +
      		        		"<option value='ZTTYMX_LRX_1_FW'>利润型_服务型</option>" +
      		        		"<option value='ZTTYMX_LRX_2_SC'>利润型_生产型</option>" +
      		        		"<option value='ZTTYMX_SZX_1_FW'>市值型_服务型</option>" +
      		        		"<option value='ZTTYMX_SZX_2_SC'>市值型_生产型</option>" +
      		        		"<option value='ZTTYMX_ZSX_1_FW'>增速型_服务型</option>" +
      		        		"<option value='ZTTYMX_ZSX_2_SC'>增速型_生产型</option>" +
      		        		"</select></div>" +
      		        		"</div></form></div></div>",
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
      			//	  	  $("#myModal").modal("show");
      				  		tempType=obj.modelSteps;
      					  	  initSteps(rating);
      				  	}else{
      				  		 swal.close();
      				  	}
      	       });
        	  
          }else{
        	  swal({
    	            title: "提示",
    	            text: "该客户存在业务系统发起-初评的记录，暂不能进行评级测算！",
    	            type: "info"
    	      });
        	  return;
          }
        }
    }); 
}

/**
 * @初始化评级步骤菜单
 * @returns
 */
function initSteps(rating){
	dataArray=[];
	num1=0;
	$.ajax({
		url : "/irs/mainRating/getRatingById",
		data : {
			ratingId : rating,
			tempType:tempType,
//			version:version
		},
		async : false,
		success : function(result) {
			if(!result){
				$("#myModal").modal("hide");
				$.ajax({
					url : "/irs/ratingMainStep/createRating",
					data : {
						bpCode : custNo,
						tempType:tempType,
//						version:version
					},
					async : false,
					success : function(ratingIdRe) {
						ratingIDGlobal=ratingIdRe;
						$.ajax({
							url : "/irs/mainRating/getRatingById",
							type:'GET',
							data : {
								ratingId : ratingIDGlobal,
								tempType:tempType,
							},
							async : false,
							success : function(result) {
//								$(".filter-box").slideUp(120);
								$(".filter-box").show();
								  console.log(5551,result);
//								  $("#myModal").modal("hide");	
								  $("#myModal").modal("show");	
								  qualitative1(result);	 
							}
						})
//						initSteps(ratingIdRe);
					}})
			}else{
								$("#myModal").modal();
				//				$("#dingliang").show();
				//				$("#dingxing").hide();
								$("#report").hide();
				//				$("#footer1").show();
				//				$("#footer2").hide();
								$("#footer3").hide();
								$("#scoreInfo").hide();
				//				$("#dlTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
				//				$("#dxTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
								$("#dxTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
								$("#reportTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
				//定量分析
			//	quantitative(result.steps[0].id);
								
								
//				qualitative(result.steps[1].id);
//				ratingStep = result.steps[1].id;
								
								$(".filter-box").show();
								console.log(5552,result);	
								 $("#myModal").modal("show");
								 if(result.steps){
									 qualitative1(result);	
								 }
								 		
								
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

function openOrClose(indexCategory,indexId){
	var display = $("."+indexId+"div").css("display");
	if(display=="none"){
		$("."+indexId).find("img").attr("src","/image/group_opened.png");
		$("."+indexId+"div").css("display","block");
	}else{
		$("."+indexId).find("img").attr("src","/image/group_closed.png");
		$("."+indexId+"div").css("display","none");
	}
}
function openOrClose1(indexCategory,parentId){
	var display = $("."+parentId+"div").css("display");
	if(display=="none"){
		$("."+parentId).find("img").attr("src","/image/group_opened.png");
		$("."+parentId+"div").css("display","block");
	}else{
		$("."+parentId).find("img").attr("src","/image/group_closed.png");
		$("."+parentId+"div").css("display","none");
	}
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
			$("#dxAnalysis").html("");
			$("#dxAnalysis").append('<h5 class="progress-title">评级信息录入  </h5>');

			// 左侧评级步骤 条目列表
			var optionList = "";

			var html="";
			var lastParentId="";
			var sn = 1;
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
		url : "/irs/ratingIndex/getRatingIndexsByStepId",
		data : {
			stepId : id
		},
		async : false,
		success : function(result) {
			result.response.data.map(item=>{
				dataArray.push(item);
				num1+=1;
			});
			qualitative2(id2);
				
		}
	})
}
function qualitative2(steps){
	$.ajax({
		url : "/irs/ratingIndex/getRatingIndexsByStepId",
		data : {
			stepId : steps
		},
		async : false,
		success : function(result) {
			result.response.data.map(item=>{
				dataArray.push(item);
			});
//			console.log(dataArray);
			$("#dxAnalysis").html("");
			$("#dxAnalysis").append('<h5 class="progress-title">评级信息录入</h5>');
			// 左侧评级步骤 条目列表
			var optionList = "";

			var html="";
			var lastParentId="";
			var sn = 1;
			var number=0;
			var is_true=false;
			var str='';
			var num2=0;
				$.each(dataArray,function(i,row){
					
					if(!row.parentId){
	                     // 左侧评级步骤 条目列表循环
						optionList += "<label><input type='checkbox' data-id='" + row.indexId + "' checked>" + row.indexCategory + "</label>";
					}					
					if(lastParentId==row.parentId&&row.indexType=='QUALITATIVE_EDIT'){//定性
						if(!is_true){
							
						}else{
							html+="<div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory'>"+sn+"、"+row.indexName+"</div>";
							
							var dxText=JSON.parse(row.dxText);
							for (var key in dxText){
								if(row.indexValue==key){
									html+="<label class='selected'><input type='radio' name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
								}else{//选项-内层
									html+="<label><input type='radio' required name='"+row.indexCode+"' value='"+key+"'/>"+dxText[key]+"</label>";
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
						
						if(row.indexType=='QUANTITATIVE'){//定量
							if(!row.parentId){
						
								num2+=1;
								html+="<div class='parent index-name "+row.parentId+" "+row.indexId+"'   onclick='openOrClose1(\""+row.indexCategory+"\",\""+row.indexId+"\")' id='"+row.parentId+"'><img src='/image/group_opened.png' />"+row.indexName+"</div>";
							}else{//指标基础
							
								num2+=1;
								str+="<div class='item'><label>"+row.indexName+"</label><input class='form-control' required  name='"+row.indexCode+"' /></div>";								

//								html+="<div class='"+row.parentId+"div'><div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory' ><label>"+row.indexName+"</label><input class='form-control'  name='"+row.indexCode+"'/></div>";
							}
							 if(num1==num2){
								 html+="<div class='"+row.parentId+"div   items'>"+str+"</div>";
							 }
							
//							html+="<div class='"+row.indexId+"div'><div class='child_row1'><div id='"+row.indexName+"Div' class='options-directory' ><input class='form-control'  name='"+row.indexCode+"'/></div>";
							is_true=false;
						}else if(row.indexType=='QUALITATIVE_EDIT'&&row.parentId){
							is_true=true;
							html+="<div class='parent index-name "+row.parentId+" "+row.indexId+"'   onclick='openOrClose(\""+row.indexCategory+"\",\""+row.indexId+"\")' id='"+row.parentId+"'><img src='/image/group_opened.png' />"+row.indexCategory+"</div>";
							html+="<div class='"+row.indexId+"div' ><div class='child_row1  child_row1_no1'><div id='"+row.indexName+"Div' class='options-directory' >"+sn+"、"+row.indexName+"</div>";
							var dxText=JSON.parse(row.dxText);
							for (var key in dxText){
								if(row.indexValue==key){
									html+="<label class='selected' ><input type='radio'   name='"+row.indexCode+"' value='"+key+"' checked/>"+dxText[key]+"</label>";
								}else{//选项-外层
									html+="<label  ><input type='radio'  required name='"+row.indexCode+"' value='"+key+"' />"+dxText[key]+"</label>";
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
							var optionId = '.' + $(this).attr('data-id');
							if ($(this).is(':checked')) {
								// 选中
								$(optionId).slideDown();
								$(optionId).next('div').slideDown();
								$(optionId).next('div').attr('data-status', 'show');
							} else {
								// 取消选中
								$(optionId).slideUp();
								$(optionId).next('div').slideUp();

							/*	// 隐藏后清除列表选中状态
								$(optionId).next('div').attr('data-status', 'hide');
								$(optionId).next('div').find('label.selected').removeClass('selected');
								$(optionId).next('div').find('input[type="radio"]').prop('checked', false);*/
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




$("#btn_next2").click(function(){
//	postOpenWindow('/irs/mainRating/mainRatingReport',{custNo:'8281ec2b-fb90-4343-b10a-e4651a686922'},"_blank") ;
	$("#dxDl").validate();
})
