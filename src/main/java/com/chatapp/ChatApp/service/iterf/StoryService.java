package com.chatapp.ChatApp.service.iterf;

import com.chatapp.ChatApp.modal.Story;
import com.chatapp.ChatApp.request.StoryRequest;
import com.chatapp.ChatApp.response.Response;
import org.springframework.web.multipart.MultipartFile;

public interface StoryService {
    public Response addStoryToUser(Integer userId, String type, MultipartFile file);
    public Response deleteStoryFromUser(Integer id);
    public Response getStoriesByUser(Integer userId);
}
