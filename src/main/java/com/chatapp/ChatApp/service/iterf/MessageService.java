package com.chatapp.ChatApp.service.iterf;

import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.request.MessageRequest;
import com.chatapp.ChatApp.response.Response;

public interface MessageService {
    public Response sendMessage(MessageRequest messageRequest);
    public Response removeMessageById(Integer messageId);
}
