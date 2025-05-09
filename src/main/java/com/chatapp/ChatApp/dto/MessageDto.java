package com.chatapp.ChatApp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDto {
    private Integer id;
    private String content;
    private String type;
    private List<MediaDto> medias;
    private LocalDateTime timestamp = LocalDateTime.now();
}
