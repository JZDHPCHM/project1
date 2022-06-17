 $(function() {
	  $("tr.parent").click(function() {
	  	$(this).toggleClass("selected").siblings('.child_'+this.id).toggle(); 
	  });
	 $("#dxTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
	 $(".filter-box").slideDown(120);
  });
  $(function() {
	  $("div.parent").click(function() {
	  	$(this).toggleClass("selected").siblings('.child_'+this.id).toggle(); 
	  });
  });
  $("#btn_prev1").click(function(){
//	  $("#dingliang").show();
//	  $("#dingxing").hide();
	  $("#report").hide();
//	  $("#footer1").show();
//	  $("#footer2").hide();
	  $("#footer3").hide();
//	  $("#dlTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
//	  $("#dxTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
	  $("#dxTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
	  $("#reportTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
	  $(".filter-box").slideUp(120);
  })
  $("#btn_next1").click(function(){
	  var nextFlag = true;
	  //浮点数正则
	  var regex = /^-?[0-9]+(\.[0-9]+)?$/;
	  $("#dlListof tr").each(function(index){
		  if($(this).find('td:eq(1) input').length!=0){
			  var ch = $(this).find('td:eq(1) input').val().replace(/,/g, '').replace(/ /g, '').replace(/，/g, '');
			  if(!regex.test(ch)){
				  if($(this).find('td:eq(1) span').length==0){
					  $(this).find('td:eq(1)').append("<span class='glyphicon glyphicon-exclamation-sign'>请输入正确数值</span>")
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
	  
	  $("#dingxing").show();
//	  $("#dingliang").hide();
	  $("#report").hide();
	  $("#footer1").hide();
	  $("#footer2").show();
	  $("#footer3").hide();
	  $("#dlTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
	  $("#dxTxt").css({"background-color":"#0062ff","color":"#ffffff", 'box-shadow': 'rgba(0, 98, 255, .5) 0 3px 7px'});
	  $("#reportTxt").css({"background-color":"#ffffff","color":"#242432", 'box-shadow': 'none'});
	  $(".filter-box").slideDown(120);
  })
  var lesseeName = [];
  $.ajax({
	  url : "/bpMaster/SearchBpMasterName",
	  type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
	  	  lesseeName=result;
	  }
  });
  $('#lesseeName').typeahead({
	  source: lesseeName,
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
	  source: SearchBpMasterCode,
	  minLength: 0,//键入字数多少开始补全
	  showHintOnFocus: "true",//将显示所有匹配项
	  fitToElement: true,//选项框宽度与输入框一致
	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	  autoSelect: true//允许你决定是否自动选择第一个建议
});
  
  
  var userSearch = [];
  $.ajax({
	  url : "/bpMaster/userSearch",
	  type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  userSearch=result;
	  }
  });
  $('#userSearch').typeahead({
	  source: userSearch,
	  minLength: 0,//键入字数多少开始补全
	  showHintOnFocus: "true",//将显示所有匹配项
	  fitToElement: true,//选项框宽度与输入框一致
	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	  autoSelect: true,//允许你决定是否自动选择第一个建议
});
  
  
//  var userSearch1 = [];
//  $.ajax({
//	  url : "/bpMaster/userSearch",
//	  type:'GET',
//	  dataType:'json',
//	  async : false,
//	  success : function(result) {
//		  userSearch1=result;
//	  }
//  });
//  $('#userSearch1').typeahead({
//	  source: userSearch1,
//	  minLength: 0,//键入字数多少开始补全
//	  showHintOnFocus: "true",//将显示所有匹配项
//	  fitToElement: true,//选项框宽度与输入框一致
//	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
//	  autoSelect: true,//允许你决定是否自动选择第一个建议
//});
  
  
	$('.chosen-select').chosen({width: "100%"});
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
