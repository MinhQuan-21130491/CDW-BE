package com.chatapp.ChatApp.request;

import com.chatapp.ChatApp.modal.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageGroupRequest {
    private Integer chatId;
    private String type;
    private String content;
    private ArrayList<String> medias;

}
