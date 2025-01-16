package com.nce.backend.cars.ui;

import com.nce.backend.cars.exceptions.CarAlreadyExistsException;
import com.nce.backend.cars.ui.responses.ErrorResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@ResponseBody
public class CarsExceptionHandler {

    private static final Logger logger = LogManager.getLogger(CarsExceptionHandler.class);

    @ExceptionHandler(CarAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCarAlreadyExistsException(CarAlreadyExistsException e) {
        logger.info(e.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }
}

