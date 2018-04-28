package com.huluwa.exception;

/**
 * 请求数据异常
 */
public class RequestDataException extends RuntimeException {

    public RequestDataException(){

    }
    public RequestDataException(String s){
        super(s);
    }
    public RequestDataException(Exception e){
        super(e);
    }
}
