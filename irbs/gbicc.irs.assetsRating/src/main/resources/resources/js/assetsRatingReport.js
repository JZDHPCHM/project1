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
//var id=getQueryVariable('custNo')?getQueryVariable('custNo'):'';
var id=getCustNo();



$(".tooltiptext").html('A类（项目租赁）、B类（非项目租赁）');
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getAssetsHeader?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       $("#company label").html(data.FD_PROJECT_NAME);
       $("#company span").html('（项目编号：'+data.FD_PROJECT_CODE+'）');
    }
});
// 信用评价仪表盘
var FD_FINAL_LEVEL='';
var FD_FINAL_DATE='';
var F_DATE='';
var TYPE='';
var WARNING='';
var INFO='';
var number = 0;
$.ajax({
     async: false,
     type: "GET",
     url: "/irs/table/getAssetsLevel?id="+id,
     contentType: false,
     cache: false,
     processData: false,
     success: function (data) {
         FD_FINAL_LEVEL=data.FD_FINAL_LEVEL;
         if(FD_FINAL_LEVEL=='AⅠ'||FD_FINAL_LEVEL=='BⅠ'){
      	   number=1; 
         }else if(FD_FINAL_LEVEL=='AⅡ'||FD_FINAL_LEVEL=='BⅡ'){
      	   number=2/3;
         }else if(FD_FINAL_LEVEL=='AⅢ'||FD_FINAL_LEVEL=='BⅢ'){
        	 number=1/3;
         }else if(FD_FINAL_LEVEL=='Ⅳ'){
        	 number=0;
         }
         FD_FINAL_DATE=data.FD_FINAL_DATE;
         FD_DATE=data.FD_DATE;
         TYPE=data.TYPE;
         FD_PD=data.FD_PD;
         INFO=data.INFO;
         $("#list_sp1").html(TYPE);
         $("#list_sp2").html(FD_PD);
         $("#time1 span:nth-child(1)").html(FD_FINAL_DATE);
         $("#time1 span:nth-child(2)").html(FD_DATE);
         
         
     }
 });

var gradeChart = echarts.init(document.getElementById('grade'));

var gradeOption = {
    series: [
        {
            name: '',
            type: 'gauge',
            radius: '62%',
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
                color:'#A2C3D8'
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
            radius: '90%',                          // 仪表盘半径
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
                        [number, '#64B4A7'],
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
                     }else if (param==66) {
                         return 'II'
                     }else if (param==32) {                 	 
                         return 'III'
                     }else if(param==0){
                         return 'IV'
                     }
                     
			    },
            },
            detail: {
            	formatter : `${FD_FINAL_LEVEL}`,
                offsetCenter: [0, '-10%'],
                fontSize: 44,
                fontWeight: 'normal',
                fontFamily: 'Arial',
                color:'#64B4A7'
            },
            data: [{ value: number*100, name: INFO }],
            pointer: {
                show: false,
            }
        },
    ]
};



//资产信用评级报告描述综述(仪表盘右边那一块)

