package com.chatapp.ChatApp.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayloadBroadcast {
    private List<Integer> receiverIds;
    private String message;
    private Integer chatId;
    private Integer requestId;
}
