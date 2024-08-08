package com.tinqinacademy.authentication.core.security;
import com.tinqinacademy.authentication.api.exceptions.Messages;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;



@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.duration-time}")
    private long expiration;

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(decodedKey())
                    .build()
                    .parse(token);
            return true;
        } catch (IllegalArgumentException | MalformedJwtException |
                 UnsupportedJwtException | ExpiredJwtException e) {
            return false;
        }
    }

    public String getUsername(String token){
        Claims claims = Jwts.parser()
                .verifyWith(decodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public String createToken(Authentication authentication) {
        Date now = new Date();
        Date expirityDate = new Date(now.getTime() + expiration);

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(Messages.NO_ASSIGNED_USER));

        String token = Jwts.builder()
                .subject(authentication.getName())
                .issuedAt(now)
                .claim("roles", role)
                .claim("iat", now.getTime() / 1000)
                .claim("exp", expirityDate.getTime() / 1000)
                .expiration(expirityDate)
                .signWith(decodedKey())
                .compact();

        return token;
    }
    private SecretKey decodedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
