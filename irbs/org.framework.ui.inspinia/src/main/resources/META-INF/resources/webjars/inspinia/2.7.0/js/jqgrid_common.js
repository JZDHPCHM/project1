/**
 * author by liuxiaofei
 * date 2019-07-31
 */

//公共调用方法合并单元格（无需修改）
function MergerRowspan(gridName, CellName) {
	//得到显示到界面的id集合
	var mya = $("#" + gridName + "").getDataIDs();
	//当前显示多少条
	var length = mya.length;
	for (var i = 0; i < length; i++) {
		//从上到下获取一条信息
		var before = $("#" + gridName + "").jqGrid('getRowData', mya[i]);
		//定义合并行数
		var rowSpanTaxCount = 1;
		for (j = i + 1; j <= length; j++) {
			//和上边的信息对比 如果值一样就合并行数+1 然后设置rowspan 让当前单元格隐藏
			var end = $("#" + gridName + "").jqGrid('getRowData', mya[j]);
			if (before[CellName] == end[CellName]) {
				rowSpanTaxCount++;
				$("#" + gridName + "").setCell(mya[j], CellName, '', {
					display : 'none'
				});
			} else {
				rowSpanTaxCount = 1;
				break;
			}
			$("#" + CellName + "" + mya[i] + "").attr("rowspan",
					rowSpanTaxCount);
		}
	}
}

//公共调用方法合并单元格（根据需要修改）
function MergerColspan(gridName, rowId, id, CellName,cell3,number) {
	var mya = $("#" + gridName + "").getDataIDs();
	var before = $("#" + gridName + "").jqGrid('getRowData',mya[rowId] );
	var a=before[CellName];
	var b=before[id];
	var c=before[cell3];
	if(a==b&&a==c&&b==c){
		$("#" + gridName + "").setCell(mya[rowId], CellName, '', {display : 'none'});
		$("#" + gridName + "").setCell(mya[rowId], cell3, '', {display : 'none'});
		$("#" + id + "" + rowId + "").attr("colspan", number);
	}
}

function MergerColspan2(gridName, rowId, id, CellName,number) {
	var mya = $("#" + gridName + "").getDataIDs();
	var before = $("#" + gridName + "").jqGrid('getRowData',mya[rowId] );
	var a=before[CellName];
	var b=before[id];
	if(a==b){
		$("#" + gridName + "").setCell(mya[rowId], CellName, '', {display : 'none'});
		$("#" + id + "" + rowId + "").attr("colspan", number);
	}
}
//数字千位符格式化
function numToMoneyField(num) {
    regExpInfo = /(\d{1,3})(?=(\d{3})+(?:$|\.))/g;
    var ret = num.toString().replace(regExpInfo, "$1,");
    return ret;
}
