package gbicc.irs.sample.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.wsp.framework.security.util.UrlSsoTokenEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
	
	private static final String url ="http://192.168.18.5:8080/irbs";
	
	public static void main(String[] args) throws Exception{
		//Resource rs =new DefaultResourceLoader().getResource("file:D:/eclipse_workSpace/Non-retail-internal-rating-system/gbicc.irs.app/work/web/export/export_1533102723697.xlsx");
		
		//String s = "{\"list\":[\"1\",\"2\",\"4\"],\"element\":\"4\"}";
		//ObjectMapper mapper = new ObjectMapper();
		//Map<String,Object> aa = mapper.readValue(s, Map.class);
//		String a = "短期投资 +上期_净资产合计 +流动资产 +上期_预付账款 ";
//		String b = a.replace("短期投资", "测试");
//		System.out.println(b);
		
		String token =UrlSsoTokenEncoder.encode("admin").getUrl();
		System.out.println(url + "?" + token);
		
	}
}
