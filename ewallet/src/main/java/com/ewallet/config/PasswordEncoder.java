package com.ewallet.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    private final BCryptPasswordEncoder bcryptPasswordEncoder;

    public PasswordEncoder() {
        this.bcryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    public String encode(String rawPassword) {
        return bcryptPasswordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String hashedPassword) {
        return bcryptPasswordEncoder.matches(rawPassword, hashedPassword);
    }
}
