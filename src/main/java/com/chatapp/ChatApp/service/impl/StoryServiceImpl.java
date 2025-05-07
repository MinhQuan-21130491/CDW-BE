package com.chatapp.ChatApp.service.impl;

import com.chatapp.ChatApp.exception.InvalidCredentialsException;
import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.modal.Story;
import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.repository.StoryRepository;
import com.chatapp.ChatApp.repository.UserRepository;
import com.chatapp.ChatApp.request.StoryRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.StoryService;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class StoryServiceImpl implements StoryService {
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final UserService userService;
    @Override
    public Response addStoryToUser(StoryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + request.getUserId()));
        Story story = new Story();
        story.setUser(user);
        story.setUrl(request.getUrl());

        storyRepository.save(story);
        user.getStories().add(story);

        return Response.builder().status(200).message("Add story success").build();
    }

    @Override
    public Response deleteStoryFromUser(Integer id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Story not found with id: " + id));
        User userCurrent = userService.getLoginUser();
        User user = story.getUser();

        if (!userCurrent.equals(user)) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        storyRepository.delete(story);
        return Response.builder()
                .status(200)
                .message("Delete story success")
                .build();
    }
}
