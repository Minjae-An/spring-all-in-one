package com.example.springallinoneproject.user.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserImage> userImages;

    @Builder
    public User(String email, String username, String password, SocialType socialType) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.socialType = socialType;
        userImages = new ArrayList<>();
    }

    public void addUserImage(UserImage userImage) {
        userImages.add(userImage);
        userImage.updateUser(this);
    }

    public boolean deleteUserImage(String deleteImageFilename) {
        return userImages.removeIf(userImage ->
                userImage.getFilename().equals(deleteImageFilename));
    }
}
