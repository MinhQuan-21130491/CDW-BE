package com.chatapp.ChatApp.websosket;

import com.chatapp.ChatApp.config.CustomUserDetailService;
import com.chatapp.ChatApp.config.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    private final TokenProvider tokenProvider;
    private final CustomUserDetailService  userDetailService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
//                .addInterceptors(new WebSocketAuthenticationInterceptor(tokenProvider, userDetailService))
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS(); // Nếu dùng SockJS
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/group", "/user", "/topic");
        registry.setUserDestinationPrefix("/user");
    }


}
