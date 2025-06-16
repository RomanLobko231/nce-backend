package com.nce.backend.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodFieldValidationException(MethodArgumentNotValidException e) {
        log.error("Validation error(s) occurred: {}", e.getMessage(), e);

        StringBuilder constraints = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            constraints.append(errorMessage).append("; ");
        });

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error(s): ".concat(constraints.toString())
        );
    }

    @ExceptionHandler(FileProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileProcessingException(FileProcessingException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDateTimeParseException(DateTimeParseException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid date format. Check and try again"
        );
    }

    @ExceptionHandler(SizeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSizeException(SizeException e) {
        log.error(e.getMessage(), e);
        log.warn("WARN WARN WARN");
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Max upload size of 10 MB exceeded. Try again with less data."
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);

        String errorMessage = e.getMessage();
        String errorCause;
        if (errorMessage.contains("violates unique constraint \"base_user_email_key\"")) {
            errorCause = "User with this email already exists";
        } else if (errorMessage.contains("violates unique constraint \"car_registration_number_key\"")) {
            errorCause = "Car with this registration number already exists";
        } else {
            errorCause = "Could not perform save.";
        }

        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                errorCause
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Your request is invalid. Cannot proceed."
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidTokenException(InvalidTokenException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Provided token is invalid."
        );
    }
}
