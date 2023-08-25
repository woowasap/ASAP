package shop.woowasap.auth.infra.configuration;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.woowasap.auth.infra.AuthenticationProvider;
import shop.woowasap.auth.infra.filter.JwtTokenAuthFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    @Value("${shop.woowasap.auth.path.get}")
    private String[] getPaths;

    @Value("${shop.woowasap.auth.path.post}")
    private String[] postPaths;

    @Value("${shop.woowasap.auth.path.admin}")
    private String[] adminPaths;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
            .addFilterBefore(new JwtTokenAuthFilter(authenticationProvider),
                UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers(HttpMethod.GET, getPaths).permitAll()
                    .requestMatchers(HttpMethod.POST, postPaths).permitAll()
                    .requestMatchers(adminPaths).hasRole("ADMIN")
                    .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
