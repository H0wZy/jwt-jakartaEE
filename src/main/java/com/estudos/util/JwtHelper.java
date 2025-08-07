package com.estudos.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKey;
import java.util.Date;

@ApplicationScoped
public class JwtHelper {

    // Chave secreta (em produção, use variável de ambiente)
    private static final String SECRET_KEY = "mySuperSecretKeyForJWTTokenGenerationThatNeedsToBeAtLeast32CharactersLong";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Tempo de expiração (24 horas)
    private static final long EXPIRATION_TIME = 86400000; // 24 horas em ms

    /**
     * Gera token JWT com informações do usuário
     */
    public String generateToken(String username, String email, String cargo, Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)                    // Username como subject
                .claim("email", email)                   // Claims customizados
                .claim("cargo", cargo)
                .claim("userId", userId)
                .setIssuedAt(now)                        // Data de criação
                .setExpiration(expiration)               // Data de expiração
                .signWith(key, SignatureAlgorithm.HS256) // Assinatura
                .compact();
    }

    /**
     * Valida se o token é válido
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extrai username do token
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extrai email do token
     */
    public String getEmailFromToken(String token) {
        return getClaims(token).get("email", String.class);
    }

    /**
     * Extrai cargo do token
     */
    public String getCargoFromToken(String token) {
        return getClaims(token).get("cargo", String.class);
    }

    /**
     * Extrai userId do token
     */
    public Long getUserIdFromToken(String token) {
        return getClaims(token).get("userId", Long.class);
    }

    /**
     * Verifica se token expirou
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Extrai todas as claims do token
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}