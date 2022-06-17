


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
var id=getQueryVariable('custNo')?getQueryVariable('custNo'):'0daf1a7f-b01a-4f9f-88ed-1b5b12a33d6a';

$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getMainHeader?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {

       $("#company label").html(data.FD_CUST_NAME);
       $("#company span").html('（客户编号：'+data.FD_CUST_CODE+'）');
    }
});

var FD_FINAL_LEVEL='';
var DESCRIBE='';
var FD_FINAL_DATE='';
var F_DATE='';
var WARNING='';
var LEVELED='';
// 信用评价仪表盘
var gradeChart = echarts.init(document.getElementById('grade'));
var number = 0;
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getLevel?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       FD_FINAL_LEVEL=data.FD_FINAL_LEVEL;
       if(FD_FINAL_LEVEL=='A'){
    	   number=1; 
       }else if(FD_FINAL_LEVEL=='B'){
    	   number=480/540;
       }else if(FD_FINAL_LEVEL=='C'){
    	   number=420/540;
       }else if(FD_FINAL_LEVEL=='D'){
    	   number=360/540;
       }else if(FD_FINAL_LEVEL=='E'){
    	   number=300/540;
       }else if(FD_FINAL_LEVEL=='F'){
    	   number=240/540;
       }else if(FD_FINAL_LEVEL=='G'){
    	   number=180/540;
       }else if(FD_FINAL_LEVEL=='H'){
    	   number=120/540;
       }else if(FD_FINAL_LEVEL=='I'){
    	   number=60/540;
       }else if(FD_FINAL_LEVEL=='J'){
    	   number=0;
       }
       console.log('number',number);
       DESCRIBE=data.DESCRIBE;

       WARNING=data.WARNING;
       LEVELED=data.LEVELED;
       
       $("#list_sp2").html(DESCRIBE);  
       $("#list_sp3").html(WARNING);  

       $("#time1_1").html(data.FD_FINAL_DATE);
       $("#time1_2").html(data.FD_DATE);
    }
});
console.log('cccc',LEVELED);
var gradeOption = {
    series: [
        {
            name: '',
            type: 'gauge',
            radius: '62%',
            min: 0,
            max: 540,
            startAngle: '210',                      // 起始位置
            endAngle: '-30',                        // 结束位置
            splitNumber: 54, 
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
            },
            data: [{ value: number*540, name: LEVELED }],
            axisTick: {
                show: true,
                splitNumber: 15,
                lineStyle: {
                    color: '#969696',
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
            max: 540,
            startAngle: '210',                      // 起始位置
            endAngle: '-30',                        // 结束位置
            silent: true,                           // 禁止响应鼠标
            splitNumber: 54,                        // 分割段数
            axisLine: {                             // 仪表盘轴线设置
                show: true,
                lineStyle: {
                    color: [
                        [number, '#8892F3'],
                        [1, '#CCCCCC'],
                    ],
                    width: 20
                }
            },
            splitLine: {
                length: 32,
                lineStyle: {
                    width: 6,
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
                
                     if (param==540) {
                         return 'A'
                     }else if (param==480) {
                         return 'B'
                     }else if (param==420) {
                         return 'C'
                     }else if (param==360) {
                         return 'D'
                     }else if(param==300){
                         return 'E'
                     }else if(param==240){
                         return 'F'
                     }else if (param==180) {
                         return 'G'
                     }else if (param==120) {
                         return 'H'
                     }else if ( param==60) {
                         return 'I'
                     }else if(param==0){
                         return 'J'
                     }
			    },
            },
            detail: {
            	formatter : `${FD_FINAL_LEVEL}`,
                offsetCenter: [0, '-10%'],
                fontSize: 68,
                fontWeight: 'bold',
                fontFamily: 'Arial',
                color:'#8892F3'
            },
            data: [{ value: number*540 , name: LEVELED, }],
            pointer: {
                show: false,
            }
        },
    ]
};



//主体信用评级报告综述(仪表盘右边那一块)

$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getMainInfo?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       var FD_ADMISSION_SUGGEST=data.FD_ADMISSION_SUGGEST;
       var FD_END_VALUE=data.FD_END_VALUE;
       var FD_FINAL_NAME=data.FD_FINAL_NAME;
       var FD_INTERN_NAME=data.FD_INTERN_NAME;
       var FD_SCORE=data.FD_SCORE;
       var FD_SCO=data.FD_SCO;
       var FD_SEGMENT_INDUSTRY=data.FD_SEGMENT_INDUSTRY;
       var TRACK_TYPE=data.TRACK_TYPE;
       var FD_TYPE=data.FD_TYPE;
       var DESCRIBE=data.DESCRIBE;
       var FD_INTERN_LEVEL=data.FD_INTERN_LEVEL;
       var FD_FINAL_LEVEL=data.FD_FINAL_LEVEL;
       $(".content1_right3_1").html(FD_ADMISSION_SUGGEST);
       $('.content1_right1_3_top1 span').html(FD_END_VALUE+" 万元");
       $('.content1_right1_3_top2 span').html(FD_SCORE);
       $('.content1_right1_3_top2 label').html(DESCRIBE);
       $('.content1_2_2 label').html(FD_FINAL_NAME);
       $('.content1_2_1 label').html(FD_INTERN_NAME);
       $('.content1_2_2 span').html(FD_FINAL_LEVEL);
       $('.content1_2_1 span').html(FD_INTERN_LEVEL);
       $('.content1_1 label').html(FD_SCO);
       $('#content1_right1_3_bottom_3').html(FD_SEGMENT_INDUSTRY);
       $('#content1_right1_3_bottom_2').html(TRACK_TYPE);
       $('#content1_right1_3_bottom_1').html(FD_TYPE);
       
    }
});
// 指标评价
var quotaChart = echarts.init(document.getElementById('quota'));

