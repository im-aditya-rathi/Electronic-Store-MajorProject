package com.asr.project.exceptions;

public class BadApiResponseException extends RuntimeException {

    public BadApiResponseException() {
        super("Bad API Request !!");
    }

    public BadApiResponseException(String msg) {
        super(msg);
    }

}
