package com.chatapp.ChatApp.repository;

import com.chatapp.ChatApp.modal.Story;
import com.chatapp.ChatApp.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<Story, Integer> {

    List<Story> findStoriesByUser(User user);
}
