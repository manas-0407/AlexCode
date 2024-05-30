package com.example.AlexCode.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JWTGenerator {

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date current = new Date();
//        Date expireDate = new Date(current.getTime() + SecurityConstant.JWT_EXPIRATION);
        Date expireDate = Date.from(Instant.now().plus(365, ChronoUnit.DAYS)); // 365 Days

        String token =Jwts.builder()
                .setSubject(username)
                .setIssuedAt(current)
                .setExpiration(expireDate)
                .signWith(getKey() , SignatureAlgorithm.HS256).compact();

        return token;
    }

    public SecretKey getKey(){
        byte[] key = Decoders.BASE64.decode(SecurityConstant.JWT_SECRET);
        return Keys.hmacShaKeyFor(key);
    }

    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(SecurityConstant.JWT_SECRET).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(SecurityConstant.JWT_SECRET).build().parseClaimsJws(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
