package com.huluwa.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * 商户可参考本类编写获取请求参数的方法，也可直接使用本类	
 *
 */

public class RequestUtils {
		
	public static JSONObject parseReq2Json(HttpServletRequest request) {
        Map paramMap = request.getParameterMap();
        JSONObject jsonObject = new JSONObject(paramMap.size());
        for (Iterator iter = paramMap.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry element = (Map.Entry) iter.next();
            String strKey = (String) element.getKey();            
            Object[] strObj = (Object[]) element.getValue();
            if (strObj != null && strObj.length > 0  && StringUtils.isNotBlank(strObj[0].toString())) {
                jsonObject.put(strKey, strObj[0]);
            }
        }
        return jsonObject;
    }


}
