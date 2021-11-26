package com.epam.clientinterface.configuration.security;

import com.epam.clientinterface.security.BruteForceProtectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Resource(name = "bruteForceProtectionService")
    private BruteForceProtectionService bruteForceProtectionService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String userEmail = event.getAuthentication().getName();
        log.info("Login successful for user email {}", userEmail);
        bruteForceProtectionService.resetBruteForceCounter(userEmail);
    }
}
