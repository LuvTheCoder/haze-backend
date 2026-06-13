package com.socialmedia.socialmedia_backend.security;


import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // step 1 - get the signing key from the secret string
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // step 2 - generate a token using the user's email
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email) //Who this token for
                .issuedAt(new Date()) //When was it created
                .expiration(new Date(System.currentTimeMillis() + expiration)) //When will it expire
                .signWith(getKey()) // SIGNED with the secret key so no one can fake it
                .compact(); // compact everything into a single string
    }

    // step 3 - read the email back from the token
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey()) // verify the signature matches
                .build()
                .parseSignedClaims(token) // parse the token
                .getPayload()
                .getSubject(); // extract the email
    }

    // step 4 - check if token is valid and not expired
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getKey()) // verify the signature matches
                    .build() // build the parser
                    .parseSignedClaims(token); // parse the token
            return true;
        } catch (JwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
