package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //Configuration One
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                //.authorizeHttpRequests().requestMatchers("/v1/index2").permitAll().anyRequest().authenticated().and()
//                //.formLogin().permitAll().and()
//                .build();
//    }

    //Configuration Two
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .authorizeHttpRequests( auth -> {
                    auth.requestMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                } )
                .formLogin( auth -> {
                    auth.successHandler(successHandler()); // URL where you will go after logging in
                    auth.permitAll();
                } )
                .sessionManagement( auth -> {
                    auth.sessionCreationPolicy(SessionCreationPolicy.ALWAYS); //ALWAYS - IF_REQUIRED - NEVER - STATELESS
                    auth.invalidSessionUrl("/login");
                    auth.maximumSessions(1);
                    auth.sessionFixation()
                            .migrateSession(); // Upon detecting the attack, Spring migrate a new session
                            //.newSession(); //Upon detecting the attack, Spring generate a new session
                } )
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
            response.sendRedirect("/v1/session");
        });
    }

}
