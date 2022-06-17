 $.validator.setDefaults({
    submitHandler: function() {
    	//不动产调整项  realEstateInfo
    	var num1=$('.ZX_BDCTZ_1').find('.panel-body').length;
    	var realEstateInfo=[];
    	if($('.ZX_BDCTZ_1').is(':visible')){
    		for(var i=0;i<num1;i++){
         	   var obj={};
         	   var value1=$('.ZX_BDCTZ_1').find('.panel-body').eq(i).find('input[type="text"]').val();
         	   var key1=$('.ZX_BDCTZ_1').find('.panel-body').eq(i).find('input[type="text"]').attr('name1');
         	   var num2=$('.ZX_BDCTZ_1').find('.panel-body').eq(i).find('ul').length;
         	   if(value1!=''){
         		  obj[key1]=value1;
         	   }else{
         		  
         	   }
         	
         	  
         	   for(var j=0;j<num2;j++){
         		   var value2=$('.ZX_BDCTZ_1').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").val();
         		   var key2=$('.ZX_BDCTZ_1').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").attr('name1');
         		   if(value2!=''){
             		  obj[key2]=value2;
             	   }else{
             		 
             	   }
         		   
         	   }
         	  if(JSON.stringify(obj) !== '{}'){
         		 realEstateInfo.push(obj);
         	  }
         	 
         	}
    	}
    	
    	
    	//土地使用权调整项  landUseRightinfo
    	
    	var num1_2=$('.ZX_TDSYQ_2').find('.panel-body').length;
    	var landUseRightinfo=[];
    	if($('.ZX_TDSYQ_2').is(':visible')){
    		for(var i=0;i<num1_2;i++){
         	   var obj={};
         	   var value1=$('.ZX_TDSYQ_2').find('.panel-body').eq(i).find('input[type="text"]').val();
         	   var key1=$('.ZX_TDSYQ_2').find('.panel-body').eq(i).find('input[type="text"]').attr('name1');
         	   var num2=$('.ZX_TDSYQ_2').find('.panel-body').eq(i).find('ul').length;
         	  if(value1!=''){
         		  obj[key1]=value1;
         	   }else{
         		   
         	   }
         	   for(var j=0;j<num2;j++){
         		   var value2=$('.ZX_TDSYQ_2').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").val();
         		   var key2=$('.ZX_TDSYQ_2').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").attr('name1');
             		  if(value2!=''){
                 		  obj[key2]=value2;
                 	   }else{
                 		   
                 	   }
         	   }
         	  if(JSON.stringify(obj) !== '{}'){
         		 landUseRightinfo.push(obj);
          	  }
         	   
         	}
    	}
    	
    	//应收账款调整项  receivablesInfo
    	var num1_3=$('.ZX_YSZKTZ_3').find('.panel-body').length;
    	var receivablesInfo=[];
    	if($('.ZX_YSZKTZ_3').is(':visible')){
    		for(var i=0;i<num1_3;i++){
         	   var obj={};
         	   var value1=$('.ZX_YSZKTZ_3').find('.panel-body').eq(i).find('input[type="text"]').val();
         	   var key1=$('.ZX_YSZKTZ_3').find('.panel-body').eq(i).find('input[type="text"]').attr('name1');
         	   var num2=$('.ZX_YSZKTZ_3').find('.panel-body').eq(i).find('ul').length;
         	  if(value1!=''){
         		  obj[key1]=value1;
         	   }else{
         		   
         	   }
         	   for(var j=0;j<num2;j++){
         		   var value2=$('.ZX_YSZKTZ_3').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").val();
         		   var key2=$('.ZX_YSZKTZ_3').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").attr('name1');
         		  if(value2!=''){
             		  obj[key2]=value2;
             	   }else{
             		   
             	   }
         	   }
         	  if(JSON.stringify(obj) !== '{}'){
         		 receivablesInfo.push(obj);
           	  }
         	   
         	}
    	}
    	
    	//收益权调整项 usufructInfo
    	var num1_4=$('.ZX_SYQTZ_4').find('.panel-body').length;
    	var usufructInfo=[];
    	if($('.ZX_SYQTZ_4').is(':visible')){
    		for(var i=0;i<num1_4;i++){
         	   var obj={};
         	   var value1=$('.ZX_SYQTZ_4').find('.panel-body').eq(i).find('input[type="text"]').val();
         	   var key1=$('.ZX_SYQTZ_4').find('.panel-body').eq(i).find('input[type="text"]').attr('name1');
         	   var num2=$('.ZX_SYQTZ_4').find('.panel-body').eq(i).find('ul').length;
         	  if(value1!=''){
         		  obj[key1]=value1;
         	   }else{
         		   
         	   }
         	   for(var j=0;j<num2;j++){
         		   var value2=$('.ZX_SYQTZ_4').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").val();
         		   var key2=$('.ZX_SYQTZ_4').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").attr('name1');                		  
         		  if(value2!=''){
             		  obj[key2]=value2;
             	   }else{
             		   
             	   }
         	   }
         	  if(JSON.stringify(obj) !== '{}'){
         		 usufructInfo.push(obj);
            	}
         	   
         	}
    		
    	}
    	
    	//股权调整项  equityInfo
    	var num1_5=$('.ZX_GQTZX_5').find('.panel-body').length;
    	var equityInfo=[];
    	if($('.ZX_GQTZX_5').is(':visible')){
    		for(var i=0;i<num1_5;i++){
         	   var obj={};
         	   var value1=$('.ZX_GQTZX_5').find('.panel-body').eq(i).find('input[type="text"]').val();
         	   var key1=$('.ZX_GQTZX_5').find('.panel-body').eq(i).find('input[type="text"]').attr('name1');
         	   var num2=$('.ZX_GQTZX_5').find('.panel-body').eq(i).find('ul').length;
         	   if(value1!=''){
         		  obj[key1]=value1;
         	   }else{
         		   
         	   }
         	   for(var j=0;j<num2;j++){
         		   var value2=$('.ZX_GQTZX_5').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").val();
         		   var key2=$('.ZX_GQTZX_5').find('.panel-body').eq(i).find('ul').eq(j).find("input[type='radio']:checked").attr('name1');
         		  if(value2!=''){
             		  obj[key2]=value2;
             	   }else{
             		   
             	   }
         	   }
         	  if(JSON.stringify(obj) !== '{}'){
         		 equityInfo.push(obj);
             	}
         	   
         	}
    	}
    	
    	
    	var creditInfo={};
    	creditInfo.realEstateInfo=realEstateInfo;
    	creditInfo.landUseRightinfo=landUseRightinfo;
    	creditInfo.receivablesInfo=receivablesInfo;
    	creditInfo.usufructInfo=usufructInfo;
    	creditInfo.equityInfo=equityInfo;
    	var map={
    			DL_FXCK:DL_FXCK2,
    			creditInfo:creditInfo,
    	}

    	$.ajax({
            async: false,
            type: "GET",
            url: "/irs/creditRating/creatCreditRating",
            data: {
           	 bpCode:csCode,
           	 proCode:prCode,
            },
            success: function (data) {
           	 var mainId=data.data;
                if (data.status) {
                	$.ajax({
                        async: false,
                        type: "GET",
                        url: "/irs/creditRating/startModel",
                        data:{
                       	 "map":JSON.stringify(map),
                       	 "type":'ZXBZBS',
                       	 "mainId":mainId,
                        },
                		 dataType:'json',
                         success: function (data) {
                       		DL_ZX_BZBS=data.finalPd;
                       	    $("#finalSco1").text(data.finalSco);
                       	    $("#finalPd1").text(data.finalPd);
//						    $("#myModal3").modal("show");
                       	   $.ajax({
                  	            async: false,
                  	            type: "GET",
                  	            url: "/irs/facilityRating/creatFacilityRating",
                  	            data:{
                  	           	 "bpCode":csCode,
                  	           	 "proCode":prCode,
                  	           	 "mainId":mainId2,
                  	           	 "assetsId":assetsId,
                  	           	 "creditId":mainId,
                  	            },
                  	    		 dataType:'json',
                  	           success: function (data) {
                  	           	
                  	           	if(data.status){
                  	           		var map2={};
                           	           	if(rs=='ZXAQPJ_LRX'){
                       	           			map2.DL_ZT_SRLRX=DL_ZT_SRLRX2;	                                     			
                       	           		}else if(rs=='ZXAQPJ_SZX'){
                       	           			map2.DL_ZT_SRSZX=DL_ZT_SRLRX2;	                                     			
                       	           		}else if(rs=='ZXAQPJ_ZSX'){	                                     			
                       	           			map2.DL_ZT_SRZSX=DL_ZT_SRLRX2;
                       	           		}
                       	             	map2.DL_ZC_BZBS=DL_ZC_BZBS2;
                       	             	map2.DL_ZX_BZBS=DL_ZX_BZBS;
                      	           		 $.ajax({
                      	                       async: false,
                      	                       type: "GET",
                      	                       url: "/irs/facilityRating/startFacilityModel",
                      	                       data:{
                      	                      	 "map":JSON.stringify(map2),
                      	                      	 "type":rs2,
                      	                      	 "facilityId":data.data,
                      	                       },
                      	                   		 dataType:'json',
                      	                          success: function (data) {
                      	                         	console.log(555,data);
                      	                             window.location.href='/irs/DebtRatingResults/debtReport?custNo='+data.id;
                      	                           }
                      	                   });
                  	           	   }
                  	              
                  	            }
                  	        });
						  
                        }
                    });
    		     
                }
            }
        }); 

    }
});





