package com.example.mendix.Service;

import com.example.mendix.Entity.UserActivityLog;
import com.example.mendix.Repository.UserActivityLogRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserActivityLogService {

    private final UserActivityLogRepository repository;

    public UserActivityLogService(UserActivityLogRepository repository) {
        this.repository = repository;
    }

    public void log(String username, String action, String entity, String details) {
        UserActivityLog log = new UserActivityLog();
        log.setUsername(username);
        log.setAction(action);
        log.setEntity(entity);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        repository.save(log);
    }
}
