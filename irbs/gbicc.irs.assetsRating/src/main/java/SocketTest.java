
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import net.sf.json.JSONObject;


public class SocketTest {

	public static void main(String[] args) {

				String data2 ="{\"projectInfo\":{\"PROJECT_NUMBER\":\"PJ2020120024\",\"BP_ID_TENANT\":\"11942\",\"RISK_MANAGER_NAME\":\"才华\",\"DOCUMENT_TYPE\":\"直租项目\",\"LEASE_CHANNEL\":\"标准项目\",\"PROJECT_NAME\":\"深圳市普渡科技有限公司直租项目1期\",\"EMPLOYEE_ID\":\"1477\",\"LEASE_ORGANIZATION\":\"大数据事业部\",\"ENTERPRISE_SCALE\":\"MIDDLE\",\"ASSISTING_PM_A\":\"\",\"LEASE_START_DATE\":\"\",\"LEASE_TERM\":\"1\",\"FINANCE_AMOUNT\":\"20000000\",\"MARGIN\":\"2000000\",\"LEASE_ITEM_SHORT_NAME\":\"普渡配送机器人\",\"PRODUCT_TYPE\":\"一般产品\",\"ISCORELEASE\":\"Y\",\"CORELEASE_PROPORTION\":\"0.98\",\"ORIGINAL_VALUE\":\"20000000\",\"NET_VALUE\":\"20000000\",\"ASSESSED_VALUE\":\"19600000\",\"ASSESSMENT_METHODS\":\"市场法\",\"CREDIT_TYPE\":\"房产抵押1个、土地抵押1个、收益权质押2个、股权质押1个、应收账款质押1个\"},\"creditInfo\":[{\"CODE\":\"DL_FXCK\",\"VALUE\":\"18000000\"},{\"usufructInfo\":[{\"CODE\":\"DL_SYQ_F14\",\"VALUE\":\"1000000\"},{\"CODE\":\"DX_SYQ_HKZL_10\",\"VALUE\":\"A\"},{\"CODE\":\"DX_SYQ_CZND_11\",\"VALUE\":\"A\"}]},{\"usufructInfo\":[{\"CODE\":\"DL_SYQ_F14\",\"VALUE\":\"1\"},{\"CODE\":\"DX_SYQ_HKZL_10\",\"VALUE\":\"A\"},{\"CODE\":\"DX_SYQ_CZND_11\",\"VALUE\":\"A\"}]},{\"equityInfo\":[{\"CODE\":\"DL_GQTZX_F14\",\"VALUE\":\"110000\"},{\"CODE\":\"DX_GQTZ_ZYL_12\",\"VALUE\":\"A\"},{\"CODE\":\"DX_GQTZ_BXND_13\",\"VALUE\":\"A\"}]},{\"receivablesInfo\":[{\"CODE\":\"DL_YSZK_F13\",\"VALUE\":\"1000000\"},{\"CODE\":\"DX_YSZK_QRQK_5\",\"VALUE\":\"A\"},{\"CODE\":\"DX_YSZK_MFSL_6\",\"VALUE\":\"A\"},{\"CODE\":\"DX_YSZK_ZL_7\",\"VALUE\":\"A\"},{\"CODE\":\"DX_YSZK_HZNX_8\",\"VALUE\":\"A\"}]},{\"realEstateInfo\":[{\"CODE\":\"DL_BDC_F11\",\"VALUE\":\"10000000\"},{\"CODE\":\"DX_BDC_FCDYQK_1\",\"VALUE\":\"A\"},{\"CODE\":\"DX_BDC_FCBXND_3\",\"VALUE\":\"A\"}]},{\"landUseRightinfo\":[{\"CODE\":\"DL_TDSYQ_F12\",\"VALUE\":\"1000000\"},{\"CODE\":\"DX_TD_DYQK_3\",\"VALUE\":\"A\"},{\"CODE\":\"DX_TD_BXND_4\",\"VALUE\":\"A\"}]}],\"debtRatingInfo\":{\"FD_INTERN_CODE\":\"1241\",\"FD_INTERN_NAME\":\"潘存瑾\",\"FD_ASSETS_CODE\":\"850\",\"FD_ASSETS_NAME\":\"潘晨\",\"FD_FINAL_CODE\":\"822\",\"FD_FINAL_NAME\":\"才华\",\"FD_RATING_TYPE\":\"DEBT_START_STAGE\",\"FD_RATING_STATUS\":\"010\",\"FD_VERSION\":\"3.0\"},\"type\":\"340\"}";

				JSONObject resultz = JSONObject.fromObject(data2);
				ArrayList<JSONObject> 初评 = new ArrayList<JSONObject>();
				初评.add(resultz);
				ArrayList<JSONObject> 复评 = new ArrayList<JSONObject>();
				for(JSONObject json :初评 ) {
					System.out.println("*******等待依次评级********");
					new Thread(new Runnable() {
						Object obj = null;
						ObjectOutputStream is = null;
						Socket socket = null;
						ObjectInputStream ou = null;
						public void run() {
							try {
								Map map = (Map) com.alibaba.fastjson.JSONObject.parseObject(resultz.toString());
								socket = new Socket("localhost", 8891);
								is = new ObjectOutputStream(socket.getOutputStream());
								is.writeObject(map);
								socket.shutdownOutput();
								ObjectInputStream ou = new ObjectInputStream(socket.getInputStream());
								try {
									obj = ou.readObject();
									System.out.println(obj);
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}finally {
								if(is !=null) {
									try {
										is.flush();
										is.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if(ou!=null) {
									try {
										ou.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								if(socket!=null) {
									try {
										socket.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						}
					}).start();
				}
			
		}
	
}
