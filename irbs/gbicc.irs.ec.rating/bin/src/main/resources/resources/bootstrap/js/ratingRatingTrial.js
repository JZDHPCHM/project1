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
	  url : "/bpMaster/fuzzySearch",
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
