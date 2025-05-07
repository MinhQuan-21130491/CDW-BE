package com.chatapp.ChatApp.request;

import com.chatapp.ChatApp.dto.UserDto;
import com.chatapp.ChatApp.modal.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoryRequest {
    private Integer id;
    private String url;
    private Integer userId;
    private String type;
}
