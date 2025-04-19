package com.chatapp.ChatApp.controller;

import com.chatapp.ChatApp.request.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RealtimeMessage {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public RealtimeMessage(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message")
    @SendTo("/group/public")
    public void messageRecei(@org.springframework.messaging.handler.annotation.Payload Payload payloadMessage) {
//        System.out.println("payloadMessage" +payloadMessage);
        simpMessagingTemplate.convertAndSend("/group/" + payloadMessage.getChatId(), payloadMessage);
    }

}