$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getAssetsInfo?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
        var FD_FINAL_SCO=data.FD_FINAL_SCO;
        var FD_INTERN_NAME=data.FD_INTERN_NAME;
        if(FD_INTERN_NAME == null)
            FD_INTERN_NAME = '';
        var FD_FINAL_NAME=data.FD_FINAL_NAME;
        if(FD_FINAL_NAME == null)
            FD_FINAL_NAME = '';
        var NET_VALUE=data.NET_VALUE;
        if(NET_VALUE == null)
            NET_VALUE = '';
		else
		    NET_VALUE += ' 万元';
        var ASSESSED_VALUE=data.ASSESSED_VALUE;
        if(ASSESSED_VALUE == null)
            ASSESSED_VALUE = '';
		else
		    ASSESSED_VALUE += ' 万元';
        var CORELEASE_PROPORTION=data.CORELEASE_PROPORTION;
        if(CORELEASE_PROPORTION === '%')
            CORELEASE_PROPORTION = '';
        var FD_ADMISSION_SUGGEST=data.FD_ADMISSION_SUGGEST;
        if(FD_ADMISSION_SUGGEST == null)
            FD_ADMISSION_SUGGEST = '';
        var PRODUCT_TYPE=data.PRODUCT_TYPE
        if(PRODUCT_TYPE == null)
            PRODUCT_TYPE = '';
        var LEASE_ITEM_SHORT_NAME=data.LEASE_ITEM_SHORT_NAME;
        if(LEASE_ITEM_SHORT_NAME == null)
            LEASE_ITEM_SHORT_NAME = '';
        var WARNING=data.WARNING;
        $(".content1_1 label").html(FD_FINAL_SCO);
        $('.content1_2_1 span').html(data.FD_INTERN_LEVEL);
        $('.content1_2_2 span').html(data.FD_FINAL_LEVEL);
        $('.content1_2_1 label').html(FD_INTERN_NAME);
        $('.content1_2_2 label').html(FD_FINAL_NAME);
        $('.content1_right1_3_top1 span').html(NET_VALUE);
        $('.content1_right1_3_top2 span').text(ASSESSED_VALUE);
        $('.content1_right1_3_top3 span').html(CORELEASE_PROPORTION);
        $('#FD_ADMISSION_SUGGEST').html(FD_ADMISSION_SUGGEST);
        $('#PRODUCT_TYPE').html('<span>产品类型： </span>'+PRODUCT_TYPE);
        $('#PRODUCT_TYPE').attr('title',PRODUCT_TYPE);
        $('#LEASE_ITEM_SHORT_NAME').html('<span>租赁物名称： </span><label title="'+LEASE_ITEM_SHORT_NAME+'">'+LEASE_ITEM_SHORT_NAME+'</label>');
        $("#WARNING").html(WARNING);
    }
});

//在同赛道中的位置
var RANKING=0;
var RANKINGONTRACK=0
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getAssetsPosition?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	RANKINGONTRACK=data.RANKINGONTRACK;
    	RANKINGONTRACK=parseFloat(RANKINGONTRACK);
        RANKING=data.RANKING;
        RANKING=parseFloat(RANKING);
    }
});

var progress1Chart = echarts.init(document.getElementById('progress1'));
var progress1Option = {
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
            fontSize: 14
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
            fontSize: 14
        },
        data: ['高',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
      series: [
        {
            name: "%",
            type: "bar",
            barWidth: 16,
            data: [RANKING,],
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                	color: '#64B4A7'
                }
            },
            zlevel: 1,
            label: { //图形上的文本标签
                show: true,
                position: 'right',//标签的位置
                offset: [-30, -30],  //标签文字的偏移，此处表示向上偏移40
                fontSize: 12,  //标签字号
                rich: {
                    img1: {
                      backgroundColor: {
                        image: '/image/point.png',
                        
                      },
                      height: 40,
                      width: 40,
                      fontSize: 12,          
                      color: '#fff',//标签字体颜色
                    }
                  },
                  formatter: function (param) {
                    var res = '';
                    res += 
                       ' {img1|  '+param.value+'%'+'}'
                    return res;
                  },
            }
//            label: { //图形上的文本标签
//                show: true,
////                position: 'right',//标签的位置
//                offset: [0, -5],  //标签文字的偏移，此处表示向上偏移40
//                fontSize: 12,  //标签字号
//                rich: {
//                    img1: {
//                      backgroundColor: {
//                        image: '/image/point2.png',
//                      },
//                      height: 30,
//                      width: 50,
//                      color: '#fff',//标签字体颜色
//                      lineHeight:40,
//                     
//                    },
//                    data1: {
//                        height: 40,
//                        width: 40,
//                        fontSize: 12,          
//                        color: '#fff',
//                        backgroundColor: {
//                          image: '/image/point.png',
//                        },
//                      }
//                  },
//                  formatter: function (param) {
//                	
//                    var res = '';
//                    res += ' {data1|  '+ param.value +'%}'+
//                       '\n\n{img1|  }';
//           
//                    return res;
//                  },
//            }
//            label: { //图形上的文本标签
//                show: true,
////                position: 'right',//标签的位置
//                offset: [0, -30],  //标签文字的偏移，此处表示向上偏移40
////                formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
////                color: '#969696',//标签字体颜色
//                fontSize: 12,  //标签字号
//                rich: {
//                    img1: {
//                      backgroundColor: {
//                        image: '/image/point.png',
//                        
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
//                       ' {img1|  '+param.value+'% }'
//                    return res;
//                  },
//            }

        }, {
            name: "进度条背景",
            type: "bar",
            barGap: "-100%",
            barWidth: 16,
            data: [100],
            color:'#F2F3F5',

        }
    ]

}

