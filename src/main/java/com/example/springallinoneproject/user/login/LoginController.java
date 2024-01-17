package com.example.springallinoneproject.user.login;

import com.example.springallinoneproject.api_payload.CommonResponse;
import com.example.springallinoneproject.user.dto.JwtResponse;
import com.example.springallinoneproject.user.dto.LoggedInUser;
import com.example.springallinoneproject.user.dto.UserProfileResponse;
import com.example.springallinoneproject.user.dto.UserRequest.JoinRequest;
import com.example.springallinoneproject.user.dto.UserRequest.LoginRequest;
import com.example.springallinoneproject.user.dto.UserResponse.AccessTokenResponse;
import com.example.springallinoneproject.user.dto.UserResponse.JoinResponse;
import com.example.springallinoneproject.user.dto.UserResponse.LoginResponse;
import com.example.springallinoneproject.user.login.jwt.JwtUtil;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final JwtUtil jwtUtil;

    @GetMapping("/social-login")
    public CommonResponse<String> socialLogin() {
        return CommonResponse.ok(loginService.getUrl());
    }

    @GetMapping("/login/oauth2/code/google")
    public CommonResponse<JwtResponse> oauthLogin(String code) {
        String accessToken = loginService.getToken(code);
        UserProfileResponse userProfile = loginService.getUserProfile(accessToken);
        LoggedInUser loggedInUser = loginService.createUserBySocialLogin(userProfile);
        String token = jwtUtil.createAuthorization(loggedInUser, Instant.now());

        return CommonResponse.ok(new JwtResponse(token));
    }

    @GetMapping("/api/v1/user")
    public String user() {
        return "<h1>user</h1>";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "<h1>manager</h1>";
    }

    @PostMapping("/join")
    public CommonResponse<JoinResponse> join(@RequestBody JoinRequest request) {
        JoinResponse response = loginService.join(request);
        return CommonResponse.ok(response);
    }

    @PostMapping("/login")
    public CommonResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = loginService.login(request);
        return CommonResponse.ok(response);
    }

    @GetMapping("/access-token")

    public CommonResponse<AccessTokenResponse> getAccessToken(@RequestParam("refreshToken") String refreshToken) {
        AccessTokenResponse response = loginService.getAccessToken(refreshToken);
        return CommonResponse.ok(response);
    }
}
