package com.example.springallinoneproject.user.dto;

import com.example.springallinoneproject.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoggedInUser {
    private String email;
    private String username;


    public LoggedInUser(User user){
        email = user.getEmail();
        username = user.getUsername();
    }
}
