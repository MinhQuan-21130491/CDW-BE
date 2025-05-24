package com.chatapp.ChatApp.service.impl;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Data
public class PresenceService {
    private final Set<Integer> onlineUsers = ConcurrentHashMap.newKeySet();

    public void userConnected(Integer userId) {
        onlineUsers.add(userId);
        System.out.println(onlineUsers);
    }

    public void userDisconnected(Integer userId) {
        onlineUsers.remove(userId);
        System.out.println(onlineUsers);
    }


}