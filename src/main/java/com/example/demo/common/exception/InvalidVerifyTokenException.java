package com.example.demo.common.exception;

public class InvalidVerifyTokenException extends Exception{
    public InvalidVerifyTokenException(String tokenString){
        super("Token "+tokenString+" is invalid or expired");
    }
}
