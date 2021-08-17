package com.vanguard.trade.reporting.engine.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

/**
 * Bean to represent API Exception. This is kind of a payload to be thrown to the caller
 */
@AllArgsConstructor
@Getter
public class ErrorMessage {
    /**
     * This error code is NOT HttpStatus code
     */
    private final int code;
    private final String message;
    private final ZonedDateTime timestamp;
}
