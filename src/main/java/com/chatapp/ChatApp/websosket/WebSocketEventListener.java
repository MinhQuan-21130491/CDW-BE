package com.chatapp.ChatApp.websosket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

// WebSocketEventListener.java
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final PresenceService presenceService;
    private final RealtimeMessage realtimeMessage;
    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        String userId = extractUserId(event);
        System.out.println("Kết nối mới - userId: " + userId);
        if (userId != null) {
            // Lưu userId vào session attributes
            StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
            accessor.getSessionAttributes().put("userId", userId);

            presenceService.userConnected(Integer.parseInt(userId));
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        // Thử nhiều cách để lấy userId
        String userId = extractUserIdFromSession(event);
        if (userId == null) {
            userId = extractUserIdFromHeaders(event);
        }

        System.out.println("Ngắt kết nối - sessionId: " + event.getSessionId() + ", userId: " + userId);

        if (userId != null) {
            presenceService.userDisconnected(Integer.parseInt(userId));
            realtimeMessage.sendInitialOnlineUsers();
        }
    }
    private String extractUserId(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        return accessor.getFirstNativeHeader("userId");
    }
    private String extractUserIdFromSession(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getSessionAttributes() != null) {
            return (String) accessor.getSessionAttributes().get("userId");
        }
        return null;
    }

    private String extractUserIdFromHeaders(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        return accessor.getFirstNativeHeader("userId");
    }
}