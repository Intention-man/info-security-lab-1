package com.example.secureapi.dto;

import com.example.secureapi.security.HtmlSanitizerSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
public class UserResponse {
    @JsonSerialize(using = HtmlSanitizerSerializer.class)
    private String username;

    public UserResponse(String username) {
        this.username = username;
    }
}
