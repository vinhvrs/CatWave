package com.catwave.demo.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // @Bean
        // public JwtDecoder vietQrJwtDecoder(VietQrProperties props) {
        // // 1) Decode the Base64 password VietQR gave you
        // byte[] keyBytes = Base64.getDecoder().decode(props.getPassword());
        // // 2) Build an HmacSHA512 SecretKey
        // SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length,
        // "HmacSHA512");

        // // 3) Tell Nimbus to expect HS512
        // return NimbusJwtDecoder
        // .withSecretKey(key)
        // .macAlgorithm(MacAlgorithm.HS512)
        // .build();
        // }

        // @Bean
        // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http
        // .csrf(csrf -> csrf
        // .ignoringRequestMatchers("/vietqr/**", "/payment/**"))
        // .authorizeHttpRequests(auth -> auth
        // // public paths:
        // .requestMatchers("/", "/login", "/css/**", "/js/**").permitAll()
        // .requestMatchers("/vietqr/token").permitAll() // fetch VietQR token
        // .requestMatchers(HttpMethod.POST, "/payment/api/token_generate").permitAll()
        // // protect the sync endpoint:
        // .requestMatchers(HttpMethod.POST, "/payment/api/transactions/sync")
        // .authenticated()
        // .anyRequest().authenticated())
        // .oauth2ResourceServer(oauth2 -> oauth2
        // .jwt(Customizer.withDefaults())) // this will pick up *that* JwtDecoder bean
        // // JWT Bearer on protected endpoints
        // // .oauth2ResourceServer(oauth2 -> oauth2
        // // .jwt(Customizer.withDefaults()))
        // // .jwt(jwt -> jwt.decoder(vietQrJwtDecoder(null))))
        // // (plus formLogin / oauth2Login if you still need them)
        // ;

        // return http.build();
        // }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http,
                        JwtDecoder vietQrJwtDecoder,
                        JwtAuthenticationConverter vietQrJwtConverter) throws Exception {

                http
                                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/auth/**", "/vietqr/**",
                                                "/payment/**", "/session/**", "/demo"))
                                .authorizeHttpRequests(auth -> auth
                                                // public
                                                .requestMatchers("/**", "/home", "/demo", "/login", "/css/**", "/js/**",
                                                                "/testPayment", "/register")
                                                .permitAll()
                                                .requestMatchers("/api/registation", "/api/login").permitAll()
                                                .requestMatchers("/vietqr/token").permitAll()
                                                .requestMatchers(HttpMethod.POST, "/payment/api/token_generate")
                                                .permitAll()
                                                // the sync endpoint *must* be authenticated
                                                .requestMatchers(HttpMethod.POST, "/payment/api/transactions/sync")
                                                .authenticated()
                                                .anyRequest().authenticated())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt
                                                                .decoder(vietQrJwtDecoder) // ← actual decoder bean
                                                                .jwtAuthenticationConverter(vietQrJwtConverter) // ← and
                                                                                                                // converter
                                                                                                                // for
                                                                                                                // authorities
                                                ))
                                .formLogin(form -> form
                                                .loginPage("/login") // your GET login form endpoint
                                                // .loginProcessingUrl("/api/login") // the POST action
                                                .defaultSuccessUrl("/demo", true)
                                                .permitAll())
                                .oauth2Login(oauth2 -> oauth2
                                                .loginPage("/login") // Custom login page
                                                .defaultSuccessUrl("/home", true)); // this is enough for standard
                                                                                    // Google OAuth2 login

                return http.build();
        }

        @Bean
        public JwtDecoder vietQrJwtDecoder(VietQrProperties props) {
                byte[] keyBytes = Base64.getDecoder().decode(props.getPassword());
                SecretKey key = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA512");
                return NimbusJwtDecoder.withSecretKey(key)
                                .macAlgorithm(MacAlgorithm.HS512)
                                .build();
        }

}
