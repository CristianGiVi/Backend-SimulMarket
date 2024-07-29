package com.CRUDJPA.JPA.Security;

import com.CRUDJPA.JPA.Security.Filter.JwtAuthenticationFilter;
import com.CRUDJPA.JPA.Security.Filter.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    /**
     * Define un bean para el AuthenticationManager.
     *
     * @return el AuthenticationManager configurado.
     * @throws Exception si ocurre un error al obtener el AuthenticationManager.
     */
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Define un bean para el PasswordEncoder utilizando BCrypt.
     *
     * @return una instancia de BCryptPasswordEncoder.
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http la instancia HttpSecurity para configurar.
     * @return la cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error durante la configuración de seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                // Añade el filtro de autenticación JWT
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                // Deshabilita la protección CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)
                // Añade el filtro de autenticación JWT nuevamente (esto parece ser un error y debería ser eliminado)
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                // Añade el filtro de validación JWT
                .addFilter(new JwtValidationFilter(authenticationManager()))
                // Configura las reglas de autorización de solicitudes HTTP
                .authorizeHttpRequests(authRequest ->
                    authRequest
                            .requestMatchers(HttpMethod.POST ,"/users/create").permitAll()
                            .requestMatchers(HttpMethod.POST ,"/users/register").permitAll()
                            .anyRequest().authenticated()
                )
                // Configura la política de gestión de sesiones como sin estado
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Construye y devuelve la cadena de filtros de seguridad
                .build();
    }
}
