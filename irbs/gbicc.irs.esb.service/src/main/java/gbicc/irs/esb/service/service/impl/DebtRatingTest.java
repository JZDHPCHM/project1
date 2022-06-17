package gbicc.irs.esb.service.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dc.eai.data.Array;
import com.dc.eai.data.CompositeData;
import com.dc.eai.data.Field;
import com.dc.eai.data.FieldAttr;
import com.dc.eai.data.FieldType;
import com.dcfs.esb.client.converter.PackUtil;
import com.hkb.esb.util.TCPConnUtil;

import gbicc.irs.esb.service.support.MessageUtil;

public class DebtRatingTest {
	private static final int HEAD_LEN = 8;
	private static final String ENCODING = "UTF-8";
	private static final String IP = "127.0.0.1";
	private static final int PORT = 8891;
	private static final int TIMEOUT = 70000;
	
	private static Log log = LogFactory.getLog(DebtRatingTest.class);
	
	public byte[] getReqMsg() {
		CompositeData req_data = new CompositeData();
		CompositeData SYS_HEAD = new CompositeData();
		CompositeData BODY= new CompositeData();
		
		FieldAttr fieldAttr = null;
		Field field = null;		
		
		//SYS_HEAD
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,11,0);
		field = new Field(fieldAttr);
		field.setValue("11002000052");
		SYS_HEAD.addField("SERVICE_CODE", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,2,0);
		field = new Field(fieldAttr);
		field.setValue("03");
		SYS_HEAD.addField("SERVICE_SCENE", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,8,0);
		field = new Field(fieldAttr);
		field.setValue("20130815");
		SYS_HEAD.addField("TRAN_DATE", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,9,0);
		field = new Field(fieldAttr);
		field.setValue("122748");
		SYS_HEAD.addField("TRAN_TIMESTAMP", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,30,0);
		field = new Field(fieldAttr);
		field.setValue("101619");
		SYS_HEAD.addField("USER_ID", field);
		
		//BODY
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,100,0);
		field = new Field(fieldAttr);
		field.setValue("20201303050000000002");
		BODY.addField("MSG_AUTHNT_CODE", field);

		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,20,0);
		field = new Field(fieldAttr);
		field.setValue("22401");
		BODY.addField("BRANCH_NO", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,50,0);
		field = new Field(fieldAttr);
		field.setValue("01");
		BODY.addField("BUSS_TYPE", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,10,0);
		field = new Field(fieldAttr);
		field.setValue("01");
		BODY.addField("CHECK_TYPE", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,10,0);
		field = new Field(fieldAttr);
		field.setValue("1");
		BODY.addField("CHECK_NUM", field);
		
		
		//BODY.ARRAY
		Array array = new Array();
		CompositeData struct = null;
		
		struct = new CompositeData();
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,150,0);
		field = new Field(fieldAttr);
		field.setValue("杨六");
		struct.addField("CLIENT_NAME", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,25,0);
		field = new Field(fieldAttr);
		field.setValue("420106197412092815");
		struct.addField("GLOBAL_ID", field);
		array.addStruct(struct);
		
		struct = new CompositeData();
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,150,0);
		field = new Field(fieldAttr);
		field.setValue("sss");
		struct.addField("CLIENT_NAME", field);
		
		fieldAttr = new FieldAttr(FieldType.FIELD_STRING,25,0);
		field = new Field(fieldAttr);
		field.setValue("420103198506020017");
		struct.addField("GLOBAL_ID", field);
		array.addStruct(1,struct);

		BODY.addArray("TRAN_LIST_ARRAY", array);
		
		//Construct request
		req_data.addStruct("SYS_HEAD", SYS_HEAD);
		req_data.addStruct("BODY", BODY);
		
		byte[] byteData = PackUtil.pack(req_data);
		byte[] reqBytes = MessageUtil.addLenHead(byteData, HEAD_LEN);
		return reqBytes;
	}
	
	public byte[] sendMessage(byte[] reqBytes) {

		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		byte[] rspData = null;

		try {
			log.debug("开始建立socket连接,address[" + IP + "]port[" + PORT + "]");
			
				socket = TCPConnUtil.getConnect(IP, PORT, TIMEOUT);
			

			if (log.isDebugEnabled()) {
				log.debug("socket连接成功");
			}
			// 将请求数据写出到Socket输出流
			os = new BufferedOutputStream(socket.getOutputStream());
			TCPConnUtil.writeMessage(os, reqBytes);

			if (log.isDebugEnabled()) {
				log.debug("请求报文的数据为[" + new String(reqBytes, ENCODING) + "]");
			}

			// 从Socket输入流中读取响应数据
			is = new BufferedInputStream(socket.getInputStream());
			// 先读headLength位的长度头,取出报文长度
			byte[] lenByte = TCPConnUtil.readLenContent(is, HEAD_LEN);
			int length = Integer.parseInt(new String(lenByte, ENCODING));

			if (log.isDebugEnabled()) {
				log.debug("返回报文的长度为[" + length + "]");
			}

			rspData = TCPConnUtil.readLenContent(is, length);

			if (log.isDebugEnabled()) {
				log.debug("返回报文的数据为[" + new String(rspData, ENCODING) + "]");
			}
		} catch (IOException e) {
			log.error("请求失败："+ e);
			e.printStackTrace();
		}
		finally {
			// 关闭连接
			TCPConnUtil.closeConnect(socket, os, is);
		}

		return rspData;
	}
	
	public static void main(String args[]) throws IOException {
		DebtRatingTest issueBondsQuery = new DebtRatingTest();
		byte[] reqBytes = issueBondsQuery.getReqMsg();
		byte[] rspData = issueBondsQuery.sendMessage(reqBytes);
		//log.debug(new String(reqBytes, ENCODING));
		byte[] byteCd = new byte[reqBytes.length - 8];
		System.arraycopy(reqBytes, 8, byteCd, 0, byteCd.length);
		CompositeData cd = PackUtil.unpack(byteCd);
		log.debug(cd);
	}
	
}
