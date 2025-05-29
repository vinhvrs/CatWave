package com.catwave.demo.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final String secret = "ReplaceThisWithAReallyLongRandomSecretKey";
    private final long expiration = 1000 * 60 * 60; // 1h

    public String generateToken(String subject) {
        Date now = new Date();
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + expiration))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }
}