var DL_FXCK2='';
 var csCode2='';
 var prCode2='';
 var mainId2='';
 var rs2='';//评级类型
 var DL_ZX_BZBS=0;
 var DL_ZT_SRLRX2=0;
 var DL_ZC_BZBS2=0;
$(document).ready(function () {
            // 左侧 label 点击效果
           var optionList = $('.inventory-nav').find('.newlabel');
           $("#lablel_list").on('click','input',function (e) {
        	   var index=$(this).attr('index');
        	   if ($(this).is(':checked')) {
        		   $('#option_' + index).slideDown(150);
        	   }else{
        		   $('#option_' + index).slideUp(150, function () {
                   /*   $(this).find('input').prop('checked', false).val('');*/
                   }).find('.new-panel-body').remove();
        	   }
           });
           //问题1：
           $("#lablel_list").on('click','.anchor',function (e) {
        	   var index=$(this).siblings('.newlabel').find('input').attr('index');
        	   $('html, body').animate({
                   scrollTop: $('#option_' + index).offset().top - 20
               }, 100);
               $('#option_' + index).addClass('active');
               setTimeout(function () {
                   $('#option_' + index).removeClass('active');
               }, 600);
           });
            // 左侧菜单浮动效果
            function scrollNav() {
                $(document).scroll(function () {
                    var docWidth = parseInt($('body').width());
                    if (docWidth > 768) {
                        $('.inventory-nav').animate({
                            top: $(document).scrollTop()
                        }, 0);
                    } else {
                        $('.inventory-nav').css('top', 0);
                    }
                })
            }
            scrollNav();
            $(window).resize(function () {
                scrollNav();
            });

            // 新增选项效果
            var cloneId = 0;
            $("#option_list").on('click','.btn-add',function(){
            	var cloneNode = $(this).parents('.panel').find('.panel-body:last-child').clone(true).removeClass('hide');
//            	var cloneNode = $(this).parents('.panel').find('.panel-body:last-child').clone(true);
                cloneNode.addClass('new-panel-body').hide().append('<div class="panel-footer"><button type="button" class="btn btn-danger btn-sm btn-delete">删除</button></div>');

                cloneNode.find('input').each(function () {
                    $(this).attr('name', $(this).attr('name1')+cloneId );
                }).prop('checked', false);

                $(this).parents('.panel-heading').after(cloneNode);
                cloneNode.slideDown(150);
                cloneId++;

                $('.panel').on('click', 'button.btn-delete', function () {
                    $(this).parents('.panel-body').slideUp(150, function () {
                        $(this).remove();
                    });
                });
            });
            // 金额格式化
            $('.panel').on('blur', 'input[type="text"]', function () {
                var newValue = parseFloat($(this).val().replace(/,/g, '')).toLocaleString();
                newValue === 'NaN' ? $(this).val('') : $(this).val(newValue);
            });
            //confirm 确定：
            $("#option_list").on('click','#confirm',function(){     
            	$("#option_list").validate();
 
            	 
           });
          
            
        });





   
   //渲染页面：
   function renderData(data,DL_FXCK,csCode,prCode,mainId,assetsId,rs,DL_ZT_SRLRX, DL_ZC_BZBS) {
	    DL_FXCK2=DL_FXCK;
	    csCode2=csCode;
	    prCode2=prCode;
	    assetsId=assetsId;
	    mainId2=mainId;
	    rs2=rs;
	    DL_ZT_SRLRX2=DL_ZT_SRLRX;
	    DL_ZC_BZBS2=DL_ZC_BZBS;
	    $("#lablel_list").html('');
	    $("#option_list").html('');
	   	 for(var i=0;i<data.length;i++){
	   		var list='<div class="checkbox" >'+
                '<div class="newlabel"><input type="checkbox"   index="'+i+'" id="label_'+i+'"checked>'+data[i].modelName+'</div>'+
                '<a href="JavaScript:void(0);" class="anchor"><span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span></a>'+
            '</div>';
	   		$("#lablel_list").append(list);
	   		var data2=data[i].modelList;
	   		var list3_1='';
	   		for(var j=0;j<data2.length;j++){
	   			var list3_2='';
	   			if(data2[j].FD_INDEXTYPE=='QUANTITATIVE'){
	   				list3_1+= '<div class="input-group">'+
		            '<span class="input-group-addon">'+data2[j].FD_INDEXCATEGORY+'</span>'+
			           ' <input type="text" required name1="'+data2[j].FD_INDEXCODE+'" name="'+data2[j].FD_INDEXCODE+'" class="form-control" placeholder="请输入'+data2[j].FD_INDEXCATEGORY+'">'+
			        '</div>' +'<div class="space-20"></div>';
	   			}else if(data2[j].FD_INDEXTYPE=='QUALITATIVE_EDIT'){
	   				for(var k=0;k<data2[j].sonList.length;k++){
	   					var data3=data2[j].sonList[k];
	   					var dxText=JSON.parse(data3.FD_DX_TEXT);
	   					list3_2+='<ul class="list-group">'+
	   		            '<li class="list-group-item title font-bold">'+data3.FD_INDEXNAME+'</li>';
	   					for(var m in dxText){
	   						list3_2+='<li class="list-group-item">'+
	   		                '<div class="radio">'+
   		                    '<label>'+
   		                        '<input type="radio"   required name1="'+data3.FD_INDEXCODE+'" name="'+data3.FD_INDEXCODE+'" value="'+m+'">'+
   		                     dxText[m]
   		                    '</label>'+
   		                '</div>'+
   		            '</li>';
	   					}
	   					list3_2+='</ul>';
	   				}
	   			}
		   		var list3='<div class="hide panel-body">'+list3_1+list3_2+
	    '</div>';
	   		}

	   		var list2=' <div  id="option_'+i+'"   class="panel panel-default '+data[i].modelCode+'"   >'+
								                    '<div class="panel-heading"><span class="glyphicon glyphicon-list"></span> '+data[i].modelName+'<button type="button" class="btn btn-sm btn-primary pull-right btn-add"><span class="glyphicon glyphicon-plus"></span> 新增'+data[i].modelName+'</button></div>'+
								                    list3+
								                '</div>';
	   		$("#option_list").append(list2);
								               
	  }
	   	 var  list4='<div class="well clear form-footer">'+
								                    '<button type="button" class="btn pull-left" id="cancel" ><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>取消</button>'+
								                    '<input class="submit btn btn-primary pull-right" type="submit" value="确定" id="confirm">'+
//								                    '<button type="button" class="btn btn-primary pull-right" id="confirm"><span class="glyphicon glyphicon-ok" aria-hidden="true"></span> 确定</button>'+
								                '</div>';
	   	$("#option_list").append(list4);
   }
   
  
