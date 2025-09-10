package com.example.secureapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.secureapi.dto.AuthRequest;
import com.example.secureapi.dto.AuthRes;
import com.example.secureapi.dto.UserResponse;
import com.example.secureapi.entity.User;
import com.example.secureapi.repository.UserRepository;
import com.example.secureapi.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<AuthRes> registration(AuthRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthRes(token));
    }

    public ResponseEntity<AuthRes> login(AuthRequest loginRequest) {
        Optional<User> optUser = userRepository.findByUsername(loginRequest.getUsername());
        if (optUser.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), optUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String token = jwtTokenProvider.generateToken(optUser.get().getUsername());
        return ResponseEntity.ok(new AuthRes(token));
    }

    public List<UserResponse> getAllUserResponses() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getUsername()))
                .collect(Collectors.toList());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
