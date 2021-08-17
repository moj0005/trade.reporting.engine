package com.vanguard.trade.reporting.engine.exception;

/**
 *  Un-checked com.vanguard.trade.reporting.engine.exception to avoid using unnecessary try/catch block
 *
 *  this could be used to validate API Request format for example
 */
public class ApiRequestException extends RuntimeException {

    public ApiRequestException(String message) {
        super(message);
    }

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
