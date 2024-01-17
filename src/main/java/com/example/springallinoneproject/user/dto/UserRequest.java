package com.example.springallinoneproject.user.dto;

import com.example.springallinoneproject.user.entity.SocialType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRequest {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class JoinRequest{
        private String email;
        private String username;
        private String password;
        private SocialType socialType;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LoginRequest{
        private String email;
        private String password;
    }
}
