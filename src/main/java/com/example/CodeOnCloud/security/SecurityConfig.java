package com.example.CodeOnCloud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfig {

    @Autowired
    private JWTAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    private final String[] swagger_endpoints = {"/v3/api-docs/**",
                                                    "/swagger-ui/**",
                                                    "/swagger-resources/*"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // Exception handling
        httpSecurity.exceptionHandling(
                exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        httpSecurity.authorizeHttpRequests( auth -> auth
                        .requestMatchers("/get").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/contest/**").authenticated()
                        .requestMatchers("/contest/running" , "/contest/upcoming").permitAll()
                        .requestMatchers("/question/**").authenticated()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers(swagger_endpoints).permitAll())

                .httpBasic(Customizer.withDefaults());

        httpSecurity.addFilterBefore(jwtAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authorizationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(){
        return new JWTAuthenticationFilter();
    }
}
