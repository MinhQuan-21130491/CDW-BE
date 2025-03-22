package com.chatapp.ChatApp.repository;

import com.chatapp.ChatApp.modal.Chat;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {


}
