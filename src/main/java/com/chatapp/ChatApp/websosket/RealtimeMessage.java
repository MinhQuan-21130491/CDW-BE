package com.chatapp.ChatApp.websosket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class RealtimeMessage {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RealtimeMessage(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/group/public")
    public void messageRecei(@Payload com.chatapp.ChatApp.request.Payload payloadMessage) {
        simpMessagingTemplate.convertAndSend("/group/" + payloadMessage.getChatId(), payloadMessage);
        simpMessagingTemplate.convertAndSend("/topic/notification/", "New message");
    }

}
