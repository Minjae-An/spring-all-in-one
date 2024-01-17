package com.example.springallinoneproject.refresh_token;

import com.example.springallinoneproject.user.login.jwt.JwtUtil;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public RefreshTokenResponse getRefreshToken(Long id){
        String refreshToken = jwtUtil.createRefreshToken(id, Instant.now());
        tokenRepository.save(new Token(id, refreshToken));
        return new RefreshTokenResponse(refreshToken);
    }
}
