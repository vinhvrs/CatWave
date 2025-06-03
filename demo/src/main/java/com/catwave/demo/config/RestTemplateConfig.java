package com.catwave.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.catwave.demo.model.TransactionDto;

import java.util.List;
import java.util.Map;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .additionalMessageConverters(jacksonConverter())
        .build();
  }

  private MappingJackson2HttpMessageConverter jacksonConverter() {
    var converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(List.of(
        MediaType.APPLICATION_JSON,
        MediaType.TEXT_HTML,
        MediaType.valueOf("text/html;charset=UTF-8")));
    return converter;
  }

  @Autowired
  private RestTemplate rest;

  public void syncWithVietQr(String accessToken, List<TransactionDto> txs) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<List<TransactionDto>> req = new HttpEntity<>(txs, headers);

    ResponseEntity<Map> resp = rest.exchange(
        "http://localhost:1212/payment/api/transactions/sync",
        HttpMethod.POST,
        req,
        Map.class);

    System.out.println("Sync response: " + resp.getBody());
  }

}
