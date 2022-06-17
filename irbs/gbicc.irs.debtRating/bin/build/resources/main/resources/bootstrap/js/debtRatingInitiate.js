 $(function() {
	  $("tr.parent").click(function() {
	  	$(this).toggleClass("selected").siblings('.child_'+this.id).toggle(); 
	  });
  });
  $(function() {
	  $("div.parent").click(function() {
	  	$(this).toggleClass("selected").siblings('.child_'+this.id).toggle(); 
	  });
  });
  $("#btn_prev1").click(function(){
	  $("#dingliang").show();
	  $("#dingxing").hide();
	  $("#report").hide();
	  $("#footer1").show();
	  $("#footer2").hide();
	  $("#footer3").hide();
	  $("#dlTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#dxTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#reportTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  $("#btn_next1").click(function(){
	  var nextFlag = true;
	  //浮点数正则
	  var regex = /^-?[0-9]+(\.[0-9]+)?$/;
	  $("#dlListof tr").each(function(index){
		  if($(this).find('td:eq(1) input').length!=0){
			  var ch = $(this).find('td:eq(1) input').val();
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
	  
	  $("#dingxing").show();
	  $("#dingliang").hide();
	  $("#report").hide();
	  $("#footer1").hide();
	  $("#footer2").show();
	  $("#footer3").hide();
	  $("#dlTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
	  $("#dxTxt").css({"background-color":"rgb(21, 127, 204)","color":"#fff"});
	  $("#reportTxt").css({"background-color":"#dadada","color":"#9e9e9e"});
  })
  var lesseeNames = [];
  $.ajax({
	  url : "/bpMaster/SearchBpMasterName",
	  type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
	  	  lesseeNames=result;
	  }
  });
  $('#lesseeName').typeahead({
	  source: lesseeNames
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
	  source: userSearch
});
  
  
  var userSearch1 = [];
  $.ajax({
	  url : "/bpMaster/userSearch",
	  type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  userSearch1=result;
	  }
  });
  $('#userSearch1').typeahead({
	  source: userSearch1
});
  
  
	$('.chosen-select').chosen({width: "100%"});
	$("#date1").datepicker({
		changeYear : true,
		changeMonth : true,
		numberOfMonths : 1,
		showButtonPanel: true,
		onSelect : function(selectedDate) {
		$("#date2").datepicker("option", "minDate", selectedDate);  }
	});		 
	$("#date2").datepicker({
		changeYear : true,
		changeMonth : true,
		numberOfMonths : 1,
		showButtonPanel: true,
		onSelect : function(selectedDate) {
		$("#date1").datepicker("option", "maxDate", selectedDate);  }
	});
	$("#date3").datepicker({
		changeYear : true,
		changeMonth : true,
		numberOfMonths : 1,
		showButtonPanel: true,
		onSelect : function(selectedDate) {
		$("#date4").datepicker("option", "minDate", selectedDate);  }
	});		 
	$("#date4").datepicker({
		changeYear : true,
		changeMonth : true,
		numberOfMonths : 1,
		showButtonPanel: true,
		onSelect : function(selectedDate) {
		$("#date3").datepicker("option", "maxDate", selectedDate);  }
	});
