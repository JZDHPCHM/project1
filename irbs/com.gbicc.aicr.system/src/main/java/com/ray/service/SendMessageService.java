package com.ray.service;

import com.gbicc.aicr.system.util.AesException;
import com.ray.pojo.message.send.BaseMessage;
import com.ray.util.WeiXinUtil;

import net.sf.json.JSONObject;

/**@desc  : 发送消息
 * 
 * @author: shirayner
 * @date  : 2017-8-18 上午10:06:23
 */
public class SendMessageService {

	private  static  String sendMessage_url="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";  

    /**
     * @desc ：0.公共方法：发送消息
     *  
     * @param accessToken
     * @param message void
     * @throws Exception 
     */
	public void sendMessage(String accessToken,BaseMessage message) throws Exception{

		//1.获取json字符串：将message对象转换为json字符串	
		String jsonMessage = JSONObject.fromObject(message).toString();
		
		//2.获取请求的url  
		sendMessage_url=sendMessage_url.replace("ACCESS_TOKEN", accessToken);

		//3.调用接口，发送消息
		JSONObject jsonObject = null;
		try {
			 jsonObject = WeiXinUtil.httpRequest(sendMessage_url, "POST", jsonMessage); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ComputeSignatureError);
		}
		 
		System.out.println("jsonObject:"+jsonObject.toString());

		//4.错误消息处理
		if (null != jsonObject) {  
			if (0 != jsonObject.getInt("errcode")) {  
			}  
		}  
	}
	
	
	
	
}
