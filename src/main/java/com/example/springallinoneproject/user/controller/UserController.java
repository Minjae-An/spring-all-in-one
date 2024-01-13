package com.example.springallinoneproject.user.controller;

import com.example.springallinoneproject.user.dto.UserImageRequest.DeleteUserImageRequest;
import com.example.springallinoneproject.user.dto.UserImageResponse.UserImageUploadResponse;
import com.example.springallinoneproject.user.dto.UserResponse.UserImagesUploadResponse;
import com.example.springallinoneproject.user.service.UserCommandService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserCommandService userCommandService;

    @PostMapping("/{userId}/single-image")
    public ResponseEntity<UserImageUploadResponse>
    uploadSingleUserImage(@PathVariable Long userId,
                          @RequestPart(value = "image") MultipartFile uploadImage) {
        UserImageUploadResponse response = userCommandService
                .uploadUserImage(userId, uploadImage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/images")
    public ResponseEntity<UserImagesUploadResponse>
    uploadUserImaeges(@PathVariable Long userId,
                      @RequestPart(value = "images") List<MultipartFile> uploadImages) {
        UserImagesUploadResponse response = userCommandService.uploadUserImages(userId, uploadImages);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/image")
    public ResponseEntity<Object> deleteUserImage(@PathVariable Long userId,
                                                  @RequestBody DeleteUserImageRequest request) {
        userCommandService.deleteUserImage(userId, request.getDeleteImageFilename());
        return ResponseEntity.accepted().build();
    }
}
