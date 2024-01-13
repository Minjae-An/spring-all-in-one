package com.example.springallinoneproject.user.dto;

import com.example.springallinoneproject.user.dto.UserImageResponse.UserImageUploadResponse;
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
}
