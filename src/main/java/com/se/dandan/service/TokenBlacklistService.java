package com.se.dandan.service;

import com.se.dandan.entity.BlacklistedToken;
import com.se.dandan.repository.BlacklistedTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class TokenBlacklistService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public TokenBlacklistService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }

    public void blacklistToken(String token, long expiration) {
        LocalDateTime expirationTime = LocalDateTime.now().plus(Duration.ofMillis(expiration));
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        blacklistedToken.setToken(token);
        blacklistedToken.setExpiration(expirationTime);
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        blacklistedTokenRepository.deleteAllByExpirationBefore(now);
    }
}
