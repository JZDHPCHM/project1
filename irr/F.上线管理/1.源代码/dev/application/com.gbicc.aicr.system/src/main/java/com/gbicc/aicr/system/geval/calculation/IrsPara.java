package com.gbicc.aicr.system.geval.calculation;
/**
 *  calcumation param vo
 * @author zch
 *
 */
public class IrsPara {
	private String code;		//param's code
	private String name;		//param's name
	private boolean unary;		//the param is unary or not
	private String expression;	//tge param's formula
	private String value;		//the calcumated value
	private String type;		//the param's type
	private String category;	//
	private String flag;		//
	private int level;			//
	private IrsPara next;		//
	
	IrsPara(){}
	
	public IrsPara(String code,boolean unary,String expression,int level){
		this.code = code;
		this.unary = unary;
		this.expression =expression;
		this.level = level;
	}
	
	public IrsPara(String code,String name,boolean unary,
			String expression,String value,String type,
			String category,String flag,int level) {
		this.code = code;
		this.name = name;
		this.unary = unary;
		this.expression = expression;
		this.value = value;
		this.type = type;
		this.category = category;
		this.flag = flag;
		this.level = level;
	}
	
	public int level() {
		return level;
	}
	
	public IrsPara next() {
		return next;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void next(IrsPara next) {
		this.next = next;
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

	public boolean isUnary() {
		return unary;
	}

	public void setUnary(boolean unary) {
		this.unary = unary;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public IrsPara getNext() {
		return next;
	}

	public void setNext(IrsPara next) {
		this.next = next;
	}
	
}
