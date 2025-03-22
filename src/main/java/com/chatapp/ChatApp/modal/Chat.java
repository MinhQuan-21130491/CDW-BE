package com.chatapp.ChatApp.modal;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String chat_name;

    private String chat_image;

    @Column(name = "is_group")
    private boolean isGroup;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "chat",  cascade = CascadeType.ALL)
    @JsonManagedReference // Đánh dấu phía cha (Chat) sẽ tuần tự hóa mối quan hệ này
    private Set<UserChat> userChats = new HashSet<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    @JsonManagedReference // Đánh dấu phía cha (Chat) sẽ tuần tự hóa mối quan hệ này
    private List<UserMessage> userMessages = new ArrayList<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, chat_name, chat_image, isGroup, createdBy);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat1 = (Chat) o;
        return id.equals(chat1.id) &&
                chat_name.equals(chat1.chat_name) &&
                chat_image.equals(chat1.chat_image);
    }


}
