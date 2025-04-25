package com.chatapp.ChatApp.service.iterf;

import com.chatapp.ChatApp.modal.Story;
import com.chatapp.ChatApp.request.StoryRequest;
import com.chatapp.ChatApp.response.Response;

public interface StoryService {
    public Response addStoryToUser(StoryRequest request);
    public Response deleteStoryFromUser(Integer id);
}
