/*自定义jquery插件*/
(function($){
	/*给form赋值*/
	$.fn.loadJson = function(jsonValue) {
	    var obj = this;
	    $.each(jsonValue, function(name, ival) {
	        var $oinput = obj.find(":input [name=" + name + "]");
	        if ($oinput.attr("type") == "radio"
	                || $oinput.attr("type") == "checkbox") {
	            $oinput.each(function() {
	                if (Object.prototype.toString.apply(ival) == '[object Array]') {//是复选框，并且是数组         
	                    for (var i = 0; i < ival.length; i++) {
	                        if ($(this).val() == ival[i])
	                            $(this).attr("checked", "checked");
	                    }
	                } else {
	                    if ($(this).val() == ival)
	                        $(this).attr("checked", "checked");
	                }
	            });
	        } else if ($oinput.attr("type") == "textarea") {//多行文本框            
	            obj.find("[name=" + name + "]").html(ival);
	        } else {
	            obj.find("[name=" + name + "]").val(ival);
	        }
	    });
	};
	/*获取form的json格式数据*/
	$.fn.getJson = function () {
        var serializeObj = {};
        $(this.serializeArray()).each(function () {
            serializeObj[this.name] = this.value;
        });
        return serializeObj;
    };
    /*获取form的json格式数据(值不为空)*/
	$.fn.getNotNullJson = function () {
        var serializeObj = {};
        $(this.serializeArray()).each(function () {
        	var value = this.value;
        	if(typeof value != "undefined" && value != null && value != ''){
        		serializeObj[this.name] = value;
        	}
        });
        return serializeObj;
    };
    /*判断form是否有空的组件*/
	$.fn.formIsHaveNull = function () {
        var isNull = false;
        $(this.serializeArray()).each(function () {
        	var value = this.value;
        	if(typeof value == "undefined" || value == null || value == ''){
        		isNull = true;
        		return false;
        	}
        });
        return isNull;
    };
})(jQuery);


