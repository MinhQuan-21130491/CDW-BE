package com.chatapp.ChatApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMessageDto {
    private Integer id;
    private MessageDto message;
    private UserDto senderUser;
    private boolean isDeleted;
}
