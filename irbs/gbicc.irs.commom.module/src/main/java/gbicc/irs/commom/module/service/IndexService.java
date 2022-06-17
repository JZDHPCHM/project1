/**  
 * Project Name:gbicc.irs.commom.module  
 * File Name:IndexService.java  
 * Package Name:gbicc.irs.commom.module.service  
 * Date:2019年6月19日上午9:25:36  
 * Copyright (c) 2019, chenzhou1025@126.com All Rights Reserved.  
 *  
*/  
  
package gbicc.irs.commom.module.service;

import java.util.List;
import java.util.Map;

/**  
 * ClassName:IndexService <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2019年6月19日 上午9:25:36 <br/>  
 * @author   xiaoxianlie
 * @version    
 * @since    JDK 1.8  
 * @see        
 */
public interface IndexService {
	public Map<String,Object> formatScore(Map<String,List<Map<String,Object>>> indexDataMap);
	
	public List<Object> getRiskFocus(String custNo);
	
	public List<Object> getRiskFocus2(String ratingId,String custNo);
	
	public List<Object> getIndexFromLst(List<Map<String,Object>> lst);
	
	public Map<String,List<Map<String,Object>>> queryModelIndexData(String custNo);
	
	public Map<String,List<Map<String,Object>>> queryModelIndexData2(String ratingId,String custNo);
	
	public List<Object> ratingGroup(String type);
	
	public Map<String, Object> findCustNo(String custNo);
	
	public List<Map<String, Object>> completedTask(Integer start,Integer end);
	
	public List<Map<String, Object>> haveToDoTask(Integer start,Integer end);
	
	public List<Map<String, Object>> queryTask(String loginNo,Integer start,Integer end);
	
	public Map<String, Object> queryDetails(String id);
	
	public List<Object> ratingFxfb();
	
	public String queryCustCode() throws Exception;
	
	public List<Map<String, Object>> queryAnnouncement();
	
	public List<Map<String, Object>> queryMsg() ;
	
	public List<Map<String, Object>> getCustOreder();
	
	public int queryMsgCnt();

	int queryAnnoCnt();

	boolean isZH();
}
  
