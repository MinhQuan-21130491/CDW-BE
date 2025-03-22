package com.chatapp.ChatApp.dto;

import com.chatapp.ChatApp.modal.UserMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatDto {
    private Integer id;
    private String chat_name;
    private String chat_image;
    private boolean isGroup;
    private UserDto createdBy;
    private Set<UserChatDto> userChat;
    private List<UserMessageDto> userMessages;
}
