package com.se.dandan.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.dandan.exception.ErrorCode;
import com.se.dandan.exception.ErrorResponse;
import com.se.dandan.service.TokenBlacklistService;
import com.se.dandan.util.JWTProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.swing.undo.CannotUndoException;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;
    private final TokenBlacklistService tokenBlacklistService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (token != null && jwtProvider.validateToken(token)) {
            if(tokenBlacklistService.isBlacklisted(token)) {

                ErrorCode errorCode = ErrorCode.ALREADY_SIGN_OUT;
                ErrorResponse errorResponse = new ErrorResponse(errorCode);

                response.setStatus(errorResponse.getStatus());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            }
            Authentication auth = jwtProvider.getAuthentication(token); // 사용자 추출
            SecurityContextHolder.getContext().setAuthentication(auth); // SecurityContextHolder에 사용자 등록
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) { // 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization"); // 헤더의 Authorization에서 값을 가져옴.
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) { // Bearer를 제외한 토큰 값 가져오기
            return bearerToken.substring(7);
        }
        return null;
    }
}
