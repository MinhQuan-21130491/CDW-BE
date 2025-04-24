package com.chatapp.ChatApp.mapper.intf;

import com.chatapp.ChatApp.dto.*;
import com.chatapp.ChatApp.modal.*;

public interface EntityDtoMapper {
    public UserDto mapUserToDtoBasic(User user);
    public ChatDto mapChatToDtoBasic(Chat chat);
    public UserChatDto mapUserChatToDtoBasic(UserChat userChat);
    public MediaDto mapMediaToDtoBasic(Media media);
    public MessageDto mapMessageToDtoBasic(Message message);
    public MessageDto mapMessageToDtoPlusMedias(Message message);
    public UserMessageDto mapUserMessageToDtoBasic(UserMessage userMessage);
    public ChatDto mapChatToDtoPlusUserChatDtoAndUserMessageDto(Chat chat);
}
