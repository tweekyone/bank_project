package com.epam.clientinterface.configuration.security;

import com.epam.clientinterface.security.BruteForceProtectionService;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Resource(name = "bruteForceProtectionService")
    private BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        log.info("Login failed for user email {}", username);
        bruteForceProtectionService.registerLoginFailure(username);
    }
}
