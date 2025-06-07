package com.nce.backend.auction.ui.rest;

import com.nce.backend.auction.exceptions.AuctionClosedException;
import com.nce.backend.auction.exceptions.AuctionDoesNotExist;
import com.nce.backend.auction.exceptions.AuctionIllegalStateException;
import com.nce.backend.auction.exceptions.InvalidBidException;
import com.nce.backend.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = AuctionController.class)
@ResponseBody
@Slf4j
public class AuctionsExceptionHandler {


    @ExceptionHandler(AuctionDoesNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAuctionDoesNotExist(AuctionDoesNotExist e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(AuctionClosedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAuctionClosedException(AuctionClosedException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(InvalidBidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidBidException(InvalidBidException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );
    }

    @ExceptionHandler(AuctionIllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAuctionIllegalStateException(AuctionIllegalStateException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }
}
