
package com.chatapp.ChatApp.service.impl;

import com.chatapp.ChatApp.config.TokenProvider;
import com.chatapp.ChatApp.dto.ChatDto;
import com.chatapp.ChatApp.dto.UserDto;
import com.chatapp.ChatApp.exception.InvalidCredentialsException;
import com.chatapp.ChatApp.exception.NotFoundException;
import com.chatapp.ChatApp.mapper.intf.EntityDtoMapper;
import com.chatapp.ChatApp.modal.User;
import com.chatapp.ChatApp.repository.UserRepository;
import com.chatapp.ChatApp.request.LoginRequest;
import com.chatapp.ChatApp.request.RegistrationRequest;
import com.chatapp.ChatApp.request.UpdateUserRequest;
import com.chatapp.ChatApp.response.Response;
import com.chatapp.ChatApp.s3.S3Service;
import com.chatapp.ChatApp.service.iterf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;
    private final EntityDtoMapper entityDtoMapper;
    private final S3Service s3Service;
    @Override
    public Response registerUser(RegistrationRequest registrationRequest) {
        Optional<User> optionUser = userRepository.findByEmail(registrationRequest.getEmail());
        if (optionUser.isPresent()) {
            throw new InvalidCredentialsException("error_email_existed");
        }
        User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .full_name(registrationRequest.getFull_name())
                .build();
        userRepository.save(user);
        return Response.builder().message("success_register").status(200).build();
    }

    @Override
    public Response LoginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new InvalidCredentialsException("error_email_not_found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("error_wrong_password");
        }
        String token = tokenProvider.generateToken(user);

        return Response.builder().message("success_login").status(200).token(token).build();
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

        if (req.getProfile_picture() != null && !req.getProfile_picture().equals(user.getProfile_picture())) {
            // Tạo tên file duy nhất
            String fileName = "profile_pictures/" + UUID.randomUUID() + ".jpg";

            // Upload base64 -> S3 và lấy URL
            String imageUrl = s3Service.uploadBase64Media(req.getProfile_picture(), fileName);

            // Lưu URL
            user.setProfile_picture(imageUrl);
        }
        userRepository.save(user);
        return Response.builder().message("success.change_profile").status(200).build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));

    }

    @Override
    public Response getInfor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user =  userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        UserDto userDto = entityDtoMapper.mapUserToDtoBasicPlusStoryDto(user);
        return Response.builder().status(200).message("Get login user success").userDto(userDto).build();
    }

    @Override
    public Response changePassword(String oldPassword, String newPassword) {
        User user = getLoginUser();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidCredentialsException("error_wrong_password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return Response.builder().message("success_change_password").status(200).build();
    }

    @Override
    public Response forgetPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new InvalidCredentialsException("error_email_not_existed");
        }
        User user = userOpt.get();
        String passwordNew = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(passwordNew));
        userRepository.save(user);
        sendEmail(user.getEmail(), passwordNew);
        return Response.builder().message("successs_send").status(200).build();
    }

    @Override
    public Response searchUserByName(String name) {
        List<User> users = userRepository.searchUserByName(name);
        List<UserDto> usersDto = users.stream().map(entityDtoMapper::mapUserToDtoBasic).collect(Collectors.toList());
        return Response.builder().message("Search successful").status(200).usersDto(usersDto).build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        List<UserDto> usersDto = users.stream().map(entityDtoMapper::mapUserToDtoBasicPlusStoryDto).collect(Collectors.toList());
        return Response.builder().message("Get all user success").usersDto(usersDto).status(200).build();
    }

    private void sendEmail(String email, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Đặt lại mật khẩu");
        message.setText("Mật khẩu mới web chat trực tuyến của bạn là : " + newPassword + "\nVui lòng không để lộ ra ngoài tránh ảnh hưởng đến tài khoản.");
        mailSender.send(message);
    }
    public static String generateRandomPassword() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int PASSWORD_LENGTH = 8;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
