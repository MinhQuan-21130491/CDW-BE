package com.chatapp.ChatApp.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadRenameGroup {
    private String name_request;
    private Integer chatId;
    private String chat_image;
    private String chat_name;
}
