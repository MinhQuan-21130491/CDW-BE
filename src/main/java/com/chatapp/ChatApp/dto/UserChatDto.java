package com.chatapp.ChatApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserChatDto {
    private Integer id;
    private boolean isDeleted;
    private boolean isAdmin;
    private UserDto user;
    private LocalDateTime joinedAt = LocalDateTime.now();
}
