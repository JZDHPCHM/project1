package gbicc.irs.app.gbicc.parsingExcel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gbicc.irs.app.gbicc.Condition;
import gbicc.irs.app.gbicc.Model;
import gbicc.irs.app.gbicc.Options;
import gbicc.irs.app.gbicc.Parameter;
import gbicc.irs.app.gbicc.Processor;

public class ObtainExcel {
	static String fileName = "C:\\Users\\wsh\\Desktop\\主体评级.xlsx";

	public static void main(String[] args) {
		ObtainExcel obt = new ObtainExcel();
		try {
			obt.getSheetIndex(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
/*	static String dlIndex = "25";
	static String dxIndex = "127";
	static String zbCode = "XJ";
	static String zbCode1 = "UNI";
	static String zbCode2 = "STA";
	static String modelName="主体新建1模型";
	static String modelCode="NEW_BUILD";
	static Integer sheetIndex=6;*/
	
/*	static String dlIndex = "73";
	static String dxIndex = "229";
	static String zbCode = "TY";
	static String zbCode1 = "UNI";
	static String zbCode2 = "STA";
	static String modelName="主体通用生产1模型";
	static String modelCode="UNIVERSAL_S_SODEL2";
	static Integer sheetIndex=5;*/
	
	
	
	/*static String dlIndex = "73";
	static String dxIndex = "227";
	static String zbCode = "TY";
	static String zbCode1 = "UNI";
	static String zbCode2 = "STA";
	static String modelName="主体通用服务1模型";
	static String modelCode="UNIVERSAL_S_SODEL";
	static Integer sheetIndex=7;*/
	
	
	

/*	static String dlIndex = "31";
	static String dxIndex = "133";
	static String zbCode = "XJ";
	static String zbCode1 = "UNI";
	static String zbCode2 = "STA";
	static String modelName="主体新建2模型";
	static String modelCode="NEW_BUILD_2";
	static Integer sheetIndex=2;*/
	
	
/*	static String dlIndex = "97";
	static String dxIndex = "251";
	static String zbCode = "TY";
	static String zbCode1 = "UNI";
	static String zbCode2 = "STA";
	static String modelName="主体通用服务2模型";
	static String modelCode="UNIVERSAL_S_SODEL_2";
	static Integer sheetIndex=8;*/
	
	
	static String dlIndex = "97";
	static String dxIndex = "253";
	static String zbCode = "TY";
	static String zbCode1 = "UNI";
	static String zbCode2 = "STA";
	static String modelName="主体通用生产2模型";
	static String modelCode="UNIVERSAL_S_SODEL2_2";
	static Integer sheetIndex=1;
	
	public void writeFile(Model model) throws Exception {
		String path = "C:\\Users\\wsh\\Desktop\\ztpj\\ztpj\\";
		String json = JSONObject.toJSON(model).toString();
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		File f = null;
		try {
			f = File.createTempFile(modelName, ".json", file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// write
		FileWriter fw = null;
		try {
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(json.toString());
		bw.flush();
		bw.close();
		fw.close();

	}

	public void getSheetIndex(String fileName) throws Exception {
		InputStream is = new FileInputStream(new File(fileName));
		Workbook hssfWorkbook = null;
		if (fileName.endsWith("xlsx")) {
			hssfWorkbook = new XSSFWorkbook(is);
		} else if (fileName.endsWith("xls")) {
			hssfWorkbook = new HSSFWorkbook(is);
		}
		String type = "";
		for (int numSheet = sheetIndex-1; numSheet < sheetIndex; numSheet++) {
			Sheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			List<List<Integer>> IndexZb = generateIndex(hssfSheet);// 获取单个指标的上下下标
			List<List<Integer>> IndexThree = generateThreeIndex(hssfSheet);// 获取三级指标的上下下标
			System.out.println(IndexZb);
			System.out.println(IndexThree);
			List<Model> Top3 = new ArrayList<Model>();
			DescThree(hssfSheet, IndexThree, IndexZb);

		}
	}

	/**
	 * @param model
	 * @TOP1级指标
	 */
	public void generateTop1(List<Model> modelArr) {
		Model model = new Model();
		
		List<Processor> proList = new ArrayList<Processor>();//结果值加和
		List<Parameter> paraArr = new ArrayList<Parameter>();
		StringBuffer buff = new StringBuffer();//结果值加和
		Parameter para = new Parameter();//结果值加和
		para.setDlResult(modelCode, modelName);//结果值加和
		Processor processor = new Processor();//结果值加和
		buff.append("${DL_RESULT}+${DX_RESULT}");
		processor.setArithmetic(buff.toString());//结果值加和
		processor.setType("Arithmetic");//结果值加和
		proList.add(processor);//结果值加和
		para.setProcessors(proList);//结果值加和
		para.setId();
		para.setOrder(3);
		paraArr.add(para);//结果值加和
		model.setParameters(paraArr);
		
		
		model.setId();
		model.setName("MODEL_"+modelName+"评级模型");// 模板名称
		model.setCode(modelCode);// 模板编码
		model.setCategory("OTHER");
		model.setIsTop(true);
		model.setChildren(modelArr);
		model.setExecuteMode("DOWN_TOP");
		try {
			writeFile(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param indexThree
	 * @param model
	 * @TOP2级指标
	 */
	public void generateTop2(List<Model> threeModel, List<List<Integer>> indexThree) {
		List<Model> childrenModelArr = new ArrayList<Model>();
		// dlIndex
		int dlxb = -1;
		for (int i = 0; i < indexThree.size(); i++) {
			String three = indexThree.get(i).get(1).toString();
			if (three.equals(dlIndex)) {
				
				List<Processor> proList = new ArrayList<Processor>();//结果值加和
				List<Parameter> paraArr = new ArrayList<Parameter>();
				StringBuffer buff = new StringBuffer();//结果值加和
				Parameter para = new Parameter();//结果值加和
				para.setDlResult("DL", "定量指标");//结果值加和
				Processor processor = new Processor();//结果值加和
				
				
				List<Model> children = new ArrayList<Model>();
				Model childrenModel = new Model();
				childrenModel.setCode("DL");
				childrenModel.setCategory("QUANTITATIVE");
				childrenModel.setName("定量指标");
				for (int j = 0; j <= i; j++) {
					
					if(i==j) {
						buff.append("${"+threeModel.get(j).getCode()+"_RESULT}");
					}else {
						buff.append("${"+threeModel.get(j).getCode()+"_RESULT}+");
					}
					
					children.add(threeModel.get(j));
				}
				
				processor.setArithmetic(buff.toString());//结果值加和
				processor.setType("Arithmetic");//结果值加和
				proList.add(processor);//结果值加和
				para.setProcessors(proList);//结果值加和
				para.setId();
				para.setOrder(i+1);
				paraArr.add(para);//结果值加和
				
				
				
				childrenModel.setParameters(paraArr);
				childrenModel.setChildren(children);
				childrenModelArr.add(childrenModel);
				dlxb = i;
			}

			if (three.equals(dxIndex)) {
				List<Model> children = new ArrayList<Model>();
				Model childrenModel = new Model();
				childrenModel.setCode("DX");
				childrenModel.setCategory("QUALITATIVE");
				childrenModel.setName("定性指标");
				
				List<Parameter> paraArr = new ArrayList<Parameter>();
				List<Processor> proList = new ArrayList<Processor>();//结果值加和
				StringBuffer buff = new StringBuffer();//结果值加和
				Parameter para = new Parameter();//结果值加和
				para.setDlResult("DX", "定性指标");//结果值加和
				Processor processor = new Processor();//结果值加和
				
				for (int j = dlxb+1; j <= i; j++) {
					if(i==j) {
						buff.append("${"+threeModel.get(j).getCode()+"_RESULT}");
					}else {
						buff.append("${"+threeModel.get(j).getCode()+"_RESULT}+");
					}
					children.add(threeModel.get(j));
				}
				
				processor.setArithmetic(buff.toString());//结果值加和
				processor.setType("Arithmetic");//结果值加和
				processor.setOrder(1);
				proList.add(processor);//结果值加和
				para.setProcessors(proList);//结果值加和
				para.setId();
				para.setOrder(i+1);
				paraArr.add(para);//结果值加和
				
				childrenModel.setParameters(paraArr);
				childrenModel.setChildren(children);
				childrenModelArr.add(childrenModel);
			}

		}
		Calculate calc = new Calculate();
		try {
			Model model = new Model();
			model.setCode("YRZL");
			model.setName("财务指标");
			model.setId();
			model.setType("MODEL");
			model.setCategory("OTHER");
			List<Model> calcZb = calc.getYrZbDf();
			model.setChildren(calcZb);
			childrenModelArr.add(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		generateTop1(childrenModelArr);
	}

	// 创建三级指标
	private void DescThree(Sheet hssfSheet, List<List<Integer>> indexThree, List<List<Integer>> indexZb) {
		List<Model> modelList = DescEnd(hssfSheet, indexZb);
		Integer temp = -1;
		List<Model> threeModel = new ArrayList<Model>();

		for (int j = 0; j < indexZb.size(); j++) {
			String end = indexZb.get(j).get(1).toString();
			for (int i = 0; i < indexThree.size(); i++) {
				String three = indexThree.get(i).get(1).toString();
				if (end.equals(three)) {
					List<Model> children = new ArrayList<Model>();
					List<Parameter> paraArr = new ArrayList<Parameter>();
					String modelCode=hssfSheet.getRow(indexThree.get(i).get(0)).getCell(2).toString();
					String modelName=hssfSheet.getRow(indexThree.get(i).get(0)).getCell(1).toString();
					
					List<Processor> proList = new ArrayList<Processor>();//结果值加和
					StringBuffer buff = new StringBuffer();//结果值加和
					Parameter para = new Parameter();//结果值加和
					para.setDlResult(modelCode, modelName);//结果值加和
					Processor processor = new Processor();//结果值加和
					for (int j2 = temp + 1; j2 <= j; j2++) {
						if(j2==j) {
							buff.append("${"+modelList.get(j2).getCode().replace("_INPUT", "")+"_RESULT}");
						}else {
							buff.append("${"+modelList.get(j2).getCode().replace("_INPUT", "")+"_RESULT}+");
						}
						modelList.get(j2).setOrder(j2);
						children.add(modelList.get(j2));
					}
					processor.setOrder(1);
					processor.setArithmetic(buff.toString());//结果值加和
					processor.setType("Arithmetic");//结果值加和
					proList.add(processor);//结果值加和
					para.setProcessors(proList);//结果值加和
					para.setId();
					para.setOrder(j+1);
					paraArr.add(para);//结果值加和
					
					temp = j;
					Model model = new Model();
					model.setCode(modelCode);
					model.setName(modelName);
					if(indexThree.get(i).get(1)>Integer.parseInt(dlIndex)) {
						model.setCategory("QUALITATIVE");
					}else {
						model.setCategory("QUANTITATIVE");
					}
					model.setParameters(paraArr);
					model.setOrder(j);
					model.setChildren(children);
					threeModel.add(model);
				}
			}
		}
		generateTop2(threeModel, indexThree);

	}

	/**
	 * @param hssfSheet
	 * @获取单个指标的第一行和最后一行指标
	 */
	public List<List<Integer>> generateIndex(Sheet hssfSheet) {
		List<Integer> Index = new ArrayList<Integer>();
		List<List<Integer>> IndexResult = new ArrayList<List<Integer>>();
		int down = -1;
		for (int rowNum = 0; rowNum < Integer.parseInt(dxIndex); rowNum++) {
			Row hssfRow = hssfSheet.getRow(rowNum);
			Cell cell = hssfRow.getCell(4);
			if (cell != null && !cell.toString().equals("")) {
				if (down != -1) {
					Index.add(down);
				}
				if (!cell.toString().equals("") && (cell.toString().indexOf(zbCode)>-1||cell.toString().indexOf(zbCode1) > -1||cell.toString().indexOf(zbCode2) > -1)) {
					down = cell.getRowIndex();
					if (down != -1) {
						Index.add(down);
						IndexResult.add(Index);
						Index = new ArrayList<Integer>();
					}

				}
			}
			if (hssfSheet.getLastRowNum() == rowNum) {
				if (down != -1) {
					Index.add(down);
				}
				down = cell.getRowIndex();
				Index.add(down + 1);
				IndexResult.add(Index);
			}
		}
		IndexResult.remove(0);
		return IndexResult;
	}

	/**
	 * @param hssfSheet
	 * @三级指标的第一行和最后一行指标
	 */
	public List<List<Integer>> generateThreeIndex(Sheet hssfSheet) {
		List<Integer> Index = new ArrayList<Integer>();
		List<List<Integer>> IndexResult = new ArrayList<List<Integer>>();
		int down = -1;
		for (int rowNum = 0; rowNum < Integer.parseInt(dxIndex); rowNum++) {
			if(rowNum>=164) {
				System.out.println(down);
			}
			Row hssfRow = hssfSheet.getRow(rowNum);
			Cell cell = hssfRow.getCell(2);
			if (cell != null && !cell.toString().equals("")) {
				if (down != -1) {
					Index.add(down);
				}
				if (!cell.toString().equals("") && (cell.toString().indexOf(zbCode) > -1||cell.toString().indexOf(zbCode2) > -1||cell.toString().indexOf(zbCode1) > -1)) {
					down = cell.getRowIndex();
					if (down != -1) {
						Index.add(down);
						IndexResult.add(Index);
						Index = new ArrayList<Integer>();
					}

				}
			}
			if (hssfSheet.getLastRowNum() == rowNum) {
				if (down != -1) {
					Index.add(down);
				}
				down = cell.getRowIndex();
				Index.add(down + 1);
				IndexResult.add(Index);
			}
		}
		IndexResult.remove(0);
		return IndexResult;
	}

	/**
	 * @param model
	 * @最后一级指标
	 */
	private List<Model> DescEnd(Sheet hssfSheet, List<List<Integer>> indexZb) {
		List<Model> modelList = new ArrayList<Model>();
		List<List<Condition>> listCon = new ArrayList<List<Condition>>();
		List<Condition> listCon_1 = new ArrayList<Condition>();// 线性插值中间值——1
		List<Condition> listCon_2 = new ArrayList<Condition>();// 线性插值中间值——2
		List<Condition> listCon_3 = new ArrayList<Condition>();// 线性插值中间值——3
		List<Condition> listCon_4 = new ArrayList<Condition>();// 线性插值中间值——4
		List<Options> optionArr = new ArrayList<Options>();// 选项值
		for (int i = 0; i < indexZb.size(); i++) {
			String weight = "";
			String yrzbCode = "";
			String yrzbName = "";
			String yrzbCalculate = "";
			Model model = new Model();
			for (int rowNum = indexZb.get(i).get(0); rowNum < indexZb.get(i).get(1); rowNum++) {
				Row hssfRow = hssfSheet.getRow(rowNum);
				if (rowNum == indexZb.get(i).get(0)) {
					listCon_1 = new ArrayList<Condition>();// 清除条件分段函数
					listCon_2 = new ArrayList<Condition>();
					listCon_3 = new ArrayList<Condition>();
					listCon_4 = new ArrayList<Condition>();
					listCon = new ArrayList<List<Condition>>();
					optionArr = new ArrayList<Options>();// 选项值
					// -----------
					if(hssfSheet.getRow(indexZb.get(i).get(0)).getCell(11)!=null) {
						yrzbCode=hssfSheet.getRow(indexZb.get(i).get(0)).getCell(11).toString();
						yrzbName=hssfSheet.getRow(indexZb.get(i).get(0)).getCell(8).toString();
						yrzbCalculate=hssfSheet.getRow(indexZb.get(i).get(0)).getCell(12).toString();
					}
					
					weight = hssfSheet.getRow(indexZb.get(i).get(0)).getCell(5).toString();
					model.setCode(hssfRow.getCell(4).toString()+"_INPUT");
					model.setName(hssfRow.getCell(3).toString());
				}
				//value(model, weight, hssfRow, null, null);
				listCon_1.add(ConditionsSection_1(model, hssfRow));
				listCon.add(listCon_1);
				listCon_2.add(ConditionsSection_2(model, hssfRow));
				listCon.add(listCon_2);
				listCon_3.add(ConditionsSection_3(model, hssfRow));
				listCon.add(listCon_3);
				listCon_4.add(ConditionsSection_4(model, hssfRow));
				listCon.add(listCon_4);

				optionArr.add(option(model, hssfRow, indexZb.get(i)));

				if (indexZb.get(i).get(1) - 1 == rowNum) {
					value(model, weight, hssfRow, listCon, optionArr,yrzbCode,yrzbName,yrzbCalculate);
				}
			}
			modelList.add(model);
		}
		return modelList;

	}

	/**
	 * @param listCon
	 * @param optionArr2
	 * @param yrzbCalculate 
	 * @param yrzb 
	 * @param topDown    @生成(输入，中间，结果值)
	 */
	private void value(Model model, String weight, Row hssfRow, List<List<Condition>> listCon,
			List<Options> optionArr2, String yrzbCode,String yrzbName, String yrzbCalculate) {
		String valueCode = model.getCode().replace("_INPUT", "");
		List<Parameter> list = new ArrayList<Parameter>();
		Cell cell=hssfRow.getCell(10);
		String defaultValue = "";
		if(cell!=null) {
			if(cell.toString().equals("指标缺失得0分")) {
				Options option = new Options();
				option.setId();
				option.setInputValue("0");
				option.setValue("0");
				option.setTitle("默认值");
				defaultValue="0";
				optionArr2.add(option);
			}
		}
		String str = hssfRow.getCell(6).toString();
		String regex1 = "[a-zA-Z]+";
		boolean result3 = str.matches(regex1);
		// 输入值
		Parameter per = new Parameter();
		if (result3) {
			per = new Parameter();
			per.setDxInput(valueCode, model.getName());
			per.setOptions(optionArr2);
			if(defaultValue.equals("0")) {
				per.setDefaultValue(defaultValue);
			}
			per.setOrder(1);
			list.add(per);
		} else {
			
			///Calculate cal = new Calculate();
			if(yrzbCode.equals("")) {
				yrzbCode=valueCode;
				yrzbName=model.getName();
			}
			per = new Parameter();
			per.setDlInput(valueCode, model.getName());
			if(defaultValue.equals("0")) {
				per.setDefaultValue(defaultValue);
			}
			per.setOrder(-1);
			list.add(per);
				//cal.yrzbInput(yrzbCode,yrzbName,list);
				per = new Parameter();
				per.setDlTemp(valueCode, model.getName(),"5");
				per.setId();
				per.setOrder(0);
				
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setId();
				
				pro.setType("ConditionRange");
				if(yrzbCalculate.equals("")) {
					pro.setArithmetic("${"+valueCode+"}");
				}else {
					List<Condition> listRe= ConditionsSection_Temp_5(yrzbCode,yrzbCalculate);
					pro.setConditionRange(JSONObject.toJSON(listRe).toString());
					//pro.setArithmetic(yrzbCalculate);
				}
				pro.setOrder(1);
				processor.add(pro);
				per.setProcessors(processor);
				
				list.add(per);
		}

		if (result3) {
			//定性中间值
			per = new Parameter();
			per.setDlTemp(valueCode, model.getName(),"-99");
			// 条件分段函数
			if (listCon != null) {
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setOptionParameterCode(valueCode);
				pro.setType("OptionValue");
				pro.setId();
				pro.setOrder(0);
				processor.add(pro);
				per.setProcessors(processor);
				per.setOrder(2);
			}
			list.add(per);
			
			
		} else {
			// 中间值_1
			per = new Parameter();
			per.setDlTemp(valueCode, model.getName(), "1");
			// 条件分段函数
			if (listCon != null) {
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setType("ConditionRange");
				pro.setConditionRange(JSONObject.toJSON(listCon.get(0)).toString());
				processor.add(pro);
				per.setProcessors(processor);
				per.setOrder(2);
			}
			list.add(per);
			// 中间值_2
			per = new Parameter();
			per.setDlTemp(valueCode, model.getName(), "2");
			// 条件分段函数
			if (listCon != null) {
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setType("ConditionRange");
				pro.setConditionRange(JSONObject.toJSON(listCon.get(1)).toString());
				processor.add(pro);
				per.setProcessors(processor);
				per.setOrder(3);
			}

			list.add(per);

			// 中间值_3
			per = new Parameter();
			per.setDlTemp(valueCode, model.getName(), "3");
			// 条件分段函数
			if (listCon != null) {
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setType("ConditionRange");
				pro.setConditionRange(JSONObject.toJSON(listCon.get(2)).toString());
				processor.add(pro);
				per.setProcessors(processor);
				per.setOrder(4);
			}

			list.add(per);

			// 中间值_4
			per = new Parameter();
			per.setDlTemp(valueCode, model.getName(), "4");
			// 条件分段函数
			if (listCon != null) {
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setId();
				pro.setType("ConditionRange");
				pro.setConditionRange(JSONObject.toJSON(listCon.get(3)).toString());
				processor.add(pro);
				per.setProcessors(processor);
				per.setOrder(5);
			}

			list.add(per);
		}

		// 权重
		per = new Parameter();
		per.setDlWeight(valueCode, model.getName(), weight);
		per.setOrder(6);
		list.add(per);
		// 结果值
		if (result3) {
			per = new Parameter();
			per.setDlResult(valueCode, model.getName());
			List<Processor> processor = new ArrayList<Processor>();
			Processor pro = new Processor();
			pro.setId();
			pro.setType("Arithmetic");
			pro.setOrder(1);
			pro.setArithmetic("${" + valueCode + "_TEMP_RESULT}*${" + valueCode + "_WEIGHT}");
			processor.add(pro);
			per.setProcessors(processor);
			per.setOrder(7);
			list.add(per);
		} else {
			if(yrzbCalculate.equals("")) {
				per = new Parameter();
				per.setDlResult(valueCode, model.getName());
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setId();
				pro.setType("Arithmetic");
				pro.setArithmetic("(((${" + valueCode + "_TEMP_RESULT_1}-${" + valueCode + "_TEMP_RESULT_2})/(${"
						+ valueCode + "_TEMP_RESULT_3}-${" + valueCode + "_TEMP_RESULT_4}))*(${"
						+ valueCode + "_TEMP_RESULT_5}-${" + valueCode + "_TEMP_RESULT_3})+${" + valueCode
						+ "_TEMP_RESULT_2})*${"+valueCode+"_WEIGHT}");
				pro.setOrder(1);
				processor.add(pro);
				per.setProcessors(processor);
				list.add(per);
				per.setOrder(7);
			}else {
				per = new Parameter();
				per.setDlResult(valueCode, model.getName());
				List<Processor> processor = new ArrayList<Processor>();
				Processor pro = new Processor();
				pro.setId();
				pro.setType("ConditionRange");
				List<Condition> listRe= ConditionsSection_Result(valueCode,"(((${" + valueCode + "_TEMP_RESULT_1}-${" + valueCode + "_TEMP_RESULT_2})/(${"
						+ valueCode + "_TEMP_RESULT_3}-${" + valueCode + "_TEMP_RESULT_4}))*(${"
						+ valueCode + "_TEMP_RESULT_5}-${" + valueCode + "_TEMP_RESULT_4})+${" + valueCode
						+ "_TEMP_RESULT_2})*${"+valueCode+"_WEIGHT}");
				pro.setConditionRange(JSONObject.toJSON(listRe).toString());
				pro.setOrder(1);
				processor.add(pro);
				per.setProcessors(processor);
				list.add(per);
				per.setOrder(7);
			}
			
		}
		model.setId();
		if (result3) {
			model.setCategory("QUALITATIVE");
		}else {
			model.setCategory("QUANTITATIVE");
		}
		
		model.setParameters(list);

	}

	/**
	 * @param list
	 * @输入项
	 */
	public Options option(Model model, Row hssfRow, List<Integer> list) {

		Options opt = new Options();
		opt.setId();
		opt.setTitle(hssfRow.getCell(7).toString());
		String level = hssfRow.getCell(6).toString();
		opt.setInputValue(level);
		if (level.equals("A")) {
			opt.setValue("100");
		}
		if ((list.get(1) - list.get(0)) == 3) {
			if (level.equals("B")) {
				opt.setValue("50");
			}
			if (level.equals("C")) {
				opt.setValue("0");
			}
		}
		if ((list.get(1) - list.get(0)) == 4) {
			if (level.equals("B")) {
				opt.setValue("67");
			}
			if (level.equals("C")) {
				opt.setValue("33");
			}
			if (level.equals("D")) {
				opt.setValue("0");
			}
		}

		if ((list.get(1) - list.get(0)) == 5) {
			if (level.equals("B")) {
				opt.setValue("75");
			}
			if (level.equals("C")) {
				opt.setValue("50");
			}
			if (level.equals("D")) {
				opt.setValue("25");
			}
			if (level.equals("E")) {
				opt.setValue("0");
			}
		}

		if ((list.get(1) - list.get(0)) == 6) {
			if (level.equals("B")) {
				opt.setValue("80");
			}
			if (level.equals("C")) {
				opt.setValue("60");
			}
			if (level.equals("D")) {
				opt.setValue("40");
			}
			if (level.equals("E")) {
				opt.setValue("20");
			}
			if (level.equals("F")) {
				opt.setValue("0");
			}
		}
		if ((list.get(1) - list.get(0)) == 7) {
			if (level.equals("B")) {
				opt.setValue("75");
			}
			if (level.equals("C")) {
				opt.setValue("0");
			}
			if (level.equals("D")) {
				opt.setValue("75");
			}
			if (level.equals("E")) {
				opt.setValue("50");
			}
			if (level.equals("F")) {
				opt.setValue("25");
			}
			if (level.equals("G")) {
				opt.setValue("50");
			}
		}

		return opt;
	}

	/**
	 * @ 条件分段函数_1
	 */
	public Condition ConditionsSection_1(Model model, Row hssfRow) {
		String modelCode = model.getCode();
		modelCode = modelCode.replace("_INPUT", "");
		Condition condition = new Condition();
		condition.setUuid();
		String str = hssfRow.getCell(6).toString();
		String regex1 = "[a-zA-Z]+";
		boolean result3 = str.matches(regex1);

		if (result3) {
			return null;
		}
		if (hssfRow.getCell(7).toString().indexOf("≤") > -1 || hssfRow.getCell(7).toString().indexOf(">") > -1) {
			String con = hssfRow.getCell(7).toString().replace("≤", "<=");
			con = con.replace(">", ">");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + con + "");
			condition.setValue(str);
		} else {
			String strCon = hssfRow.getCell(7).toString().replace("(", ">").replace("]", "");
			String arr[] = strCon.split(",");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + arr[0] + " && ${" + modelCode
					+ "_TEMP_RESULT_5}<=" + arr[1] + "");
			str = str.replace("(", "").replace("]", "");
			condition.setValue(str.split(",")[0]);
		}

		return condition;
	}

	/**
	 * @ 条件分段函数_2
	 */
	public Condition ConditionsSection_2(Model model, Row hssfRow) {
		String modelCode = model.getCode();
		modelCode = modelCode.replace("_INPUT", "");
		Condition condition = new Condition();
		String str = hssfRow.getCell(6).toString();
		String regex1 = "[a-zA-Z]+";
		boolean result3 = str.matches(regex1);

		if (result3) {
			return null;
		}
		if (hssfRow.getCell(7).toString().indexOf("≤") > -1 || hssfRow.getCell(7).toString().indexOf(">") > -1) {
			String con = hssfRow.getCell(7).toString().replace("≤", "<=");
			con = con.replace(">", ">");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + con + "");
			condition.setValue(str);
		} else {
			String strCon = hssfRow.getCell(7).toString().replace("(", ">").replace("]", "");
			String arr[] = strCon.split(",");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + arr[0] + " && ${" + modelCode
					+ "_TEMP_RESULT_5}<=" + arr[1] + "");
			str = str.replace("(", "").replace("]", "");
			condition.setValue(str.split(",")[1]);
		}
		return condition;
	}

	/**
	 * @ 条件分段函数_3
	 */
	public Condition ConditionsSection_3(Model model, Row hssfRow) {
		String modelCode = model.getCode();
		modelCode = modelCode.replace("_INPUT", "");
		Condition condition = new Condition();
		String str = hssfRow.getCell(6).toString();
		String regex1 = "[a-zA-Z]+";
		boolean result3 = str.matches(regex1);

		if (result3) {
			return null;
		}
		if (hssfRow.getCell(7).toString().indexOf("≤") > -1 || hssfRow.getCell(7).toString().indexOf(">") > -1) {
			String con = hssfRow.getCell(7).toString().replace("≤", "<=");
			con = con.replace(">", ">");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + con + "");
			str.replace("(", "").replace("]", "");
			condition.setValue("0");
		} else {
			String strCon = hssfRow.getCell(7).toString().replace("(", ">").replace("]", "");
			String arr[] = strCon.split(",");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + arr[0] + " && ${" + modelCode
					+ "_TEMP_RESULT_5}<=" + arr[1] + "");
			condition.setValue(arr[0].replace(">", ""));
		}

		return condition;
	}

	/**
	 * @ 条件分段函数_4
	 */
	public Condition ConditionsSection_4(Model model, Row hssfRow) {
		String modelCode = model.getCode();
		modelCode = modelCode.replace("_INPUT", "");
		Condition condition = new Condition();
		String str = hssfRow.getCell(6).toString();
		String regex1 = "[a-zA-Z]+";
		boolean result3 = str.matches(regex1);

		if (result3) {
			return null;
		}
		if (hssfRow.getCell(7).toString().indexOf("≤") > -1 || hssfRow.getCell(7).toString().indexOf(">") > -1) {
			String con = hssfRow.getCell(7).toString().replace("≤", "<=");
			con = con.replace(">", ">");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + con + "");
			condition.setValue(con.replace("<=", "").replace(">", ""));
		} else {
			String strCon = hssfRow.getCell(7).toString().replace("(", ">").replace("]", "");
			String arr[] = strCon.split(",");
			condition.setCondition("${" + modelCode + "_TEMP_RESULT_5}" + arr[0] + " && ${" + modelCode
					+ "_TEMP_RESULT_5}<=" + arr[1] + "");
			condition.setValue(arr[1].replace(">", ""));
		}

		return condition;
	}

	
	
	
	/**
	 * @ 条件分段函数_中间值5
	 */
	public List<Condition> ConditionsSection_Temp_5(String code,String yrzbCalculate) {
		List<Condition> listCon = new ArrayList<Condition>();
		String bm[]=code.split(",");
		Condition condition = new Condition();
		StringBuffer buff = new StringBuffer();
		StringBuffer buff2 = new StringBuffer();
		for(int i=0;i<bm.length;i++) {
			if(i==bm.length-1) {
				buff.append("${" + bm[i] + "}.equals(\"\")");
			}else {
				buff.append("${" + bm[i] + "}.equals(\"\")||");
			}
			
			if(i==bm.length-1) {
				buff2.append("!${" + bm[i] + "}.equals(\"\")");
			}else {
				buff2.append("!${" + bm[i] + "}.equals(\"\")&&");
			}
		}
		String strEnd="";
		if(yrzbCalculate.indexOf("/")>-1) {
			strEnd = yrzbCalculate.substring(yrzbCalculate.indexOf("/")+1,yrzbCalculate.length());	
				buff.append("||" + strEnd + "==0");
		}
		
		
		condition.setCondition(buff.toString());
		condition.setValue("0");
		Condition condition2 = new Condition();
		condition2.setCondition(buff2.toString());
		condition2.setValue(yrzbCalculate);
		listCon.add(condition);
		listCon.add(condition2);
		return listCon;
	}
	
	/**
	 * @ 条件分段函数_结果值
	 */
	public List<Condition> ConditionsSection_Result(String code,String yrzbCalculate) {
		List<Condition> listCon = new ArrayList<Condition>();
		Condition condition = new Condition();
		condition.setCondition("${"+code+"_TEMP_RESULT_5}==0");
		condition.setValue("0");
		Condition condition2 = new Condition();
		condition2.setCondition("${"+code+"_TEMP_RESULT_5}!=0");
		condition2.setValue(yrzbCalculate);
		listCon.add(condition);
		listCon.add(condition2);
		return listCon;
	}
	
	
	/**
	 * @生成三级指标(业务指标)
	 */
	public void generateTop3(List<Model> Top3, List<String> codeAndName) {
		Model modelTree = new Model();
		modelTree.setCode(codeAndName.get(0));
		modelTree.setCode(codeAndName.get(1));
		Top3.add(modelTree);
	}

}
