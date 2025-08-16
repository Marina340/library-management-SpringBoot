package com.example.mendix.Repository;

import com.example.mendix.Entity.UserActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, Long> { }