//在同产品中所处的位置;
var progress2Chart = echarts.init(document.getElementById('progress2'));
var progress2Option = {
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
            fontSize: 14
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
            fontSize: 14
        },
        data: ['高',]//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
      series: [
        {
            name: "%",
            type: "bar",
            barWidth: 16,
            data: [RANKINGONTRACK,],
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                	color: '#64B4A7'
                }
            },
            zlevel: 1,
            label: { //图形上的文本标签
                show: true,
                position: 'right',//标签的位置
                offset: [-30, -30],  //标签文字的偏移，此处表示向上偏移40
                fontSize: 12,  //标签字号
                rich: {
                    img1: {
                      backgroundColor: {
                        image: '/image/point.png',
                      },
                      height: 40,
                      width: 40,
                      fontSize: 12,          
                      color: '#fff',//标签字体颜色
                    }
                  },
                  formatter: function (param) {
                    var res = '';
                    res += 
                       ' {img1|  '+param.value+'%'+'}'
                    return res;
                  },
            }
//            label: { //图形上的文本标签
//                show: true,
////                position: 'right',//标签的位置
//                offset: [0, -5],  //标签文字的偏移，此处表示向上偏移40
//                fontSize: 12,  //标签字号
//                rich: {
//                    img1: {
//                      backgroundColor: {
//                        image: '/image/point2.png',
//                      },
//                      height: 30,
//                      width: 50,
//                      color: '#fff',//标签字体颜色
//                      lineHeight:40,
//                     
//                    },
//                    data1: {
//                        height: 40,
//                        width: 40,
//                        fontSize: 12,          
//                        color: '#fff',
//                        backgroundColor: {
//                          image: '/image/point.png',
//                        },
//                      }
//                  },
//                  formatter: function (param) {
//                	
//                    var res = '';
//                    res += ' {data1|  '+ param.value +'%}'+
//                       '\n\n{img1|  }';
//           
//                    return res;
//                  },
//            }
//            label: { //图形上的文本标签
//                show: true,
////                position: 'right',//标签的位置
//                offset: [0, -30],  //标签文字的偏移，此处表示向上偏移40
////                formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
////                color: '#969696',//标签字体颜色
//                fontSize: 12,  //标签字号
//                rich: {
//                    img1: {
//                      backgroundColor: {
//                        image: '/image/point.png',
//                        
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
//                       ' {img1|  '+param.value+'% }'
//                    return res;
//                  },
//            }

        }, {
            name: "进度条背景",
            type: "bar",
            barGap: "-100%",
            barWidth: 16,
            data: [100],
            color:'#F2F3F5',

        }
    ]

}

