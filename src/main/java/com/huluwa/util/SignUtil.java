package com.huluwa.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商户可参考本类编写加密和验签的方法，也可直接使用本类
 *
 */

public class SignUtil {
	

	public static JSONObject paraFilter(JSONObject sArray) {
		JSONObject result = new JSONObject();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		DecimalFormat formater = new DecimalFormat("###0.00");
		for (String key : sArray.keySet()) {
			String finalValue = null;
			Object value = sArray.get(key);
			if(value instanceof BigDecimal){
				finalValue = formater.format(value);
			}else {
				finalValue = (String) value;
			}
			if (value == null || value.equals("")
					|| key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, finalValue);
		}

		return result;
	}

	public static String createLinkString(JSONObject params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.getString(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        if(prestr.endsWith("&")) {
        	prestr = prestr.substring(0 , prestr.length() - 1);
        }
        return prestr;
    }

    public static String genSign(String key, String str) {
        String md5sign = md5(str + "&key=" + key).toUpperCase();
        return md5sign;
    }

    public static String md5(String plainText) {
        try {
            return Hex.encodeHexString(md5(new ByteArrayInputStream(plainText.getBytes("utf-8"))));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean validSign(JSONObject map, String key) {
        String oldSign = map.getString("sign");
        String signPlainText = createLinkString(map);
        String sign = genSign(key, signPlainText);
        boolean isValid = sign.equals(oldSign);
        if (!isValid) {
        }
        return isValid;
    }

    public static byte[] md5(InputStream input) throws Exception {
        return digest(input, "MD5");
    }

    public static byte[] sha1(InputStream input) throws Exception {
        return digest(input, "SHA-1");
    }

    private static byte[] digest(InputStream input, String algorithm) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        short bufferLength = 8192;
        byte[] buffer = new byte[bufferLength];

        for (int read = input.read(buffer, 0, bufferLength); read > -1; read = input.read(buffer, 0, bufferLength)) {
            messageDigest.update(buffer, 0, read);
        }
        return messageDigest.digest();

    }
}
