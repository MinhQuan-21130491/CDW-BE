package com.chatapp.ChatApp.modal;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonBackReference
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    @JsonBackReference
    private Message message;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted = false;
}