//跟公司所有存量资产对比
var compare1_data=[];
var compare1_data_new=[];
var compare1_data2=[];
var compare1_data3=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getGroup?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
        data.map(item=>{
        	if(item.TYPE=='0'){//评级对象
        		compare1_data2.push(item.FD_SCORE);
        		compare1_data.push({ name: item.NAME, },);
        	}else if(item.TYPE=='1'){//平均值
        		compare1_data3.push(item.FD_SCORE);
//        		compare1_data.push({ name: item.NAME.slice(0,3),   },);
        	}	
        });

        data.map(item=>{
        	if(item.TYPE=='-1'){
        		compare1_data.map(item2=>{
        			if(item.NAME.indexOf(item2.name) != -1 ){
        				compare1_data_new.push({
        					name:item2.name,
        					max:item.FD_SCORE?item.FD_SCORE:1
            			});
        			}
        			
        		});
        		
        	}
        });
    }
});
var compare1hart = echarts.init(document.getElementById('compare1'));
var compare1Option={};
if(compare1_data2.length!=0){
	compare1Option = {
		    color: ['#7EE2A7', '#94D3AC'],
		    tooltip: {},
		    legend: {
		        right: '0',
		        top: 0,
		        orient: 'vertical',
		        itemWidth: 14,
		        itemHeight: 14,
		        itemGap: 12,
		        icon: 'rect',
		        data: ['评级对象', '全量资产平均值'],
		        textStyle: {
		            padding: [3, 0, 0, 5],
		            lineHeight: 18,
		            color: '#969696'
		        }
		    },
		    radar: {
		        shape: 'polygon',
		        name: {
		            textStyle: {
		                color: '#777',
		                padding: [0, 3]
		            }
		        },
		        splitNumber: 4,
		        splitArea: {
		            show: false
		        },
		        splitLine: {
		            lineStyle: {
		                color: '#ececec'
		            }
		        },
		        axisLine: {
		            lineStyle: {
		                color: '#ececec'
		            }
		        },
		        indicator: compare1_data_new,  //compare1_data_new
		    },
		    series: [{
		        name: '评价指标',
		        type: 'radar',
		        itemStyle: {
		            normal: {
		                areaStyle: {
		                    type: 'default',
		                    opacity: '0.1'
		                }
		            }
		        },
		        symbol: 'none',
		        data: [
		            {
		            	value:compare1_data2,//compare1_data2
		                name: '评级对象'
		            },
		            {
		            	 value:compare1_data3,//compare1_data3
		                name: '全量资产平均值'
		            }
		        ]
		    }]
		};

}


//跟同产品的对比
var compare2_data=[];
var compare2_data_new=[];
var compare2_data2=[];
var compare2_data3=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getProduct?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	 data.map(item=>{
         	if(item.TYPE=='0'){//评级对象
         		compare2_data2.push(item.FD_SCORE);
         		compare2_data.push({ name: item.NAME, },);
         	}else if(item.TYPE=='1'){//平均值
         		compare2_data3.push(item.FD_SCORE);
//         		compare2_data.push({ name: item.NAME.slice(0,3),},);
         	}	
         });
         data.map(item=>{
         	if(item.TYPE=='-1'){
         		compare2_data.map(item2=>{
         			if(item.NAME.indexOf(item2.name) != -1 ){
         				compare2_data_new.push({
         					name:item2.name,
         					max:item.FD_SCORE?item.FD_SCORE:1
             			});
         			}
         			
         		});
         		
         	}
         });

    }
});

