package com.underdogCounty.underdogCountyProject.domain.login.config;

import com.underdogCounty.underdogCountyProject.domain.login.filter.JwtAuthenticationFilter;
import com.underdogCounty.underdogCountyProject.domain.login.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            //login
            .antMatchers("/api/members/login").permitAll()
//            //about
//            .antMatchers(HttpMethod.GET,"/api/about/**").permitAll()
//            .antMatchers(HttpMethod.POST,"/api/about").hasRole("USER")
//            .antMatchers(HttpMethod.PUT,"/api/about/**").hasRole("USER")
//            .antMatchers(HttpMethod.DELETE,"/api/about/**").hasRole("USER")
            //application
            .antMatchers(HttpMethod.GET,"/api/application/**").hasRole("USER")
            .antMatchers(HttpMethod.POST,"/api/application").permitAll()
            .antMatchers(HttpMethod.PUT,"/api/application/**").hasRole("USER")
            .antMatchers(HttpMethod.DELETE,"/api/application/**").hasRole("USER")
            //artist
            .antMatchers(HttpMethod.GET,"/api/artist/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/artist").hasRole("USER")
            .antMatchers(HttpMethod.PUT,"/api/artist/**").hasRole("USER")
            .antMatchers(HttpMethod.DELETE,"/api/artist/**").hasRole("USER")
            .antMatchers(HttpMethod.POST,"/api/artist/s3/**").hasRole("USER")
            .antMatchers(HttpMethod.DELETE,"/api/artist/**").hasRole("USER")
            //work
            .antMatchers(HttpMethod.GET,"/api/work/**").permitAll()
            .antMatchers(HttpMethod.POST,"/api/work").hasRole("USER")
            .antMatchers(HttpMethod.PUT,"/api/work/**").hasRole("USER")
            .antMatchers(HttpMethod.DELETE,"/api/work/**").hasRole("USER")
            .antMatchers(HttpMethod.POST,"/api/work/s3/**").hasRole("USER")
            .antMatchers(HttpMethod.DELETE,"/api/work/s3/**").hasRole("USER")
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
