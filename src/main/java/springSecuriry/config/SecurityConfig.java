package springSecuriry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springSecuriry.jwt.JwtAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;  
    private final AuthenticationProvider authProvider;  

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF (no necesario con JWT)

                .authorizeHttpRequests(authRequest -> authRequest
                .requestMatchers("/api/auth/**").permitAll() // Permite acceso público a login/register
                .anyRequest().authenticated() // Todo lo demás requiere autenticación
                )
                .sessionManagement(sessionManager -> sessionManager
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Sin sesiones (usa JWT)

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) //  Añade el filtro JWT
                .authenticationProvider(authProvider) // ✅ Configura el proveedor de autenticación
                .build();
    }
}
