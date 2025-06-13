package com.chatapp.ChatApp.controller;

import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.request.RequestChangePassword;
import com.chatapp.ChatApp.request.RequestForgetPassword;
import com.chatapp.ChatApp.request.UpdateUserRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Response> getUserProfile()  {
        return  ResponseEntity.ok(userService.getInfor());
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchUser(@RequestParam("query") String query) {
        return ResponseEntity.ok(userService.searchUserByName(query));
    }
    @PutMapping("/update")
    public ResponseEntity<Response> updateUser(@RequestBody UpdateUserRequest userRequest) {
        User user = userService.getLoginUser();
        return ResponseEntity.ok(userService.updateUser(user.getId(), userRequest));
    }
    @GetMapping("/users")
    public ResponseEntity<Response> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/change-password")
    public ResponseEntity<Response> changePassword(@Valid @RequestBody RequestChangePassword request) {
        return ResponseEntity.ok(userService.changePassword(request.getOldPassword(), request.getNewPassword()));
    }


}