package gbicc.irs.commom.module.business;

/**
 * 对于总行小企业部人员，界面显示企业规模为“小微型”客户的公司客户评级分布。
 * @author Administrator
 *
 */
public class PJFBXiaoWei extends PJFBQuery{

	@Override
	public String get() {

		String sql = 
				"SELECT\n" +
						" SC.FD_SCALE_LEVEL as PD_LEVEL,\n" + 
						" nvl((\n" + 
						"     SELECT COUNT(1) FROM NS_COMPANY_RATING AA LEFT JOIN (SELECT FD_CUSTNO,FD_INPUT_ORGID FROM ESB_RATING_CUSTOMER GROUP BY FD_CUSTNO,FD_INPUT_ORGID ) CUS ON AA.FD_CUSTNO=CUS.FD_CUSTNO \n"
						+ " LEFT JOIN ns_customer c \n"
						+ " ON c.fd_custno =  CUS.FD_CUSTNO \n" + 
						"     WHERE AA.FD_FIN_LEVEL=SC.FD_SCALE_LEVEL AND AA.FD_VAILD='1' "
						+ 		"AND AA.FD_RATE_STATUS='010' AND AA.Fd_Source ='IRB' \n" +
						// 小微规模
						" and c.fd_enterprise_scale in (4,5) " +
						"     GROUP BY AA.FD_FIN_LEVEL \n" + 
						" ),0) AS RATINGNUM \n" + 
						" FROM NS_CFG_MAIN_SCALE SC  WHERE SC.FD_TYPE='010' ORDER BY SC.FD_SORT";
		return sql;
	}
}
