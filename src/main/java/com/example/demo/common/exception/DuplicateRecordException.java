package com.example.demo.common.exception;

public class DuplicateRecordException extends Exception{
    public DuplicateRecordException(final String id, final String type){
        super(type + " with Id: " + id+" already existed");
    }
}
