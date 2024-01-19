package com.example.springallinoneproject.user.entity;

import com.example.springallinoneproject.common.BaseEntity;
import com.example.springallinoneproject.notification.Notification;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserImage> userImages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    public void addUserImage(UserImage userImage) {
        userImages.add(userImage);
        userImage.updateUser(this);
    }

    public boolean deleteUserImage(String deleteImageFilename) {
        return userImages.removeIf(userImage ->
                userImage.getFilename().equals(deleteImageFilename));
    }

    public void receiveNotification(Notification notification) {
        notifications.add(notification);
    }

    public void readNotification(Notification notification) {
        notification.read();
        notifications.remove(notification);
    }
}
