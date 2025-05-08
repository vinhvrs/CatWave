package com.catwave.demo.controller;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import com.catwave.demo.model.Member;
import com.catwave.demo.repository.MemRepo;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {
    @Autowired
    private MemRepo memRepo;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    HttpSession session;

    @GetMapping("/oauth2")
    public String home(Member member, OAuth2AuthenticationToken authentication, HttpSession session) {
        OAuth2User user = authentication.getPrincipal();
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");
        String phoneString = user.getAttribute("phone");

        session.setAttribute("name", name);

        Member existingMember = memRepo.findByEmail(email);
        if (existingMember != null) {
            UUID uid = existingMember.getUID();
            session.setAttribute("uid", uid);
            return "login";
        }

        member.setUsername(name);
        member.setEmail(email);
        member.setPassword("defaultpassword");
        member.setPhone(phoneString);

        memRepo.save(member);
        existingMember = memRepo.findByEmail(email);
        UUID uid = existingMember.getUID();
        session.setAttribute("uid", uid);
        session.setMaxInactiveInterval(3600); // 1 hour

        return "login";
    }

    @GetMapping("auth/setCookie")
    public ResponseEntity<Map<String, String>> setCookie(@RequestBody Member memInfo, HttpServletRequest request,
            HttpServletResponse response) {
        String email = memInfo.getEmail();
        String username = memInfo.getUsername();
        UUID uid = memRepo.findUIDByUsername(username);
        if (uid == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
        }
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

        return ResponseEntity.ok(Map.of("message", "Cookie set successfully"));
    }

    @GetMapping("auth/getCookie")
    public ResponseEntity<Map<String, String>> getCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("sessionId")) {
                    return ResponseEntity.ok(Map.of("sessionId", cookie.getValue()));
                }
            }
        }
        return ResponseEntity.badRequest().body(Map.of("message", "No session cookie found"));
    }

    @GetMapping("auth/getToken")
    public ResponseEntity<Map<String, String>> getToken(
            OAuth2AuthenticationToken authentication) {

        if (authentication == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "No authentication"));
        }

        String clientRegId = authentication.getAuthorizedClientRegistrationId();
        String principal = authentication.getName();

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientRegId, principal);

        if (client == null || client.getAccessToken() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "No OAuth client/token"));
        }

        String accessToken = client.getAccessToken().getTokenValue();
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

}
