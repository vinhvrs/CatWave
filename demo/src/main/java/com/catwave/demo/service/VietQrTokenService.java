package com.catwave.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.catwave.demo.config.VietQrProperties;
import com.catwave.demo.model.VietQrTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.catwave.demo.config.VietQrProperties;

@Service
public class VietQrTokenService {
    private final VietQrProperties props;
    private final RestTemplate rest;

    public VietQrTokenService(VietQrProperties props) {
        this.props = props;
        this.rest  = new RestTemplate();
    }

    public VietQrTokenResponse fetchToken() {
        String creds = props.getUsername() + ":" + props.getPassword();
        String basic = Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basic);
        // no need to set Content-Type for GET

        // Use GET, not POST
        ResponseEntity<VietQrTokenResponse> resp = rest.exchange(
            props.getTokenUrl(),
            HttpMethod.GET,
            new HttpEntity<>(headers),
            VietQrTokenResponse.class
        );

        return resp.getBody();
    }
}
