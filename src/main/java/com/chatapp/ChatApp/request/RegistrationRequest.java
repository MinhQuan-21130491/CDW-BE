package com.chatapp.ChatApp.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @Valid
    @NotBlank(message = "error_email_blank")
    @Email(message = "error_invalid_email")
    private String email;
    @Size(min = 4,max=10, message = "error_length_name")
    private String full_name;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{6,}$",
            message = "error_password_partern"
    )
    private String password;
}
