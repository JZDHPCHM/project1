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
function MergerColspan(gridName, rowId, id, CellName) {
	$("#" + gridName + "").setCell(rowId, id, '', {
		display : 'none'
	});
	$("#" + CellName + "" + rowId + "").attr("colspan", 2);
}
