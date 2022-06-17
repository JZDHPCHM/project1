package gbicc.irs.commom.module.service.impl;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @模板处理
 */
@Service
public class OfficeTemplate {
	
	@Autowired
	JdbcTemplate jdbc;
	
    /**
     * @param officeTemlateName
     * @return
     * @获取数据第一行的位置
     */
    public Integer checkNotation(String officeTemlateName) {
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
    }

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
    public Workbook workbookType(String officeTemlateName) {
        Workbook hssfWorkbook = null;
        FileInputStream is = streamProcessing(officeTemlateName);
        try {
            if (officeTemlateName.endsWith("xlsx")) {
                hssfWorkbook = new XSSFWorkbook(is);
            } else if (officeTemlateName.endsWith("xls")) {
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
    public boolean createExcel(Workbook hssfWorkbook) {
        File file = new File("C:\\Users\\wsh\\Desktop\\test.xlsx");
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
    public boolean export(String officeTemlateName) {
        Integer index = checkNotation(officeTemlateName);
        Workbook hssfWorkbook = workbookType(officeTemlateName);
        List<Map<String, Object>> data =  jdbc.queryForList("select * from fr_aa_user");
        int sheetIndex=0;
        for (Map<String, Object> map : data) {
            Row row = hssfWorkbook.getSheetAt(sheetIndex).createRow(index);
            int cellIndex=0;
            for (String key : map.keySet()) {
                Cell cell = row.createCell(cellIndex);
                cell.setCellValue(map.get(key)==null?"":map.get(key).toString());
                cellIndex++;
            map.get(key);
            }
            index++;
        }
        createExcel(hssfWorkbook);
        return false;
    }


    public static void main(String[] args) throws Exception {
        OfficeTemplate officeTemplate = new OfficeTemplate();
        officeTemplate.export("C:\\Users\\wsh\\Desktop\\DataModel.xlsx");
    }


}