var compare2hart = echarts.init(document.getElementById('compare2'));
var compare2Option={};
if(compare2_data2.length!==0){
	compare2Option = {
			color: ['#7EE2A7', '#94D3AC'],
			tooltip: {},
		    legend: {
		        right: '0',
		        top: 0,
		        orient: 'vertical',
		        itemWidth: 14,
		        itemHeight: 14,
		        itemGap: 12,
		        icon: 'rect',
		        data: ['评级对象', '同类产品平均值'],
		        textStyle: {
		            padding: [3, 0, 0, 5],
		            lineHeight: 18,
		            color: '#969696'
		        }
		    },
		    radar: {
		        shape: 'polygon',
		        name: {
		            textStyle: {
		                color: '#777',
		                padding: [0, 3]
		            }
		        },
		        splitNumber: 4,
		        splitArea: {
		            show: false
		        },
		        splitLine: {
		            lineStyle: {
		                color: '#ececec'
		            }
		        },
		        axisLine: {
		            lineStyle: {
		                color: '#ececec'
		            }
		        },
		        indicator:compare1_data_new,
		    },
		    series: [{
		        name: '评价指标',
		        type: 'radar',
		        itemStyle: {
		            normal: {
		                areaStyle: {
		                    type: 'default',
		                    opacity: '0.1'
		                }
		            }
		        },
		        symbol: 'none',
		        data:  [
		            {
		            	value:compare2_data2,
		                name: '评级对象'
		            },
		            {
		            	 value: compare2_data3,
		                name: '同类产品平均值'
		            }
		        ]
		    }]
		};

}
//跟部门的对比
var compare3_data=[];
var compare3_data_new=[];
var compare3_data2=[];
var compare3_data3=[];;
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getAssetsDepartment?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	data.map(item=>{
        	if(item.TYPE=='0'){//评级对象
        		compare3_data2.push(item.FD_SCORE);
        		compare3_data.push({ name: item.NAME, },);
        	}else if(item.TYPE=='1'){//平均值
        		compare3_data3.push(item.FD_SCORE);
        	}	
        });
 
        data.map(item=>{
        	if(item.TYPE=='-1'){
        		compare3_data.map(item2=>{
        			if(item.NAME.indexOf(item2.name) != -1 ){
        				compare3_data_new.push({
        					name:item2.name,
        					max:item.FD_SCORE?item.FD_SCORE:1
            			});
        			}
        			
        		});
        		
        	}
        });
       
    }
});
var compare3hart = echarts.init(document.getElementById('compare3'));
var compare3Option={};
if(compare3_data2.length!==0){
	compare3Option = {
			color: ['#7EE2A7', '#94D3AC'],
			tooltip: {},
		    legend: {
		        right: '0',
		        top: 0,
		        orient: 'vertical',
		        itemWidth: 14,
		        itemHeight: 14,
		        itemGap: 12,
		        icon: 'rect',
		        data: ['评级对象', '同部门资产平均值'],
		        textStyle: {
		            padding: [3, 0, 0, 5],
		            lineHeight: 18,
		            color: '#969696'
		        }
		    },
		    radar: {
		        shape: 'polygon',
		        name: {
		            textStyle: {
		                color: '#777',
		                padding: [0, 3]
		            }
		        },
		        splitNumber: 4,
		        splitArea: {
		            show: false
		        },
		        splitLine: {
		            lineStyle: {
		                color: '#ececec'
		            }
		        },
		        axisLine: {
		            lineStyle: {
		                color: '#ececec'
		            }
		        },
		        indicator:compare3_data_new,
		    },
		    series: [{
		        name: '评价指标',
		        type: 'radar',
		        itemStyle: {
		            normal: {
		                areaStyle: {
		                    type: 'default',
		                    opacity: '0.1'
		                }
		            }
		        },
		        symbol: 'none',
		        data: [
		            {
		            	value:compare3_data2,
		                name: '评级对象'
		            },
		            {
		            	 value: compare3_data3,
		                name: '同部门资产平均值'
		            }
		        ]
		    }]
		};
}



//创收性
var incomeGenerating_data1=[];
var incomeGenerating_data2=[];
var incomeGenerating_data3=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getBarIncome?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
      var html='';
      data.map(item=>{
    	  if(item.FD_INDEXNAME!=='最大值描述'&&item.FD_INDEXNAME!=='最小值描述'){
    		  incomeGenerating_data1.push(item['FD_INDEXVALUE']);
        	  incomeGenerating_data2.push(item['FD_INDEXNAME']);
        	  incomeGenerating_data3.push({
        		  value:item['FD_SCORE'],
        		  name:item['FD_INDEXVALUE'],
        	  });
    	  }else{
    		  var img='<img src="../../image/icon_2.jpg"/>'
              	   if(item.FD_INDEXNAME=='最大值描述'){
              		   img='<img src="../../image/icon_2.jpg"/>'
              	   }else if(item.FD_INDEXNAME=='最小值描述'){
              		   img='<img src="../../image/icon_1.jpg"/>'
              	   }
       		  html+='<h1>'+img+' '+item['INDEXNAME']+':<span>'+item['FD_QUALITATIVE_OPTIONS']+'</span></h1>';
    	  }
    	 
      });
      /* $("#title").html(html);*/
       $("#title").hide();
    }
});

