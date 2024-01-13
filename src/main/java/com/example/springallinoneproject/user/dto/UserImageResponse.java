package com.example.springallinoneproject.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserImageResponse {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class UserImageUploadResponse{
        private Long userId;
        private Long userImageId;
        private String imageUrl;
        private String imageFilename;
    }
}
