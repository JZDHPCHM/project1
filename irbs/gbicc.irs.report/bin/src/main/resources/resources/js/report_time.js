/**
 * 将公用的时间控件抽出来
 */
// 选择项类型
var years = [];

var endYear = new Date().getFullYear();

var year = 2010, month = '1', quarter = 1;

for (var i = endYear; i >= year; i--) {
    years.push(i);
}

year = endYear;

var months = [];

for (i = 1; i < 13; i++) {
    months.push(i);
}

var quarters = {'1': "一季度", '2': "二季度", '3': "三季度", '4': "四季度"};

var tQuery = [
    {
        width: '*',
        name: 'year',
        title: '报表时间(年)',
        type: 'select',
        operator: 'equals',
        valueMap: years,
        defaultToFirstOption: true,
        changed: function (form, item, value) {
            year = value;
            // form.submit();
        }
    },
    {
        width: '*',
        name: 'month',
        title: '报表时间(月)',
        type: 'select',
        operator: 'equals',
        valueMap: months,
        defaultToFirstOption: true,
        changed: function (form, item, value) {
            month = value;
            // form.submit();
        }
    }
];


var tQueryQuarter = [
    {
        width: '*',
        name: 'year',
        title: '报表时间(年)',
        type: 'select',
        valueMap: years,
        defaultToFirstOption: true,
        changed: function (form, item, value) {
            year = value;
            // form.submit();
        }
    },
    {
        width: '*',
        name: 'quarter',
        title: '报表时间(季度)',
        type: 'select',
        valueMap: quarters,
        defaultToFirstOption: true,
        changed: function (form, item, value) {
            quarter = value;
            // form.submit();
        }
    }
];

var fdType = {
    '1': '客户数目',
    '2': '授信金额',
    '3': '授信余额'
};

var typeName = '客户数目';

var sumData = {};

function formatPer(val) {
    return typeof val == 'undefined' ? '' : mul(val, 100.0) + '%';
}


function formatNum(val) {
    return typeof val == 'undefined' ? '' : mul(val, 100.0);
}

// js浮点数不精确bug解决
function mul(a, b) {
	a =a+"";
	b=b+"";
    var c = 0,
        d = a.toString(),
        e = b.toString();
    try {
        c += d.split(".")[1].length;
    } catch (f) {
    }
    try {
        c += e.split(".")[1].length;
    } catch (f) {
    }
    return Number(d.replace(".", "")) * Number(e.replace(".", "")) / Math.pow(10, c);
}


function export2Excel(config){

   isc.FwRPCManager.get({
        url: config.url,//defaultId
        urlParameters:{
            exportType: "ooxml",
            exportFilename: config.fileName+".xlsx",
            operationType: "fetch",
            textMatchStyle: "substring",
            // componentId: config.excelModeName +"FieldsListGrid",
            isc_dataFormat: "json",
             willHandleError: false,
            isc_metaDataPrefix:'',
            sortBy: config.sortBy,
            year:typeof config.year =='undefined'?'':config.year,
            month:typeof config.month =='undefined'?'':config.month,
            type:typeof config.type =='undefined'?'':config.type,
            quarter:typeof config.quarter =='undefined'?'':config.quarter,
            half:typeof config.half =='undefined'?'':config.half
        },
        callback: function(response,rawData,request){
            isc.FwDownLoader.create({
                autoDraw: true,
                src: FrameworkUiInterface.webContextPath + '/system/download?filePath=' + encodeURIComponent(rawData.filePath) + '&exportName=' + encodeURIComponent(rawData.exportName)
            });
        }
    });
}