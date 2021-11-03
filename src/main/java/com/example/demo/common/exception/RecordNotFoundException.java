package com.example.demo.common.exception;

public class RecordNotFoundException extends Exception{
    public RecordNotFoundException(final String id, final String type){
        super("Not found " + type + " with Id: " + id);
    }
}
