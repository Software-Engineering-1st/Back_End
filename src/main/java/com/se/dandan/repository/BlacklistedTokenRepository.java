package com.se.dandan.repository;

import com.se.dandan.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByRefreshToken(String refreshToken);
}
