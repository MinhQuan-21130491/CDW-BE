package com.chatapp.ChatApp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequest {
    private List<Integer> usersId;
    private String chatName;
    private String chat_image;
}
