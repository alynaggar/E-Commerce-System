package com.example.amazon.Config;

import org.springframework.context.ApplicationEvent;

public class EmailEvent extends ApplicationEvent {
    private String email;
    private String otp;
    private String subject;
    private String username;

    public EmailEvent(Object source, String email, String otp, String subject, String username) {
        super(source);
        this.email = email;
        this.otp = otp;
        this.subject = subject;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getOtp() {
        return otp;
    }

    public String getSubject() {
        return subject;
    }
}