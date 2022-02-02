package com.epam.bank.operatorinterface.configuration.security.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@AllArgsConstructor
public class BasicAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        response.setHeader(HttpHeaders.AUTHORIZATION, authentication.getCredentials().toString());
        response.setStatus(HttpStatus.OK.value());
    }
}