package com.catwave.demo.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            JwtDecoder vietQrJwtDecoder,
            JwtAuthenticationConverter vietQrJwtConverter) throws Exception {

        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/song", "/auth/**", "/vietqr/**", "/payment/**", "/session/**", "/demo"))
                .authorizeHttpRequests(auth -> auth
                        // public
                        .requestMatchers("/**","/home", "/demo", "/login", "/css/**", "/js/**", "/testPayment", "/register").permitAll()
                        .requestMatchers("/api/auth/registration", "/api/auth/login").permitAll()
                        .requestMatchers("/api/session/setCookie").permitAll()
                        .requestMatchers("/api/session/getCookie").permitAll()
                        .requestMatchers("/vietqr/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payment/token_generate").permitAll()
                        // the sync endpoint *must* be authenticated
                        .requestMatchers(HttpMethod.POST, "/api/payment/transactions/sync").authenticated()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(vietQrJwtDecoder) // ← actual decoder bean
                                .jwtAuthenticationConverter(vietQrJwtConverter) // ← and converter for authorities
                        ))
                .formLogin(form -> form
                        .loginPage("/login") // your POST login form endpoint
                        // .loginProcessingUrl("/api/auth/login") // your POST login form endpoint
                        .defaultSuccessUrl("/demo", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout") // your logout endpoint
                        .logoutSuccessUrl("/login?logout") // where to redirect after logout
                        .invalidateHttpSession(true) // invalidate session
                        .deleteCookies("JSESSIONID") // delete the session cookie
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Custom login page
                        .defaultSuccessUrl("/home", true)); // this is enough for standard Google OAuth2 login

        return http.build();
    }

    @Bean
    public JwtDecoder vietQrJwtDecoder(VietQrProperties props) {
        // 1) take the ASCII bytes of the password, not the Base64-decoded bytes
        byte[] keyBytes = props.getPassword().getBytes(StandardCharsets.UTF_8);

        // 2) build an HmacSHA512 key
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA512");

        // 3) tell Nimbus to expect HS512
        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter vietQrJwtConverter() {
        JwtAuthenticationConverter conv = new JwtAuthenticationConverter();
        conv.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> auths = jwt.getClaimAsStringList("authorities");
            if (auths == null) {
                return Collections.emptyList();
            }
            // map each String → GrantedAuthority
            return auths.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        });
        return conv;
    }

}
