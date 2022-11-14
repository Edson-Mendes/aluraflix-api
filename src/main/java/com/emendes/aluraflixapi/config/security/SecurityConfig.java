package com.emendes.aluraflixapi.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

  private final UserDetailsService userDetailsService;

  @Bean
  public SecurityFilterChain web(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/auth/signup").permitAll();
    http.authorizeRequests().anyRequest().authenticated();

    http.httpBasic(withDefaults()).userDetailsService(userDetailsService)
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.csrf().disable();

    return http.build();
  }

  @Bean
  PasswordEncoder encoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
