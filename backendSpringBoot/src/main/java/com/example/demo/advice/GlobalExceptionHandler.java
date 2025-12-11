package com.example.demo.advice;

import com.example.demo.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex){ return build(HttpStatus.NOT_FOUND,"NOT_FOUND",ex.getMessage()); }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex){ return build(HttpStatus.BAD_REQUEST,"BAD_REQUEST",ex.getMessage()); }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenException ex){ return build(HttpStatus.FORBIDDEN,"FORBIDDEN",ex.getMessage()); }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex){
        ex.printStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR,"ERROR","Internal server error");
    }

    private ResponseEntity<Map<String,Object>> build(HttpStatus status, String code, String message){
        Map<String,Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "code", code,
                "message", message
        );
        return ResponseEntity.status(status).body(body);
    }
}
