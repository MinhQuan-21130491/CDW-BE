package com.chatapp.ChatApp.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    private String error;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

}
