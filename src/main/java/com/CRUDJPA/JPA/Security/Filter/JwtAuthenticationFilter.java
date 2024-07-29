package com.CRUDJPA.JPA.Security.Filter;

import com.CRUDJPA.JPA.Models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.CRUDJPA.JPA.Security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    // Constructor que recibe el AuthenticationManager
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // Método que intenta autenticar al usuario
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        User user = null;
        String userName = null;
        String password = null;

        try {
            // Lee los datos del usuario desde la solicitud
            user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            userName = user.getUserName();
            password = user.getPassword();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Crea un token de autenticación con los datos del usuario
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userName,
                password);

        // Autentica al usuario utilizando el AuthenticationManager
        return authenticationManager.authenticate(authenticationToken);

    }

    // Método que se llama cuando la autenticación es exitosa
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User user = ( org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String userName = user.getUsername();

        // Genera el token JWT
        String token = Jwts.builder()
                .subject(userName)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();

        // Añade el token en la cabecera de la respuesta
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        // Crea el cuerpo de la respuesta con el token y un mensaje de éxito
        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("userName", userName);
        body.put("message", "Has iniciado sesion con exito");

        // Escribe el cuerpo de la respuesta
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);
    }

    // Método que se llama cuando la autenticación falla
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        // Crea el cuerpo de la respuesta con un mensaje de error
        Map<String, String> body = new HashMap<>();
        body.put("message", "Error en la autentificafion, usename o password incorrectos");
        body.put("error", failed.getMessage());

        // Escribe el cuerpo de la respuesta
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE);
    }
}
