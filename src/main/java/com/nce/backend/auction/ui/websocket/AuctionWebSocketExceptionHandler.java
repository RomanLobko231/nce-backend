package com.nce.backend.auction.ui.websocket;

import com.nce.backend.auction.exceptions.AuctionClosedException;
import com.nce.backend.auction.exceptions.InvalidBidException;
import com.nce.backend.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = AuctionWebSocketController.class)
@Slf4j
public class AuctionWebSocketExceptionHandler {

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAuctionClosedException(AuctionClosedException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidBidException(InvalidBidException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                e.getMessage()
        );
    }
}
