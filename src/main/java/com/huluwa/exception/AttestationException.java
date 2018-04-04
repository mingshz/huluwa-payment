package com.huluwa.exception;

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
