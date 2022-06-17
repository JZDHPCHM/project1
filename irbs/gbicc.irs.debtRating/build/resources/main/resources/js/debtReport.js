

function getQueryVariable(variable)
{
       var query = window.location.search.substring(1);
       var vars = query.split("&");
       for (var i=0;i<vars.length;i++) {
               var pair = vars[i].split("=");
               if(pair[0] == variable){return pair[1];}
       }
       return(false);
}
var id=getQueryVariable('custNo')?getQueryVariable('custNo'):'';
//34eebad6-9f88-49d6-8d8f-902bd1677638
//贡献最大
var maxValue="";

$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getFacilityHeader?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       $("#company label").html(data.FD_PROJECT_NAME);
       $("#company span").html('（项目编号：'+data.FD_PROJECT_CODE+'）');
    }
});

// 信用评价仪表盘
var gradeChart = echarts.init(document.getElementById('grade'));
var number = 0;
var FD_DATE='';
var FD_FINAL_DATE='';
var FD_FINAL_LEVEL='';
var INFO='';

$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getFacilityLevel?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
		FD_FINAL_DATE=/\d{4}-\d{1,2}-\d{1,2}/g.exec(data.FD_FINAL_DATE);
		FD_DATE=/\d{4}-\d{1,2}-\d{1,2}/g.exec(data.FD_DATE);
    	FD_FINAL_LEVEL=data.FD_FINAL_LEVEL?data.FD_FINAL_LEVEL:'';
    	if(FD_FINAL_LEVEL=='Ⅰ'){
       	   number=1; 
          }else if(FD_FINAL_LEVEL=='Ⅱ'){
       	   number=0.8;
          }else if(FD_FINAL_LEVEL=='Ⅲ'){
         	 number=0.6;
          }else if(FD_FINAL_LEVEL=='Ⅳ'){
         	 number=0.4;
          }else if(FD_FINAL_LEVEL=='Ⅴ'){
         	 number=0.2;
          }else if(FD_FINAL_LEVEL=='Ⅵ'){
         	 number=0;
          }
    	INFO=data.INFO;
    	$("#time1 span:nth-child(1)").html(FD_FINAL_DATE);
        $("#time1 span:nth-child(2)").html(FD_DATE);
      
    }
});

var gradeOption = {
    series: [
        {
            name: '',
            type: 'gauge',
            radius: '57%',
            min: 0,
            max: 100,
            startAngle: '210',                      // 起始位置
            endAngle: '-30',                        // 结束位置
            splitNumber: 2,
            axisLine: {                             // 仪表盘轴线设置
                show: true,
                lineStyle: {
                    color: [
                        [1, '#fff'],
                    ],
                    width: 1
                }
            },
            axisLabel: {
                show: true,
            }, //刻度标签。
            detail: {
            	formatter : `${FD_FINAL_LEVEL}`,
                offsetCenter: [0, '-10%'],
                fontSize: 68,
                fontWeight: 'bold',
                fontFamily: 'Arial',
                color:'#828AE8'
            },
            data: [{ value: number*100, name: INFO }],
            axisTick: {
                show: true,
                splitNumber: 15,
                lineStyle: {
                    color: '#ccc',
                    width: 2
                },
                length: -3
            }, //刻度样式
            splitLine: {
                show: false
            },
            detail: {
                show: false
            },
            pointer: {
                show: false
            }
        },
        {
            title: {                                // '信用较好'样式
                show: true,
                backgroundColor: '#ffffff',
                offsetCenter: [0, 0],
                width: 130,
                height: 130,
                padding: 0,
                fontSize: 16,
                color: '#333333',
                lineHeight: 190,
                borderRadius: 150,
                rich: {}
            },
            name: '',
            type: 'gauge',
            radius: '80%',                          // 仪表盘半径
            min: 0,
            max: 100,
            startAngle: '210',                      // 起始位置
            endAngle: '-30',                        // 结束位置
            silent: true,                           // 禁止响应鼠标
            splitNumber: 50,                        // 分割段数
            axisLine: {                             // 仪表盘轴线设置
                show: true,
                lineStyle: {
                    color: [
                        [number, '#828AE8'],
                        [1, '#CCCCCC'],
                    ],
                    width: 30
                }
            },
            splitLine: {
                length: 32,
                lineStyle: {
                    width: 5,
                    color: '#ffffff',
                }
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                show: true,      
                distance:10,
                formatter:function(param){
    
                     if (param==100) {
                         return 'I'
                     }else if (param==80) {
                         return 'II'
                     }else if (param==60) {                 	 
                         return 'III'
                     }else if (param==40) {
                         return 'IV'
                     }else if(param==20){
                         return 'Ⅴ'
                     }else if(param==0){
                         return 'Ⅵ'
                     }
			    },
            },
            detail: {
            	formatter : `${FD_FINAL_LEVEL}`,
                offsetCenter: [0, '-10%'],
                fontSize: 44,
                fontWeight: 'normal',
                fontFamily: 'Arial',
                color:'#828AE8'
            },
            data: [{ value: number*100, name: INFO }],
            pointer: {
                show: false,
            }
        },
    ]
};

