package com.example.mendix.Controller;

import com.example.mendix.Config.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final JwtUtil jwtUtil;

    public TestController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/token")
    public String generateToken(@RequestParam String username) {
        return jwtUtil.generateToken(username);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam String token) {
        boolean isValid = jwtUtil.validateToken(token);
        if (isValid) {
            return "Valid token for user: " + jwtUtil.extractUsername(token);
        } else {
            return "Invalid token";
        }
    }
}