var type1='';
var quotaOption='';
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getIndicator?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
    	var compare3_data=[];
    	var compare3_data_new=[];
    	var compare3_data2=[];
    	var compare3_data3=[];
    	data.map(item=>{
    		type1='存量平均值'
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
           					max:item.FD_SCORE?item.FD_SCORE:50
               			});
           			}
           			
           		});
           		
           	}
           });
           quotaOption = {
       		    color: ['#6A85B9', '#A2C3D8'],
       		    tooltip: {},
       		    legend: {
       		        left: 38,
       		        top: 40,
       		        orient: 'vertical',
       		        itemWidth: 14,
       		        itemHeight: 14,
       		        itemGap: 12,
       		        icon: 'rect',
       		        data: ['评级对象', type1],
       		        textStyle: {
       		            padding: [3, 0, 0, 5],
       		            lineHeight: 18,
       		            color: '#969696'
       		        }
       		    },
       		    radar: {
       		        center: ['65%', '50%'],
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
       		        indicator:compare3_data_new

       		        	
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
       		            	value:compare3_data2,
       		                name: '评级对象'
       		            },
       		            {
       		            	value: compare3_data3,
       		                name: type1
       		            }
       		        ]
       		    }]
       		};
          quotaChart.setOption(quotaOption);
    }
});
$("#select1").change(function(e){
	if(e.target.value=='1'){
		type1='存量平均值';
		var compare3_data=[];
		var compare3_data_new=[];
		var compare3_data2=[];
		var compare3_data3=[];
		$.ajax({
		    async: false,
		    type: "GET",
		    url: "/irs/ratingIndex/getIndicator?id="+id,
		    contentType: false,
		    cache: false,
		    processData: false,
		    success: function (data) {
		    	var compare3_data=[];
		    	var compare3_data_new=[];
		    	var compare3_data2=[];
		    	var compare3_data3=[];
		    	data.map(item=>{
		    		type1='存量平均值'
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
		           					max:item.FD_SCORE?item.FD_SCORE:50
		               			});
		           			}
		           			
		           		});
		           		
		           	}
		           });
		           quotaOption = {
		       		    color: ['#6A85B9', '#A2C3D8'],
		       		    tooltip: {},
		       		    legend: {
		       		        left: 38,
		       		        top: 40,
		       		        orient: 'vertical',
		       		        itemWidth: 14,
		       		        itemHeight: 14,
		       		        itemGap: 12,
		       		        icon: 'rect',
		       		        data: ['评级对象', type1],
		       		        textStyle: {
		       		            padding: [3, 0, 0, 5],
		       		            lineHeight: 18,
		       		            color: '#969696'
		       		        }
		       		    },
		       		    radar: {
		       		        center: ['65%', '50%'],
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
		       		        indicator:compare3_data_new

		       		        	
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
		       		            	value:compare3_data2,
		       		                name: '评级对象'
		       		            },
		       		            {
		       		            	value: compare3_data3,
		       		                name: type1
		       		            }
		       		        ]
		       		    }]
		       		};
		          quotaChart.setOption(quotaOption);
		    }
		});
		
	}else if(e.target.value=='2'){
		type1='部门平均值';
		var compare3_data=[];
		var compare3_data_new=[];
		var compare3_data2=[];
		var compare3_data3=[];
		$.ajax({
		    async: false,
		    type: "GET",
		    url: "/irs/ratingIndex/getDepartment?id="+id,
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
		           					max:item.FD_SCORE?item.FD_SCORE:100
		               			});
		           			}
		           			
		           		});
		           		
		           	}
		           });
		           quotaOption = {
		        		    color: ['#6A85B9', '#A2C3D8'],
		        		    legend: {
		        		        left: 38,
		        		        top: 40,
		        		        orient: 'vertical',
		        		        itemWidth: 14,
		        		        itemHeight: 14,
		        		        itemGap: 12,
		        		        icon: 'rect',
		        		        data: ['评级对象', type1],
		        		        textStyle: {
		        		            padding: [3, 0, 0, 5],
		        		            lineHeight: 18,
		        		            color: '#969696'
		        		        }
		        		    },
		        		    radar: {
		        		        center: ['65%', '50%'],
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
		        		        indicator:compare3_data_new

		        		        	
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
		        		        // areaStyle: {normal: {}},
		        		        data:  [
		        		            {
		        		            	value:compare3_data2,
		        		                name: '评级对象'
		        		            },
		        		            {
		        		            	value: compare3_data3,
		        		                name: type1
		        		            }
		        		        ]
		        		    }]
		        		};
		           quotaChart.setOption(quotaOption);   
		    }
		});

	}else if(e.target.value=='3'){
		type1='赛道平均值';
		var compare3_data=[];
		var compare3_data_new=[];
		var compare3_data2=[];
		var compare3_data3=[];
		$.ajax({
		    async: false,
		    type: "GET",
		    url: "/irs/ratingIndex/getGrade?id="+id,
		    contentType: false,
		    cache: false,
		    processData: false,
		    success: function (data) {

		    	data.map(item=>{
		           	if(item.TYPE=='0'){//评级对象
		           		compare3_data2.push(item.FD_SCORE);
		           		compare3_data.push({ name: item.NAME, },);
		           	}else if(item.TYPE=='1'){//平均值
		           		compare3_data3.push(parseInt(item.FD_SCORE));
		           		
		           	}	
		           });
		      
		           data.map(item=>{
		           	if(item.TYPE=='-1'){
		           		compare3_data.map(item2=>{
		           			if(item.NAME.indexOf(item2.name) != -1 ){
		           				compare3_data_new.push({
		           					name:item2.name,
		           					max:item.FD_SCORE?item.FD_SCORE:100
		               			});
		           			}
		           			
		           		});
		           		
		           	}
		           });
		           quotaOption = {
		        		    color: ['#6A85B9', '#A2C3D8'],
		        		    legend: {
		        		        left: 38,
		        		        top: 40,
		        		        orient: 'vertical',
		        		        itemWidth: 14,
		        		        itemHeight: 14,
		        		        itemGap: 12,
		        		        icon: 'rect',
		        		        data: ['评级对象', type1],
		        		        textStyle: {
		        		            padding: [3, 0, 0, 5],
		        		            lineHeight: 18,
		        		            color: '#969696'
		        		        }
		        		    },
		        		    radar: {
		        		        center: ['65%', '50%'],
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
		        		        indicator:compare3_data_new

		        		        	
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
		        		        // areaStyle: {normal: {}},
		        		        data:  [
		        		            {
		        		            	value:compare3_data2,
		        		                name: '评级对象'
		        		            },
		        		            {
		        		            	value: compare3_data3,
		        		                name: type1
		        		            }
		        		        ]
		        		    }]
		        		};
		           quotaChart.setOption(quotaOption);

		    }
		});
	}
	
});



