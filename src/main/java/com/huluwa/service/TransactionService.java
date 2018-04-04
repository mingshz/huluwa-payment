package com.huluwa.service;

import com.huluwa.model.RequestMessage;

import java.io.IOException;

/**
 * 交易接口
 */
public interface TransactionService {


    /**
     * 下单请求
     * @param r 商户必须的请求信息.
     */
    String transaction(RequestMessage r) throws IOException;


}
