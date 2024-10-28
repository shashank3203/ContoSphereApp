package com.shashank.ContoSphere.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailsService userDetailsService;
    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests
                        (configurer ->
                                configurer // We are using opposite or normal method
                                        .requestMatchers("/user/**").authenticated()
                                        .anyRequest().permitAll()

                        )

                .formLogin
                        (formLogin ->
                                        formLogin.loginPage("/contosphere/login")
                                                .loginProcessingUrl("/user/authenticate")
                                                .successForwardUrl("/user/signed-in")
//                    .failureUrl("/login?error=true")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                        )
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout.permitAll())
                .exceptionHandling(configurer ->
                        configurer
                                .accessDeniedPage("/access-denied"));
                httpSecurity.oauth2Login(oauth ->
                        oauth.loginPage("/contosphere/login")
                                .successHandler(oAuthSuccessHandler)
                );
        return httpSecurity.build();
    }
}


/* Just for testing Purpose

    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user1= User.builder()
                .username("user1")
                .password("{noop}test")
                .roles("ADMIN")
                .build();
        UserDetails user2= User.builder()
                .username("user2")
                .password("{noop}test")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2);
    }

 */
