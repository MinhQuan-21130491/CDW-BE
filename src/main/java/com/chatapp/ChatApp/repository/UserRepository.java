package com.chatapp.ChatApp.repository;

import com.chatapp.ChatApp.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {

}
