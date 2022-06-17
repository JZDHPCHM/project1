$("#btn_prev1").click(function(){
	  $("#steps1").show();
	  $("#steps2").hide();
	  $("#steps3").hide();
	  $("#steps4").hide();
	  $("#steps5").hide();
	  $("#report").hide();
	  
	  $("#footer1").show();
	  $("#footer2").hide();
	  $("#footer3").hide();
	  $("#footer4").hide();
	  $("#footer5").hide();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#step_txt2").css({"background-color":"#dadada","color":"#9e9e9e"});
      $("#step_txt3").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt4").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt5").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  $("#btn_prev2").click(function(){
	  $("#steps1").hide();
	  $("#steps2").show();
	  $("#steps3").hide();
	  $("#steps4").hide();
	  $("#steps5").hide();
	  $("#report").hide();
	  
	  $("#footer1").hide();
	  $("#footer2").show();
	  $("#footer3").hide();
	  $("#footer4").hide();
	  $("#footer5").hide();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt2").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
      $("#step_txt3").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt4").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt5").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  $("#btn_prev3").click(function(){
	  $("#steps1").hide();
	  $("#steps2").hide();
	  $("#steps3").show();
	  $("#steps4").hide();
	  $("#steps5").hide();
	  $("#report").hide();
	  
	  $("#footer1").hide();
	  $("#footer2").hide();
	  $("#footer3").show();
	  $("#footer4").hide();
	  $("#footer5").hide();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt2").css({"background-color":"#dadada","color":"#9e9e9e"});
      $("#step_txt3").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#step_txt4").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt5").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  $("#btn_prev4").click(function(){
	  $("#steps1").hide();
	  $("#steps2").hide();
	  $("#steps3").hide();
	  $("#steps4").show();
	  $("#steps5").hide();
	  $("#report").hide();
	  
	  $("#footer1").hide();
	  $("#footer2").hide();
	  $("#footer3").hide();
	  $("#footer4").show();
	  $("#footer5").hide();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt2").css({"background-color":"#dadada","color":"#9e9e9e"});
      $("#step_txt3").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt4").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#step_txt5").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  $("#btn_next1").click(function(){
	  var nextFlag = true;
	  //浮点数正则
	  var regex = /^-?[0-9]+(\.[0-9]+)?$/;
	  $("#dlListof tr").each(function(index){
		  if($(this).find('td:eq(1) input').length!=0){
			  var ch = $(this).find('td:eq(1) input').val().replace(/,/g, '');
			  if(!regex.test(ch)){
				  if($(this).find('td:eq(1) span').length==0){
					  $(this).find('td:eq(1)').append("<span style='color:red' class='glyphicon glyphicon-exclamation-sign'>请输入正确数值</span>")
				  }
				  if(nextFlag){
					  nextFlag = false;
				  }
			  }else{
				  $(this).find('td:eq(1) span').remove();
			  }
		  }
	  })
	  if(!nextFlag){
		  return false;
	  }
	  if(!checkQuan()){
		  return false;
	  }
	  
	  $("#steps1").hide();
	  $("#steps2").show();
	  $("#steps3").hide();
	  $("#steps4").hide();
	  $("#steps5").hide();
	  $("#report").hide();
	  
	  $("#footer1").hide();
	  $("#footer2").show();
	  $("#footer3").hide();
	  $("#footer4").hide();
	  $("#footer5").hide();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt2").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
      $("#step_txt3").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt4").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt5").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  //添加文本框的校验
  function checkQuan(){
	var P038 = $("#P038").val().replace(new RegExp(",","gm"),"");
	var P039 = $("#P039").val().replace(new RegExp(",","gm"),"");
	var P042 = $("#P042").val().replace(new RegExp(",","gm"),"");
	var BS002_SM = $("#BS002_SM").val().replace(new RegExp(",","gm"),"");
	var BS002_YX = $("#BS002_YX").val().replace(new RegExp(",","gm"),"");
	
	var flag = false;
	if(P038==P039){
		swal({
            title: "提示",
            text: "租赁余额及保证金不能相等！",
            type: "info"
        });
		return flag;
	}
	if(P042>3||P042<0){
		swal({
            title: "提示",
            text: "现金流期限必须大于0且小于等于3年！",
            type: "info"
        });
		return flag;
	}
	if(BS002_SM>10){
		swal({
            title: "提示",
            text: "租赁物经济寿命最多10年！",
            type: "info"
        });
		return flag;
	}
	if(BS002_YX>10){
		swal({
            title: "提示",
            text: "租赁物已使用年限最多10年！",
            type: "info"
        });
		return flag;
	}
	
	return true;
}
  
    //定性分析校验方法
	function checkQualitativeAnalysis(labelId){
	 //校验步骤二 
	   var flag=true;
	   var childLength=$("#"+labelId+" .child_row1").length;
	   for(var i=0;i<childLength;i++){
	   var val=$("#"+labelId+" .child_row1").eq(i).find("input[type='radio']:checked").val();
	   if(val==null || val==""){
	    var nameText=$("#"+labelId+" .child_row1").eq(i).find(".options-directory label").text();
	    var nameTip=$("#"+labelId+" .child_row1").eq(i).find(".options-directory span").html();
	    //添加提示
	    if(nameTip==null || nameTip==""){
	     $("#"+labelId+" .child_row1").eq(i).find(".options-directory").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style='display: inline-block;color:red;' class='glyphicon glyphicon-exclamation-sign'>请选择"+nameText+"</span>");
	    }
	    //console.log("请选择"+nameText+"的值");
	    //return; //方式一 单次逐一校验
	    flag=false;//方式二  一次校验全部
	   }else{
	    $("#"+labelId+" .child_row1").eq(i).find(".options-directory").find("span").remove();
	   }
	   }
	   //if(!flag)return;//有空值，则返回
	   return flag;
	}
  
  $("#btn_next2").click(function(){
	  //调用定性分析校验方法
	  var flag=checkQualitativeAnalysis("dxAnalysis");
	  if(!flag){//校验不通过，则return
	   return;
	  }
	  $("#steps1").hide();
	  $("#steps2").hide();
	  $("#steps3").show();
	  $("#steps4").hide();
	  $("#steps5").hide();
	  $("#report").hide();
	  
	  $("#footer1").hide();
	  $("#footer2").hide();
	  $("#footer3").show();
	  $("#footer4").hide();
	  $("#footer5").hide();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt2").css({"background-color":"#dadada","color":"#9e9e9e"});
      $("#step_txt3").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#step_txt4").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt5").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  //
  $("#btn_next3").click(function(){
	  //调用定性分析校验方法
	  var flag=checkQualitativeAnalysis("zlwbzbs");
	  if(!flag){//校验不通过，则return
	   return;
	  }
	  $("#steps1").hide();
	  $("#steps2").hide();
	  $("#steps3").hide();
	  $("#steps4").show();
	  $("#steps5").hide();
	  $("#report").hide();
	  
	  $("#footer1").hide();
	  $("#footer2").hide();
	  $("#footer3").hide();
	  $("#footer4").show();
	  $("#footer5").hide();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt2").css({"background-color":"#dadada","color":"#9e9e9e"});
      $("#step_txt3").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt4").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#step_txt5").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  //
  $("#btn_next4").click(function(){
	  //调用定性分析校验方法
	  var flag=checkQualitativeAnalysis("zxcsbzbs");
	  if(!flag){//校验不通过，则return
	   return;
	  }
	  $("#steps1").hide();
	  $("#steps2").hide();
	  $("#steps3").hide();
	  $("#steps4").hide();
	  $("#steps5").show();
	  $("#report").hide();
	  
	  $("#footer1").hide();
	  $("#footer2").hide();
	  $("#footer3").hide();
	  $("#footer4").hide();
	  $("#footer5").show();
	  $("#footer6").hide();
	  
	  $("#step_txt1").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt2").css({"background-color":"#dadada","color":"#9e9e9e"});
      $("#step_txt3").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt4").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#step_txt5").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#step_txt6").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
