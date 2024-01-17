package com.example.springallinoneproject.user.repository;

import com.example.springallinoneproject.user.dto.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByAuthId(Long authId);
}