var incomeGeneratingChart = echarts.init(document.getElementById('incomeGenerating'));
var incomeGeneratingOption = {
	    grid: {   // 直角坐标系内绘图网格
	        left: '10',  //grid 组件离容器左侧的距离,
	        //left的值可以是80这样具体像素值，
	        //也可以是'80%'这样相对于容器高度的百分比
	        top: '10',
	        right: '0',
	        bottom: '10%',
	        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
	        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
	        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
	    },
	    xAxis: {
	        type: 'value',
	        splitLine: { show: false },
	        axisLabel: { show: false },
	        axisTick: { show: false },
	        axisLine: { show: false },
	        max: 1
	    },
	    yAxis: [{
	        type: 'category',
	        axisTick: { show: false },
	        axisLine: { show: false },
	        axisLabel: {
	            color: '#969696',
	            fontSize: 12
	        },
	        
	        data: incomeGenerating_data2//类目数据，在类目轴（type: 'category'）中有效。
	        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
	    },
	    {
	        type: 'category',
	        axisTick: { show: false },
	        axisLine: { show: false },
	        axisLabel: {
	            color: '#969696',
	            fontSize: 12
	        },
	        data: incomeGenerating_data1//类目数据，在类目轴（type: 'category'）中有效。
	        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
	    }],
	    series: [
	        {
	            name: "",
	            type: "bar",
	            barWidth: 16,
	            data: incomeGenerating_data3,
	            barCategoryGap: 20,
	            itemStyle: {
	                normal: {
	                    color: function(params) {
	                        if(params.name=='C档'){
	                            return '#5E9DA8';
	                        }
	                        if(params.name=='B档'){
	                            return '#94D3AC';
	                        }
	                        if(params.name=='A档'){
	                            return '#64B4A7';
	                        }
	                    },
	                }
	            },
	            zlevel: 1,
	            label: { //图形上的文本标签
	                show: true,
	                position: 'left',//标签的位置
	                offset: [30, 0],  //标签文字的偏移，此处表示向上偏移40
	                formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
	                color: '#fff',//标签字体颜色
	                fontSize: 12  //标签字号
	            }

	        }, 
	    ]
	}

//保值性
var valuePreservation_data1=[];
var valuePreservation_data2=[];
var valuePreservation_data3=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getBarPreservation?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
      var html='';
      data.map(item=>{
    	  if(item.FD_INDEXNAME!=='最大值描述'&&item.FD_INDEXNAME!=='最小值描述'){
    		  valuePreservation_data1.push(item['FD_INDEXVALUE']);
        	  valuePreservation_data2.push(item['FD_INDEXNAME']);
        	  valuePreservation_data3.push({
        		  value:item['FD_SCORE'],
        		  name:item['FD_INDEXVALUE'],
        	  });
    	  }else{
    		  var img='<img src="../../image/icon_2.jpg"/>'
           	   if(item.FD_INDEXNAME=='最大值描述'){
           		   img='<img src="../../image/icon_2.jpg"/>'
           	   }else if(item.FD_INDEXNAME=='最小值描述'){
           		   img='<img src="../../image/icon_1.jpg"/>'
           	   }
    		  html+='<h1>'+img+' '+item['INDEXNAME']+':<span>'+item['FD_QUALITATIVE_OPTIONS']+'</span></h1>';
    	  }
      });
     /* $("#title2").html(html);*/
      $("#title2").hide();
    }
});



var valuePreservationChart = echarts.init(document.getElementById('valuePreservation'));
var valuePreservationOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '10%',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false },
        max: 1
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 12
        },
//        max: 4,
        data: valuePreservation_data2//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 12
        },
