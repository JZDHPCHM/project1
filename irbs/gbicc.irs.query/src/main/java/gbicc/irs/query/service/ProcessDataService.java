package gbicc.irs.query.service;


import com.alibaba.fastjson.JSONObject;

public interface ProcessDataService {

	
	public JSONObject findFlowStatus();
	
	public JSONObject findOrgs();
	
	public String findRatingLevels();
	
	public String findCurrentOrg();
	
	public String findCurrentParentOrg();
	
}
