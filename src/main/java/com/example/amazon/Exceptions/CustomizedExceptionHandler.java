package com.example.amazon.Exceptions;

import com.example.amazon.Entities.AmazonResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        AmazonResponseEntity<String> amazonResponseEntity = new AmazonResponseEntity<>(ex.getMessage(), request.getDescription(false), 500);
        return new ResponseEntity<>(amazonResponseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<?> handleNotFoundException(RecordNotFoundException ex, WebRequest request) {
        AmazonResponseEntity<String> amazonResponseEntity = new AmazonResponseEntity<>(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(amazonResponseEntity, HttpStatus.NOT_FOUND);
    }
}
