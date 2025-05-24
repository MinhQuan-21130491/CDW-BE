package com.chatapp.ChatApp.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadBroadcast {
    private Integer id;
    private String message;

}
