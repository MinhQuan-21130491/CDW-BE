package com.chatapp.ChatApp.controller;

import com.chatapp.ChatApp.request.StoryRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stories")
public class StoryController {

    private final StoryService storyService;

    @PostMapping("/create")
    public ResponseEntity<Response> addStory(@RequestParam("userId") Integer userId,
                                               @RequestParam("type") String type,
                                               @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(storyService.addStoryToUser(userId, type, file ));

    }

    @DeleteMapping("/delete/{storyId}")
    public ResponseEntity<Response> deleteStory(@PathVariable Integer storyId) {

        return ResponseEntity.ok(storyService.deleteStoryFromUser(storyId));
    }

    @GetMapping("{userId}")
    public ResponseEntity<Response> getStories(@PathVariable Integer userId) {
        return ResponseEntity.ok(storyService.getStoriesByUser(userId));
    }
}
