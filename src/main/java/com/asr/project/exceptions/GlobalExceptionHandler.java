package com.asr.project.exceptions;

import com.asr.project.payloads.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundHandler(ResourceNotFoundException e) {

        logger.info("Resource Not Found Exception Invoked !!");
        ApiResponseMessage message = ApiResponseMessage.builder().message(e.getMessage()).
                status(HttpStatus.NOT_FOUND).success(true).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidHandler(MethodArgumentNotValidException e) {

        logger.info("Method Argument Not Valid Exception Invoked !!");
        e.getBindingResult().getAllErrors();
        List<ObjectError> errors = e.getAllErrors();
        Map<String, Object> response = new HashMap<>();
        errors.forEach(err-> {
            String key = ((FieldError)err).getField();
            String value = err.getDefaultMessage();
            response.put(key ,value);
        });
        return new  ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
