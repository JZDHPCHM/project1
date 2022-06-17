package org.wsp.framework.security.impl.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MDC {

	public static String calcHMACSAH256(String data, String key) {
		System.out.println("calcHMACSAH256 data:" + data);
		String result = null;
		String HMAC_SHA1_ALGORITHM = "HmacSHA256";
		try {
			SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signinKey);
			byte[] rawHmac = mac.doFinal(data.getBytes("utf-8"));

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < rawHmac.length; i++) {
				String hex = Integer.toHexString(rawHmac[i] & 0xFF);
				if (hex.length() == 1)
					hex = "0" + hex;
				sb.append(hex);
			}
			// System.out.println("hex:"+sb.toString());
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static String gen32Encode() {
		String chose = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char ran[] = new char[32];
		Random rand = new Random();

		for (int i = 0; i < 32; i++) {

			ran[i] = chose.charAt(rand.nextInt(chose.length()));
		}
		return String.valueOf(ran);
	}
}
