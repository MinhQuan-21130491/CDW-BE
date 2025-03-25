package com.chatapp.ChatApp.config;

import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private  final UserRepository userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByEmail(username).orElseThrow(() -> new NotFoundException("User/Email Not Found"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}

