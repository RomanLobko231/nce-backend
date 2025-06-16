package com.nce.backend.logging.application.service;

import com.nce.backend.common.annotation.logging.LoggableAction;
import com.nce.backend.logging.application.port.Authenticator;
import com.nce.backend.logging.domain.LogEntry;
import com.nce.backend.security.userauth.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final LoggingApplicationService loggingApplicationService;

    @AfterReturning(
            pointcut = "@annotation(loggableAction)",
            returning = "result"
    )
    public void logAction(JoinPoint joinPoint, LoggableAction loggableAction, Object result) {
        loggingApplicationService.logAction(joinPoint, loggableAction, result);
    }
}
