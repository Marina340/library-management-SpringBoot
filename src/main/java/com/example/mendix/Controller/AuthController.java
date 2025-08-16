package com.example.mendix.Controller;

import com.example.mendix.Config.JwtUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(
                    body.get("username"), body.get("password")
            );
            authManager.authenticate(authToken);

            String token = jwtUtil.generateToken(body.get("username"));
            return Map.of("token", token);

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Username or password is incorrect");
        } catch (DisabledException e) {
            throw new RuntimeException("User account is disabled");
        } catch (LockedException e) {
            throw new RuntimeException("User account is locked");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed");
        }
    }
}