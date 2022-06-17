package gbicc.irs.app.gbicc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Parameter {
	private String id;
	private String code;
	private String name;
	private String description=null;
	private String type;
	private String valueType;
	private Integer valueScale=6;
	private String valueRoundingMode=null;
	private boolean isList=false;
	private Integer order=null;
	private String defaultValue=null;
	private List<Options> options = new ArrayList<Options>();
	
	private List<String> validators = new ArrayList<String>();
	private List<Processor> processors = new ArrayList<Processor>();
	
	public void setTzxInput(String head) {
		this.name=head.substring(0, head.indexOf("("))+"输入值";
		this.code=head.substring(head.lastIndexOf("(")+1,head.length()-1 );
		this.type="IN_OPTION";
		this.valueType="java.lang.Long";
		this.setId();
	}
	
	public void setTzxResult(String head) {
		this.name=head.substring(0, head.indexOf("("))+"结果值";
		this.code=head.substring(head.lastIndexOf("(")+1,head.length()-1 )+"_RESULT";
		this.type="OUT";
		this.valueType="java.lang.Long";
		this.setId();
	}
	
	public void setDlInput(String code,String name) {
		this.name=name+"输入值";
		this.code=code;
		this.type="IN";
		this.valueType="java.math.BigDecimal";
		this.valueRoundingMode="HALF_UP";
		this.setId();
	}
	
	public void setDlResult(String code,String name) {
		this.name=name+"结果值";
		this.code=code+"_RESULT";
		this.type="OUT";
		this.valueType="java.math.BigDecimal";
		this.valueRoundingMode="HALF_UP";
		this.setId();
	}
	public void setDlWeight(String code,String name,String weight) {
		this.name=name+"权重";
		this.code=code+"_WEIGHT";
		this.type="CONSTANT";
		this.valueType="java.math.BigDecimal";
		this.defaultValue=weight;
		try {
			this.description=Double.valueOf(weight)*100+"%";
		} catch (Exception e) {
			System.out.println(weight);
		}
		
		this.valueRoundingMode="HALF_UP";
		this.setId();
	}
	public void setDlTemp(String code,String name,String index) {
		
		if(index.equals("-99")) {
			this.name=name+"中间值";
			this.code=code+"_TEMP_RESULT";
		}else {
			this.name=name+"中间值"+index;
			this.code=code+"_TEMP_RESULT_"+index;
		}
		
		this.type="INTERMEDIATE";
		this.valueType="java.math.BigDecimal";
		this.valueRoundingMode="HALF_UP";
		this.setId();
	}
	
	
	//定性
	public void setDxInput(String code,String name) {
		this.name=name+"输入值";
		this.code=code;
		this.type="IN_OPTION";
		this.valueType="java.lang.String";
		this.setId();
	}
	//权重
	public void setDxWeight(String head,String description) {
		this.name=head.substring(0, head.indexOf("("))+"权重";
		this.code=head.substring(head.lastIndexOf("(")+1,head.length()-1 )+"_WEIGHT";
		this.type="CONSTANT";
		this.valueType="java.math.BigDecimal";
		this.defaultValue=description;
	
		try {
			Double desValue=Double.valueOf(description);
			this.description=Double.valueOf(description)*100+"%";
		} catch (Exception e) {
			System.out.println(description);
		}
	
		//this.description=(desValue*100)+"%";
		this.valueRoundingMode="HALF_UP";
		this.setId();
	}
	 //中间值
	public void setDxTemp(String head) {
		this.name=head.substring(0, head.indexOf("("))+"中间值";
		this.code=head.substring(head.lastIndexOf("(")+1,head.length()-1 )+"_TEMP_RESULT";
		this.type="INTERMEDIATE";
		this.valueType="java.math.BigDecimal";
		this.valueRoundingMode="HALF_UP";
		this.setId();
	}
	
	 //结果值
	public void setDxResult(String head) {
		this.name=head.substring(0, head.indexOf("("))+"结果值";
		this.code=head.substring(head.lastIndexOf("(")+1,head.length()-1 )+"_RESULT";
		this.type="OUT";
		this.valueType="java.math.BigDecimal";
		this.valueRoundingMode="HALF_UP";
		this.setId();
	}
	
	public List<String> getValidators() {
		return validators;
	}
	public void setValidators(List<String> validators) {
		this.validators = validators;
	}
	public List<Processor> getProcessors() {
		return processors;
	}
	public void setProcessors(List<Processor> processors) {
		this.processors = processors;
	}
	
	public List<Options> getOptions() {
		return options;
	}
	public void setOptions(List<Options> options) {
		this.options = options;
	}
	
	public String getId() {
		return id;
	}
	public void setId() {
		this.id = UUID.randomUUID().toString();
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public Integer getValueScale() {
		return valueScale;
	}
	public void setValueScale(Integer valueScale) {
		this.valueScale = valueScale;
	}
	public String getValueRoundingMode() {
		return valueRoundingMode;
	}
	public void setValueRoundingMode(String valueRoundingMode) {
		this.valueRoundingMode = valueRoundingMode;
	}
	public boolean getIsList() {
		return isList;
	}
	public void setIsList(boolean isList) {
		this.isList = isList;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	

}
