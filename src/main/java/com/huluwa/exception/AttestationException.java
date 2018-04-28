package com.huluwa.exception;


/**
 * 验证错误时异常
 */
public class AttestationException extends RuntimeException{

    public AttestationException(){

    }
    public AttestationException(String s){
        super(s);
    }
    public AttestationException(Exception e){
        super(e);
    }
}
