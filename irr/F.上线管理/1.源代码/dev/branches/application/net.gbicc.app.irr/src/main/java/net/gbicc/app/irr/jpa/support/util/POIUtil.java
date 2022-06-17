package net.gbicc.app.irr.jpa.support.util;
/**
* POI工具类
*
*/

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;

public class POIUtil {
	
	/**
	 * 创建单元格thin边框样式
	 * @param wb
	 * @param borderStyle 边框样式
	 * @return
	 */
	public static CellStyle createBorderCellStyle(Workbook wb,BorderStyle borderStyle) {
		CellStyle cellBorderStyle = wb.createCellStyle();
		cellBorderStyle.setBorderBottom(borderStyle);
		cellBorderStyle.setBorderTop(borderStyle);
		cellBorderStyle.setBorderLeft(borderStyle);
		cellBorderStyle.setBorderRight(borderStyle);
		return cellBorderStyle;
	}
	
	/**
	 * 创建单元格背景色样式
	 * @param wb
	 * @param bg 背景色
	 * @param fp 单元格填充样式
	 * @return
	 */
	public static CellStyle createBackgroundColor(Workbook wb,Short bg,FillPatternType fp) {
		CellStyle cellBackgroundStyle = wb.createCellStyle();
		cellBackgroundStyle.setFillBackgroundColor(bg);
		cellBackgroundStyle.setFillPattern(fp);
		return cellBackgroundStyle;
	}
	
	/**
	 * 创建单元格字体样式
	 * @param wb
	 * @param fontName
	 * @param color
	 * @param bold
	 * @return
	 */
	public static CellStyle createFontCellStyle(Workbook wb,String fontName,Short color,Boolean bold) {
		CellStyle fontCellStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setColor(color);
		font.setBold(bold);
		fontCellStyle.setFont(font);
		return fontCellStyle;
	}
	
	/**
	 * 创建EXCEL区域的下拉框
	 * @param helper 帮助类
	 * @param option 下拉框内容
	 * @param startRow 开始行
	 * @param endRow   结束行
	 * @param startCol 开始列
	 * @param endCol   结束列
	 * @return
	 */
	public static DataValidation createExcelSelect(DataValidationHelper helper,String[] option,
			Integer startRow,Integer endRow,Integer startCol,Integer endCol) {
		/*设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列*/
		CellRangeAddressList regions = new CellRangeAddressList(startRow,endRow, startCol, endCol);
		/*加载下拉框内容*/
		DataValidationConstraint constraint = helper.createExplicitListConstraint(option); 
		return helper.createValidation(constraint, regions);
	}
	
	/**
	 * 获取单元格的值
	 * @param cell 单元格
	 * @return
	 * @throws Exception
	 */
	public static Object getCellValue(Cell cell) throws Exception{
    	if(cell != null){
    		if(cell.getCellTypeEnum() == CellType.STRING){
    			return cell.getStringCellValue();
    		}else if(cell.getCellTypeEnum() == CellType.NUMERIC){
    			return cell.getNumericCellValue();
    		}else if(cell.getCellTypeEnum() == CellType.FORMULA){
    			return cell.getNumericCellValue();
    		}
    	}
    	return null;
    }
}
