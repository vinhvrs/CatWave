package com.catwave.demo.model;

public class VietQrTokenResponse {
    private String access_token;
    private String token_type;
    private String expires_in;

    // getters & setters
    public String getAccess_token() { return access_token; }
    public void setAccess_token(String t) { this.access_token = t; }
    public String getToken_type() { return token_type; }
    public void setToken_type(String t) { this.token_type = t; }
    public String getExpires_in() { return expires_in; }
    public void setExpires_in(String e) { this.expires_in = e; }
}
