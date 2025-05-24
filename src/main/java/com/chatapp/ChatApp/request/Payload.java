package com.chatapp.ChatApp.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payload {
    private boolean deleted;
    private int chatId;
    private MessageDtoPayload message;
    private UserDtoPayload senderUser;
    private List<Integer> receiverIds;
    private boolean showAvatar;

}
