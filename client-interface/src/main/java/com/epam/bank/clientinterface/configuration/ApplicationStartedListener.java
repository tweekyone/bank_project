package com.epam.bank.clientinterface.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationStartedListener {

    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Application started with active profiles: {}",
            (Object) contextRefreshedEvent.getApplicationContext().getEnvironment().getActiveProfiles());
    }
}
