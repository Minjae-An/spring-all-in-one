package com.example.springallinoneproject.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileResponse {
    private String email;
    private String username;

    @Builder
    public UserProfileResponse(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
