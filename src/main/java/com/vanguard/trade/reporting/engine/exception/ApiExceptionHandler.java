package com.vanguard.trade.reporting.engine.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * To handle custom and existing exceptions the way defined in this class, the way we want to through it to the client
 *
 * Also http status code is different to the error code being returned.
 */

 @ControllerAdvice // To handle all API Related exceptions // handle exceptions across the whole application, for all controllers having @RequestMapping
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler (value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorMessage apiExceptionBean = new ErrorMessage(
            1000,
            e.getMessage(),
            ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiExceptionBean, httpStatus);
    }

    @ExceptionHandler (value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        // This is payload
        ErrorMessage apiExceptionBean = new ErrorMessage(
            1001,
            e.getMessage(),
            ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiExceptionBean, httpStatus);
    }

    @ExceptionHandler (value = EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleConstraintViolationException(EmptyResultDataAccessException e) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        // This is payload
        ErrorMessage apiExceptionBean = new ErrorMessage(
            1002,
            e.getMessage(),
            ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiExceptionBean, httpStatus);
    }

    @ExceptionHandler (value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(DataIntegrityViolationException e) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        // This is payload
        ErrorMessage apiExceptionBean = new ErrorMessage(
            1003,
            "This action can't be performed because of data integrity issues with this request", // e.getMessage(), Don't use it as it is not user friendly
            ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiExceptionBean, httpStatus);
    }
}
