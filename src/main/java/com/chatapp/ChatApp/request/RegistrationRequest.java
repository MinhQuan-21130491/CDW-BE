package com.chatapp.ChatApp.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @Valid
    @NotBlank(message = "Email not Blank")
    @Email(message = "Invalid email")
    private String email;
    private String full_name;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{6,}$",
            message = "Password must be at least 6 characters, contain uppercase, lowercase, numbers and special characters"
    )
    private String password;
}
