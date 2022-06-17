package gbicc.irs.esb.service.support;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageUtil {

	public static byte[] addLenHead(byte[] byteData, int len) {
		byte[] byteSize = new byte[len];
		String strSize = new Integer(byteData.length).toString();
		for(int i=strSize.length(); i<len; i++) {
			strSize = "0" + strSize;
		}
		byteSize = strSize.getBytes();
		byte[] byteOut = new byte[byteSize.length + byteData.length];
		System.arraycopy(byteSize, 0, byteOut, 0, byteSize.length);
		System.arraycopy(byteData, 0, byteOut, byteSize.length, byteData.length);
		
		return byteOut;
	}
	
	public static String getCurrDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String strDate = sdf.format(date);
		return strDate;
	}
	
	public static String getCurrTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String strTime = sdf.format(date);
		return strTime;
	}
	
}
