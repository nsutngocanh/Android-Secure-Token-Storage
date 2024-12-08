package com.kt.securcetokenstorage.network.token;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTokenHandler implements TokenHandler {
    private final SecretKey secretKey;

    private final JWTTokenAction action;

    public JwtTokenHandler(String secretKeyString, JWTTokenAction action) {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        this.action = action;
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String refresh() {
        return action.whenTokenExpired();
    }

    public interface JWTTokenAction {
        String whenTokenExpired();
    }
}
