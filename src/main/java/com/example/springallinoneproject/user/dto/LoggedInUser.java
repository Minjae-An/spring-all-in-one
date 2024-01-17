package com.example.springallinoneproject.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class LoggedInUser {
    private String email;
    private String username;

    @Builder
    private LoggedInUser(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
