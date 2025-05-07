package com.se.dandan.util;

import com.se.dandan.dto.TokenInfo;
import com.se.dandan.entity.Member;
import com.se.dandan.exception.CustomException;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JWTProvider {

    private SecretKey secretKey;

    private final long expiredMs;

    private final long refreshedMs;

    private final MemberRepository memberRepository;

    public JWTProvider(@Value("${jwt.secret}") String secret,
                       @Value("${jwt.expiredMS}") long expiredMs,
                       @Value("${jwt.refreshedMs}")long refreshedMs,
                       MemberRepository memberRepository) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expiredMs = expiredMs;
        this.refreshedMs = refreshedMs;
        this.memberRepository = memberRepository;
    }

    public String generateAccessToken(String nickname, String role) {
        Date now = new Date();
        Date accessTokenExpiredAt = new Date(now.getTime() + expiredMs);

        return Jwts.builder()
                .subject(nickname)
                .claim("role", role)
                .issuedAt(now)
                .expiration(accessTokenExpiredAt)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String nickname, String role) {
        Date now = new Date();
        Date refreshTokenExpiredAt = new Date(now.getTime() + refreshedMs);

        return Jwts.builder()
                .subject(nickname)
                .claim("role", role)
                .issuedAt(now)
                .expiration(refreshTokenExpiredAt)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            return true;
        }
        catch (SecurityException | MalformedJwtException e) {
            log.error("Signature verification failed");
            throw new JwtException("잘못된 JWT 서명 입니다.");
        }
        catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
            throw new JwtException("지원하지 않는 토큰 입니다.");
        }
        catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
            throw new JwtException("만료된 토큰 입니다.");
        }
        catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty");
        }

        return false;
    }

    public TokenInfo generateToken(String nickname, String role) {

        String accessToken = generateAccessToken(nickname, role);

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .role(role)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();

        String nickname = claims.getSubject();

        String role = claims.get("role", String.class);

        Member member = memberRepository.findByNickname(nickname);

        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

        return new UsernamePasswordAuthenticationToken(nickname, token, authorities);
    }
}
