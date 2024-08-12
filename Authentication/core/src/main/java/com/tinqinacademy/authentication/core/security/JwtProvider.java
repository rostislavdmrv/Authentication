package com.tinqinacademy.authentication.core.security;
import com.tinqinacademy.authentication.persistence.models.enums.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.duration-time}")
    private long expiration;

    public String createToken(String username, List<RoleType> roles) {
        Date currentTime = new Date();
        Date expireTime = new Date(currentTime.getTime() + this.expiration);

        return Jwts.builder()
                .claim("username", username)
                .claim("roles", roles)
                .issuedAt(currentTime)
                .expiration(expireTime)
                .signWith(decodedKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token).get("username", String.class);
    }

    public List<RoleType> getRolesFromToken(String token) {
        return extractClaims(token).get("roles", ArrayList.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(decodedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey decodedKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
