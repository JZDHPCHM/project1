package gbicc.irs.report.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.wsp.framework.core.DirectoryManager;
import org.wsp.framework.security.util.SecurityUtil;

/**
 * @模板处理
 */
@Service
public class OfficeTemplateService {
	
	@Autowired
	JdbcTemplate jdbc;
	
    /**
     * @param officeTemlateName
     * @return
     * @获取数据第一行的位置
     */
    public Integer checkNotationRow(Resource resource,String checkNotation) {
        Integer notationIndex = -1;
        Workbook hssfWorkbook = workbookType(resource);
        //获取工作表
        for (int sheetIndex = 0; sheetIndex < hssfWorkbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = hssfWorkbook.getSheetAt(sheetIndex);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                String notation = cell.getCellComment() == null ? "" : cell.getCellComment().getString().toString();
                if (notation.equals(checkNotation)) {
                    notationIndex = rowIndex;
                }
            }
        }
        return notationIndex;
    }

    
    
    
    /**
     * @param officeTemlateName
     * @return
     * @获取数据第一行的位置
     */
    public String getHeadReatHead(Resource resource,String checkNotation) {
        Workbook hssfWorkbook = workbookType(resource);
       String result="";
        //获取工作表
        for (int sheetIndex = 0; sheetIndex < hssfWorkbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = hssfWorkbook.getSheetAt(sheetIndex);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                String notation = cell.getCellComment() == null ? "" : cell.getCellComment().getString().toString();
                if (notation.equals(checkNotation)) {
                result= row.getCell(1).toString();
                }
            }
        }
        return result;
    }
    /**
     * @param officeTemlateName
     * @return
     * @获取需要导出的表
     */
    public String getDataSource(Resource resource,String checkNotation) {
        String tableName="";
        Workbook hssfWorkbook = workbookType(resource);
        //获取工作表
        for (int sheetIndex = 0; sheetIndex < hssfWorkbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = hssfWorkbook.getSheetAt(sheetIndex);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                String notation = cell.getCellComment() == null ? "" : cell.getCellComment().getString().toString();
                if (notation.equals(checkNotation)) {
                    tableName=cell.toString();
                }
            }
        }
        return tableName;
    }


    /**
     * @param officeTemlateName
     * @return
     * @获取数据列结尾下标
     */
    /*public Integer checkNotationColumn(String officeTemlateName) {
        Integer notationIndex = -1;
        Workbook hssfWorkbook = workbookType(officeTemlateName);
        //获取工作表
        for (int sheetIndex = 0; sheetIndex < hssfWorkbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = hssfWorkbook.getSheetAt(sheetIndex);
            for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                Cell cell = row.getCell(0);
                String notation = cell.getCellComment() == null ? "" : cell.getCellComment().getString().toString();
                if (notation.equals("dataStart")) {
                    notationIndex = rowIndex;
                }
            }
        }
        return notationIndex;
    }*/

    /**
     * @param officeTemlateName
     * @return
     * @将文件转化为输入流
     */
    public FileInputStream streamProcessing(String officeTemlateName) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(officeTemlateName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * @param officeTemlateName
     * @return
     * @判断excel类型
     */
    public Workbook workbookType(Resource resource) {
        Workbook hssfWorkbook = null;
        InputStream is=null;
		try {
			is =  resource.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
            if (resource.getFilename().endsWith("xlsx")) {
                hssfWorkbook = new XSSFWorkbook(is);
            } else if (resource.getFilename().endsWith("xls")) {
                hssfWorkbook = new HSSFWorkbook(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hssfWorkbook;
    }

    /**
     * @param hssfWorkbook
     * @return
     * @导出前创建excel
     */
    public boolean createExcel(String filename,Workbook hssfWorkbook,String name) {
    	String uploadDir =DirectoryManager.getDirectoryByName("dir.work.web.export");
    	String id=UUID.randomUUID().toString();
    	jdbc.update("insert into FR_DOWNLOAD_FILE values(?,?,?,?,?,sysdate,?,?,?,?,?,sysdate,?,sysdate)",
    			id,
    			filename,
    			uploadDir+File.separator+id+name.substring(name.lastIndexOf(".")),
    			name.substring(name.lastIndexOf(".")),
    			"SUCCESS",SecurityUtil.getLoginName(),"","","SUCCESS",SecurityUtil.getLoginName(),SecurityUtil.getLoginName());
    	
        File file = new File(uploadDir+File.separator+id+name.substring(name.lastIndexOf(".")));
        try {
            hssfWorkbook.write(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * @param officeTemlateName
     * @return
     * @导出excel
     */
    public boolean export(String filename,Resource resoult,String sj,String[] date,String frequency) {
        //读取数据起始位置
        Integer indexRowStart = checkNotationRow(resoult,"dataStart");
        //读取结尾位置是否需要处理
        Integer indexRowEnd= checkNotationRow(resoult,"end");
        Integer indexRepat= -1;
         //读取数据源
        String tableName = getDataSource(resoult,"dataStart");
        Workbook hssfWorkbook = workbookType(resoult);
        int sheetIndex=0;
        if(!getHeadReatHead(resoult,"repeatHead").equals("")) {
        	indexRepat=checkNotationRow(resoult,"repeatHead");
        	 Row headRow = hssfWorkbook.getSheetAt(sheetIndex).getRow(indexRepat);
        	 String head=getHeadReatHead(resoult,"repeatHead");
        	 String[] data = head.split(":");
        	 List<String> list=jdbc.queryForList(data[1],String.class,date);
        	 int z=1;
        	 if(list.size()<5) {
        		String str = "无";
        		 if (list.size() < 5) {
        				for (int i = list.size(); i < 5; i++) {
        					list.add(str);
        				}
        			}
        	 }
        	 for (int i = 0; i < list.size(); i++) {
        		 headRow.getCell(z).setCellValue(list.get(i).toString());
        		 z=z+4;
			}
        	
        	 
        	
        }
        List<Map<String, Object>> data = jdbc.queryForList(tableName,date);
        for (Map<String, Object> map : data) {
        		map.remove("DATA_DATE");
		}
        CellStyle style = getCellStyle(hssfWorkbook);
        //给报表添加日期
        Row rowDate = hssfWorkbook.getSheetAt(sheetIndex).getRow(1);
        Cell cellDate = rowDate.createCell(1);
        cellDate.setCellValue(sj);
        cellDate = rowDate.createCell(3);
        cellDate.setCellValue(frequency);
        //清空表格
        if(data.size()==0){
            Row row = hssfWorkbook.getSheetAt(sheetIndex).createRow(indexRowStart);
            Cell cell = row.createCell(0);
            cell.setCellValue("");
        }
        for (int dataIndex=0;dataIndex<data.size();dataIndex++) {
            Map<String, Object> map = data.get(dataIndex);
            Row row = hssfWorkbook.getSheetAt(sheetIndex).createRow(indexRowStart);
            int cellIndex=0;
            for (String key : map.keySet()) {
                if(dataIndex>=data.size()-1&&indexRowEnd>-1){
                    Cell cell = row.createCell(cellIndex);
                    String cellResult= map.get(key)==null?"":map.get(key).toString();
                	Pattern pattern = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$"); 
        			Matcher matcher = pattern.matcher((CharSequence)cellResult);
        			boolean result=matcher.matches();
        			if(result) {
        				  DecimalFormat format = new DecimalFormat("0.00");
        			      String a = format.format(new BigDecimal(cellResult));
        			   	cell.setCellValue(a);
        			}else {
      			   	cell.setCellValue(cellResult);
        			}
                    
                    CellStyle cellStyle = hssfWorkbook.createCellStyle();
                    cellStyle.cloneStyleFrom(style);
                    cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(cellStyle);
                }else{
                    Cell cell = row.createCell(cellIndex);
                    String cellResult = map.get(key)==null?"":map.get(key).toString();
                    Pattern pattern = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$"); 
        			Matcher matcher = pattern.matcher((CharSequence)cellResult);
        			boolean result=matcher.matches();
        			if(result) {
        				  DecimalFormat format = new DecimalFormat("0.00");
        			      String a = format.format(new BigDecimal(cellResult));
        			   	cell.setCellValue(a);
        			}else {
      			   	cell.setCellValue(cellResult);
        			}
                    //cell.setCellValue(map.get(key)==null?"":map.get(key).toString());
                    cell.setCellStyle(style);
                }
                cellIndex++;
            }
            indexRowStart++;
        }
        String url="";
        try {
			url= resoult.getURL().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
        String name =url.substring(url.lastIndexOf("/"));
        createExcel(filename,hssfWorkbook,name);
        return false;
    }


    /**
     * @报表整体样式
     * @param workbook
     * @return
     */
    public CellStyle getCellStyle( Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

   /* public static void main(String[] args) throws Exception {
        OfficeTemplate officeTemplate = new OfficeTemplate();
        officeTemplate.export("C:\\Users\\wsh\\Desktop\\DataModel.xlsx");
    }*/


}
