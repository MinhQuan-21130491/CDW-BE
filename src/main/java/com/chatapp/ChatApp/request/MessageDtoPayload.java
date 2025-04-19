package com.chatapp.ChatApp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MessageDtoPayload {
    private Integer id;
    private String content;
    private String type;
    private LocalDateTime timestamp;
}