package com.chatapp.ChatApp.service.iterf;

import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.request.GroupChatRequest;
import com.chatapp.ChatApp.response.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ChatService {
    public Response createChat(User reqUser, Integer userId);
    public Response findChatById(Integer id);
    public Response findAllChatByUserId(Integer userId);
    public Response findSingleChatByUserId(Integer userId);
    public Response createGroupChat(User reqUser, GroupChatRequest groupChatRequest);
    public Response addUserToGroup(Integer idUserToAdd, Integer idChat);
    public Response editGroup(Integer idChat, User reqUser, String newName, MultipartFile file);
    public Response removeUserFromGroup(Integer idUserRemove, Integer idChat, User reqUser);
    public Response removeChat(Integer idChat, User reqUser);
}
