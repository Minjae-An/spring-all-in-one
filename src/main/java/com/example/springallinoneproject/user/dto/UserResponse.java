package com.example.springallinoneproject.user.dto;

import com.example.springallinoneproject.user.dto.UserImageResponse.UserImageUploadResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserImagesUploadResponse{
        private Long userId;
        private List<UserImageUploadResponse> uploadedImages;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class JoinResponse{
        private Long userId;
        private LocalDateTime joinedAt;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class LoginResponse{
        private Long userId;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    public static class AccessTokenResponse{
        private String accessToken;
    }
}
