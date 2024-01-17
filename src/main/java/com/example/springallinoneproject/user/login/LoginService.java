package com.example.springallinoneproject.user.login;

import static com.example.springallinoneproject.user.dto.UserRequest.JoinRequest;
import static com.example.springallinoneproject.user.dto.UserResponse.JoinResponse;

import com.example.springallinoneproject.api_payload.status_code.ErrorStatus;
import com.example.springallinoneproject.converter.UserConverter;
import com.example.springallinoneproject.exception.GeneralException;
import com.example.springallinoneproject.user.dto.LoggedInUser;
import com.example.springallinoneproject.user.dto.RefreshToken;
import com.example.springallinoneproject.user.dto.UserProfileResponse;
import com.example.springallinoneproject.user.dto.UserRequest.LoginRequest;
import com.example.springallinoneproject.user.dto.UserResponse.AccessTokenResponse;
import com.example.springallinoneproject.user.dto.UserResponse.LoginResponse;
import com.example.springallinoneproject.user.entity.Role;
import com.example.springallinoneproject.user.entity.SocialType;
import com.example.springallinoneproject.user.entity.User;
import com.example.springallinoneproject.user.login.jwt.JwtUtil;
import com.example.springallinoneproject.user.repository.RefreshTokenRepository;
import com.example.springallinoneproject.user.repository.UserRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class LoginService {
    @Value("${oauth2.login-url}")
    private String loginUrl;

    @Value("${oauth2.client-id}")
    private String clientId;

    @Value("${oauth2.client-pw}")
    private String clientPassword;

    @Value("${oauth2.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.token-uri}")
    private String tokenUri;

    @Value("${oauth2.user-info-uri}")
    private String userInfoUri;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public String getUrl() {
        return loginUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri
                + "&response_type=code&scope=email profile";
    }

    public String getToken(String code) {
        WebClient webClient = WebClient
                .builder()
                .baseUrl("https://oauth2.googleapis.com")
                .build();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("code", code);
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientPassword);
        requestBody.put("redirect_uri", redirectUri);
        requestBody.put("grant_type", "authorization_code");

        Map<String, Object> response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/token")
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.ACCEPT, "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return (String) response.get("access_token");
    }

    public UserProfileResponse getUserProfile(String accessToken) {
        WebClient webClient = WebClient
                .builder()
                .baseUrl("https://www.googleapis.com")
                .build();

        Map<String, Object> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("userinfo/v2/me")
                        .queryParam("access_token", accessToken)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String email = (String) response.get("email");
        String name = (String) response.get("name");

        return UserProfileResponse.builder()
                .email(email)
                .username(name)
                .build();
    }

    public LoggedInUser createUserBySocialLogin(UserProfileResponse userProfile) {
        String email = userProfile.getEmail();
        Optional<User> signedUser = userRepository.findByEmail(email);

        if (signedUser.isPresent()) {
            return UserConverter.toLoggedInUser(signedUser.get());
        }

        User user = User.builder()
                .email(userProfile.getEmail())
                .username(userProfile.getUsername())
                .password("1234")
                .socialType(SocialType.GOOGLE)
                .build();
        return UserConverter.toLoggedInUser(userRepository.save(user));
    }

    public JoinResponse join(JoinRequest request) {
        Optional<User> foundUser = userRepository.findByEmail(request.getEmail());
        if (foundUser.isPresent()) {
            throw new GeneralException(ErrorStatus._ALREADY_JOINED_USER);
        }

        User user = UserConverter.toUser(request);
        return UserConverter
                .toJoinResponse(userRepository.save(user));
    }

    public LoginResponse login(LoginRequest request) {
        String email = request.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND_BY_EMAIL));
        LoggedInUser loggedInUser = UserConverter.toLoggedInUser(user);

        String accessToken = jwtUtil.createAccessToken(loggedInUser, Instant.now());
        String refreshToken = jwtUtil.createRefreshToken(user.getId(), Instant.now());

        RefreshToken token = RefreshToken.builder()
                .authId(user.getId())
                .token(refreshToken)
                .role(Role.USER.getRole())
                .build();
        refreshTokenRepository.save(token);

        return LoginResponse.builder()
                .userId(user.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AccessTokenResponse getAccessToken(String refreshToken) {
        validateRefreshToken(refreshToken);
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).get();
        User user = userRepository.findById(token.getAuthId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._USER_NOT_FOUND));
        LoggedInUser loggedInUser = UserConverter.toLoggedInUser(user);
        String accessToken = jwtUtil.createAccessToken(loggedInUser, Instant.now());
        return new AccessTokenResponse(accessToken);
    }

    private void validateRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new GeneralException(ErrorStatus._INVALID_REFRESH_TOKEN));

        if (!jwtUtil.isTokenNotManipulated(token)) {
            refreshTokenRepository.delete(refreshToken);
            throw new GeneralException(ErrorStatus._TOKEN_SIGNATURE_NOT_VALID);
        }

        if (jwtUtil.isTokenExpired(token)) {
            refreshTokenRepository.delete(refreshToken);
            throw new GeneralException(ErrorStatus._REFRESH_TOKEN_EXPIRED);
        }
    }
}

