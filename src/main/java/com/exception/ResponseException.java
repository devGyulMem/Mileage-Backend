package com.exception;

import com.devmem.mileagebackend.utils.ResponseMap;

import lombok.Getter;

@Getter
public class ResponseException extends RuntimeException{
    String code;
    ResponseMap data;

    public ResponseException(String msg, String code){super(msg);this.code=code;}
    public ResponseException(String msg, String code, ResponseMap data){super(msg);this.code=code;this.data=data;}
    
}
