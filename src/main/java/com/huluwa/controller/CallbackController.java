package com.huluwa.controller;

import com.huluwa.event.CallbackFail;
import com.huluwa.event.CallbackSuccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 回调处理控制器
 */
@Controller
public class CallbackController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @RequestMapping("/huluwa/transfer")
    @ResponseBody
    public String transferResult(HttpServletRequest request){
        logger.info("callback参数:{}", request.getParameterMap());
        //解析返回串
        Map<String, String[]> returnMap = request.getParameterMap();
        //获取通信Code
        String[] returnCodes = returnMap.get("returnCode");
        String returnCode = returnCodes[0];
        if("1".equals(returnCode)){
            //失败
            return errorLog(returnMap);
        }

        //获取业务Code
        String[] resultCodes = returnMap.get("resultCode");
        String resultCode = resultCodes[0];
        if("1".equals(resultCode)){
            //失败
            return errorLog(returnMap);
        }

        //查看交易状态
        String[] statuses = returnMap.get("status");
        String status = statuses[0];
        if("01".equals(status)){
            //目前不需要
        }else if("02".equals(status)){
            applicationEventPublisher.publishEvent(new CallbackSuccess("支付成功"));
        }else if("05".equals(status)){
            //目前不需要
        }else if("09".equals(status)){
            applicationEventPublisher.publishEvent(new CallbackFail("支付失败:"));
        }
        return "SUCCESS";
    }

    private String errorLog(Map<String,String[]> returnMap){
        String[] errCodeDes = returnMap.get("errCodeDes");
        if(errCodeDes != null) {
            String errCodeDe = errCodeDes[0];
            logger.error(errCodeDe);
        }
        return "FAIL";
    }
}
