package com.chatapp.ChatApp.controller;

import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.request.GroupChatRequest;
import com.chatapp.ChatApp.request.SingleChatRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.ChatService;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final UserService userService;
    private final ChatService chatService;
    @PostMapping("/create-chat")
    public ResponseEntity<Response> createChat(@RequestBody SingleChatRequest singleChatRequest ) {
        User user = userService.getLoginUser();
        return ResponseEntity.ok(chatService.createChat(user, singleChatRequest.getId()));
    }
    @GetMapping("/find/{chatId}")
    public ResponseEntity<Response> getChatById(@PathVariable Integer chatId) {
        return ResponseEntity.ok(chatService.findChatById(chatId));
    }
    @GetMapping("/find-single")
    public ResponseEntity<Response> getChatSingle(@RequestParam("idUser") Integer idUser ) {
        return ResponseEntity.ok(chatService.findSingleChatByUserId(idUser));
    }
    @GetMapping("/find-all/{userId}")
    public ResponseEntity<Response> getChatAll(@PathVariable Integer userId) {
        return ResponseEntity.ok(chatService.findAllChatByUserId(userId));
    }
    @PostMapping("/create-chat/group")
    public ResponseEntity<Response> createGroupChat(@RequestBody GroupChatRequest groupChatRequest) {
        User user = userService.getLoginUser();
        return ResponseEntity.ok(chatService.createGroupChat(user, groupChatRequest));
    }
    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Response> addUserToGroup(@PathVariable Integer chatId, @PathVariable Integer userId) {
        return ResponseEntity.ok(chatService.addUserToGroup(userId, chatId));
    }
    @PostMapping("/edit-group")
    public ResponseEntity<Response> editGroup(@RequestParam("chatId") Integer chatId,
                                              @RequestParam("newName") String newName,
                                              @RequestPart(value = "groupAvatar", required = false) MultipartFile groupAvatar) {
        User user = userService.getLoginUser();
        return ResponseEntity.ok(chatService.editGroup(chatId, user, newName, groupAvatar));
    }
    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Response> removeUserFromGroup(@PathVariable Integer chatId, @PathVariable Integer userId) {
        User user = userService.getLoginUser();
        return ResponseEntity.ok(chatService.removeUserFromGroup( userId, chatId, user));
    }
    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<Response> deleteChat(@PathVariable Integer chatId) {
        User user = userService.getLoginUser();
        return ResponseEntity.ok(chatService.removeChat( chatId, user));
    }
}