//债项安全评级报告综述(仪表盘右边那一块):
var FD_FINAL_SCO='';
var FD_INTERN_NAME='';
var FD_FINAL_NAME='';
var LEASE_TERM='';
var FINANCE_AMOUNT='';
var FD_ADMISSION_SUGGEST='';
var RANKINGONCLIQUE='';
var RANKINGONGROUP='';
var FD_FINAL_LEVEL='';
var FD_INTERN_LEVEL='';
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getAssetsDescribe?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	FD_FINAL_SCO=data.FD_FINAL_SCO;
    	FD_INTERN_NAME=data.FD_INTERN_NAME;
    	FD_FINAL_NAME=data.FD_FINAL_NAME;
    	LEASE_TERM=data.LEASE_TERM;
    	FINANCE_AMOUNT=data.FINANCE_AMOUNT;
    	FD_ADMISSION_SUGGEST=data.FD_ADMISSION_SUGGEST;
    	RANKINGONCLIQUE=data.RANKINGONCLIQUE;
    	RANKINGONGROUP=data.RANKINGONGROUP;
    	FD_FINAL_LEVEL=data.FD_FINAL_LEVEL;
    	FD_INTERN_LEVEL=data.FD_INTERN_LEVEL;
    	DESCRIBE=data.DESCRIBE;
    	$("#FD_FINAL_SCO label").html(FD_FINAL_SCO);
    	$("#FD_INTERN_NAME label").html(FD_INTERN_NAME);
    	$('#FD_INTERN_NAME span').html(FD_INTERN_LEVEL);
    	$('#FD_FINAL_NAME label').html(FD_FINAL_NAME);
    	$('#FD_FINAL_NAME span').html(FD_FINAL_LEVEL);
    	$("#LEASE_TERM span").html(LEASE_TERM);
    	
    	$('#FD_ADMISSION_SUGGEST').html(FD_ADMISSION_SUGGEST);
    	$('#DESCRIBE').html(DESCRIBE);
    	$("#FINANCE_AMOUNT span").html(FINANCE_AMOUNT);
    }
});
//债项分数贡献比例
//得分分位数
var debtRateChart1 = echarts.init(document.getElementById('debtRate1'));
var debtRateOption1 = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false }
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
        	color: '#464646',
            fontSize: 12
        },
        nameTextStyle:{
        	fontSize: 14,
        	color: 'red',
        	fontWeight:500,
        },
        data: [RANKINGONGROUP?RANKINGONGROUP:'',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
        	color: '#707071',
            fontSize: 12
        },
        data: ['（超过存量客户的百分比）',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 10,
            data: [{
            	value:(RANKINGONGROUP.replace("%","")/100),
            	
            }],
            barCategoryGap: 20,
            itemStyle: {
                normal: {
                    barBorderRadius: 10,
                	color:'#828AE8',
                }
            },
            zlevel: 1,
        }, {
            name: "进度条背景",
            type: "bar",
            barGap: "-100%",
            barWidth: 10,
            data: [1],
            color:"#F2F3F5",
            itemStyle:{
            	barBorderRadius: 100,
            }
        }
    ]
}



