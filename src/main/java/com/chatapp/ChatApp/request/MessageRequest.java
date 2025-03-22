package com.chatapp.ChatApp.request;

import com.chatapp.ChatApp.modal.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private Integer chatId;
    private String content;
    private Integer receiverId = null;
}
