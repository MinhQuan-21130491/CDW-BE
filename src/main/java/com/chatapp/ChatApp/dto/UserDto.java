package com.chatapp.ChatApp.dto;

import com.chatapp.ChatApp.modal.Story;
import com.chatapp.ChatApp.modal.UserMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Integer id;
    private String email;
    private String full_name;
    private String profile_picture;
    private List<StoryDto> stories = new ArrayList<>();
}
