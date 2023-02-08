package com.ravikumar.springbootdemo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http.csrf(csrf -> csrf.disable())
                .cors()
                .and()
                .authorizeRequests(auth -> {
                    //order based decline!!!!
                    auth.requestMatchers("/", "/static/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                                String username = userDetails.getUsername();
                                System.out.println("The user " + username + " has logged in.");
                                boolean hasUserRole = authentication.getAuthorities().stream()
                                        .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
                                boolean hasAdminRole = authentication.getAuthorities().stream()
                                        .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
                                if (hasUserRole){
                                    response.sendRedirect("/user-dashboard");
                                }else if (hasAdminRole){
                                    response.sendRedirect("/admin-dashboard");
                                }
                            }
                        })
                )
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                });

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(this.userDetailsService);
        provider.setPasswordEncoder(this.bCryptPasswordEncoder);
        return provider;
    }
}