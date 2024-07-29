package com.CRUDJPA.JPA.Security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;

import java.util.Map;

import static com.CRUDJPA.JPA.Security.TokenJwtConfig.SECRET_KEY;

public class JwtUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> getClaimsMapFromToken(String header) {
        try {
            String token = header.replace("Bearer ", "");
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            return objectMapper.convertValue(claims, Map.class);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}
