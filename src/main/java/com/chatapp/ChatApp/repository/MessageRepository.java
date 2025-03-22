package com.chatapp.ChatApp.repository;

import com.chatapp.ChatApp.modal.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

}
