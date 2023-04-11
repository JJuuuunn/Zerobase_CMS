package com.zerobase.cms.order.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<CustomException.CustomExceptionResponse> exceptionHandler(HttpServletRequest req, final CustomException e) {
        return ResponseEntity.status(e.getStatus())
                .body(CustomException.CustomExceptionResponse.builder()
                        .code(e.getErrorCode().name())
                        .message(e.getMessage())
                        .status(e.getStatus())
                        .build());
    }
}
