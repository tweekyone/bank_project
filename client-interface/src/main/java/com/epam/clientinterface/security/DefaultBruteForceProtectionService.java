package com.epam.clientinterface.security;


import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service("bruteForceProtectionService")
@PropertySource(value = "classpath:application.properties")
public class DefaultBruteForceProtectionService implements BruteForceProtectionService {

    @Value("${jdj.security.failedlogin.count}")//5
    private int maxFailedLogins;

    @Autowired
    UserRepository userRepository;

    @Override
    public void registerLoginFailure(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user != null && user.isEnabled()) {
            int failedCounter = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(failedCounter);
            if (maxFailedLogins <= failedCounter) {
                user.setEnabled(false);
            }
            userRepository.save(user);
        }
    }

    @Override
    public void resetBruteForceCounter(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user != null) {
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
        }
    }

    @Override
    public boolean isBruteForceAttack(String username) {
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            return user.getFailedLoginAttempts() >= maxFailedLogins;
        }
        return false;
    }
}
