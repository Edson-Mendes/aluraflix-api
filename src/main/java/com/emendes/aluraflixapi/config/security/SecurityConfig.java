package com.emendes.aluraflixapi.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain web(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(HttpMethod.GET, "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/favicon.ico")
        .permitAll()
        .antMatchers(HttpMethod.POST, "/auth/signup").permitAll();
    http.authorizeRequests().anyRequest().authenticated();

    http.httpBasic(withDefaults()).userDetailsService(userDetailsService());
    http.csrf().disable();

    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user1 = User.withDefaultPasswordEncoder()
        .username("user")
        .password("123456")
        .roles("USER")
        .build();

    return new InMemoryUserDetailsManager(user1);
  }

  @Bean
  PasswordEncoder encoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

}
