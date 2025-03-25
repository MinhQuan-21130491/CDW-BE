package com.chatapp.ChatApp.repository;

import com.chatapp.ChatApp.modal.Chat;
import com.chatapp.ChatApp.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c JOIN c.userChats u WHERE u.user.id = :userId  and u.isDeleted = false")
    List<Chat> findChatsByUserId( @Param("userId") Integer userId);

    @Query("select c from Chat c join c.userChats uc1 join c.userChats uc2 " +
            "where c.isGroup = false and uc1.user = :user and uc2.user = :reqUser")
    Chat findSingleChatByUserIds(@Param("user") User user, @Param("reqUser") User reqUser);
}
