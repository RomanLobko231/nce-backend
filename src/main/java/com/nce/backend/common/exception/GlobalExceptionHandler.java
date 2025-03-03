package com.nce.backend.common.exception;

import com.nce.backend.cars.ui.CarController;
import com.nce.backend.cars.ui.responses.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later."
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodFieldValidationException(MethodArgumentNotValidException e) {
        log.error("Validation error(s) occurred: {}", e.getMessage(), e);

        StringBuilder constraints = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            constraints.append(errorMessage).append("\n");
        });

        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error(s) occurred:\n".concat(constraints.toString())
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMAxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.info(e.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Max upload size of 10 MB exceeded. Try again with less data."
        );
    }
}
