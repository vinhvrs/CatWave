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
import org.springframework.web.bind.annotation.*;

import com.catwave.demo.model.Member;
import com.catwave.demo.model.LoginRequest;
import com.catwave.demo.repository.MemRepo;
import com.catwave.demo.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
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

    @PostMapping("/token_generate")
    public ResponseEntity<?> tokenGenerate(@RequestHeader("Authorization") String authHeader, 
                                           @RequestParam(value="grant_type", defaultValue="client_credentials") String grantType) {
        if (!"client_credentials".equals(grantType)) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "unsupported_grant_type"));
        }

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Basic ")) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "invalid_client"));
        }

        String base64Creds = authHeader.substring(6);
        String decoded    = new String(Base64.getDecoder().decode(base64Creds), StandardCharsets.UTF_8);
        String[] parts    = decoded.split(":", 2);
        if (parts.length != 2) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_client"));
        }

        String clientId = parts[0], clientSecret = parts[1];
        Member m = memRepo.findByUsername(clientId);
        if (m == null || !passwordEncoder.matches(clientSecret, m.getPassword())) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "invalid_client"));
        }

        String jwt = jwtService.generateToken(clientId);
        return ResponseEntity.ok(Map.of(
          "access_token", jwt,
          "token_type",   "bearer",
          "expires_in",   String.valueOf(60 * 60)
        ));
    }

    @PostMapping("/getToken")
    public ResponseEntity<Map<String, String>> getToken(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "No authentication"));
        }

        String clientRegId = authentication.getAuthorizedClientRegistrationId();
        String principal = authentication.getName();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(clientRegId, principal);

        if (client == null || client.getAccessToken() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "No OAuth client/token"));
        }

        String accessToken = client.getAccessToken().getTokenValue();
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

    @PostMapping("/registration")
    public ResponseEntity<String> memberRegister(@RequestBody Member member) {
        if (member.getUsername() == null || member.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (member.getPassword() == null || member.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        if (memRepo.findByUsername(member.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memRepo.save(member);
        return ResponseEntity.ok("Member registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Member> memberLogin(@RequestBody LoginRequest loginRequest) {
        System.out.println("🔧 [DEBUG] Login body username: " + loginRequest.getUsername());

        Member member = memRepo.findByUsername(loginRequest.getUsername());
        if (member == null) {
            System.out.println("⚠️ [WARN] Member not found.");
            return ResponseEntity.status(404).body(null);
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            System.out.println("❌ [WARN] Invalid password.");
            return ResponseEntity.status(401).body(null);
        }

        System.out.println("✅ [INFO] Login success for: " + member.getUsername());
        return ResponseEntity.ok(member);
    }

    @GetMapping("/logout")
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

    @GetMapping("/oauth2")
    public String oauth2(OAuth2AuthenticationToken authentication, HttpServletRequest request, HttpServletResponse response) {
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

            Cookie cookie = new Cookie("sessionId", session.getId());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60*60);
            response.addCookie(cookie);
            return "redirect:/home";
        }

        Member newMember = new Member();
        newMember.setEmail(email);
        newMember.setUsername(username != null ? username : email.split("@")[0]);
        newMember.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
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
        session.setMaxInactiveInterval(60*60);

        Cookie cookie = new Cookie("sessionId", session.getId());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);

        return "redirect:/home";
    }
}
