package gbicc.irs.commom.module.business;

import java.util.List;

import org.wsp.framework.jpa.model.org.entity.Org;

public class PJFBFactory {

	
	public PJFBQuery getPJFB(Org currOrg,List<String> orgs){
		int type = 1;
		boolean flag = true;

		//传入机构判断所属部门
		//小企业金融部
		if(currOrg.getCode() == "1009699" ){
			type =2;
			flag = false;
		}
		//总行科技金融部
		if(currOrg.getCode() == "1007599" ){
			type =3;
			flag = false;
		}
		//总行部门
		if(flag){
			if(orgs.contains(currOrg.getCode()))
				type =4;
		}
		PJFBQuery res = null;
		switch(type){
			case 1:
				res = new PJFBOrdenarry();break;
			case 2:
				res = new PJFBXiaoWei();break;
			case 3:
				res = new PJFBJinron();break;
			default:
				res = new PJFBOther4Supper();break;
		}
		res.setCurrOrg(currOrg);
		res.setType(type);
		return res;
	}

}
