package com.chatapp.ChatApp.service.impl;

import com.chatapp.ChatApp.dto.ChatDto;
import com.chatapp.ChatApp.exception.InvalidCredentialsException;
import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.mapper.impl.EntityDtoMapperImpl;
import com.chatapp.ChatApp.mapper.intf.EntityDtoMapper;
import com.chatapp.ChatApp.modal.Chat;
import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.modal.UserChat;
import com.chatapp.ChatApp.modal.UserMessage;
import com.chatapp.ChatApp.repository.ChatRepository;
import com.chatapp.ChatApp.request.GroupChatRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.s3.S3Service;
import com.chatapp.ChatApp.service.iterf.ChatService;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;
    private final S3Service s3Service;

    @Override
    public Response createChat(User reqUser, Integer userId) {
        User receiUser = userService.findUserById(userId).getUser();
        Chat chatResult = new Chat();
        String message = "Create chat room successfully";
        Chat isChatExist = chatRepository.findSingleChatByUserIds(receiUser, reqUser);
        Chat isChatExistRecei = chatRepository.findSingleChatByUserIds(reqUser, receiUser);

        if(isChatExist != null) {
            chatResult = isChatExist;
            message = "Chat existed";
        }else if(isChatExistRecei != null) {
            isChatExistRecei.getUserChats().forEach(userChat -> {
                userChat.setDeleted(false);

            });
            chatResult = isChatExistRecei;
            message = "ReCreate chat existed";
        }else {
            //model userchat
            UserChat usc1 = new UserChat();
            usc1.setChat(chatResult);
            usc1.setUser(reqUser);
            //model userchat
            UserChat usc2 = new UserChat();
            usc2.setChat(chatResult);
            usc2.setUser(receiUser);
            Set<UserChat> uss = new HashSet<>();
            uss.add(usc1);
            uss.add(usc2);
            // set model chat
            chatResult.setCreatedBy(reqUser);
            chatResult.setUserChats(uss);
            chatResult.setGroup(false);

            chatRepository.save(chatResult);
        }
        return Response.builder().status(200).chatEntity(chatResult).message(message).build();
    }

    @Override
    public Response findChatById(Integer idChat) {
        Chat chat = chatRepository.findById(idChat)
                .orElseThrow(() -> new NotFoundException("Chat with ID " + idChat + " not found"));
        ChatDto chatDto = entityDtoMapper.mapChatToDtoPlusUserChatDtoAndUserMessageDto(chat);
        return Response.builder().status(200).chat(chatDto).build();
    }
    @Override
    public Response findAllChatByUserId(Integer userId) {
        User user = userService.findUserById(userId).getUser();
        List<Chat> chats = chatRepository.findChatsByUserId(user.getId());

        List<ChatDto> chatDtos = chats.stream()
                .map(entityDtoMapper::mapChatToDtoPlusUserChatDtoAndUserMessageDto)
                .sorted((chat1, chat2) -> {
                    // Dùng Optional để tránh lỗi null
                    LocalDateTime lastMessage1 = Optional.ofNullable(chat1.getUserMessages())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(userMsg -> userMsg.getMessage().getTimestamp())
                            .max(LocalDateTime::compareTo)
                            .orElse(LocalDateTime.MIN);

                    LocalDateTime lastMessage2 = Optional.ofNullable(chat2.getUserMessages())
                            .orElse(Collections.emptyList())
                            .stream()
                            .map(userMsg -> userMsg.getMessage().getTimestamp())
                            .max(LocalDateTime::compareTo)
                            .orElse(LocalDateTime.MIN);

                    return lastMessage2.compareTo(lastMessage1); // sắp xếp theo thời gian giảm dần
                })
                .collect(Collectors.toList());

        return Response.builder().status(200).chats(chatDtos).build();
    }


    @Override
    public Response findSingleChatByUserId(Integer userId) {
        // Tìm người nhận và người yêu cầu
        User userRecei = userService.findUserById(userId).getUser();
        User userReq = userService.getLoginUser();

        // Kiểm tra nếu người dùng không tìm thấy hoặc không hợp lệ
        if (userRecei == null || userReq == null) {
            return Response.builder().status(400).message("User not found").build();
        }

        // Tìm cuộc trò chuyện giữa 2 người dùng
        Chat chat = chatRepository.findSingleChatByUserIds(userRecei, userReq);
        // Nếu không có chat nào, trả về thông báo lỗi
        if (chat == null) {
            return Response.builder().status(404).message("No chat found").build();
        }
        UserChat userChat = chat.getUserChats().stream()
                .filter(userChat1 -> userChat1.getUser().getId().equals(userReq.getId()))
                .findFirst()
                .orElse(null);
        if (userChat != null && userChat.getDeleteLastAt() != null) {
            List<UserMessage> userMessages = chat.getUserMessages().stream()
                    .filter(userMessage -> userMessage.getMessage().getTimestamp().isAfter(userChat.getDeleteLastAt()))
                    .collect(Collectors.toList());
            chat.setUserMessages(userMessages);
        }

        // Chuyển đổi chat thành DTO
        ChatDto chatDto = entityDtoMapper.mapChatToDtoPlusUserChatDtoAndUserMessageDto(chat);

        // Trả về response
        return Response.builder().status(200).chat(chatDto).build();
    }
    @Override
    public Response createGroupChat(User reqUser, GroupChatRequest groupChatRequest) {
        Chat groupChat = new Chat();
        groupChat.setCreatedBy(reqUser);
        groupChat.setGroup(true);
        groupChat.setChat_name(groupChatRequest.getChat_name());

        if(groupChatRequest.getChat_image() != null) {
            // Tạo tên file duy nhất
            String fileName = "group_image/" + UUID.randomUUID() + ".jpg";
            System.out.println(groupChatRequest.getChat_image());
            // Upload base64 -> S3 và lấy URL
            String imageUrl = s3Service.uploadBase64Media(groupChatRequest.getChat_image(), fileName);

            // Lưu URL
            groupChat.setChat_image(imageUrl);
        }
        UserChat usc = new UserChat();
        usc.setChat(groupChat);
        usc.setUser(reqUser);
        usc.setAdmin(true);
        groupChat.getUserChats().add(usc);
        for(Integer userId: groupChatRequest.getUsersId()) {
            User user = userService.findUserById(userId).getUser();
            UserChat uscInGroup = new UserChat();
            uscInGroup.setChat(groupChat);
            uscInGroup.setUser(user);
            uscInGroup.setAdmin(false);
            groupChat.getUserChats().add(uscInGroup);
        }
        chatRepository.save(groupChat);
        ChatDto groupChatDto = entityDtoMapper.mapChatToDtoPlusUserChatDtoAndUserMessageDto(groupChat);
        return Response.builder().status(200).message("Create group chat successfully").chat(groupChatDto).build();
    }

    @Override
    public Response addUserToGroup(Integer idUserToAdd, Integer idChat) {
        Chat chat = chatRepository.findById(idChat).orElseThrow(() -> new NotFoundException("Not found chat with id " + idChat));
        User userToAdd = userService.findUserById(idUserToAdd).getUser();

        // Kiểm tra user đã có trong group chưa
        boolean userExists = chat.getUserChats().stream()
                .anyMatch(uc -> uc.getUser().getId().equals(userToAdd.getId()));

        if (userExists) {
            return Response.builder()
                    .status(400)
                    .message("User already in group")
                    .build();
        }
        UserChat usc = new UserChat();
        usc.setChat(chat);
        usc.setUser(userToAdd);
        usc.setAdmin(false);
        chat.getUserChats().add(usc);
        chatRepository.save(chat);
        return Response.builder().status(200).message("Add user to group success").build();
    }

    @Override
    public Response renameGroup(Integer idChat, User user, String newName) {
        Chat chat = chatRepository.findById(idChat).orElseThrow(() -> new NotFoundException("Not found chat with id " + idChat));
        if(isContainUser(chat.getUserChats(), user)) {
            chat.setChat_name(newName);
            chatRepository.save(chat);
            return Response.builder().status(200).message("Rename group successfully").build();
        }
        throw new InvalidCredentialsException("You are not member of this group");
    }

    @Override
    public Response removeUserFromGroup(Integer idUserRemove, Integer idChat, User reqUser) {
        User user = userService.findUserById(idUserRemove).getUser();
        Optional<Chat> optChat = chatRepository.findById(idChat);
        if(optChat.isPresent()){
            Chat chat = optChat.get();
            if(isAdmin(chat.getUserChats(), reqUser)) {
                for (UserChat userChat : chat.getUserChats()) {
                    if(userChat.getUser().equals(user)) {
                        chat.getUserChats().remove(userChat);
                        break;
                    }
                }
                chatRepository.save(chat);
                return Response.builder().status(200).message("Remove user in group successfully").build();
            }
            else if (isContainUser(chat.getUserChats(), reqUser)) {
                if(user.getId().equals(reqUser.getId())) {
                    for (UserChat userChat : chat.getUserChats()) {
                        if (userChat.getUser().equals(user)) {
                            chat.getUserChats().remove(userChat);
                            break;
                        }
                    }
                    chatRepository.save(chat);
                    return Response.builder().status(200).message("Out group successfully").build();
                }
            }else {
                throw new InvalidCredentialsException("You can't remove user from this group");
            }
        }
        throw new NotFoundException("Not found chat with id " + idChat);
    }

    @Override
    public Response removeChat(Integer idChat, User reqUser) {
        Chat chat = chatRepository.findById(idChat).orElseThrow(() -> new NotFoundException("Chat with ID " + idChat + " not found"));
        for(UserChat userChat: chat.getUserChats()) {
            if(userChat.getUser().equals(reqUser)) {
                userChat.setDeleted(true);
                userChat.setDeleteLastAt(LocalDateTime.now());
                break;
            }
        }
        for(UserMessage userMessage: chat.getUserMessages()) {
            if(userMessage.getUser().equals(reqUser) && userMessage.getChat().equals(chat)) {
                userMessage.setDeleted(true);
            }
        }
        chatRepository.save(chat);
        return Response.builder().status(200).message("Remove chat successfully").build();
    }
    private boolean isAdmin(Set<UserChat> userChats, User user) {
        for(UserChat userChat: userChats) {
            if(userChat.getUser().equals(user)) {
                if(userChat.isAdmin()) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isContainUser(Set<UserChat> userChats, User user) {
        for(UserChat userChat: userChats) {
            if(userChat.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

}
