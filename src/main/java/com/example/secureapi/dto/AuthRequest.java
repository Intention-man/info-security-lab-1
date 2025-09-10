package com.example.secureapi.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
