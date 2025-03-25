package com.chatapp.ChatApp.controller;

import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.request.UpdateUserRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile()  {
        return  ResponseEntity.ok(userService.getLoginUser());
    }
    @GetMapping("/search")
    public ResponseEntity<Response> searchUser(@RequestParam("query") String query) {
        return ResponseEntity.ok(userService.searchUser());
    }
    @PutMapping("/update")
    public ResponseEntity<Response> updateUser(@RequestBody UpdateUserRequest userRequest) {
        User user = userService.getLoginUser();
        return ResponseEntity.ok(userService.updateUser(user.getId(), userRequest));
    }
}