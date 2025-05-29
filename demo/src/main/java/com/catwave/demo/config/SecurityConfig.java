package com.catwave.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        // 1) Disable CSRF for all /api/** endpoints
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
                .ignoringRequestMatchers("/auth/**") // Disable CSRF for auth endpoints
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**","/home", "/error", "/login", "/api/**", "/css/**", "/js/**", "/images/**", "/demo", "/testPlayer").permitAll() // Allow API access without auth
                .requestMatchers(HttpMethod.GET,"/auth/connetion/info").permitAll() // Allow connection info without auth
                .requestMatchers(HttpMethod.POST,"/auth/token_generate").permitAll() 
                .requestMatchers("/auth/**").authenticated() // Require auth for all other auth endpoints
                .requestMatchers("/api/transactions/sync").authenticated()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            // enable form-login
            .formLogin(form -> form
                .loginPage("/login")                // your GET login form endpoint
                // .loginProcessingUrl("/api/login") // the POST action
                .defaultSuccessUrl("/demo", true)
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Custom login page
                .defaultSuccessUrl("/home", true)
            ); // this is enough for standard Google OAuth2 login

        return http.build();
    }
}
