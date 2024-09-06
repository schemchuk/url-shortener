package de.telran.urlshortener.config;

import de.telran.urlshortener.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder()
//                .username("tom")
//                .password(encoder().encode("password"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(encoder().encode("password"))
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
//
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * The JWT filter bean for processing JWT tokens.
     */
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures the security filter chain.
     * <p>
     * This method sets up the security configurations such as CSRF disabling, session management policy,
     * authorization rules and adds the JWT filter after the UsernamePasswordAuthenticationFilter.
     * </p>
     *
     * @param http the HttpSecurity instance to configure.
     * @return the SecurityFilterChain instance.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers(
                                        "/r{shortKey}",
                                        "/users",
                                        "/api/auth/login",
                                        "/api/auth/token",
                                        "/swagger-ui.html",
                                        "/api/v1/auth/**",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/h2-console/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                ).addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // Разрешаем запросы со всех источников (настроить для продакшена)
        configuration.addAllowedMethod("*"); // Разрешаем все методы
        configuration.addAllowedHeader("*"); // Разрешаем любые заголовки
        configuration.setAllowCredentials(true); // Если используются куки
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
