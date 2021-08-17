package com.vanguard.trade.reporting.engine.exception;

/**
 *  Checked exception to deleberatliy checking for exceptions
 */
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
