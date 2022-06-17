package gbicc.irs.app.gbicc.parsingExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import gbicc.irs.app.gbicc.Model;
import gbicc.irs.app.gbicc.Parameter;
import gbicc.irs.app.gbicc.Processor;

public class Calculate {
	static String fileName = "C:\\Users\\wsh\\Desktop\\主体评级.xlsx";

	public void yrzbInput(String yrzbCode, String yrzbName, List<Parameter> list2) {
		String yrzbCodeArr[] = yrzbCode.split(",");
		String yrzbNameArr[] = yrzbName.split(",");
		for (int i = 0; i < yrzbCodeArr.length; i++) {
			Parameter per = new Parameter();
			per.setDlInput(yrzbCodeArr[i], yrzbNameArr[i]);
			per.setId();
			per.setOrder(-10 + i);
			list2.add(per);
		}
	}

	public List<Model> getYrZbDf() throws Exception {
		InputStream is = new FileInputStream(new File(fileName));
		Workbook hssfWorkbook = null;
		if (fileName.endsWith("xlsx")) {
			hssfWorkbook = new XSSFWorkbook(is);
		} else if (fileName.endsWith("xls")) {
			hssfWorkbook = new HSSFWorkbook(is);
		}
		List<Model> list =new ArrayList<Model>();
		for (int numSheet = 2; numSheet < 3; numSheet++) {
			Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			for (int i = 0; i <= hssfSheet.getLastRowNum(); i++) {
				List<Parameter> parametersList = new ArrayList<Parameter>();
				String cellCode = hssfSheet.getRow(i).getCell(1).toString();
				String cellName = hssfSheet.getRow(i).getCell(0).toString();
				Model modelChi = new Model();
				modelChi.setCode(cellCode.toString());
				modelChi.setName(cellName.toString());
				modelChi.setCategory("QUANTITATIVE");
				Parameter parameters = new Parameter();
				parameters.setDlInput(cellCode, cellName);
				parameters.setOrder(0);
				parameters.setId();
				parametersList.add(parameters);

				parameters = new Parameter();
				parameters.setDlResult(cellCode, cellName);

				List<Processor> proList = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setType("Arithmetic");
				pro.setArithmetic("${"+cellCode+"}");
				pro.setOrder(1);
				proList.add(pro);
				parameters.setProcessors(proList);
				parameters.setOrder(1);
				parametersList.add(parameters);
				modelChi.setParameters(parametersList);
				list.add(modelChi);
			}
		}
		return list;
	}

	public static void main(String[] args) throws Exception {
		Calculate obt = new Calculate();
		//obt.getYrZbDf(fileName);
	}

}
