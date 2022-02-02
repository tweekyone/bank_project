package com.epam.bank.operatorinterface.configuration.security.filter;

import com.epam.bank.operatorinterface.configuration.security.handler.BasicAuthenticationSuccessHandler;
import com.epam.bank.operatorinterface.exception.InitialAuthenticationFilterException;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class BasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LOG_IN_URL = "/login";

    public BasicAuthenticationFilter(AuthenticationManager manager,
                                     BasicAuthenticationSuccessHandler handler) {
        super(new AntPathRequestMatcher(LOG_IN_URL, "POST"));
        this.setAuthenticationManager(manager);
        this.setAuthenticationSuccessHandler(handler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        Authentication authenticate = null;
        try {
            final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            String pair = new String(Base64.getDecoder().decode(authorization.substring(6)));
            String username = pair.split(":")[0];
            String password = pair.split(":")[1];

            authenticate = this.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authenticate);

        } catch (BadCredentialsException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new InitialAuthenticationFilterException(e.getMessage());
        } catch (LockedException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            throw new InitialAuthenticationFilterException(e.getMessage());
        }
        return authenticate;
    }
}
