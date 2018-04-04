package com.huluwa.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huluwa.event.CallbackFail;
import com.huluwa.event.CallbackSuccess;
import com.huluwa.exception.AttestationException;
import com.huluwa.util.SignUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 回调处理控制器
 */
@Controller
@CommonsLog
public class CallbackController {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private final String key;

    @Autowired
    public CallbackController(Environment environment) {
        this.key = environment.getProperty("huluwa.Mkey", "55be454630e847d7815c2c2d3bc59c0d");
    }

    @RequestMapping("/huluwa/transfer")
    @ResponseBody
    public String transferResult(@RequestBody String request) throws DecoderException {
        log.debug("callback参数:"+request);

        //解析返回串
        JSONObject returnMap = JSON.parseObject(request);
        // 验签
        if(!SignUtil.validSign(returnMap, key)){
            throw new AttestationException("验签错误");
        };
        //获取通信Code
        String returnCode = returnMap.getString("returnCode");
        if("1".equals(returnCode)){
            //失败
            return errorLog(returnMap);
        }

        //获取业务Code
        String resultCode = returnMap.getString("resultCode");
        if("1".equals(resultCode)){
            //失败
            return errorLog(returnMap);
        }

        //查看交易状态
        String status = returnMap.getString("status");
        //获取订单号
        String outTradeNo = returnMap.getString("outTradeNo");
        if("01".equals(status)){
            //目前不需要
        }else if("02".equals(status)){
            applicationEventPublisher.publishEvent(new CallbackSuccess(outTradeNo));
        }else if("05".equals(status)){
            //目前不需要
        }else if("09".equals(status)){
            applicationEventPublisher.publishEvent(new CallbackFail(outTradeNo));
        }
        return "SUCCESS";
    }

    private String errorLog(JSONObject returnMap){
        String errCodeDes = returnMap.getString("errCodeDes");
        if(errCodeDes != null) {
            log.error(errCodeDes);
        }
        return "FAIL";
    }
}
