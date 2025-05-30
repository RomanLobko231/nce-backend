package com.nce.backend.auction.ui.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws-auction")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(25000);

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .enableSimpleBroker("/topic", "/queue")
                .setHeartbeatValue(new long[] {10000, 10000})
                .setTaskScheduler(taskScheduler());

        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");

    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        scheduler.setThreadNamePrefix("websocket-heartbeat-");
        return scheduler;
    }
}