//        max: 4,
        data: valuePreservation_data1//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 16,
            data: valuePreservation_data3,
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                	color: function(params) {
                        if(params.name=='C档'){
                            return '#5E9DA8';
                        }
                        if(params.name=='B档'){
                            return '#94D3AC';
                        }
                        if(params.name=='A档'){
                            return '#64B4A7';
                        }
                    },
                }
            },
            zlevel: 1,
            label: { //图形上的文本标签
                show: true,
                position: 'left',//标签的位置
                offset: [30, 0],  //标签文字的偏移，此处表示向上偏移40
                formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
                color: '#fff',//标签字体颜色
                fontSize: 12  //标签字号
            }

        }, 
    ]
}


//可控性
var controllabilityChart = echarts.init(document.getElementById('controllability'));
var controllability_data1=[];
var controllability_data2=[];
var controllability_data3=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getBarSteerable?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
      var html='';
      data.map(item=>{
    	  if(item.FD_INDEXNAME!=='最大值描述'&&item.FD_INDEXNAME!=='最小值描述'){
    		  controllability_data1.push(item['FD_INDEXVALUE']);
        	  controllability_data2.push(item['FD_INDEXNAME']);
        	  controllability_data3.push({
        		  value:item['FD_SCORE'],
        		  name:item['FD_INDEXVALUE'],
        	  });
    	  }else{
    		  var img='<img src="../../image/icon_2.jpg"/>'
              	   if(item.FD_INDEXNAME=='最大值描述'){
              		   img='<img src="../../image/icon_2.jpg"/>'
              	   }else if(item.FD_INDEXNAME=='最小值描述'){
              		   img='<img src="../../image/icon_1.jpg"/>'
              	   }
       		  html+='<h1>'+img+' '+item['INDEXNAME']+':<span>'+item['FD_QUALITATIVE_OPTIONS']+'</span></h1>';
    	  }
      });
     /* $("#title3").html(html);*/
      $("#title3").hide();
    }
});


var controllabilityOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '10%',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false },
        max: 1
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 12
        },
        data: controllability_data2 //类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 12
        },
        data: controllability_data1 //类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 16,
            data: controllability_data3,
            barCategoryGap: 20,
            itemStyle: {
                normal: {
                	color: function(params) {
                        if(params.name=='C档'){
                            return '#5E9DA8';
                        }
                        if(params.name=='B档'){
                            return '#94D3AC';
                        }
                        if(params.name=='A档'){
                            return '#64B4A7';
                        }
                    },
                }
            },
            zlevel: 1,
            label: { //图形上的文本标签
                show: true,
                position: 'left',//标签的位置
                offset: [30, 0],  //标签文字的偏移，此处表示向上偏移40
                formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
                color: '#fff',//标签字体颜色
                fontSize: 12  //标签字号
            }

        }, 
    ]
}

//流通性
var liquidityChart = echarts.init(document.getElementById('liquidity'));
var liquidity_data1=[];
var liquidity_data2=[];
var liquidity_data3=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getBarCirculation?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
     var html='';
      data.map(item=>{
    	  if(item.FD_INDEXNAME!=='最大值描述'&&item.FD_INDEXNAME!=='最小值描述'){
    		  liquidity_data1.push(item['FD_INDEXVALUE']);
        	  liquidity_data2.push(item['FD_INDEXNAME']);
        	  liquidity_data3.push({
        		 value:item['FD_SCORE'],
        		 name:item['FD_INDEXVALUE'],
        	  });
    	  }else{
    		  var img='<img src="../../image/icon_2.jpg"/>'
              	   if(item.FD_INDEXNAME=='最大值描述'){
              		   img='<img src="../../image/icon_2.jpg"/>'
              	   }else if(item.FD_INDEXNAME=='最小值描述'){
              		   img='<img src="../../image/icon_1.jpg"/>'
              	   }
       		  html+='<h1>'+img+' '+item['INDEXNAME']+':<span>'+item['FD_QUALITATIVE_OPTIONS']+'</span></h1>';
    	  }
      });
     /* $("#title4").html(html);*/
      $("#title4").hide();
      console.log(111,liquidity_data3);
    }
});

var liquidityOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '10',
        right: '0',
        bottom: '10%',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
    },
    xAxis: {
        type: 'value',
        splitLine: { show: false },
        axisLabel: { show: false },
        axisTick: { show: false },
        axisLine: { show: false },
        max: 1,
    },
    yAxis: [{
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 12
        },
        data: liquidity_data2 //类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 12
        },
        data: liquidity_data1//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 16,
            data: liquidity_data3,
            barCategoryGap: 20,
            itemStyle: {
                normal: {
                	color: function(params) {
                        if(params.name=='C档'){
                            return '#5E9DA8';
                        }
                        if(params.name=='B档'){
                            return '#94D3AC';
                        }
                        if(params.name=='A档'){
                            return '#64B4A7';
                        }
                    },
                }
            },
            zlevel: 1,
            label: { //图形上的文本标签
                show: true,
                position: 'left',//标签的位置
                offset: [30, 0],  //标签文字的偏移，此处表示向上偏移40
                formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
                color: '#fff',//标签字体颜色
                fontSize: 12  //标签字号
            }

        }, 
    ]
}



//风险提示
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/table/getFooter?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	if(data&&data.msg==='没有数据'){
    		$("#riskTips").hide();
			return;
		}
      $("#MAJOR_RISK_WARNING").html(data.MAJOR_RISK_WARNING);
    }
});



//标签
$.ajax({
	//async: false,
	type: "GET",
	url: "/irs/table/getAssetsTrait?id=" + id,
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
					$("#li3").addClass('active2');
				}else{
					$("#li3").removeClass('active2');
				}
			}else if(data[i].TYPE=='3'){
				if(data[i].result=='Y'){
					$("#li5").addClass('active2');
				}else{
					$("#li5").removeClass('active2');
				}
			}else if(data[i].TYPE=='4'){
				if(data[i].result=='Y'){
					$("#li4").addClass('active2');
				}else{
					$("#li4").removeClass('active2');
				}
			}
		}
	}
});






var downPdf = document.getElementById("btn");
downPdf.onclick = function () {
	 document.body.scrollTop = document.documentElement.scrollTop = 0;
    html2canvas(
            document.getElementById("export_content"),
            {
//                dpi:1440,//导出pdf清晰度
                scale: 10,
                onrendered: function (canvas) {
                    var contentWidth = canvas.width;
                    var contentHeight = canvas.height;

                    //一页pdf显示html页面生成的canvas高度;
                    var pageHeight = contentWidth / 592.28 * 841.89;
                    //未生成pdf的html页面高度
                    var leftHeight = contentHeight;
                    //pdf页面偏移
                    var position = 0;
                    //html页面生成的canvas在pdf中图片的宽高（a4纸的尺寸[595.28,841.89]）
                    var imgWidth = 595.28;
                    var imgHeight = 592.28 / contentWidth * contentHeight;

                    var pageData = canvas.toDataURL('image/jpeg', 1.0);
                    var pdf = new jsPDF('', 'pt', 'a4');

                    //有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
                    //当内容未超过pdf一页显示的范围，无需分页
                    if (leftHeight < pageHeight) {
                        pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight);
                    } else {
                        while (leftHeight > 0) {
                            pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight)
                            leftHeight -= pageHeight;
                            position -= 841.89;
                            //避免添加空白页
                            if (leftHeight > 0) {
                                pdf.addPage();
                            }
                        }
                    }
                    pdf.save('债项安全评级报告.pdf');
                },
                //背景设为白色（默认为黑色）
                background: "#fff"  
            })
}

gradeChart.setOption(gradeOption);
progress1Chart.setOption(progress1Option); 
progress2Chart.setOption(progress2Option);

compare1hart.setOption(compare1Option);
compare2hart.setOption(compare3Option);
compare3hart.setOption(compare2Option);
incomeGeneratingChart.setOption(incomeGeneratingOption);


valuePreservationChart.setOption(valuePreservationOption);
controllabilityChart.setOption(controllabilityOption);
liquidityChart.setOption(liquidityOption);