//得分分位数
var progress1Chart = echarts.init(document.getElementById('progress1'));
var RANKING='';
var RANKINGONTRACK='';
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getTrack?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       RANKING=data.RANKING;
       RANKING=parseFloat(RANKING);
       RANKINGONTRACK=parseFloat(data.RANKINGONTRACK);

    }
});
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
            fontSize: 12
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
            fontSize: 12
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
                    // barBorderRadius: 10,
                	color: '#8DA0ED'
                }
            },
            zlevel: 1,
            label: { //图形上的文本标签
                show: true,
                position: 'right',//标签的位置
                offset: [-30, -30],  //标签文字的偏移，此处表示向上偏移40
//                formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
//                color: '#969696',//标签字体颜色
                fontSize: 12,  //标签字号
                rich: {
                    img1: {
                      backgroundColor: {
                        
                    	  image: '/image/point5.png',
                        
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
            fontSize: 12
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
            fontSize: 12
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
                	color: '#8DA0ED'
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
                        image: '/image/point5.png',
                        
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
//  指标描述-经营质量
var manageChart = echarts.init(document.getElementById('manage'));
var manage_data1=[];
var manage_data2=[];
var manage_data1_2=[];
var manage_data2_2=[];
var manage_data3=[];
var max=100;
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getManage?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
      
       data.map(item=>{
    	   if(item.TYPE=='0'){
    		   manage_data1.push(item.FD_INDEXNAME);
        	   manage_data2.push(item.FD_SCORE);
    	   }else if(item.TYPE=='1'){
    		   manage_data1_2.push(item.FD_INDEXNAME);
        	   manage_data2_2.push(item.FD_SCORE);
    	   }else if(item.TYPE=='-1'){
    		   manage_data3.push(item);
    	   }else if(item.TYPE=='-3'){
    		   max=item.FD_SCORE;
    	   }
       });
       var str='';
       manage_data3.map((item1,index)=>{
    	   var img='<img src="../../image/icon_2.jpg"/>'
    	   if(index%2==0){
    		   img='<img src="../../image/icon_2.jpg"/>'
    	   }else{
    		   img='<img src="../../image/icon_1.jpg"/>'
    	   }
    	   str+='<div class="content3_1_content1">'+
           '<div class="content3_1_content1_1">'+
           img+'<span>'+item1.FD_INDEXNAME+':</span><span>'+item1.FD_SCORE+'</span>'+
        '</div>'+
    '</div>';  	   
       });
        $("#title3").html(str);
       
    }
});

var manageOption = {
	tooltip: {},
    legend: {
        left: 120,
        top: 10,
        orient: 'horizontal',
        itemWidth: 12,
        itemHeight: 12,
        itemGap: 12,
        icon: 'rect',
        data: ['自身', '行业平均',],
        textStyle: {
            padding: [3, 0, 0, 5],
            lineHeight: 10,
            color: '#969696'
        }
    },
    yAxis: {
        type: 'category',
        inverse: true,
        boundaryGap: true,
        axisLine: {
            lineStyle: {
                color: '#a8a8a8'
            }
        },
        axisTick: {
            alignWithLabel: true,
            length: 4,
            lineStyle: {
                color: '#cdcdcd'
            }
        },
        axisLabel: {
            margin: 12,
            fontSize: 12,
            color: '#9c9c9c'
        },
        data: manage_data1
    },
    xAxis: {
        type: 'value',
        min: 0,
        max: max,
        interval: 25,
        splitLine: {
            show: false
        },
        axisLine: {
            lineStyle: {
                color: '#a8a8a8'
            }
        },
        axisTick: {
            length: 4,
            lineStyle: {
                color: '#cdcdcd'
            }
        },
        axisLabel: {
            margin: 12,
            fontSize: 10,
            color: '#969696'
        }
    },
    grid: {
        left: '5%',
        right: '10%',
        top: '15%',
        bottom: '2%',
        containLabel: true,
    },

    series: [
        {
            data: manage_data2,
            type: 'bar',
            name: '自身',
            barWidth: 17,
            itemStyle: {
            	color: 'rgba(136, 146, 243, 0.4)',
               
            },
//            barGap:'-100%',		
        },
        {
            data: manage_data2_2,
            type: 'bar',
            name: '行业平均',
            barWidth: 9,
            itemStyle: {
            	 color: '#8892f3', 
            },
//          barGap:'-100%',		
            barGap:'-80%',		
        },
      /*  {
            data: manage_data2_3,
            type: 'bar',
            name: '特色指标',
            barWidth: 16,
            itemStyle: {
                color: '#A2C3D8',
            },
            barGap:'-100%',	
        },*/
       
        
    ]
}
//管理能力
var ability_data1=[];
var ability_data2=[];
var ability_data3=[];
var ability_data4=[];
var abilityChart = echarts.init(document.getElementById('ability'));
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getAbility?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       data.map(item=>{
    	   if(item.FD_INDEXNAME!=='最小值描述'&&item.FD_INDEXNAME!=='最大值描述'){
    		   ability_data1.push(item.FD_INDEXNAME);
        	   ability_data2.push(item.FD_INDEXVALUE);
        	   ability_data3.push(Number(item.FD_SCORE));
    	   }else{
    		   ability_data4.push(item);
    	   } 
       });
       var str='';
       ability_data4.map((item1,index)=>{
    	   var img='<img src="../../image/icon_2.jpg"/>'
        	   if(item1.FD_INDEXNAME=='最大值描述'){
        		   img='<img src="../../image/icon_2.jpg"/>'
        	   }else if(item1.FD_INDEXNAME=='最小值描述'){
        		   img='<img src="../../image/icon_1.jpg"/>'
        	   }
        	   str+='<div class="content3_1_content1">'+
               '<div class="content3_1_content1_1">'+
               img+'<span>'+item1.INDEXNAME+':</span><span>'+item1.FD_QUALITATIVE_OPTIONS+'</span>'+
            '</div>'+
        '</div>';  	   
       });
       $('#title4').html(str);
       
    }
});
var abilityOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '0',
        right: '0',
        bottom:  '0',
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
            fontSize: 12
        },
        max: 10,
        data: ability_data1,//类目数据，在类目轴（type: 'category'）中有效。
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
        max: 10,
        data: ability_data2,//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 18,
            data: ability_data3,
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                    color: function(params) {
                        if(params.value<50){
                            return '#6A85B9';
                        }
                        if(params.value>=50&&params.value<80){
                            return '#8892F3';
                        }
                        if(params.value>=80){
                            return '#A2C3D8';
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
//产业环境
var industryChart = echarts.init(document.getElementById('industry'));
var industry_data1=[];
var industry_data2=[];
var industry_data3=[];
var industry_data4=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getEnvironment?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       data.map(item=>{
    	   if(item.FD_INDEXNAME!=='最小值描述'&&item.FD_INDEXNAME!=='最大值描述'){
    		   industry_data1.push(item.FD_INDEXNAME);
    		   industry_data2.push(item.FD_INDEXVALUE);
    		   industry_data3.push(Number(item.FD_SCORE));
    	   }else{
    		   industry_data4.push(item);
    	   } 
       });
       var str='';
       ability_data4.map((item1,index)=>{
    	   var img='<img src="../../image/icon_2.jpg"/>'
        	   if(item1.FD_INDEXNAME=='最大值描述'){
        		   img='<img src="../../image/icon_2.jpg"/>'
        	   }else if(item1.FD_INDEXNAME=='最小值描述'){
        		   img='<img src="../../image/icon_1.jpg"/>'
        	   }
        	   str+='<div class="content3_1_content1">'+
               '<div class="content3_1_content1_1">'+
               img+'<span>'+item1.INDEXNAME+':</span><span>'+item1.FD_QUALITATIVE_OPTIONS+'</span>'+
            '</div>'+
        '</div>';  	   
       });
       $('#title5').html(str);
      
    }
});
var industryOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '0',
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
            fontSize: 12
        },
        max: 8,
        data: industry_data1,//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    },
    {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
            color: '#969696',
            fontSize: 12,
            textAlign:'left'
        },
        max: 8,
        data: industry_data2,//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 18,
            data: industry_data3,
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                    color: function(params) {
                        if(params.value<50){
                            return '#6A85B9';
                        }
                        if(params.value>=50&&params.value<80){
                            return '#8892F3';
                        }
                        if(params.value>=80){
                            return '#A2C3D8';
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
//产品品质
var productQualityChart = echarts.init(document.getElementById('productQuality'));
var productQuality_data1=[];
var productQuality_data2=[];
var productQuality_data3=[];
var productQuality_data4=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getQuality?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       data.map(item=>{
    	   if(item.FD_INDEXNAME!=='最小值描述'&&item.FD_INDEXNAME!=='最大值描述'){
    		   productQuality_data1.push(item.FD_INDEXNAME);
    		   productQuality_data2.push(item.FD_INDEXVALUE);
    		   productQuality_data3.push(Number(item.FD_SCORE));
    	   }else{
    		   productQuality_data4.push(item);
    	   } 
       });
       var str='';
       productQuality_data4.map((item1,index)=>{
    	   var img='<img src="../../image/icon_2.jpg"/>'
        	   if(item1.FD_INDEXNAME=='最大值描述'){
        		   img='<img src="../../image/icon_2.jpg"/>'
        	   }else if(item1.FD_INDEXNAME=='最小值描述'){
        		   img='<img src="../../image/icon_1.jpg"/>'
        	   }
        	   str+='<div class="content3_1_content1">'+
               '<div class="content3_1_content1_1">'+
               img+'<span>'+item1.INDEXNAME+':</span><span>'+item1.FD_QUALITATIVE_OPTIONS+'</span>'+
            '</div>'+
        '</div>';  	   
       });
       $('#title6').html(str);
    }
});
var productQualityOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '0',
        right: '0',
        bottom: '15%',
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
            fontSize: 12
        },
        max: 6,
        data: productQuality_data1//类目数据，在类目轴（type: 'category'）中有效。
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
        max: 6,
        data: productQuality_data2,//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 18,
            data: productQuality_data3,
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                    color: function(params) {
                        if(params.value<50){
                            return '#6A85B9';
                        }
                        if(params.value>=50&&params.value<80){
                            return '#8892F3';
                        }
                        if(params.value>=80){
                            return '#A2C3D8';
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
//市场地位
var marketPositionChart = echarts.init(document.getElementById('marketPosition'));
var marketPosition_data1=[];
var marketPosition_data2=[];
var marketPosition_data3=[];
var marketPosition_data4=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getPosition?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       data.map(item=>{
    	   if(item.FD_INDEXNAME!=='最小值描述'&&item.FD_INDEXNAME!=='最大值描述'){
    		   marketPosition_data1.push(item.FD_INDEXNAME);
    		   marketPosition_data2.push(item.FD_INDEXVALUE);
    		   marketPosition_data3.push(Number(item.FD_SCORE));
    	   }else{
    		   marketPosition_data4.push(item);
    	   } 
       });
       var str='';
       marketPosition_data4.map((item1,index)=>{
    	   var img='<img src="../../image/icon_2.jpg"/>'
        	   if(item1.FD_INDEXNAME=='最大值描述'){
        		   img='<img src="../../image/icon_2.jpg"/>'
        	   }else if(item1.FD_INDEXNAME=='最小值描述'){
        		   img='<img src="../../image/icon_1.jpg"/>'
        	   }
        	   str+='<div class="content3_1_content1">'+
               '<div class="content3_1_content1_1">'+
               img+'<span>'+item1.INDEXNAME+':</span><span>'+item1.FD_QUALITATIVE_OPTIONS+'</span>'+
            '</div>'+
        '</div>';  	   
       });
       $('#title7').html(str);
    }
});
var marketPositionOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '0',
        right: '0',
        bottom: '15%',
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
            fontSize: 12
        },
        max: 6,
        data: marketPosition_data1,//类目数据，在类目轴（type: 'category'）中有效。
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
        max: 6,
        data: marketPosition_data2,//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 18,
            data: marketPosition_data3,
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                    color: function(params) {
                        if(params.value<50){
                            return '#6A85B9';
                        }
                        if(params.value>=50&&params.value<80){
                            return '#8892F3';
                        }
                        if(params.value>=80){
                            return '#A2C3D8';
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
//商业模式
var businessModelChart = echarts.init(document.getElementById('businessModel'));
var businessModel_data1=[];
var businessModel_data2=[];
var businessModel_data3=[];
var businessModel_data4=[];
$.ajax({
    async: false,
    type: "GET",
    url: "/irs/ratingIndex/getModel?id="+id,
    contentType: false,
    cache: false,
    processData: false,
    success: function (data) {
       data.map(item=>{
    	   if(item.FD_INDEXNAME!=='最小值描述'&&item.FD_INDEXNAME!=='最大值描述'){
    		   businessModel_data1.push(item.FD_INDEXNAME);
    		   businessModel_data2.push(item.FD_INDEXVALUE);
    		   businessModel_data3.push(Number(item.FD_SCORE));
    	   }else{
    		   businessModel_data4.push(item);
    	   } 
       });
       var str='';
       businessModel_data4.map((item1,index)=>{
    	   var img='<img src="../../image/icon_2.jpg"/>'
        	   if(item1.FD_INDEXNAME=='最大值描述'){
        		   img='<img src="../../image/icon_2.jpg"/>'
        	   }else if(item1.FD_INDEXNAME=='最小值描述'){
        		   img='<img src="../../image/icon_1.jpg"/>'
        	   }
        	   str+='<div class="content3_1_content1">'+
               '<div class="content3_1_content1_1">'+
               img+'<span>'+item1.INDEXNAME+':</span><span>'+item1.FD_QUALITATIVE_OPTIONS+'</span>'+
            '</div>'+
        '</div>';  	   
       });
       $('#title8').html(str);
    }
});
var businessModelOption = {
    grid: {   // 直角坐标系内绘图网格
        left: '10',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '0',
        right: '0',
        bottom: '15%',
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
            fontSize: 12
        },
        max: 6,
        data: businessModel_data1,//类目数据，在类目轴（type: 'category'）中有效。
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
        max: 6,
        data: businessModel_data2,//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
    }],
    series: [
        {
            name: "",
            type: "bar",
            barWidth: 18,
            data: businessModel_data3,
            barCategoryGap: 20,
             itemStyle: {
                normal: {
                    color: function(params) {
                        if(params.value<50){
                            return '#6A85B9';
                        }
                        if(params.value>=50&&params.value<80){
                            return '#8892F3';
                        }
                        if(params.value>=80){
                            return '#A2C3D8';
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
	url: "/irs/ratingIndex/getTip?id="+id,
	contentType: false,
	cache: false,
	processData: false,
	success: function (data) {
		 var html='';
		 for(var i=0;i<data.length;i++){
			 html+='<h1><span></span>'+data[i].TIP+'<span>   ('+data[i].RISK_POINT+')</span></h1>'
		 }
		
	    $("#lists2").append(html);
	    $("#COUN").html(data[0].COUN);
	}
});



gradeChart.setOption(gradeOption);

progress1Chart.setOption(progress1Option);
progress2Chart.setOption(progress2Option);
manageChart.setOption(manageOption);
abilityChart.setOption(abilityOption);
industryChart.setOption(industryOption);
productQualityChart.setOption(productQualityOption);
marketPositionChart.setOption(marketPositionOption);
businessModelChart.setOption(businessModelOption);