var debtRateChart2 = echarts.init(document.getElementById('debtRate2'));
var debtRateOption2 = {
	    grid: {   // 直角坐标系内绘图网格
	        left: '10',  //grid 组件离容器左侧的距离,
	        //left的值可以是80这样具体像素值，
	        //也可以是'80%'这样相对于容器高度的百分比
	        top: '10',
	        right: '0',
	        bottom: '0',
	        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
	        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
	        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
	    },
	    xAxis: {
	        type: 'value',
	        splitLine: { show: false },
	        axisLabel: { show: false },
	        axisTick: { show: false },
	        axisLine: { show: false }
	    },
	    yAxis: [{
	        type: 'category',
	        axisTick: { show: false },
	        axisLine: { show: false },
	        axisLabel: {
	        	color: '#464646',
	            fontSize: 12
	        },
	        nameTextStyle:{
	        	fontSize: 14,
	        	color: 'red',
	        	fontWeight:500,
	        },
	        data: [RANKINGONCLIQUE?RANKINGONCLIQUE:'',]//类目数据，在类目轴（type: 'category'）中有效。
	        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
	    },
	    {
	        type: 'category',
	        axisTick: { show: false },
	        axisLine: { show: false },
	        axisLabel: {
	        	color: '#707071',
	            fontSize: 12
	        },
	        data: ['（超过同部门客户的百分比）',]//类目数据，在类目轴（type: 'category'）中有效。
	        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
	    }],
	    series: [
	        {
	            name: "",
	            type: "bar",
	            barWidth: 10,
	            data: [{
	            	value:(RANKINGONCLIQUE.replace("%","")/100),
	            	
	            }],
	            barCategoryGap: 20,
	            itemStyle: {
	                normal: {
	                    barBorderRadius: 10,
	                	color:'#828AE8',
	                }
	            },
	            zlevel: 1,
	        }, {
	            name: "进度条背景",
	            type: "bar",
	            barGap: "-100%",
	            barWidth: 10,
	            data: [1],
	            color:"#F2F3F5",
	            itemStyle:{
	            	barBorderRadius: 100,
	            }
	        }
	    ]
	}

