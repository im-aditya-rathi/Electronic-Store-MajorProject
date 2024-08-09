package com.asr.project.exceptions;

import com.asr.project.payloads.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundHandler(ResourceNotFoundException ex) {

        logger.info("Resource Not Found Exception Invoked !!");
        ApiResponseMessage message = ApiResponseMessage.builder().message(ex.getMessage()).
                status(HttpStatus.NOT_FOUND).success(true).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(BadApiResponseException.class)
    public ResponseEntity<ApiResponseMessage> badApiResponseHandler(BadApiResponseException ex) {

        logger.info("Bad API Request !!");
        ApiResponseMessage message = ApiResponseMessage.builder().message(ex.getMessage()).
                status(HttpStatus.BAD_REQUEST).success(true).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseMessage> MissingServletRequestParameterHandler(MissingServletRequestParameterException ex) {

        logger.info("Required request parameter is not present while hitting APIs !!");

        ProblemDetail eBody = ex.getBody();
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .success(false)
                .message(ex.getMessage())
                .status(HttpStatus.valueOf(eBody.getStatus()))
                .build();

        return ResponseEntity.status(HttpStatus.valueOf(eBody.getStatus())).body(responseMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {

        logger.info("Method Argument Not Valid Exception Invoked !!");
        Map<String, Object> response = new HashMap<>();

        List<ObjectError> errors = ex.getAllErrors();
        errors.forEach(err-> {
            String key = ((FieldError)err).getField();
            String value = err.getDefaultMessage();
            response.put(key ,value);
        });

        return new  ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
