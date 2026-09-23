package com.greenfield.loanorigination.domain.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET = "plGiiysw2ctoNVXkIXOjsORmJ+4XRAvLf4BgOJCr37s=";

    public String generateToken(String username) throws NoSuchAlgorithmException {

        Map<String, Object> claims = new HashMap<>();

       return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000*60 ))
                .claims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getKey() throws NoSuchAlgorithmException {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public String extractUserName(String token) throws NoSuchAlgorithmException {
        return extractClaim(token, Claims::getSubject);
    }
    public boolean validateToken(String token, UserDetails userDetails) throws NoSuchAlgorithmException {

        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) throws NoSuchAlgorithmException {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws NoSuchAlgorithmException {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();



    }

    private boolean isTokenExpired(String token) throws NoSuchAlgorithmException {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) throws NoSuchAlgorithmException {
        return extractClaim(token, Claims::getExpiration);
    }
}
