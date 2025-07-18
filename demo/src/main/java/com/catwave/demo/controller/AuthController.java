package com.catwave.demo.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;

import com.catwave.demo.model.Member;
import com.catwave.demo.repository.MemRepo;
import com.catwave.demo.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private MemRepo memRepo;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;
    @Autowired 
    private JwtService jwtService;
    @Autowired 
    private PasswordEncoder passwordEncoder;

        HttpSession session;


    @PostMapping("/api/token_generate")
    public ResponseEntity<?> tokenGenerate(@RequestHeader("Authorization") String authHeader, @RequestParam(value="grant_type", defaultValue="client_credentials") String grantType) {
        if (!"client_credentials".equals(grantType)) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "unsupported_grant_type"));
        }

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "invalid_client"));
        }

        // Decode Basic credentials
        String base64Creds = authHeader.substring(6);
        String decoded    = new String(Base64.getDecoder().decode(base64Creds), StandardCharsets.UTF_8);
        String[] parts    = decoded.split(":", 2);
        if (parts.length != 2) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_client"));
        }
        String clientId = parts[0], clientSecret = parts[1];

        // Lookup your member record by username
        Member m = memRepo.findByUsername(clientId);
        if (m == null || !passwordEncoder.matches(clientSecret, m.getPassword())) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "invalid_client"));
        }

        // Issue a JWT
        String jwt = jwtService.generateToken(clientId);
        return ResponseEntity.ok(Map.of(
          "access_token", jwt,
          "token_type",   "bearer",
          "expires_in",   String.valueOf(60 * 60)
        ));
    }

    @PostMapping("/api/auth/getToken")
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

    @PostMapping("/api/auth/registration")
    public ResponseEntity<String> memberRegister(@RequestBody Member member) {
        if (member.getUsername() == null || member.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        String username = member.getUsername();
        if (memRepo.findByUsername(username) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        String encodePassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodePassword);
        memRepo.save(member);
        return ResponseEntity.ok("Member registered successfully");

    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Member> memberLogin(@RequestBody Member loginMember) {
        Member member = memRepo.findByUsername(loginMember.getUsername());
        String password = loginMember.getPassword();
        if (member == null) {
            return ResponseEntity.status(404).body(null);
        }
        if (!passwordEncoder.matches(password, member.getPassword())) {
            return ResponseEntity.status(401).body(null);
        }
        return ResponseEntity.ok(member);
    }

    @GetMapping("/api/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie("sessionId", null);
        cookie.setDomain(request.getServerName());
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
    


    @GetMapping("/api/auth/oauth2")
    public String oauth2(OAuth2AuthenticationToken authentication,
        HttpServletRequest request,
        HttpServletResponse response) {
        OAuth2User user = authentication.getPrincipal();

        String username = user.getAttribute("name");
        String email = user.getAttribute("email");
        String phoneString = user.getAttribute("phone");

        if (email == null) {
            return "redirect:/home";
        }

        Member existingMember = memRepo.findByEmail(email);
        if (existingMember != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("uid", existingMember.getUID().toString());
            session.setAttribute("username", existingMember.getUsername());
            session.setMaxInactiveInterval(60*60);

            // Create a cookie that holds the session ID
            Cookie cookie = new Cookie("sessionId", session.getId());
            cookie.setPath("/");               // send cookie on all paths
            cookie.setHttpOnly(true);          // JavaScript cannot read this cookie
            cookie.setMaxAge(60*60); // expire in 1 hour
            response.addCookie(cookie);
            return "redirect:/home";
        }

        Member newMember = new Member();
        newMember.setEmail(email);
        newMember.setUsername(username != null ? username : email.split("@")[0]);

        String randomPassword = UUID.randomUUID().toString();
        newMember.setPassword(passwordEncoder.encode(randomPassword));
        newMember.setPhone(phoneString);

        try {
            memRepo.save(newMember);
        } catch (DataIntegrityViolationException ex) {
            existingMember = memRepo.findByEmail(email);
            HttpSession session = request.getSession(true);
            session.setAttribute("uid", existingMember.getUID().toString());
            session.setAttribute("username", existingMember.getUsername());
            session.setMaxInactiveInterval(60*60);

            Cookie cookie = new Cookie("sessionId", session.getId());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60*60);
            response.addCookie(cookie);
            return "redirect:/home";
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("uid", newMember.getUID().toString());
        session.setAttribute("username", newMember.getUsername());
        session.setMaxInactiveInterval(60*60); // 1 hour

        Cookie cookie = new Cookie("sessionId", session.getId());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);

        return "redirect:/home";
    }

}
