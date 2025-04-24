package com.chatapp.ChatApp.modal;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("message-media")
    private List<Media> medias;

    private String type = "text";

    private final LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference ("message-userMessages") // Đánh dấu phía cha (message) sẽ tuần tự hóa mối quan hệ này
    private List<UserMessage> userMessages = new ArrayList<>();
}
