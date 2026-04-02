package com.devfolio.backend.auth;

import com.devfolio.backend.auth.dto.LoginRequest;
import com.devfolio.backend.auth.dto.LoginResponse;
import com.devfolio.backend.common.exception.UnauthorizedException;
import com.devfolio.backend.config.AppSecurityProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppSecurityProperties securityProperties;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppSecurityProperties securityProperties, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.securityProperties = securityProperties;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        if (!securityProperties.getAdminUsername().equals(request.username())
                || !passwordEncoder.matches(request.password(), securityProperties.getAdminPassword())) {
            throw new UnauthorizedException("用户名或密码错误");
        }

        String token = jwtService.generateToken(request.username());
        return new LoginResponse(token, "Bearer", jwtService.extractExpiration(token));
    }
}

