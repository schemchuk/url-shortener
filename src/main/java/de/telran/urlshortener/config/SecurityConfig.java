package de.telran.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем защиту от CSRF для упрощения тестирования
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/h2-console/**").permitAll() // Разрешаем доступ к H2 консоли
                                .requestMatchers("/public/**").permitAll() // Разрешаем доступ к публичным URL
                                .anyRequest().permitAll() // Разрешаем доступ ко всем остальным URL
                )
                .logout(logout ->
                        logout.permitAll() // Разрешаем доступ к logout
                )
                .headers(headers ->
                        headers.frameOptions().disable() // Разрешаем использование фреймов (необходимо для H2 консоли)
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**"); // Игнорируем H2 консоль для настроек безопасности
    }
}
