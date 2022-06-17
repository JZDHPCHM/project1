package net.gbicc.app.irr.jpa.support;

import org.wsp.framework.jpa.service.support.protocol.QueryParameter;
import org.wsp.framework.jpa.service.support.protocol.criteria.FetchMode;
import org.wsp.framework.jpa.service.support.protocol.criteria.TextMatchStyle;

/**
* irr查询参数对象
*
*/
public class IrrQueryParameter extends QueryParameter {

	public IrrQueryParameter() {
		super();
	}

	public IrrQueryParameter(FetchMode fetchMode,TextMatchStyle textMatchStyle) {
		super();
		super.fetchMode = fetchMode;
		super.textMatchStyle = textMatchStyle;
	}
	
	/**
	 * 获取Irr默认的查询对象
	 * @return
	 */
	public static IrrQueryParameter getIrrDefaultQP() {
		return new IrrQueryParameter(FetchMode.EMPTY_CRITERIA_EMPTY,TextMatchStyle.substring);
	}
	
}
