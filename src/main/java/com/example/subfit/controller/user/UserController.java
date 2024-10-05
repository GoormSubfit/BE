package com.example.subfit.controller.user;

import com.example.subfit.dto.user.UserLoginDto;
import com.example.subfit.dto.user.UserRegistrationDto;
import com.example.subfit.entity.user.User;
import com.example.subfit.security.JwtTokenProvider;
import com.example.subfit.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @GetMapping("/check-userid")
    public ResponseEntity<Map<String, String>> checkUserId(@RequestParam String userId) {
        boolean isAvailable = userService.isUserIdAvailable(userId);

        Map<String, String> response = new HashMap<>();
        if (isAvailable) {
            response.put("message", "사용 가능한 아이디입니다.");
            return ResponseEntity.ok().body(response);
        } else {
            response.put("message", "이미 사용 중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(
            @Valid @ModelAttribute UserRegistrationDto userRegistrationDto,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImageFile) {

        if (!userService.isUserIdAvailable(userRegistrationDto.getUserId())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "이미 사용 중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        userService.registerUser(userRegistrationDto, profileImageFile);
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원가입 되었습니다");
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getUserId(), userLoginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(userLoginDto.getUserId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "로그인 되었습니다");
        response.put("token", token);

        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User user = userService.getUserByUserId(userId);
        Map<String, Object> profileData = new HashMap<>();
        profileData.put("profileImage", user.getProfileImage());
        profileData.put("name", user.getName());
        profileData.put("job", user.getJob());

        return ResponseEntity.ok(profileData);
    }

}