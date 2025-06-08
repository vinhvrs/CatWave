package com.catwave.demo.controller;

import java.util.Map;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    

    @GetMapping("/api/session/setCookie/{uid}")
    public ResponseEntity<Map<String, Object>> setCookie(@PathVariable UUID uid, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Setting session cookie for UID: " + uid);
        Member memInfo = memRepo.findByUID(uid);
        if (memInfo == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }
        
        String email = memInfo.getEmail();
        String username = memInfo.getUsername();

        session = request.getSession();
        session.setAttribute("email", email);
        session.setAttribute("username", username);
        session.setAttribute("uid", uid.toString());
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

    @GetMapping("/api/session/validateCookie")
    public ResponseEntity<Map<String, Object>> validateCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if ("sessionId".equals(cookie.getName())){
                    String sessionId = cookie.getValue();
                    HttpSession session = request.getSession(false);
                    if (session != null && sessionId.equals(session.getId())){
                        String username = (String) session.getAttribute("username");
                        String uid = (String) session.getAttribute("uid");

                        if (username != null && uid != null) {
                            return ResponseEntity.ok(Map.of(
                                "message", "Session is valid",
                                "username", username,
                                "uid", uid
                            ));
                        } else {
                            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Session attributes not found"));
                        }
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Session cookie not found or invalid"));
    }
    
    
}