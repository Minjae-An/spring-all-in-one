package com.example.springallinoneproject.converter;

import com.example.springallinoneproject.user.dto.UserImageResponse.UserImageUploadResponse;
import com.example.springallinoneproject.user.dto.UserResponse.UserImagesUploadResponse;
import com.example.springallinoneproject.user.entity.UserImage;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserConverter {
    public static UserImageUploadResponse toUserImageResponse(Long userId, UserImage userImage) {
        return UserImageUploadResponse.builder()
                .userImageId(userImage.getId())
                .userId(userId)
                .imageFilename(userImage.getFilename())
                .imageUrl(userImage.getImageUrl())
                .build();
    }

    public static UserImagesUploadResponse toUserImagesResponse(Long userId, List<UserImage> userImages) {
        List<UserImageUploadResponse> uploadedImages = userImages.stream()
                .map(userImage -> toUserImageResponse(userId, userImage))
                .toList();

        return UserImagesUploadResponse.builder()
                .userId(userId)
                .uploadedImages(uploadedImages)
                .build();
    }
}
