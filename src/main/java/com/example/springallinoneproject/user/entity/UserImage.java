package com.example.springallinoneproject.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private String filename;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserImage(String imageUrl, String filename) {
        this.imageUrl = imageUrl;
        this.filename = filename;
    }

    public void updateUser(User user) {
        this.user = user;
    }
}
