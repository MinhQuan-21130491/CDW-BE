package com.chatapp.ChatApp.response;

import com.chatapp.ChatApp.dto.ChatDto;
import com.chatapp.ChatApp.dto.StoryDto;
import com.chatapp.ChatApp.dto.UserDto;
import com.chatapp.ChatApp.modal.Chat;
import com.chatapp.ChatApp.modal.Story;
import com.chatapp.ChatApp.modal.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int status;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    private String token;
    private String expirationTime;

    private User user;
    private UserDto userDto;
    private List<User> users;
    private List<UserDto> usersDto;
    
    private Chat chatEntity;
    private ChatDto chat;
    private List<ChatDto> chats;

    private Story story;
    private StoryDto storyDto;
    private List<StoryDto> stories;
}
