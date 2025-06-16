package com.nce.backend.logging;

import com.nce.backend.common.annotation.logging.LoggableAction;
import com.nce.backend.logging.application.port.Authenticator;
import com.nce.backend.logging.application.service.LoggingApplicationService;
import com.nce.backend.logging.domain.LogEntry;
import com.nce.backend.logging.domain.LoggingDomainService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoggingApplicationServiceTest {

    @Mock
    JoinPoint joinpoint;

    @Mock
    LoggableAction loggableAction;

    @Mock
    Authenticator authenticator;

    @Mock
    MethodSignature methodSignature;

    @Mock
    LoggingDomainService domainService;

    @InjectMocks
    LoggingApplicationService loggingApplicationService;


    @Test
    @Transactional
    void givenLoggableAnnotation_whenMethodCalled_saveLog(){
        UUID userId = UUID.randomUUID();
        UUID affectedId = UUID.randomUUID();
        String username = "user@email.com";
        String affectedIdParam = "#userId";
        String methodName = "deleteUserById";
        String action = "DELETE_USER";
        String[] parameterNames = {"userId", "otherId", "otherParameter"};
        Object[] parameterValues = {affectedId, UUID.randomUUID(), "otherParameter"};

        when(loggableAction.action()).thenReturn(action);
        when(loggableAction.affectedIdParam()).thenReturn(affectedIdParam);

        when(authenticator.getCurrentUserId()).thenReturn(userId);
        when(authenticator.getCurrentUserName()).thenReturn(username);

        when(methodSignature.getParameterNames()).thenReturn(parameterNames);
        when(methodSignature.getName()).thenReturn(methodName);
        when(joinpoint.getSignature()).thenReturn(methodSignature);
        when(joinpoint.getArgs()).thenReturn(parameterValues);

        loggingApplicationService.logAction(joinpoint, loggableAction, null);

        ArgumentCaptor<LogEntry> captor = ArgumentCaptor.forClass(LogEntry.class);
        verify(domainService).save(captor.capture());

        LogEntry logEntry = captor.getValue();
        assertEquals(action, logEntry.getAction());
        assertEquals(userId, logEntry.getUserId());
        assertEquals(affectedId, logEntry.getAffectedId());
        assertEquals(username, logEntry.getUsername());
        assertEquals(methodName, logEntry.getMethodName());
        assertNotNull(logEntry.getLogTimestamp());

    }
}
