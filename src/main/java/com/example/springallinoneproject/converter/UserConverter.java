package com.example.springallinoneproject.converter;

import com.example.springallinoneproject.user.dto.LoggedInUser;
import com.example.springallinoneproject.user.dto.UserImageResponse.UserImageUploadResponse;
import com.example.springallinoneproject.user.dto.UserRequest.JoinRequest;
import com.example.springallinoneproject.user.dto.UserResponse.JoinResponse;
import com.example.springallinoneproject.user.dto.UserResponse.UserImagesUploadResponse;
import com.example.springallinoneproject.user.entity.User;
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

    public static JoinResponse toJoinResponse(User user) {
        return JoinResponse.builder()
                .userId(user.getId())
                .joinedAt(user.getCreatedAt())
                .build();
    }

    public static LoggedInUser toLoggedInUser(User user) {
        return LoggedInUser.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(JoinRequest request) {
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(request.getPassword())
                .socialType(request.getSocialType())
                .build();
    }
}
