package com.nce.backend.cars.ui;

import com.nce.backend.cars.exceptions.CarAlreadyExistsException;
import com.nce.backend.cars.ui.responses.ErrorResponse;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.NoSuchElementException;

@ControllerAdvice(assignableTypes = CarController.class)
@ResponseBody
@Slf4j
public class CarsExceptionHandler {

    @ExceptionHandler(CarAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCarAlreadyExistsException(CarAlreadyExistsException e) {
        log.info(e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCarDoesNotExistException(NoSuchElementException e) {
        log.info(e.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }
}

