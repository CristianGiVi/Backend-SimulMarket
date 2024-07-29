package com.CRUDJPA.JPA.Security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;

import java.util.Map;

import static com.CRUDJPA.JPA.Security.TokenJwtConfig.SECRET_KEY;

public class JwtUtil {

    // ObjectMapper para convertir objetos a y desde JSON
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene un mapa de reclamaciones (claims) del token JWT.
     *
     * @param header el encabezado de autorización que contiene el token JWT.
     * @return las reclamaciones contenidas en el token JWT.
     * @throws IllegalArgumentException si el token es inválido.
     */
    public static Map<String, Object> getClaimsMapFromToken(String header) {
        try {
            // Elimina el prefijo "Bearer " del token
            String token = header.replace("Bearer ", "");

            // Analiza el token para obtener las reclamaciones
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return objectMapper.convertValue(claims, Map.class);
        } catch (JwtException e) {
            // Lanza una excepción si el token es inválido
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}
