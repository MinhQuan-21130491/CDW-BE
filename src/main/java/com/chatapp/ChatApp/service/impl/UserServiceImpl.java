
package com.chatapp.ChatApp.service.impl;

import com.chatapp.ChatApp.config.TokenProvider;
import com.chatapp.ChatApp.exception.InvalidCredentialsException;
import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.repository.UserRepository;
import com.chatapp.ChatApp.request.LoginRequest;
import com.chatapp.ChatApp.request.RegistrationRequest;
import com.chatapp.ChatApp.request.UpdateUserRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Response registerUser(RegistrationRequest registrationRequest) {
        System.out.println(registrationRequest);
        Optional<User> optionUser = userRepository.findByEmail(registrationRequest.getEmail());
        if (optionUser.isPresent()) {
            throw new InvalidCredentialsException("Email existed");
        }
        User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .full_name(registrationRequest.getFull_name())
                .build();
        userRepository.save(user);
        return Response.builder().message("Register successful").status(200).build();
    }

    @Override
    public Response LoginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new InvalidCredentialsException("Email not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Wrong password");
        }
        String token = tokenProvider.generateToken(user);

        return Response.builder().message("Login successful").status(200).token(token).build();
    }

    @Override
    public Response findUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return Response.builder()
                    .status(200)
                    .message("Successfully")
                    .user(userOptional.get())
                    .build();
        }
        throw new NotFoundException("User not found with id " + id);
    }

    @Override
    public Response updateUser(Integer id, UpdateUserRequest req) {
        User user = getLoginUser();
        if (req.getFull_name() != null) {
            user.setFull_name(req.getFull_name());
        }
        if (req.getProfile_picture() != null) {
            user.setProfile_picture(req.getProfile_picture());
        }
        userRepository.save(user);
        return Response.builder().message("Update successful").status(200).build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));
    }

    @Override
    public Response searchUser() {
        List<User> users = userRepository.findAll();
        return Response.builder().message("Search successful").status(200).users(users).build();
    }
}
