package com.example.springallinoneproject.user.login;

import com.example.springallinoneproject.user.dto.LoggedInUser;
import com.example.springallinoneproject.user.dto.UserProfileResponse;
import com.example.springallinoneproject.user.entity.SocialType;
import com.example.springallinoneproject.user.entity.User;
import com.example.springallinoneproject.user.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserRepository userRepository;


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

    public LoggedInUser createUser(UserProfileResponse userProfile) {
        String email = userProfile.getEmail();
        Optional<User> signedUser = userRepository.findByEmail(email);

        if (signedUser.isPresent()) {
            return new LoggedInUser(signedUser.get());
        }

        User user = User.builder()
                .email(userProfile.getEmail())
                .username(userProfile.getUsername())
                .password("1234")
                .socialType(SocialType.GOOGLE)
                .build();
        return new LoggedInUser(userRepository.save(user));
    }
}

