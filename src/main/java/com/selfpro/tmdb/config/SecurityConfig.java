package com.selfpro.tmdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests()
                .requestMatchers("/api/public/**").permitAll()
//                .requestMatchers("/api/public/**").hasAnyRole("ADMIN") // if roles setting code
                // Add Defferent Roles here
//                .requestMatchers("/app/admin/++").hasRole("SUPER_ADMIN")

                // Common Admin Role both two users
                .requestMatchers("/app/admin/++").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and().httpBasic();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails adminUser = User.withUsername(adminUsername)
//                .password(passwordEncoder().encode(adminPassword))
//                .roles("ADMIN")
//                .build();


        // How to add more role

//        UserDetails adminUser = User.withUsername(adminUsername)
//                .password(passwordEncoder().encode(adminPassword))
//                .roles("NORMAL_ADMIN")
//                .build();
//
//        UserDetails superAmin = User.withUsername("super"+adminUsername)
//                .password(passwordEncoder().encode("super"+adminPassword))
//                .roles("SUPER_ADMIN")
//                .build();


        UserDetails adminUser = User.withUsername(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("NORMAL_ADMIN", "ADMIN")
                .build();

        UserDetails superAmin = User.withUsername("super"+adminUsername)
                .password(passwordEncoder().encode("super"+adminPassword))
                .roles("SUPER_ADMIN", "ADMIN")
                .build();

//        return new InMemoryUserDetailsManager(adminUser);
        return new InMemoryUserDetailsManager(adminUser, superAmin);
    }

}
