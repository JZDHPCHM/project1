$(function(){
  //承租人名称模糊查找	
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
	  source: lesseeNames,
	  minLength: 0,//键入字数多少开始补全
	  showHintOnFocus: true,//将显示所有匹配项
	  fitToElement: true,//选项框宽度与输入框一致
	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	  autoSelect: true,//允许你决定是否自动选择第一个建议
  });
  //预警规则模糊查找
  var warningRules = [];
  $.ajax({
	  url : "/bpMaster/fuzzySearchRule",
	 type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  warningRules=result;
	  }
	 });
  $('#warningRule').typeahead({
	  source: warningRules,
	  minLength: 0,//键入字数多少开始补全
	  showHintOnFocus: "true",//将显示所有匹配项
	  fitToElement: true,//选项框宽度与输入框一致
	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	  autoSelect: true,//允许你决定是否自动选择第一个建议
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
  var fuzzyAssoName = []; 
  $.ajax({
	  url : "/bpMaster/fuzzyAssoName",
	 type:'GET',
	  dataType:'json',
	  async : false,
	  success : function(result) {
		  fuzzyAssoName=result;
	  }
	 });  
  $('#associatesName').typeahead({
	  source: fuzzyAssoName,
	  minLength: 0,//键入字数多少开始补全
	  showHintOnFocus: "true",//将显示所有匹配项
	  fitToElement: true,//选项框宽度与输入框一致
	  items: 'all',//下拉选项中出现条目的最大数量。也可以设置为“all”
	  autoSelect: true,//允许你决定是否自动选择第一个建议
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