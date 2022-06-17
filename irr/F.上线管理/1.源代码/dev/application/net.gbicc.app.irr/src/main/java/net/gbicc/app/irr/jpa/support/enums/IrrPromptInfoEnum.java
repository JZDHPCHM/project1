package net.gbicc.app.irr.jpa.support.enums;
/**
* IRR系统提示信息枚举
*
*/
public enum IrrPromptInfoEnum {
	/**
	 * 添加成功
	 */
	ADD_SUCCESS("添加成功！"),
	/**
	 * 添加失败
	 */
	ADD_ERROR("添加失败！"),
	/**
	 * 更新成功
	 */
	UPDATE_SUCCESS("更新成功！"),
	/**
	 * 更新失败
	 */
	UPDATE_ERROR("更新失败！"),
	/**
	 * 删除成功
	 */
	REMOVE_SUCCESS("删除成功！"),
	/**
	 * 删除失败
	 */
	REMOVE_ERROR("删除失败！"),
	/**
	 * 查询成功
	 */
	FETCH_SUCCESS("查询成功！"),
	/**
	 * 查询失败
	 */
	FETCH_ERROR("查询失败！"),
	/**
	 * 内容为空，操作失败
	 */
	ERROR_NULLPOINT("内容为空，操作失败！"),
	/**
	 * 操作异常
	 */
	ERROR_EXCEPTION("操作异常！"),
	/**
	 * 提交成功
	 */
	SUBMIT_SUCCESS("提交成功！"),
	/**
	 * 提交失败
	 */
	SUBMIT_ERROR("提交失败!"),
	/**
	 * 操作成功
	 */
	OPERATION_SUCCESS("操作成功！"),
	/**
	 * 操作失败
	 */
	OPERATION_ERROR("操作失败!");
	
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private IrrPromptInfoEnum(String value) {
		this.value = value;
	};
	
}
