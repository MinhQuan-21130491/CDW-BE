package com.chatapp.ChatApp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDtoPayload {
    private Integer id;
    private String email;
    private String full_name;
    private String profile_picture;
}