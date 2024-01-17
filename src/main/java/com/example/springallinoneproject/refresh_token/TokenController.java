package com.example.springallinoneproject.refresh_token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;
    @GetMapping("/refresh/{id}")
    public ResponseEntity<RefreshTokenResponse> getRefresh(@PathVariable("id") Long id) {
        RefreshTokenResponse response = tokenService.getRefreshToken(id);
        return ResponseEntity.ok(response);
    }
}
