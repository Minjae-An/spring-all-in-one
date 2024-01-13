package com.example.springallinoneproject.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserImageRequest {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeleteUserImageRequest{
        private String deleteImageFilename;
    }
}
