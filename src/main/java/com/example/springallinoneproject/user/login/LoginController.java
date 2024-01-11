package com.example.springallinoneproject.user.login;

import com.example.springallinoneproject.user.dto.JwtResponse;
import com.example.springallinoneproject.user.dto.LoggedInUser;
import com.example.springallinoneproject.user.dto.UserProfileResponse;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String login() {
        return loginService.getUrl();
    }

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<JwtResponse> oauthLogin(String code) {
        String accessToken = loginService.getToken(code);
        UserProfileResponse userProfile = loginService.getUserProfile(accessToken);
        LoggedInUser loggedInUser = loginService.createUser(userProfile);
        String token = jwtUtil.createToken(loggedInUser, Instant.now());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/api/v1/user")
    public String user(){
        return "<h1>user</h1>";
    }

    @GetMapping("/api/v1/manager")
    public String manager(){
        return "<h1>manager</h1>";
    }
}