var FD_INTERN_LEVEL3='';
var FD_INTERN_NAME3='';
var FD_FINAL_LEVEL3='';
var FD_FINAL_NAME3='';
var PRODUCT_TYPE='';
var LEASE_ITEM_SHORT_NAME='';
var NET_VALUE='';
var ASSESSED_VALUE="";
var CORELEASE_PROPORTION='';
var FD_ADMISSION_SUGGEST='';
var RANKING='';
//评级等级-    债项安全评级报告(资产信用评级)   无数据
var rate1Chart = echarts.init(document.getElementById('rate1'));
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getFacilityAssetsInfo?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	FD_INTERN_LEVEL3=data.FD_INTERN_LEVEL;
    	FD_INTERN_NAME3=data.FD_INTERN_NAME;
    	FD_FINAL_LEVEL3=data.FD_FINAL_LEVEL?data.FD_FINAL_LEVEL:'';
    	FD_FINAL_NAME3=data.FD_FINAL_NAME;
    	PRODUCT_TYPE=data.PRODUCT_TYPE;
    	FD_ADMISSION_SUGGEST=data.FD_ADMISSION_SUGGEST;
    	LEASE_ITEM_SHORT_NAME=data.LEASE_ITEM_SHORT_NAME;
    	NET_VALUE=data.NET_VALUE;
    	ASSESSED_VALUE=data.ASSESSED_VALUE;
    	CORELEASE_PROPORTION=data.CORELEASE_PROPORTION;
    	RANKING=data.RANKING;
    	RANKING=parseFloat(RANKING);
    	$("#FD_INTERN_LEVEL3").html(FD_INTERN_LEVEL3);
    	$("#FD_INTERN_NAME3").html(FD_INTERN_NAME3);
    	$("#FD_FINAL_LEVEL3").html(FD_FINAL_LEVEL3);
    	$("#FD_FINAL_NAME3").html(FD_FINAL_NAME3);
    	$("#PRODUCT_TYPE").html(PRODUCT_TYPE);
    	$("#PRODUCT_TYPE").attr('title',PRODUCT_TYPE);
    	$("#LEASE_ITEM_SHORT_NAME").html(LEASE_ITEM_SHORT_NAME);//LEASE_ITEM_SHORT_NAME
    	$("#LEASE_ITEM_SHORT_NAME").attr('title',LEASE_ITEM_SHORT_NAME);
    	$("#NET_VALUE").html(NET_VALUE);
    	$("#ASSESSED_VALUE").html(ASSESSED_VALUE);
    	$("#CORELEASE_PROPORTION").html(CORELEASE_PROPORTION);
    	$("#FD_ADMISSION_SUGGEST5").html(FD_ADMISSION_SUGGEST);
    }
});
var rate1Option = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: true}
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['I',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['Ⅳ',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: 'Ⅰ',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL3?FD_FINAL_LEVEL3.substring(1)=='Ⅰ':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	              img1: {
    	            	  backgroundColor: {
    		                    image: '/image/assets1.png',
    		               },
    		            height: 15,
       	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#969696',
    	                  fontWeight:1,
    	                  fontFamily:'Arial'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     Ⅰ}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '20%',
            data: [10, ],
            itemStyle: {
                normal: {
                    color:'#81E3D2',
                }
            },

        },
        {
            name: 'Ⅱ',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL3?FD_FINAL_LEVEL3.substring(1)=='Ⅱ':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	              img1: {
    	            	  backgroundColor: {
    		                    image: '/image/assets2.png',
    		               },
    		            height: 15,
       	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#969696',
    	                  fontWeight:1,
    	                  fontFamily:'Arial'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     Ⅱ}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [10],
            itemStyle: {
                normal: {
                    color:'#7AD6C6',
                }
            },
        },
        {
            name: 'Ⅲ',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL3?FD_FINAL_LEVEL3.substring(1)=='Ⅲ':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	              img1: {
    	            	  backgroundColor: {
    		                    image: '/image/assets3.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  fontWeight:100,
    	                  fontFamily:'Arial',
//    	                  backgroundColor:'#fff'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     Ⅲ}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [10],
            itemStyle: {
                normal: {
                    color:'#74CCBD',
                }
            },
        },
        {
            name: 'Ⅳ',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL3?FD_FINAL_LEVEL3.substring(1)=='Ⅳ':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	              img1: {
    	            	  backgroundColor: {
    		                    image: '/image/assets4.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#969696',
    	                  fontWeight:1,
    	                  fontFamily:'Arial'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     Ⅳ}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [10],
            itemStyle: {
                normal: {
                    color:'#6ABAAC',
                }
            },
        },
    ]
}
//得分分位数
var rate2Chart = echarts.init(document.getElementById('rate2'));
var rate2Option = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false }
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['低',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['高',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
  
    series: [
        {
            name: "%",
            type: "bar",
            barWidth: 14,
            data: [RANKING,],
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                	color: '#6AB9AC'
                }
            },
            zlevel: 1,
            label: { //图形上的文本标签
                show: true,
                position: 'right',//标签的位置
                offset: [-25, 3],
                fontSize: 12,  //标签字号
                rich: {
                    img1: {
                      backgroundColor: {
                        image: '/image/assets11.png',
                      },
                      height: 15,
  	                  width: 45,	            
  	                  lineHeight:40,    
                    },
                    data1: {
  	                  height: 35,
  	                  lineHeight:35,
  	                  width: 35,
  	                  fontSize: 12,          
  	                  color: '#969696',
  	                  fontWeight:1,
  	                  fontFamily:'Arial'
  	                }
                  },
	  	          formatter: function (param) {
		              var res = '';
		              res += ' {data1|  '+param.value+'%'+'}'+
		                 '\n{img1|  }';
		              return res;
		            },
            }
        }, {
            name: "进度条背景",
            type: "bar",
            barGap: "-100%",
            barWidth: 14,
            data: [100],
            color:'#F2F3F5',
        }
    ]


}
var FD_INTERN_NAME4='';
var FD_INTERN_LEVEL4='';
var FD_FINAL_NAME4='';
var FD_FINAL_LEVEL4='';
var FD_TYPE4='';
var RANKING4='';
var TRACK_TYPE4='';
var FD_SEGMENT_INDUSTRY4='';
var FD_ADMISSION_SUGGEST4='';
// 债项安全评级报告(主体信用评级)   无数据
var rate3Chart = echarts.init(document.getElementById('rate3'));
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getFacilityMainInfo?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	FD_INTERN_NAME4=data.FD_INTERN_NAME;
    	FD_INTERN_LEVEL4=data.FD_INTERN_LEVEL;
    	FD_FINAL_NAME4=data.FD_FINAL_NAME;
    	FD_FINAL_LEVEL4=data.FD_FINAL_LEVEL;
    	RANKING4=data.RANKING;
    	RANKING4=parseFloat(RANKING4);
    	FD_TYPE4=data.FD_TYPE;
    	TRACK_TYPE4=data.TRACK_TYPE;
    	FD_SEGMENT_INDUSTRY4=data.FD_SEGMENT_INDUSTRY;
    	FD_ADMISSION_SUGGEST4=data.FD_ADMISSION_SUGGEST;
    	$("#FD_INTERN_NAME4").html(FD_INTERN_NAME4);
    	$("#FD_INTERN_LEVEL4").html(FD_INTERN_LEVEL4);
    	$("#FD_FINAL_NAME4").html(FD_FINAL_NAME4);
    	$("#FD_FINAL_LEVEL4").html(FD_FINAL_LEVEL4);
    	$("#FD_TYPE4").html(FD_TYPE4);
    	$('#TRACK_TYPE4').html(TRACK_TYPE4);
    	$("#FD_SEGMENT_INDUSTRY4").html(FD_SEGMENT_INDUSTRY4);
    	$("#FD_ADMISSION_SUGGEST4").html(FD_ADMISSION_SUGGEST4);
    }
});
var rate3Option = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false }
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['A',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['J',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: 'A',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='A':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	              img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject1.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     A}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '20%',
            data: [2, ],
            itemStyle: {
                normal: {
                    color:'#E6EAEE',
                }
            },

        },
        {
            name: 'B',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='B':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject2.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     B}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#DAE5F2',
                }
            },
        },
        {
            name: 'C',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='C':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject3.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	            	console.log(1111,param);
    	              var res = '';
    	              res += ' {data1|     C}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#C9DCF2',
                }
            },
        },
        {
            name: 'D',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='D':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject4.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     D}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#BAD4F2',
                }
            },
        },
        {
            name: 'E',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='E':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject5.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     E}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#A9CAF2',
                }
            },
        },
        {
            name: 'F',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='F':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject6.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     F}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#96BFF2',
                }
            },
        },
        {
            name: 'G',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='G':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject7.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     G}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#82B2ED',
                }
            },
        },
        {
            name: 'H',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='H':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject8.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     H}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#6FA8ED',
                }
            },
        },
        {
            name: 'I',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='I':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject9.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     I}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#5893ED',
                }
            },
        },
        {
            name: 'J',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL4?FD_FINAL_LEVEL4=='J':false,
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/subject10.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'sans-serif'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     J}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [2],
            itemStyle: {
                normal: {
                    color:'#5085D6',
                }
            },
        },
    ]
}
//得分分位数
var rate4Chart = echarts.init(document.getElementById('rate4'));
var rate4Option = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false }
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['低',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['高',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
   
    series: [
        {
            name: "%",
            type: "bar",
            barWidth: 14,
            data: [RANKING4,],
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                	color: '#7BA8E1'
                }
            },
            zlevel: 1,
//            label: { //图形上的文本标签
//                show: true,
//                position: 'right',//标签的位置
//                offset: [-30, -30],  //标签文字的偏移，此处表示向上偏移40
//                fontSize: 12,  //标签字号
//                rich: {
//                    img1: {
//                      backgroundColor: {
//                        image: '/image/point0.png',
//                      },
//                      height: 40,
//                      width: 40,
//                      fontSize: 12,          
//                      color: '#fff',//标签字体颜色
//                    }
//                  },
//                  formatter: function (param) {
//                    var res = '';
//                    res += 
//                       ' {img1|  '+param.value+'%'+'}'
//                    return res;
//                  },
//            }
             label: { //图形上的文本标签
                show: true,
                position: 'right',//标签的位置
                offset: [-25, 3],
                fontSize: 12,  //标签字号
                rich: {
                    img1: {
                      backgroundColor: {
                        image: '/image/subject11.png',
                      },
                      height: 15,
  	                  width: 45,	            
  	                  lineHeight:40,    
                    },
                    data1: {
  	                  height: 35,
  	                  lineHeight:35,
  	                  width: 35,
  	                  fontSize: 12,          
  	                  color: '#969696',
  	                  fontWeight:1,
  	                  fontFamily:'Arial'
  	                }
                  },
	  	          formatter: function (param) {
		              var res = '';
		              res += ' {data1|  '+param.value+'%'+'}'+
		                 '\n{img1|  }';
		              return res;
		            },
            }
        }, {
            name: "进度条背景",
            type: "bar",
            barGap: "-100%",
            barWidth: 14,
            data: [100],
            color:'#F2F3F5',

        }
    ]
}
//债项安全评级报告(增信措施评价)  评级等级
var rate5Chart = echarts.init(document.getElementById('rate5'));
var CREDIT_TYPE2='';
var FD_FINAL_LEVEL2='';
var FD_FINAL_NAME2='';
var FD_INTERN_NAME2='';
var FD_INTERN_LEVEL2='';
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getFacilityCredit?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	CREDIT_TYPE2=data.CREDIT_TYPE;
    	FD_FINAL_LEVEL2=data.FD_FINAL_LEVEL;
    	FD_FINAL_NAME2=data.FD_FINAL_NAME;
    	FD_INTERN_NAME2=data.FD_INTERN_NAME;
    	FD_INTERN_LEVEL2=data.FD_INTERN_LEVEL;
    	$("#FD_INTERN_NAME2").html(FD_INTERN_NAME2);
    	$("#FD_FINAL_NAME2").html(FD_FINAL_NAME2);
    	$("#FD_FINAL_LEVEL2").html(FD_FINAL_LEVEL2);
    	$("#CREDIT_TYPE2").html(CREDIT_TYPE2);    
    	$("#FD_INTERN_LEVEL2").html(FD_INTERN_LEVEL2);
    }
});
var rate5Option = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false }
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['Ⅰ',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 15
        },
        data: ['Ⅳ',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: 'I',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
	          show: FD_FINAL_LEVEL2=='Ⅰ',
	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
	          rich: {
	              img1: {
	            	  backgroundColor: {
		                    image: '/image/debt1.png',
		               },
	                height: 15,
	                width: 50,	            
	                lineHeight:40,              
	              },
	              data1: {
	                  height: 35,
	                  width: 35,
	                  fontSize: 12,          
  	                  color: '#D1C5D8',
  	                  fontWeight:1,
  	                  fontFamily:'Arial'
	                }
	            },
	            formatter: function (param) {
	              var res = '';
	              res += ' {data1|     Ⅰ}'+
	                 '\n{img1|  }';
	              return res;
	            },
            },
            barWidth: '20%',
            data: [10, ],
            itemStyle: {
                normal: {
                    color:'#C7CCFF',
                    borderColor:'#fff',
                    borderWith:3,
                }
            },
        },
        {
            name: 'Ⅱ',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
              show: FD_FINAL_LEVEL2=='Ⅱ',
  	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
  	          rich: {
  	        	  img1: {
	            	  backgroundColor: {
		                    image: '/image/debt2.png',
		               },
	                height: 15,
	                width: 50,	            
	                lineHeight:40,              
	              },
  	              data1: {
  	                  height: 35,
  	                  lineHeight:35,
  	                  width: 35,
  	                  fontSize: 12,          
  	                  color: '#D1C5D8',
  	                  fontWeight:1,
  	                  fontFamily:'Arial'
  	                }
  	            },
  	            formatter: function (param) {
  	              var res = '';
  	              res += ' {data1|     Ⅱ}'+
  	                 '\n{img1|  }';
  	              return res;
  	            },
              },
            barWidth: '10%',
            data: [10],
            itemStyle: {
                normal: {
                    color:'#A8B0FF',
                }
            },
        },
        {
            name: 'Ⅲ',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL2=='Ⅲ',
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/debt3.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'Arial'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     Ⅲ}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [10],
            itemStyle: {
                normal: {
                    color:'#8C95FA',
                }
            },
        },
        {
            name: 'Ⅳ',
            type: 'bar',
            stack: '一班',
            label: { //图形上的文本标签
                show: FD_FINAL_LEVEL2=='Ⅳ',
    	          offset: [0, 3],  //标签文字的偏移，此处表示向上偏移40
    	          rich: {
    	        	  img1: {
    	            	  backgroundColor: {
    		                    image: '/image/debt4.png',
    		               },
    	                height: 15,
    	                width: 50,	            
    	                lineHeight:40,              
    	              },
    	              data1: {
    	                  height: 35,
    	                  lineHeight:35,
    	                  width: 35,
    	                  fontSize: 12,          
    	                  color: '#D1C5D8',
    	                  fontWeight:1,
    	                  fontFamily:'Arial'
    	                }
    	            },
    	            formatter: function (param) {
    	              var res = '';
    	              res += ' {data1|     Ⅳ}'+
    	                 '\n{img1|  }';
    	              return res;
    	            },
                },
            barWidth: '10%',
            data: [10],
            itemStyle: {
                normal: {
                    color:'#828AE8',
                }
            },
        },
    ]
}
//债项安全评级报告(风险提示) 
var SELECT_EXPLAIN5='';
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getFacilityWarning?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	if(data&&data.msg==='没有数据'){
    		$("#riskTips").hide();
			return;
		}
    	var list=[];
    	for(var i=0;i<data.length;i++){
    		if(data[i].NAME==='资产重大风险提示'){
    			SELECT_EXPLAIN5=data[i].SELECT_EXPLAIN;
    		}else{
    			list.push({
    				SELECT_EXPLAIN:data[i].SELECT_EXPLAIN,
    				TIP:data[i].TIP,
    				count:data[i].count,
    			});
    		}
    	}
    	$("#SELECT_EXPLAIN5").html(SELECT_EXPLAIN5);
    	if(list.length==0){
    		$("#riskTip2").hide();
			return;
		}
    	var str='';
    	if(list&&list.length>0){
    		for(var j=0;j<list.length;j++){
    			var TIP=list[j].TIP?list[j].TIP:'';
    			var SELECT_EXPLAIN=list[j].SELECT_EXPLAIN?list[j].SELECT_EXPLAIN:'';
    			if(SELECT_EXPLAIN){
    				str+="<li>"+TIP+"<span>   ("+ SELECT_EXPLAIN +")</span></li>";
    			}else{
    				str+="<li>"+TIP+"</li>";
    			}
        	}
    	} 
    	
    	$("#list6").append(str);
    	$("#count6").html(list[0]?list[0].count:0);
    }
});
//贡献最大
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/DebtIndexController/getFacilityCompare?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	maxValue=data[0].value;
    	if(maxValue=='1'){//主体大
    		$("#contribution1").show();
    	}else if(maxValue=='2'){//资产大
    		$("#contribution2").show();
    	}if(maxValue=='0'){//主体大
    		$("#contribution1").show();
    		$("#contribution2").show();
    	}
    }
});

