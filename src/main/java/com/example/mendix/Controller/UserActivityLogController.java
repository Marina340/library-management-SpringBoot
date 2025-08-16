package com.example.mendix.Controller;

import com.example.mendix.Entity.UserActivityLog;
import com.example.mendix.Repository.UserActivityLogRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class UserActivityLogController {

    private final UserActivityLogRepository logRepository;

    public UserActivityLogController(UserActivityLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserActivityLog> getLogs(Pageable pageable) {
        return logRepository.findAll(pageable);
    }

    @GetMapping("/logs")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserActivityLog> getLogsByUser(@RequestParam String username) {
        return logRepository.findAll().stream()
                .filter(log -> log.getUsername().equals(username))
                .toList();
    }

}
