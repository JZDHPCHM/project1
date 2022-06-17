package com.gbicc.aicr.system.controller;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.wsp.framework.mvc.service.impl.SystemParameterServiceImpl;
import org.xml.sax.InputSource;

import com.gbicc.aicr.system.util.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.ray.pojo.message.send.Article;
import com.ray.pojo.message.send.News;
import com.ray.pojo.message.send.NewsMessage;
import com.ray.service.SendMessageService;
import com.ray.util.WeiXinParamesUtil;
import com.ray.util.WeiXinUtil;

@RestController
@RequestMapping(value = "/wechat")
public class WeixinCoreController {

	@Autowired
	SystemParameterServiceImpl systemParameterServiceImpl;

	@RequestMapping(value = "/access", method = RequestMethod.GET)
	public String WeChatInterface(HttpServletRequest request, String msg_signature, String timestamp, String nonce,
			String echostr) throws Exception {
		/*
		 * int DecryptMsg(const string &sMsgSignature, const string &sTimeStamp, const
		 * string &sNonce, const string &sPostData, string &sMsg);
		 */
		String msg = qywxCheck(msg_signature, timestamp, nonce, echostr);
		return msg;
	}

	public String qywxCheck(String msg_signature, String timestamp, String nonce, String echostr) throws AesException {
		String sToken = "M75YB9Cd";
		String sCorpID = "wxc21a858872704da1";
		String sEncodingAESKey = "0rK4DgFBy3Klw6NV2oOjKrUKkiJdcYuICSOzFsg2kwq";

		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
		/*
		 * ------------使用示例一：验证回调URL--------------- 企业开启回调模式时，企业号会向验证url发送一个get请求
		 * 假设点击验证时，企业收到类似请求： GET
		 * /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&
		 * timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%
		 * 2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
		 * HTTP/1.1 Host: qy.weixin.qq.com
		 * 
		 * 接收到该请求时，企业应 1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)
		 * 以及公众平台推送过来的随机加密字符串(echostr), 这一步注意作URL解码。 2.验证消息体签名的正确性 3.
		 * 解密出echostr原文，将原文当作Get请求的response，返回给公众平台 第2，3步可以用公众平台提供的库函数VerifyURL来实现。
		 * 
		 */
		// 解析出url上的参数值如下：
		// String sVerifyMsgSig = HttpUtils.ParseUrl("msg_signature");
		String sVerifyMsgSig = msg_signature;
		// String sVerifyTimeStamp = HttpUtils.ParseUrl("timestamp");
		String sVerifyTimeStamp = timestamp;
		// String sVerifyNonce = HttpUtils.ParseUrl("nonce");
		String sVerifyNonce = nonce;
		// String sVerifyEchoStr = HttpUtils.ParseUrl("echostr");
		String sVerifyEchoStr = echostr;
		String sEchoStr = ""; // 需要返回的明文
		try {
			sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
			System.out.println("verifyurl echostr: " + sEchoStr);
			// 验证URL成功，将sEchoStr返回
			// HttpUtils.SetResponse(sEchoStr);
		} catch (Exception e) {
			// 验证URL失败，错误原因请查看异常
			e.printStackTrace();
		}

		/*
		 * ------------使用示例二：对用户回复的消息解密---------------
		 * 用户回复消息或者点击事件响应时，企业会收到回调消息，此消息是经过公众平台加密之后的密文以post形式发送给企业，密文格式请参考官方文档
		 * 假设企业收到公众平台的回调消息如下： POST /cgi-bin/wxpush?
		 * msg_signature=477715d11cdb4164915debcba66cb864d751f3e6&timestamp=1409659813&
		 * nonce=1372623149 HTTP/1.1 Host: qy.weixin.qq.com Content-Length: 613 <xml>
		 * <ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName><Encrypt><![CDATA[
		 * RypEvHKD8QQKFhvQ6QleEB4J58tiPdvo+rtK1I9qca6aM/wvqnLSV5zEPeusUiX5L5X/
		 * 0lWfrf0QADHHhGd3QczcdCUpj911L3vg3W/
		 * sYYvuJTs3TUUkSUXxaccAS0qhxchrRYt66wiSpGLYL42aM6A8dTT+
		 * 6k4aSknmPj48kzJs8qLjvd4Xgpue06DOdnLxAUHzM6+kDZ+HMZfJYuR+
		 * LtwGc2hgf5gsijff0ekUNXZiqATP7PF5mZxZ3Izoun1s4zG4LUMnvw2r+KqCKIw+3IQH03v+
		 * BCA9nMELNqbSf6tiWSrXJB3LAVGUcallcrw8V2t9EL4EhzJWrQUax5wLVMNS0+
		 * rUPA3k22Ncx4XXZS9o0MBH27Bo6BpNelZpS+/uh9KsNlY6bHCmJU9p8g7m3fVKn28H3KDYA5Pl/
		 * T8Z1ptDAVe0lXdQ2YoyyH2uyPIGHBZZIs2pDBS8R07+qN+E7Q==]]></Encrypt>
		 * <AgentID><![CDATA[218]]></AgentID> </xml>
		 * 
		 * 企业收到post请求之后应该
		 * 1.解析出url上的参数，包括消息体签名(msg_signature)，时间戳(timestamp)以及随机数字串(nonce)
		 * 2.验证消息体签名的正确性。
		 * 3.将post请求的数据进行xml解析，并将<Encrypt>标签的内容进行解密，解密出来的明文即是用户回复消息的明文，明文格式请参考官方文档
		 * 第2，3步可以用公众平台提供的库函数DecryptMsg来实现。
		 */
		// String sReqMsgSig = HttpUtils.ParseUrl("msg_signature");
		String sReqMsgSig = msg_signature;
		// String sReqTimeStamp = HttpUtils.ParseUrl("timestamp");
		String sReqTimeStamp = timestamp;
		// String sReqNonce = HttpUtils.ParseUrl("nonce");
		String sReqNonce = nonce;
		// post请求的密文数据
		// sReqData = HttpUtils.PostData();"+sCorpID+"
		String sReqData = "<xml><ToUserName><![CDATA[" + sCorpID
				+ "]]></ToUserName><Encrypt><![CDATA[RypEvHKD8QQKFhvQ6QleEB4J58tiPdvo+rtK1I9qca6aM/wvqnLSV5zEPeusUiX5L5X/0lWfrf0QADHHhGd3QczcdCUpj911L3vg3W/sYYvuJTs3TUUkSUXxaccAS0qhxchrRYt66wiSpGLYL42aM6A8dTT+6k4aSknmPj48kzJs8qLjvd4Xgpue06DOdnLxAUHzM6+kDZ+HMZfJYuR+LtwGc2hgf5gsijff0ekUNXZiqATP7PF5mZxZ3Izoun1s4zG4LUMnvw2r+KqCKIw+3IQH03v+BCA9nMELNqbSf6tiWSrXJB3LAVGUcallcrw8V2t9EL4EhzJWrQUax5wLVMNS0+rUPA3k22Ncx4XXZS9o0MBH27Bo6BpNelZpS+/uh9KsNlY6bHCmJU9p8g7m3fVKn28H3KDYA5Pl/T8Z1ptDAVe0lXdQ2YoyyH2uyPIGHBZZIs2pDBS8R07+qN+E7Q==]]></Encrypt><AgentID><![CDATA[218]]></AgentID></xml>";

		/*
		 * <xml> <ToUserName><![CDATA[mycreate]]></ToUserName>
		 * <FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName>
		 * <CreateTime>1348831860</CreateTime> <MsgType><![CDATA[text]]></MsgType>
		 * <Content><![CDATA[this is a test]]></Content> </xml>
		 */

		try {
			String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
			System.out.println("after decrypt msg: " + sMsg);
			// TODO: 解析出明文xml标签的内容进行处理
			// For example:
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader sr = new StringReader(sMsg);
			InputSource is = new InputSource(sr);
			Document document = db.parse(is);

			Element root = document.getDocumentElement();
			NodeList nodelist1 = root.getElementsByTagName("Content");
			String Content = nodelist1.item(0).getTextContent();
			System.out.println("Content：" + Content);

		} catch (Exception e) {
			// TODO
			// 解密失败，失败原因请查看异常
			e.printStackTrace();
		}

		/*
		 * ------------使用示例三：企业回复用户消息的加密---------------
		 * 企业被动回复用户的消息也需要进行加密，并且拼接成密文格式的xml串。 假设企业需要回复用户的明文如下： <xml>
		 * <ToUserName><![CDATA[mycreate]]></ToUserName>
		 * <FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName>
		 * <CreateTime>1348831860</CreateTime> <MsgType><![CDATA[text]]></MsgType>
		 * <Content><![CDATA[this is a test]]></Content> <MsgId>1234567890123456</MsgId>
		 * <AgentID>128</AgentID> </xml>
		 * 
		 * 为了将此段明文回复给用户，企业应：
		 * 1.自己生成时间时间戳(timestamp),随机数字串(nonce)以便生成消息体签名，也可以直接用从公众平台的post url上解析出的对应值。
		 * 2.将明文加密得到密文。 3.用密文，步骤1生成的timestamp,nonce和企业在公众平台设定的token生成消息体签名。
		 * 4.将密文，消息体签名，时间戳，随机数字串拼接成xml格式的字符串，发送给企业。 以上2，3，4步可以用公众平台提供的库函数EncryptMsg来实现。
		 */
		String sRespData = "<xml><ToUserName><![CDATA[mycreate]]></ToUserName><FromUserName><![CDATA[wx5823bf96d3bd56c7]]></FromUserName><CreateTime>1348831860</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content><MsgId>1234567890123456</MsgId><AgentID>128</AgentID></xml>";
		String sEncryptMsg = "";
		try {
			sEncryptMsg = wxcpt.EncryptMsg(sRespData, sReqTimeStamp, sReqNonce);
			System.out.println("after encrypt sEncrytMsg: " + sEncryptMsg);
			// 加密成功
			// TODO:
			// HttpUtils.SetResponse(sEncryptMsg);
		} catch (Exception e) {
			e.printStackTrace();
			// 加密失败
		}
		return sEchoStr;

	}

