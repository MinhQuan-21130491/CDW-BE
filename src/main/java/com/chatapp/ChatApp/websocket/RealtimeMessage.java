package com.chatapp.ChatApp.websocket;

import com.chatapp.ChatApp.request.PayloadBroadcast;
import com.chatapp.ChatApp.request.PayloadRenameGroup;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class RealtimeMessage {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PresenceService presenceService;

    @MessageMapping("/message")
    public void handleIncomingMessage(@Payload com.chatapp.ChatApp.request.Payload payloadMessage) {
            simpMessagingTemplate.convertAndSend("/group/" + payloadMessage.getChatId(), payloadMessage);
            for(Integer id: payloadMessage.getReceiverIds()) {
            simpMessagingTemplate.convertAndSend("/topic/message/" + id, "New message");
        }
    }

    @MessageMapping("/notification")
    public void handleGroupRenameNotification(@Payload PayloadRenameGroup payloadMessage) {
        simpMessagingTemplate.convertAndSend("/topic/change", payloadMessage);
    }

    @MessageMapping("/broadcast-notification")
    public void handleBroadcastNotification(@Payload PayloadBroadcast payloadBroadcast) {
        for(Integer id: payloadBroadcast.getReceiverIds()) {
            simpMessagingTemplate.convertAndSend("/topic/notification/" + id, payloadBroadcast);
        }
    }

    @MessageMapping("/init-online-users")
    public void sendInitialOnlineUsers() {
        simpMessagingTemplate.convertAndSend("/topic/online-users", presenceService.getOnlineUsers());
    }

}
