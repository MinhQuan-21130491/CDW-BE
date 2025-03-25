package com.chatapp.ChatApp.service.impl;

import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.mapper.impl.EntityDtoMapperImpl;
import com.chatapp.ChatApp.mapper.intf.EntityDtoMapper;
import com.chatapp.ChatApp.modal.Chat;
import com.chatapp.ChatApp.modal.Message;
import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.modal.UserMessage;
import com.chatapp.ChatApp.repository.ChatRepository;
import com.chatapp.ChatApp.repository.MessageRepository;
import com.chatapp.ChatApp.request.MessageRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.ChatService;
import com.chatapp.ChatApp.service.iterf.MessageService;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImp implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    @Override
    public Response sendMessage(MessageRequest messageRequest) {
        User reqUser = userService.getLoginUser();
        Chat chat = new Chat();
        // xu ly cho nhan tin rieng
        if(messageRequest.getReceiverId() != null) {
            chat = chatService.createChat(reqUser, messageRequest.getReceiverId()).getChatEntity();
        }else {
            // xu ly cho nhan tin trong nhom (khong can receiverId)
            chat = chatRepository.findById(messageRequest.getChatId()).orElseThrow(() -> new NotFoundException("Chat not found"));
        }
        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setSender(reqUser);
        UserMessage userMessage = new UserMessage();
        userMessage.setChat(chat);
        userMessage.setMessage(message);
        userMessage.setUser(reqUser);
        message.getUserMessages().add(userMessage);
        messageRepository.save(message);
        return Response.builder().status(200).message("Send message successfully").build();
    }

    @Override
    public Response removeMessageById(Integer messageId) {
        return null;
    }
}
