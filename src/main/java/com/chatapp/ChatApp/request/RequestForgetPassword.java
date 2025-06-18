package com.chatapp.ChatApp.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestForgetPassword {
    @Email(message = "error_invalid_email")
    private String email;
}
