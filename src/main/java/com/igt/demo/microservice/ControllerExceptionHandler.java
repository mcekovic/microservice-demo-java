package com.igt.demo.microservice;

import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

   @Override protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
   }
}