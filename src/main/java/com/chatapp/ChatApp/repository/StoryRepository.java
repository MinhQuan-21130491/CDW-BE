package com.chatapp.ChatApp.repository;

import com.chatapp.ChatApp.modal.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryRepository extends JpaRepository<Story, Integer> {
}
