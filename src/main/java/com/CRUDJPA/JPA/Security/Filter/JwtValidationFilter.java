package com.CRUDJPA.JPA.Security.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.CRUDJPA.JPA.Security.TokenJwtConfig.*;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    // Constructor que recibe el AuthenticationManager
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    // Método que se llama para cada solicitud entrante
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Obtiene el encabezado de autorización de la solicitud
        String header = request.getHeader(HEADER_AUTHORIZATION);

        // Si el encabezado es nulo o no comienza con el prefijo del token, continúa con el siguiente filtro
        if(header == null || !(header.startsWith(PREFIX_TOKEN))){
            chain.doFilter(request, response);
            return;
        }

        // Elimina el prefijo del token para obtener el token real
        String token = header.replace(PREFIX_TOKEN, "");

        try {
            // Parsear y validar el token JWT
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();

            // Obtiene el nombre de usuario de los claims del token
            String userName = (String) claims.get("userName");

            // Crea un objeto de autenticación con el nombre de usuario
            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(userName, null, null);

            // Establece la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Continúa con el siguiente filtro
            chain.doFilter(request, response);

        } catch (JwtException e){

            // Si el token no es válido, responde con un mensaje de error
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "El token no es valido");

            // Escribe el cuerpo de la respuesta
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(401);
            response.setContentType(CONTENT_TYPE);
        }


    }
}
