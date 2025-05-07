package com.chatapp.ChatApp.service.impl;

import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.modal.*;
import com.chatapp.ChatApp.repository.ChatRepository;
import com.chatapp.ChatApp.repository.MessageRepository;
import com.chatapp.ChatApp.request.MessageGroupRequest;
import com.chatapp.ChatApp.request.MessageRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.s3.S3Service;
import com.chatapp.ChatApp.service.iterf.ChatService;
import com.chatapp.ChatApp.service.iterf.MessageService;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final S3Service s3Service;

    @Override
    public Response sendMessageGroup(MessageGroupRequest messageRequest) {
        User reqUser = userService.getLoginUser();
        Chat chat = new Chat();
        // xu ly cho nhan tin trong nhom (khong can receiverId)
        chat = chatRepository.findById(messageRequest.getChatId()).orElseThrow(() -> new NotFoundException("Chat not found"));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setType(messageRequest.getType());
        message.setSender(reqUser);

        ArrayList<Media> medias = new ArrayList<>();
        for(String url: messageRequest.getMedias()){
            Media media = new Media();
            if (url != null) {
                // Tạo tên file duy nhất
                String fileName = "pictures/" + UUID.randomUUID() + ".jpg";

                // Upload base64 -> S3 và lấy URL
                String imageUrl = s3Service.uploadBase64Media(url, fileName);

                // Lưu URL
                media.setUrl(imageUrl);
            }
            media.setMessage(message);
            medias.add(media);
        }
        message.setMedias(medias);

        UserMessage userMessage = new UserMessage();
        userMessage.setChat(chat);
        userMessage.setMessage(message);
        userMessage.setUser(reqUser);
        message.getUserMessages().add(userMessage);
        messageRepository.save(message);
        return Response.builder().status(200).message("Send message in group successfully").build();
    }

    @Override
    public Response sendMessage(MessageRequest messageRequest) {
        User reqUser = userService.getLoginUser();
        Chat chat = new Chat();
        // xu ly cho nhan tin rieng
        chat = chatService.createChat(reqUser, messageRequest.getReceiverId()).getChatEntity();

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setType(messageRequest.getType());
        message.setSender(reqUser);
        UserMessage userMessage = new UserMessage();

        ArrayList<Media> medias = new ArrayList<>();
        for(String url: messageRequest.getMedias()){
            Media media = new Media();
            if (url != null) {
                // Tạo tên file duy nhất
                String fileName = "pictures/" + UUID.randomUUID() + ".jpg";

                // Upload base64 -> S3 và lấy URL
                String imageUrl = s3Service.uploadBase64Media(url, fileName);

                // Lưu URL
                media.setUrl(imageUrl);
            }
            media.setMessage(message);
            medias.add(media);
        }
        message.setMedias(medias);
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
