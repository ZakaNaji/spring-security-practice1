package com.example.secplayground.events;

import com.example.secplayground.model.VerificationToken;
import org.springframework.context.ApplicationEvent;

public class CustomerRegistredEvent {
    private String username;
    private VerificationToken token;

    public CustomerRegistredEvent(String username, VerificationToken token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public VerificationToken getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(VerificationToken token) {
        this.token = token;
    }
}