	@RequestMapping("sendWarnMsgB")
	public void sendWarnMsgB() {
		// 0 0 0 1 * ?
		// 1.创建图文消息对象
		NewsMessage message = new NewsMessage();
		// 1.1非必需
		//
		message.setToparty("");

		// 1.2必需
		message.setMsgtype("news");
		message.setAgentid(WeiXinParamesUtil.agentId);
		// 设置图文消息
		Article article1 = new Article();
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		article1.setTitle(formateDate(today.getYear()+"-"+sjc).substring(0,4)+"年"+formateDate(today.getYear()+"-"+sjc).substring(4)+"月预警信息报告");
		article1.setDescription("");
		String url = "";
		String user = "";
		try {
			user = systemParameterServiceImpl.getParameter("UserYl");
			url = systemParameterServiceImpl.getParameter("sendUrl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		message.setTouser(user); // 不区分大小写
		article1.setPicurl(url + "/images/risk_warning_img.png");
		
		article1.setUrl(url + "/riskWarning?sj=" + formateDate(today.getYear()+"-"+sjc));

		List<Article> articles = new ArrayList<Article>();
		articles.add(article1);

		News news = new News();
		news.setArticles(articles);
		message.setNews(news);

		// 2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
		String accessToken = WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.agentSecret)
				.getToken();
		System.out.println("accessToken:" + accessToken);

		// 3.发送消息：调用业务类，发送消息
		SendMessageService sms = new SendMessageService();
		try {
			sms.sendMessage(accessToken, message);
		} catch (Exception e) {
			e.printStackTrace();
		}	}
	@RequestMapping("sendDebtMsgB")
	public void sendDebtMsgB() {
		// 0 0 0 1 * ?
		// 1.创建图文消息对象
		NewsMessage message = new NewsMessage();
		// 1.1非必需
		message.setToparty("");

		// 1.2必需
		message.setMsgtype("news");
		message.setAgentid(WeiXinParamesUtil.agentId);
		// 设置图文消息
		Article article1 = new Article();
		String url = "";
		String user = "";
		try {
			user = systemParameterServiceImpl.getParameter("UserYl");
			url = systemParameterServiceImpl.getParameter("sendUrl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		article1.setTitle(formateDate(today.getYear()+"-"+sjc).substring(0,4)+"年"+formateDate(today.getYear()+"-"+sjc).substring(4)+"月债项评级报告");
		message.setTouser(user); // 不区分大小写
		article1.setDescription("");
		article1.setPicurl(url + "/images/debt_rating_img.png");
		
		article1.setUrl(url + "/debtRating?sj=" + formateDate(today.getYear()+"-"+sjc));

		List<Article> articles = new ArrayList<Article>();
		articles.add(article1);

		News news = new News();
		news.setArticles(articles);
		message.setNews(news);

		// 2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
		String accessToken = WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.agentSecret)
				.getToken();
		System.out.println("accessToken:" + accessToken);

		// 3.发送消息：调用业务类，发送消息
		SendMessageService sms = new SendMessageService();
		try {
			sms.sendMessage(accessToken, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("sendMainMsgB")
	public void sendMainMsgB() {
		// 0 0 0 1 * ?
		// 1.创建图文消息对象
		NewsMessage message = new NewsMessage();
		// 1.1非必需
		message.setToparty("");

		// 1.2必需
		message.setMsgtype("news");
		message.setAgentid(WeiXinParamesUtil.agentId);
		// 设置图文消息
		Article article1 = new Article();
		String url = "";
		String user = "";
		try {
			user = systemParameterServiceImpl.getParameter("UserYl");
			url = systemParameterServiceImpl.getParameter("sendUrl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		article1.setTitle(formateDate(today.getYear()+"-"+sjc).substring(0,4)+"年"+formateDate(today.getYear()+"-"+sjc).substring(4)+"月主体评级报告");
		message.setTouser(user); // 不区分大小写
		article1.setDescription("");
		article1.setPicurl(url + "/images/main_rating_img.png");
		
		article1.setUrl(url + "/mainRating?sj=" + formateDate(today.getYear()+"-"+sjc));

		List<Article> articles = new ArrayList<Article>();
		articles.add(article1);

		News news = new News();
		news.setArticles(articles);
		message.setNews(news);

		// 2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
		String accessToken = WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.agentSecret)
				.getToken();
		System.out.println("accessToken:" + accessToken);

		// 3.发送消息：调用业务类，发送消息
		SendMessageService sms = new SendMessageService();
		try {
			sms.sendMessage(accessToken, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("send")
	public void auth() {
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		System.out.println(formateDate(today.getYear()+"-"+sjc));
	}
	public static String formateDate(String strDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date date = sdf.parse(strDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH,-1);
            
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH)+1;
            String strMo="";
            if(month<10) {
            	strMo = "0" + month;
            }else {
            	strMo = "" + month;
            }
            
            return year+strMo;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * @主体，债项，预警信息推送
	 */
	@RequestMapping(value = "/sendMainMsg")
	@ResponseBody
	@Scheduled(cron = "0 14 17 16 * ?")
	public void sendMainMsg() {
		// 0 0 0 1 * ?
		// 1.创建图文消息对象
		NewsMessage message = new NewsMessage();
		// 1.1非必需
		message.setToparty("");

		// 1.2必需
		message.setMsgtype("news");
		message.setAgentid(WeiXinParamesUtil.agentId);
		// 设置图文消息
		String url = "";
		String user = "";
		String month = "";
		try {
			user = systemParameterServiceImpl.getParameter("User");
			url = systemParameterServiceImpl.getParameter("sendUrl");
			month = systemParameterServiceImpl.getParameter("month");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		message.setTouser(user); // 不区分大小写
		Article article1 = new Article();
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		article1.setTitle(formateDate(today.getYear()+"-"+sjc).substring(0,4)+"年"+formateDate(today.getYear()+"-"+sjc).substring(4)+"月主体评级报告");
		article1.setDescription("");
		article1.setPicurl(url + "/images/main_rating_img.png");
		article1.setUrl(url + "/mainRating?sj=" + formateDate(today.getYear()+"-"+sjc));
		List<Article> articles = new ArrayList<Article>();
		articles.add(article1);

		News news = new News();
		news.setArticles(articles);
		message.setNews(news);

		// 2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
		String accessToken = WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.agentSecret)
				.getToken();
		System.out.println("accessToken:" + accessToken);

		// 3.发送消息：调用业务类，发送消息
		SendMessageService sms = new SendMessageService();
		try {
			sms.sendMessage(accessToken, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/sendDebtMsg")
	@ResponseBody
	@Scheduled(cron = "0 14 17 16 * ?")
	public void sendDebtMsg() {
		// 0 0 0 1 * ?
		// 1.创建图文消息对象
		NewsMessage message = new NewsMessage();
		// 1.1非必需
		message.setToparty("");

		// 1.2必需
		message.setMsgtype("news");
		message.setAgentid(WeiXinParamesUtil.agentId);
		// 设置图文消息
		Article article1 = new Article();
		String url = "";
		String user = "";
		try {
			user = systemParameterServiceImpl.getParameter("User");
			url = systemParameterServiceImpl.getParameter("sendUrl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		article1.setTitle(formateDate(today.getYear()+"-"+sjc).substring(0,4)+"年"+formateDate(today.getYear()+"-"+sjc).substring(4)+"月债项评级报告");
		message.setTouser(user); // 不区分大小写
		article1.setDescription("");
		article1.setPicurl(url + "/images/debt_rating_img.png");
		
		article1.setUrl(url + "/debtRating?sj=" + formateDate(today.getYear()+"-"+sjc));

		List<Article> articles = new ArrayList<Article>();
		articles.add(article1);

		News news = new News();
		news.setArticles(articles);
		message.setNews(news);

		// 2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
		String accessToken = WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.agentSecret)
				.getToken();
		System.out.println("accessToken:" + accessToken);

		// 3.发送消息：调用业务类，发送消息
		SendMessageService sms = new SendMessageService();
		try {
			sms.sendMessage(accessToken, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/sendWarnMsg")
	@ResponseBody
	@Scheduled(cron = "0 14 17 16 * ?")
	public void sendWarnMsg() {
		// 0 0 0 1 * ?
		// 1.创建图文消息对象
		NewsMessage message = new NewsMessage();
		// 1.1非必需
		//
		message.setToparty("");

		// 1.2必需
		message.setMsgtype("news");
		message.setAgentid(WeiXinParamesUtil.agentId);
		// 设置图文消息
		Article article1 = new Article();
		LocalDate today = LocalDate.now();
		String sjc = "";
		if (today.getMonth().getValue() < 10) {
			sjc = "0" + today.getMonth().getValue();
		} else {
			sjc = String.valueOf(today.getMonth().getValue());
		}
		article1.setTitle(formateDate(today.getYear()+"-"+sjc).substring(0,4)+"年"+formateDate(today.getYear()+"-"+sjc).substring(4)+"月预警信息报告");
		article1.setDescription("");
		String url = "";
		String user = "";
		try {
			user = systemParameterServiceImpl.getParameter("User");
			url = systemParameterServiceImpl.getParameter("sendUrl");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		message.setTouser(user); // 不区分大小写
		article1.setPicurl(url + "/images/risk_warning_img.png");
		
		article1.setUrl(url + "/riskWarning?sj=" + formateDate(today.getYear()+"-"+sjc));

		List<Article> articles = new ArrayList<Article>();
		articles.add(article1);

		News news = new News();
		news.setArticles(articles);
		message.setNews(news);

		// 2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
		String accessToken = WeiXinUtil.getAccessToken(WeiXinParamesUtil.corpId, WeiXinParamesUtil.agentSecret)
				.getToken();
		System.out.println("accessToken:" + accessToken);

		// 3.发送消息：调用业务类，发送消息
		SendMessageService sms = new SendMessageService();
		try {
			sms.sendMessage(accessToken, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
