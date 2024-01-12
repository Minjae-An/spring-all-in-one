package com.example.springallinoneproject.interceptor;

import com.example.springallinoneproject.user.login.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!jwtUtil.isIncludeTokenPrefix(header)){
            throw new IllegalStateException("No Bearer prefix");
        }

        String token = jwtUtil.extractTokenFromAuthorization(header);
        if(jwtUtil.isTokenExpired(token)){
            throw new IllegalStateException("token expired");
        }

        if(jwtUtil.isTokenNotManipulated(token)){
            throw new IllegalStateException("token manipulated");
        }

        return true;
    }
}
