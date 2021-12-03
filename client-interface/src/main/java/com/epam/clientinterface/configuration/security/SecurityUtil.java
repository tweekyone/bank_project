package com.epam.clientinterface.configuration.security;

import static java.util.Objects.requireNonNull;

import com.epam.clientinterface.domain.UserDetailAuth;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static UserDetailAuth safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof UserDetailAuth) ? (UserDetailAuth) principal : null;
    }

    public static UserDetailAuth get() {
        return requireNonNull(safeGet(), "No authorized user found");
    }

    public static long authUserId() {
        return get().getId();
    }
}
