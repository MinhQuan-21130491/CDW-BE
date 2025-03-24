package com.chatapp.ChatApp.config;

import com.chatapp.ChatApp.modal.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class TokenProvider {
    private static final Long EXPIRATION_TIME_IN_MILISEC = 1000L * 60L * 14L *30L *6L; // 6 months
    SecretKey key;
    @PostConstruct
    private void init() {
        key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    }
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MILISEC))
                .signWith(key)
                .compact();
    }
    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7);
        return extractClaims(jwt, Claims::getSubject);
    }
    private <T> T extractClaims(String token, Function<Claims, T> claimsFunction) {
        return claimsFunction.apply(Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload());
    }
    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = getEmailFromToken(jwt);
        jwt = jwt.substring(7);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));
    }
    private boolean isTokenExpired(String jwt){
        return  extractClaims(jwt, Claims::getExpiration).before(new Date());
    }
}
