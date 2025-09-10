package com.example.secureapi.dto;

import lombok.Data;

@Data
public class AuthRes {
    private String token;

    public AuthRes(String token) {
        this.token = token;
    }
}
