package com.chatapp.ChatApp.service.iterf;

import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.request.GroupChatRequest;
import com.chatapp.ChatApp.response.Response;

public interface ChatService {
    public Response createChat(User reqUser, Integer userId);
    public Response findChatById(Integer id);
    public Response findAllChatByUserId(Integer userId);
    public Response createGroupChat(User reqUser, GroupChatRequest groupChatRequest);
    public Response addUserToGroup(User idUser, Integer idUserToAdd, Integer idChat);
    public Response renameGroup(Integer idChat, User reqUser, String newName);
    public Response removeUserFromGroup(Integer idUserRemove, Integer idChat, User reqUser);
    public Response removeChat(Integer idChat, User reqUser);
}