//标签
$.ajax({
	type: "GET",
	url: "/irs/DebtIndexController/getFacilityTrait?id=" + id,
	contentType: false,
	cache: false,
	processData: false,
	success: function (data) {
		for(var i=0;i<data.length;i++){
			if(data[i].TYPE==='0'){
				if(data[i].result=='Y'){
					$("#li1").addClass('active1');
				}else{
					$("#li1").removeClass('active1');
				}
			}else if(data[i].TYPE=='1'){
				if(data[i].result=='Y'){
					$("#li2").addClass('active1');
				}else{
					$("#li2").removeClass('active1');
				}
			}else if(data[i].TYPE=='2'){
				if(data[i].result=='Y'){
					$("#li4").addClass('active2');
				}else{
					$("#li4").removeClass('active2');
				}
			}else if(data[i].TYPE=='3'){
				if(data[i].result=='Y'){
					$("#li5").addClass('active2');
				}else{
					$("#li5").removeClass('active2');
				}
			}else if(data[i].TYPE=='4'){
				if(data[i].result=='Y'){
					$("#li3").addClass('active1');
				}else{
					$("#li3").removeClass('active1');
				}
			}
		}
	}
});



//详情
$("#detail1").click(function(){
	$.ajax({
    url : "/irs/DebtIndexController/getFacilityAssetsScore",
    type : "GET",
    data : {id: id},
    dataType : "JSON",
    success : function(data){
      if(data.FD_FINAL_SCO == '0'){
			swal({
					  title: "租赁物占比小于 60% 或资产得分为 0 无评级报告",
					  timer: 2000,
					  showConfirmButton: false,
					  imageUrl: "/image/swal_info.png"
					});
        //alert("租赁物占比小于 60% 或资产得分为 0 无评级报告")
      } else {
	    window.open('/irs/DebtIndexController/getFacilityToAssets?id='+id);
        //window.open('/irs/assetsRating/assetsRatingReport?custNo='+id);
      }
    }
  })
	
});
$("#detail2").click(function(){
	$.ajax({
      url : "/irs/DebtIndexController/getFacilityMainScore",
      type : "GET",
      data : {id: id},
      dataType : "JSON",
      success : function(data){
        if(data.FD_SCO == '0'){
			swal({
					  title: "非评级主体或主体评级得分为 0 无评级报告",
					  timer: 2000,
					  showConfirmButton: false,
					  imageUrl: "/image/swal_info.png"
					});
          // alert("非评级主体或主体评级得分为 0 无评级报告")
        } else {
          //window.open('/irs/mainRating/mainRatingReport?custNo='+id);
          window.open('/irs/DebtIndexController/getFacilityToMain?id='+id);
        }
      }
    })
	
});





gradeChart.setOption(gradeOption);
// scoreChart.setOption(scoreOption);
debtRateChart1.setOption(debtRateOption1);
debtRateChart2.setOption(debtRateOption2);
rate1Chart.setOption(rate1Option);
rate2Chart.setOption(rate2Option);
rate3Chart.setOption(rate3Option);
rate4Chart.setOption(rate4Option);
rate5Chart.setOption(rate5Option);