package com.catwave.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.print.attribute.standard.Media;

import com.catwave.demo.config.VietQrProperties;
import com.catwave.demo.model.VietQrTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VietQrTokenService {
    private final VietQrProperties props;
    private final RestTemplate rest;

    public VietQrTokenService(VietQrProperties props, RestTemplate rest) {
        this.props = props;
        this.rest = rest;
    }

    public VietQrTokenResponse fetchToken() {
        String creds = props.getUsername() + ":" + props.getPassword();
        // String basic = Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(props.getUsername(), props.getPassword());
        // headers.set(HttpHeaders.AUTHORIZATION, "Basic " + basic);
        // headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> req = new HttpEntity<>("{}", headers);

        ResponseEntity<VietQrTokenResponse> resp = rest.exchange(
                props.getTokenUrl(),
                HttpMethod.GET,
                req,
                VietQrTokenResponse.class);

        VietQrTokenResponse body = resp.getBody();
        if (body == null || body.getAccess_token() == null) {
            throw new RuntimeException("Failed to fetch VietQR token");
        }

        return resp.getBody();
    }

    public String getToken() {
        VietQrTokenResponse tokenResp = fetchToken();
        if (tokenResp == null || tokenResp.getAccess_token() == null) {
            throw new RuntimeException("Failed to fetch VietQR token");
        }
        return tokenResp.getAccess_token();
    }
}
