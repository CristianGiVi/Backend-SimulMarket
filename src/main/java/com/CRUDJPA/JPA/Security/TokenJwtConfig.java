package com.CRUDJPA.JPA.Security;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenJwtConfig {

    // Define la clave secreta utilizada para firmar y verificar los tokens JWT
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    // Define el prefijo que se añade a los tokens JWT en las cabeceras HTTP
    public static final String PREFIX_TOKEN = "Bearer ";

    // Define el nombre de la cabecera HTTP donde se debe enviar el token JWT
    public static final String HEADER_AUTHORIZATION = "Authorization";

    // Define el tipo de contenido esperado en las solicitudes HTTP
    public static final String CONTENT_TYPE = "applitacion/json";

}
