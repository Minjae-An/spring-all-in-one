package com.example.springallinoneproject.user.service;

import com.example.springallinoneproject.api_payload.status_code.ErrorStatus;
import com.example.springallinoneproject.exception.GeneralException;
import com.example.springallinoneproject.notification.domain.Notification;
import com.example.springallinoneproject.user.entity.User;
import com.example.springallinoneproject.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()->new GeneralException(ErrorStatus._USER_NOT_FOUND));
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public List<Notification> getReceivedNotifications(Long userId){
        User user = findById(userId);
        return user.getNotifications();
    }
}
