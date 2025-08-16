package com.example.mendix.CustomAnnotation;
import com.example.mendix.Service.UserActivityLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserActivityAspect {

    private final UserActivityLogService logService;

    public UserActivityAspect(UserActivityLogService logService) {
        this.logService = logService;
    }

    @AfterReturning("@annotation(loggable)")
    public void logAction(JoinPoint joinPoint, LoggableAction loggable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "SYSTEM";

        // Build details from method arguments
        Object[] args = joinPoint.getArgs();
        StringBuilder details = new StringBuilder();
        for (Object arg : args) {
            details.append(arg).append("; ");
        }

        logService.log(username, loggable.action(), loggable.entity(), details.toString());
    }
}
