package com.catwave.demo.controller;
import com.catwave.demo.repository.MemRepo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class PageCotroller {
    @Autowired
    private MemRepo memRepo;

    @GetMapping("/")
    public String index() {
        return "homepage";
    }

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

    @GetMapping("/testPlayer")
    public String testPlayer() {
        return "testPlayer";
    }

    @GetMapping("/testPlaylist")
    public String testPlaylist() {
        return "testPlaylist";
    }
    
    @GetMapping("/connection/info")
    public String connectionInfo(){
        return "connection";
    }

    @GetMapping("/auth_test")
    public String authTest(){
        return "auth_test";
    }
    
}
