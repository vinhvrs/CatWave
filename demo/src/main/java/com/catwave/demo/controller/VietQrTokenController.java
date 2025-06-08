package com.catwave.demo.controller;

import com.catwave.demo.config.VietQrProperties;
import com.catwave.demo.model.VietQrTokenResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class VietQrTokenController {

    private final VietQrProperties props;
    private final RestTemplate rest; // ← inject, don’t new it up

    public VietQrTokenController(
            VietQrProperties props,
            RestTemplate rest // ← Spring will wire your customized RestTemplate bean here
    ) {
        this.props = props;
        this.rest = rest;
    }

    @PostMapping("/api/payment/token_generate")
    public ResponseEntity<VietQrTokenResponse> fetchToken() {
        // build “Basic ” header from raw username:password
        String creds = props.getUsername() + ":" + props.getPassword();
        String basic = Base64.getEncoder()
                .encodeToString(creds.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basic);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // POST with no body
        ResponseEntity<VietQrTokenResponse> resp = rest.exchange(
                props.getTokenUrl(),
                HttpMethod.POST,
                new HttpEntity<>(headers),
                VietQrTokenResponse.class);

        return resp;
    }
}
