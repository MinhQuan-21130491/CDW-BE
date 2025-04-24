package com.chatapp.ChatApp.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private boolean deleted;
    private int id;
    private int chatId;
    private MessageDtoPayload message;
    private UserDtoPayload senderUser;
    private boolean showAvatar;
}
