    package com.chatapp.ChatApp.modal;

    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDateTime;
    import java.util.Objects;

    @Entity
    @Table(name = "user_chats")
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public class UserChat {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @ManyToOne
        @JoinColumn(name = "chat_id")
        @JsonBackReference
        private Chat chat;

        @ManyToOne
        @JoinColumn(name = "user_id")
        @JsonBackReference
        private User user;

        @Column(name = "is_deleted", columnDefinition = "boolean default false")
        private boolean isDeleted = false;

        @Column(name = "is_admin", columnDefinition = "boolean default false")
        private boolean isAdmin = false;

        @Column(name = "joined_at")
        private final LocalDateTime joinedAt = LocalDateTime.now();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserChat userChat = (UserChat) o;
            return id.equals(userChat.id) &&
                    user.equals(userChat.user) &&
                    chat.equals(userChat.chat);
        }
        @Override
        public int hashCode() {
            return Objects.hash(id, user, chat, isDeleted, isAdmin);
        }

    }
