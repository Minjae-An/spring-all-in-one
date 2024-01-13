package com.example.springallinoneproject.user.service;

import static com.example.springallinoneproject.user.dto.UserImageResponse.UserImageUploadResponse;

import com.example.springallinoneproject.converter.UserConverter;
import com.example.springallinoneproject.user.dto.UserResponse.UserImagesUploadResponse;
import com.example.springallinoneproject.user.entity.User;
import com.example.springallinoneproject.user.entity.UserImage;
import com.example.springallinoneproject.user.repository.UserImageRepository;
import com.example.springallinoneproject.user.repository.UserRepository;
import com.example.springallinoneproject.util.GetS3Res;
import com.example.springallinoneproject.util.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserCommandService {
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final S3Service s3Service;

    public UserImageUploadResponse uploadUserImage(Long userId, MultipartFile uploadImage){
        User user = findById(userId);
        GetS3Res getS3Res = s3Service.uploadSingleFile(uploadImage);
        UserImage userImage = UserImage.builder()
                .imageUrl(getS3Res.getImageUrl())
                .filename(getS3Res.getFilename())
                .build();
        user.addUserImage(userImage);
        saveUserImage(userImage);

        return UserImageUploadResponse.builder()
                .userId(userId)
                .userImageId(userImage.getId())
                .imageFilename(userImage.getFilename())
                .imageUrl(userImage.getImageUrl())
                .build();
    }

    public UserImagesUploadResponse uploadUserImages(Long userId, List<MultipartFile> uploadImages){
        User user = findById(userId);
        List<GetS3Res> getS3ResList = s3Service.uploadFiles(uploadImages);
        List<UserImage> userImages = getS3ResList.stream()
                .map(getS3Res -> new UserImage(getS3Res.getImageUrl(), getS3Res.getFilename()))
                .toList();
        userImages.forEach(user::addUserImage);
        userImages.forEach(this::saveUserImage);
        return UserConverter.toUserImagesResponse(userId, userImages);
    }

    public void deleteUserImage(Long userId, String deleteImageFilename){
        User user = findById(userId);
        if(!user.deleteUserImage(deleteImageFilename))
            throw new IllegalStateException("해당 파일이 존재하지 않습니다");
        s3Service.deleteFile(deleteImageFilename);
    }

    private UserImage saveUserImage(UserImage userImage){
        return userImageRepository.save(userImage);
    }

    private User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()->new IllegalStateException("해당 사용자가 존재하지 않음"));
    }
}
