/*package gbicc.irs.esb.service.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wsp.framework.security.util.SecurityUtil;

import com.alibaba.fastjson.JSONObject;
import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.converter.PackUtil;

import gbicc.irs.esb.service.service.impl.CustomerRatingService;
import gbicc.irs.esb.service.service.impl.RatingQuery;

@RestController
@RequestMapping("/irs/esbTest")
public class EsbTestController{
	
	@Autowired
	private CustomerRatingService customerRatingService;
	
	@Autowired
	private DebtRatingService debtRatingService;
	@Autowired
	private RatingQuery ratinngQ;
	
	private static Log log = LogFactory.getLog(EsbTestController.class);
	
	@RequestMapping(value="testRating", method=RequestMethod.GET)
	public void test() throws Exception{
		ratinngQ.queryTaskGroup("102826");
		
		//customerRatingService.test();
		//debtRatingService.test();
	}
	
	
	@RequestMapping(value="testRating1", method=RequestMethod.POST)
	public void test1(String ratingId) throws Exception{
		try {
			CompanyRating rating = companyRatingService.findById(ratingId);
			log.info("---------------------开始ESB回写接口，信息："+rating.toString());
			// 发起回写评级接口
			byte[] reqData = companyRatingServiceImpl.getReqMsg(rating);
			byte[] retData = ESBRequestUtil.sendMessage(reqData);
			// 拆包
			CompositeData req = PackUtil.unpack(retData);
			if(req == null) {
				throw new Exception("接口返回信息异常");
			}
			CompositeData sys_head = req.getStruct("SYS_HEAD");
			if(sys_head == null) {
				throw new Exception("接口返回信息异常");
			}
			Array arr = sys_head.getArray("RET");
			CompositeData array = arr.getStruct(0);
			String retCode = array.getField("RET_CODE").strValue();
			if(!"000000".equals(retCode)) {
				String retMsg = array.getField("RET_MSG").strValue();
				throw new Exception("MIS返回评信息:"+retMsg);
			}
		} catch (Exception e) {
			log.error("回写接口err-info:",e);
			throw new Exception("MIS返回评级结果失败！，信息："+e);
		}finally {
			log.info("End-Esb-WriteBack");
		}
	}
	
	
	

}
*/