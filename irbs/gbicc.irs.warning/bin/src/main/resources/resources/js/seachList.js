
$(function(){
	
//  var lesseeNames = []; 
//  $.ajax({
//	  url : "/bpMaster/fuzzySearch",
//	 type:'GET',
//	  dataType:'json',
//	  async : false,
//	  success : function(result) {
//		  lesseeNames=result;
//	  }
//	 })	;
  
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
  
  var fuzzyAssoName = []; 
  $.ajax({
	  url : "/bpMaster/fuzzyAssoName",
	 type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  fuzzyAssoName=result;
	  }
	 })	; 
  
  
  
	   var ruleWarn=[];
  $.ajax({
	  url : "/bpMaster/fuzzySearchRule",
	 type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  ruleWarn=result;
	  }
	 })	;
  var fuzzySearchSmallClass=[];
  $.ajax({
	  url : "/bpMaster/fuzzySearchSmallClass",
	 type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  fuzzySearchSmallClass=result;
	  }
	 })	;
	
  
//     $("#lesseeName").autocomplete({
//         minLength: 0,
//         source: lesseeNames,
//          select: function(event, ui) {
//             $("#lesseeName").val(ui.item.label);
//          }
//     });

  $("#warningRule").autocomplete({
      minLength: 0,
      source: ruleWarn,
       select: function(event, ui) {
          $("#warningRule").val(ui.item.label);
       }
  });
  
  $("#warningSmall").autocomplete({
      minLength: 0,
      source: fuzzySearchSmallClass,
       select: function(event, ui) {
          $("#warningSmall").val(ui.item.label);
       }
  });
  
  $("#associatesName").autocomplete({
      minLength: 0,
      source: fuzzyAssoName,
       select: function(event, ui) {
          $("#associatesName").val(ui.item.label);
       }
  });
  
}) 


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