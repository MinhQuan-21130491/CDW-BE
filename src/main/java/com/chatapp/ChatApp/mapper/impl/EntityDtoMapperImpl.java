package com.chatapp.ChatApp.mapper.impl;

import com.chatapp.ChatApp.dto.*;
import com.chatapp.ChatApp.mapper.intf.EntityDtoMapper;
import com.chatapp.ChatApp.modal.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityDtoMapperImpl implements EntityDtoMapper {
    @Override
    public UserDto mapUserToDtoBasic(User user) {
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFull_name(user.getFull_name());
        userDTO.setProfile_picture(user.getProfile_picture());
        return userDTO;
    }

    @Override
    public ChatDto mapChatToDtoBasic(Chat chat) {
        ChatDto chatDto = new ChatDto();
        chatDto.setId(chat.getId());
        chatDto.setChat_name(chat.getChat_name());
        chatDto.setChat_image(chat.getChat_image());
        chatDto.setGroup(chat.isGroup());
        chatDto.setCreatedBy(mapUserToDtoBasic(chat.getCreatedBy()));
        return chatDto;
    }

    @Override
    public UserChatDto mapUserChatToDtoBasic(UserChat userChat) {
        UserChatDto userChatDto = new UserChatDto();
        userChatDto.setId(userChat.getId());
        userChatDto.setAdmin(userChat.isAdmin());
        userChatDto.setDeleted(userChat.isDeleted());
        userChatDto.setUser(mapUserToDtoBasic(userChat.getUser()));
        return userChatDto;
    }

    @Override
    public MessageDto mapMessageToDtoBasic(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setTimestamp(message.getTimestamp());
        messageDto.setContent(message.getContent());
        return messageDto;
    }

    @Override
    public UserMessageDto mapUserMessageToDtoBasic(UserMessage userMessage) {
        UserMessageDto userMessageDto = new UserMessageDto();
        userMessageDto.setId(userMessage.getId());
        userMessageDto.setMessage(mapMessageToDtoBasic(userMessage.getMessage()));
        userMessageDto.setDeleted(userMessage.isDeleted());
        userMessageDto.setSenderUser(mapUserToDtoBasic(userMessage.getUser()));
        return userMessageDto;
    }

    @Override
    public ChatDto mapChatToDtoPlusUserChatDtoAndUserMessageDto(Chat chat) {
        ChatDto chatDto = mapChatToDtoBasic(chat);

        // Sao chép userChats
        Set<UserChat> userChats = new HashSet<>(chat.getUserChats());
        if (!userChats.isEmpty()) {
            chatDto.setUserChat(userChats.stream()
                    .map(this::mapUserChatToDtoBasic)
                    .collect(Collectors.toSet()));
        }

        // Sao chép userMessages
        List<UserMessage> userMessages = new ArrayList<>(chat.getUserMessages());
        if (!userMessages.isEmpty()) {
            chatDto.setUserMessages(userMessages.stream()
                    .map(this::mapUserMessageToDtoBasic)
                    .collect(Collectors.toList()));
        }

        return chatDto;
    }
}
