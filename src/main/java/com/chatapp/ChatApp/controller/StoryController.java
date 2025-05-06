package com.chatapp.ChatApp.controller;

import com.chatapp.ChatApp.request.StoryRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping("/add-story")
    public ResponseEntity<Response> addStory(@RequestBody StoryRequest request) {
        return ResponseEntity.ok(storyService.addStoryToUser(request));
    }

    @DeleteMapping("/delete/{storyId}")
    public ResponseEntity<Response> deleteStory(@PathVariable Integer storyId) {
        return ResponseEntity.ok(storyService.deleteStoryFromUser(storyId));
    }
}
