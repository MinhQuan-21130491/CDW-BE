package com.chatapp.ChatApp.service.impl;

import com.chatapp.ChatApp.dto.StoryDto;
import com.chatapp.ChatApp.exception.InvalidCredentialsException;
import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.mapper.intf.EntityDtoMapper;
import com.chatapp.ChatApp.modal.Story;
import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.repository.StoryRepository;
import com.chatapp.ChatApp.repository.UserRepository;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.s3.S3Service;
import com.chatapp.ChatApp.s3.S3ServicePartFile;
import com.chatapp.ChatApp.service.iterf.StoryService;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class StoryServiceImpl implements StoryService {
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final UserService userService;
    private final S3ServicePartFile s3ServicePartFile;
    private final EntityDtoMapper entityDtoMapper;


    @Override
    public Response addStoryToUser(Integer userId, String type, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        Story story = new Story();
        story.setUser(user);
        story.setType(type);
        if (file != null && !file.isEmpty()) {
            String fileName;
            if ("image".equals(type)) {
                fileName = "story/" + UUID.randomUUID() + ".jpg";
            } else if ("video".equals(type)) {
                fileName = "story/" + UUID.randomUUID() + ".mp4";
            } else {
                throw new IllegalArgumentException("Invalid type: " + type);
            }

            // Upload file (dưới dạng stream) lên S3
            String url = s3ServicePartFile.uploadFile(file, fileName);
            story.setUrl(url);
        }

        storyRepository.save(story);
        user.getStories().add(story);
        return Response.builder().status(200).message("success_add_story").build();
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
                .message("success_delete_story")
                .build();
    }

    @Override
    public Response getStoriesByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        List<Story> stories = storyRepository.findStoriesByUser(user);
        List<StoryDto> storiesDto = stories.stream()
                .map(entityDtoMapper::mapStoryToDtoBasic)
                .sorted((s1, s2) -> s2.getTimestamp().compareTo(s1.getTimestamp())) // sort theo thời gian giảm dần
                .toList();
        return Response.builder().message("Get stories by user success").status(200).stories(storiesDto).build();
    }
}
