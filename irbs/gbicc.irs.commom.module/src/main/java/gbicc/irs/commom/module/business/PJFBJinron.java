package gbicc.irs.commom.module.business;

import java.util.List;

import org.wsp.framework.jpa.model.org.entity.Org;

/**
 * 
 * @author Administrator
 *对于总行科技金融部人员，界面显示本部门所管辖客户经理分管客户的公司客户评级分布
 */
public class PJFBJinron extends PJFBQuery{

	@Override
	public String get() {
		//获取子机构
		List<Org> orgs = currOrg.getChildren();
		StringBuilder childersOrg = new StringBuilder("'"+currOrg.getCode()+"',");
		
		for(Org o : orgs) {
			childersOrg.append("'"+o.getCode()+"',");
		}
		
		String strchilderOrgs = childersOrg.substring(0,childersOrg.length()-1);
		
		String sql = 
				"SELECT\n" +
						" SC.FD_SCALE_LEVEL as PD_LEVEL,\n" + 
						" nvl((\n" + 
						"     SELECT COUNT(1) FROM NS_COMPANY_RATING AA LEFT JOIN (SELECT FD_CUSTNO,FD_INPUT_ORGID FROM ESB_RATING_CUSTOMER GROUP BY FD_CUSTNO,FD_INPUT_ORGID ) CUS ON AA.FD_CUSTNO=CUS.FD_CUSTNO \n" + 
						"     WHERE AA.FD_FIN_LEVEL=SC.FD_SCALE_LEVEL AND AA.FD_VAILD='1' "
						+ 		"AND AA.FD_RATE_STATUS='010' AND AA.Fd_Source ='IRB' \n" 
						+ 		"AND CUS.FD_INPUT_ORGID IN("+strchilderOrgs+") \n" + 
						"     GROUP BY AA.FD_FIN_LEVEL \n" + 
						" ),0) AS RATINGNUM \n" + 
						" FROM NS_CFG_MAIN_SCALE SC  WHERE SC.FD_TYPE='010' ORDER BY SC.FD_SORT";
		return sql;
	}
	
}
