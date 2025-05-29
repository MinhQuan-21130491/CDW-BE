package com.chatapp.ChatApp.controller;

import com.chatapp.ChatApp.request.MessageGroupRequest;
import com.chatapp.ChatApp.request.MessageRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.MessageService;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageControler {
    private final MessageService messageService;

    @PostMapping("/create")
    public ResponseEntity<Response> sendMessage(@RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(messageService.sendMessage(messageRequest));
    }
    @PostMapping("/create/group")
    public ResponseEntity<Response> sendMessageGroup(@RequestBody MessageGroupRequest messageGroupRequest) {
        return ResponseEntity.ok(messageService.sendMessageGroup(messageGroupRequest));
    }
}
