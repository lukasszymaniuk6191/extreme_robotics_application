package com.lukas.er.monitoring.controller;

import com.lukas.er.monitoring.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.text.ParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ExceptionDto> parseExceptionHandler(HttpServletRequest req, ParseException e) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST.toString(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<ExceptionDto>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> constraintViolationExceptionHandler(HttpServletRequest req, ConstraintViolationException e) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST.toString(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<ExceptionDto>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> rulesConstraintViolationException(HttpServletRequest req, IllegalArgumentException e) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST.toString(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<ExceptionDto>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionDto> missingServletRequestParameterExceptionHandler(
            HttpServletRequest req, MissingServletRequestParameterException e) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST.toString(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<ExceptionDto>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<ExceptionDto> indexOutOfBoundsExceptionHandler(
            HttpServletRequest req, IndexOutOfBoundsException e) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST.toString(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<ExceptionDto>(exceptionDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> exceptionHandler(HttpServletRequest req, Exception e) {
        ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.BAD_REQUEST.toString(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<ExceptionDto>(exceptionDto, HttpStatus.BAD_REQUEST);
    }


}
