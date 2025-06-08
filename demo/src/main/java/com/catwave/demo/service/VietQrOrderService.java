package com.catwave.demo.service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.catwave.demo.config.VietQrProperties;
import com.catwave.demo.model.TransactionDto;
import com.catwave.demo.model.VietQrOrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class VietQrOrderService {
    private final VietQrTokenService tokenService;
    private final VietQrProperties props;
    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public VietQrOrderService(VietQrTokenService tokenService, VietQrProperties props) {
        this.tokenService = tokenService;
        this.props = props;
    }

    public VietQrOrderResponse generateQr(TransactionDto dto) throws Exception {
        // 1) Lấy Bearer token
        String bearer = tokenService.getToken();

        // 2) Xây dựng payload theo doc VietQR
        Map<String,Object> payload = new HashMap<>();
        payload.put("bankCode",     props.getBankCode());
        payload.put("bankAccount",  props.getBankAccount());
        payload.put("userBankName", props.getUserBankName());
        payload.put("content",      dto.getDescription());
        payload.put("qrType",       dto.getQrType());        // 0/1/3
        payload.put("transType",    dto.getTransType());     // "C" hoặc "D"
        // Chỉ add amount nếu qrType=0 hoặc 3
        if (dto.getAmount()!=null) payload.put("amount", dto.getAmount());
        // Chỉ add orderId nếu qrType=0
        if (dto.getOrderId()!=null) payload.put("orderId", dto.getOrderId());
        // Terminal/Service cho static/semi-dynamic
        if (dto.getTerminalCode()!=null) payload.put("terminalCode", dto.getTerminalCode());
        if (dto.getServiceCode()!=null)  payload.put("serviceCode", dto.getServiceCode());
        // URL redirect sau quét
        if (dto.getReturnUrl()!=null)    payload.put("urlLink", dto.getReturnUrl());
        // Ghi chú & thêm data nếu cần
        payload.put("note",           dto.getDescription());
        // payload.put("additionalData", dto.getAdditionalData() != null
        //                                ? dto.getAdditionalData()
        //                                : Collections.emptyList());

        String rawJson = mapper.writeValueAsString(payload);

        // 3) Tính chữ ký HMAC‐SHA256 nếu VietQR bắt buộc (header X-Signature)
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key = new SecretKeySpec(
            props.getPassword().getBytes(StandardCharsets.UTF_8),
            "HmacSHA256"
        );
        hmac.init(key);
        String signature = bytesToHex(hmac.doFinal(rawJson.getBytes(StandardCharsets.UTF_8)));

        // 4) Tạo headers: Authorization + Content-Type + X-Signature
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearer);
        headers.set("X-Signature", signature);

        // 5) Gọi API Generate VietQR Code
        HttpEntity<String> req = new HttpEntity<>(rawJson, headers);
        ResponseEntity<VietQrOrderResponse> resp = rest.exchange(
            props.getCreateOrderUrl(),
            HttpMethod.POST,
            req,
            VietQrOrderResponse.class
        );

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody()==null) {
            throw new RuntimeException("Tạo QR thất bại: HTTP " +
                resp.getStatusCodeValue());
        }
        return resp.getBody();
    }

    private static String bytesToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b: data) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
