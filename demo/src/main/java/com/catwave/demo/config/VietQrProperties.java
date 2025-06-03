package com.catwave.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds properties prefixed with "vietqr.*"
 */
@Component
@ConfigurationProperties(prefix = "vietqr")
public class VietQrProperties {
    /** The Base64‐encoded username VietQR gave you. */
    private String username;

    /** The Base64‐encoded password VietQR gave you. */
    private String password;

    /** The URL to call to generate a VietQR access token. */
    private String tokenUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
}
