package com.nce.backend.logging.application.service;

import com.nce.backend.cars.domain.entities.Car;
import com.nce.backend.common.annotation.logging.LoggableAction;
import com.nce.backend.logging.application.port.Authenticator;
import com.nce.backend.logging.domain.LogEntry;
import com.nce.backend.logging.domain.LoggingDomainService;
import com.nce.backend.security.userauth.AuthenticatedUser;
import com.nce.backend.users.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingApplicationService {

    private final LoggingDomainService domainService;
    private final Authenticator authenticator;
    private static final SpelExpressionParser parser = new SpelExpressionParser();


    @Async("loggingTaskExecutor")
    public void logAction(JoinPoint joinPoint, LoggableAction loggableAction, Object result) {
        UUID userId = authenticator.getCurrentUserId();
        String username = authenticator.getCurrentUserName();
        UUID affectedId = resolveAffectedId(joinPoint, result, loggableAction.affectedIdParam());

        LogEntry entry = LogEntry.builder()
                .action(loggableAction.action())
                .userId(userId)
                .affectedId(affectedId)
                .username(username)
                .logTimestamp(Instant.now())
                .methodName(joinPoint.getSignature().getName())
                .build();

        domainService.save(entry);
    }

    private UUID resolveAffectedId(JoinPoint joinPoint, Object result, String spelExpression) {
        if (result instanceof AuthenticatedUser user) return user.getId();
        if (result instanceof User user) return user.getId();
        if (result instanceof Car car) return car.getId();
        return extractIdFromSpEL(joinPoint, spelExpression).orElse(null);
    }

    //SpEL evaluator/parser for nested fields
    private Optional<UUID> extractIdFromSpEL(JoinPoint joinPoint, String spelExpression) {
        if (spelExpression == null || spelExpression.isBlank()) return Optional.empty();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], parameterValues[i]);
        }

        try {
            Object value = parser.parseExpression(spelExpression).getValue(context);
            if (value instanceof UUID uuid) {
                return Optional.of(uuid);
            } else if (value instanceof String str) {
                return Optional.of(UUID.fromString(str));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to evaluate SpEL expression: " + spelExpression, e);
        }
    }
}
