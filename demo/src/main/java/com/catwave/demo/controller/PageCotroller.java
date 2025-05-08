package com.catwave.demo.controller;
import com.catwave.demo.model.Member;
import com.catwave.demo.repository.MemRepo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageCotroller {
    @Autowired
    private MemRepo memRepo;

    @GetMapping("/home")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }
    
}
