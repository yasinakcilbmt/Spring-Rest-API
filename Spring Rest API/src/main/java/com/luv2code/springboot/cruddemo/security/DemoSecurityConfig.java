package com.luv2code.springboot.cruddemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class DemoSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        UserDetails osman = User.builder().username("osman").password("{noop}test123").roles("EMPLOYEE").build();
        UserDetails bmt = User.builder().username("bmt").password("{noop}test123").roles("EMPLOYEE", "MANAGER").build();
        UserDetails yasin = User.builder().username("yasin").password("{noop}test123").roles("EMPLOYEE", "MANAGER", "ADMIN").build();
        UserDetails patrick = User.builder().username("patrick").password("{noop}test123").roles("EMPLOYEE").build();

        return new InMemoryUserDetailsManager(osman, bmt, yasin, patrick);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "api/employees").hasRole("EMPLOYEE").
                        requestMatchers(HttpMethod.GET, "api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "api/employees").hasRole("MANAGER").
                        requestMatchers(HttpMethod.POST, "api/employees").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "api/employees/**").hasRole("ADMIN")
        );
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}