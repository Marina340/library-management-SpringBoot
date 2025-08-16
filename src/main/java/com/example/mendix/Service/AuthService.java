package com.example.mendix.Service;

import com.example.mendix.Config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Inject AuthenticationManager via constructor
    public AuthService(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return jwtUtil.generateToken(username);
    }
}
