package com.tinqinacademy.authentication.core.security;
import com.tinqinacademy.authentication.api.exceptions.TokenExpiredException;
import com.tinqinacademy.authentication.api.exceptions.messages.Messages;
import com.tinqinacademy.authentication.api.models.enums.RoleType;
import com.tinqinacademy.authentication.persistence.models.entities.Role;
import com.tinqinacademy.authentication.persistence.models.entities.User;
import com.tinqinacademy.authentication.persistence.repositories.BlacklistedTokenRepository;
import com.tinqinacademy.authentication.persistence.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class JwtProvider {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

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
        List<String> rolesAsString = extractClaims(token).get("roles", ArrayList.class);
        return rolesAsString.stream()
                .map(RoleType::valueOf)
                .toList();
    }

    public Date getExpirationTimeFromToken(String token) {
        return extractClaims(token).getExpiration();
    }

    public void validate(String token) throws JwtException {
        extractClaims(token);

        boolean isTokenInvalidated = blacklistedTokenRepository.existsByToken(token);
        if (isTokenInvalidated) {
            throw new TokenExpiredException(Messages.TOKEN_EXPIRED);
        }
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
