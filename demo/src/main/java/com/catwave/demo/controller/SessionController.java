package com.catwave.demo.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.catwave.demo.model.Member;
import com.catwave.demo.repository.MemRepo;
import com.catwave.demo.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

    @Autowired
    private MemRepo memRepo;

    private HttpSession session;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    

    @PostMapping("/api/session/setCookie")
    public ResponseEntity<Map<String, Object>> setCookie(@RequestBody UUID uid, HttpServletRequest request,
            HttpServletResponse response) {
        
        Member memInfo = memRepo.findByUID(uid);
        if (memInfo == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }
        
        String email = memInfo.getEmail();
        String username = memInfo.getUsername();

        session = request.getSession();
        session.setAttribute("email", email);
        session.setAttribute("username", username);
        session.setAttribute("uid", uid);
        session.setMaxInactiveInterval(60 * 60); // 1 hour

        Cookie cookie = new Cookie("sessionId", session.getId()); // Create a new cookie
        cookie.setPath("/"); // Set the path for the cookie
        cookie.setMaxAge(60 * 60); // Set the max age for the cookie (1 hour)
        cookie.setHttpOnly(true); // Set the cookie to be HTTP only
        cookie.setSecure(true); // Set the cookie to be secure (only sent over HTTPS)
        response.addCookie(cookie); // Add the cookie to the response

        return ResponseEntity.ok(Map.of(
            "message", "Session cookie set successfully",
            "sessionId", session.getId(),
            "email", email,
            "username", username,
            "uid", uid.toString()
        ));
    }

    @GetMapping("/api/session/getCookie")
    public ResponseEntity<Map<String, Object>> getCookie(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            // no session at all
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error","Not logged in"));
        }

        // Attempt to read the stored attributes:
        UUID uid         = (UUID)      session.getAttribute("uid");
        String username  = (String)    session.getAttribute("username");
        String email     = (String)    session.getAttribute("email");
        if (uid == null) {
            // Maybe the session is old or not our login
            session.invalidate();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error","Invalid session"));
        }

        // If everything looks good, return the user’s info:
        return ResponseEntity.ok(Map.of(
            "uid",      uid.toString(),
            "username", username,
            "email",    email
        ));
    }

    
}
