package com.example.secureapi.controller;

import com.example.secureapi.dto.AuthRequest;
import com.example.secureapi.dto.AuthRes;
import com.example.secureapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthRes> login(@RequestBody AuthRequest registerRequest) {
        return userService.login(registerRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthRes> registration(@RequestBody AuthRequest registerRequest) {
        return userService.registration(registerRequest);
    }
}
