package gbicc.irs.rti.service.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.DependsOn;

import gbicc.irs.commom.module.jpa.support.SpringUtil;
import gbicc.irs.fbinterface.service.impl.registrationInformationImpl;
import gbicc.irs.main.rating.service.impl.BpMasterServiceImpl;
import gbicc.irs.warning.service.impl.AftWarnInfoServiceImpl;
import net.sf.json.JSONObject;

/**
 * @实时接口处理
 * @author hzw
 *
 */
@DependsOn("springContextUtils")
public class EsbServiceHandler implements Runnable {

	private Socket socket;
	private static Log log = LogFactory.getLog(EsbServiceHandler.class);

	public EsbServiceHandler(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @根据汉德接口表示判断调用的方法
	 */
	public void run() {
		ObjectInputStream is = null;
		ObjectOutputStream os = null;
		Map<String, String> result = new HashMap<String, String>();
		try {
			is = new ObjectInputStream(socket.getInputStream());
			os = new ObjectOutputStream(socket.getOutputStream());
			RTIBpMasterServiceImpl bean = SpringUtil.getBean("rTIService", RTIBpMasterServiceImpl.class);
			// 按指定长度读取报文
			Object obj = is.readObject();
			JSONObject resultz = JSONObject.fromObject(obj);
			Map map = (Map) com.alibaba.fastjson.JSONObject.parseObject(resultz.toString());
			// resultz = JSONObject.fromObject(map);
			log.info("实时接口接收报文 : "+resultz);

			// 预警接口
			if (map.get("type").equals("010")) {
				AftWarnInfoServiceImpl afrWarn = (AftWarnInfoServiceImpl) SpringUtil.getBean("AftWarnInfoServiceImpl");
				System.out.println("jinlaile~~~");
				// result=afrWarn.updateResult(map);
			}
			// 主体评级接口3.0
			if (map.get("type").equals("320")) {
				result = bean.parsingData(map);
			}
			//主体评级接口2.0
            if(map.get("type").equals("020")) {
            	result = bean.parsingData(map);
            }
			// 注册信息导入
			if (map.get("type").equals("030")) {
				registrationInformationImpl bpService = (registrationInformationImpl) SpringUtil
						.getBean("registrationInformationImpl");
				try {
					String data = bpService.companySearch(map.get("regNumber").toString());
					result.put("data", data);
					result.put("flag", "010");
				} catch (Exception e) {
					result.put("flag", "020");
					result.put("msg", "无注册信息!");
					e.printStackTrace();
				}
			}
			// 债项评级3.0
			if (map.get("type").equals("340")) {
				result = bean.parsingData(map);
			}
			// 债项评级2.0
			if (map.get("type").equals("040")) {
				result = bean.parsingData(map);
			}
			// 关注信息
			if (map.get("type").equals("050")) {
				AftWarnInfoServiceImpl bpService = (AftWarnInfoServiceImpl) SpringUtil
						.getBean("AftWarnInfoServiceImpl");
				try {
					result = bpService.changeFocusOnInter(map);
					if (result.get("flag").toString().equals("020")) {
						result.put("msg", "关注信息修改成功!");
					}

				} catch (Exception e) {
					result.put("flag", "020");
					result.put("msg", "关注信息修改成功!");
					e.printStackTrace();
				}
			}
			// 资产评级
			if (map.get("type").equals("060")) {
				result = bean.parsingData(map);
			}

			// 执行业务逻辑代码，获取返回报文
//			os.writeObject(result);
//			os.flush();
			log.info("实时接口返回报文信息："+result);
		} catch (Exception e) {
			log.info(this.getClass().getName()+" method："+Thread.currentThread().getStackTrace()[1].getMethodName()+" line："+Thread.currentThread().getStackTrace()[1].getLineNumber()+"：实时接口异常结束！",e);
			e.printStackTrace();
		} finally {
			try {
				if(os!=null) {
					os.writeObject(result);
					os.flush();
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

}
