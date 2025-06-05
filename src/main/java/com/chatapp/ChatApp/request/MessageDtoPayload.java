package com.chatapp.ChatApp.request;

import com.chatapp.ChatApp.dto.MediaDto;
import com.chatapp.ChatApp.modal.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MessageDtoPayload {
    private Integer id;
    private String content;
    private String type;
    private LocalDateTime timestamp;
    private List<MediaDto> medias;
}