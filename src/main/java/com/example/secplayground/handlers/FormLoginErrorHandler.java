package com.example.secplayground.handlers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class FormLoginErrorHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String code = switch (exception) {
            case DisabledException e              -> "disabled";
            case LockedException e                -> "locked";
            case CredentialsExpiredException e    -> "credExpired";
            case AccountExpiredException e        -> "acctExpired";
            default                               -> "bad";
        };

        getRedirectStrategy().sendRedirect(request, response, "/login?error=" + code);
    }
}
