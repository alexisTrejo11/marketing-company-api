package at.backend.MarketingCompany.account.auth.adapters.outbound.token;

import at.backend.MarketingCompany.account.auth.core.application.TokenProvider;
import at.backend.MarketingCompany.account.auth.core.domain.entitiy.valueobject.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${app.auth.jwt.access-secret}")
    private String accessTokenSecret;

    @Value("${app.auth.jwt.refresh-secret}")
    private String refreshTokenSecret;

    @Value("${app.auth.jwt.access-expiration-minutes:15}")
    private int accessTokenExpirationMinutes;

    @Value("${app.auth.jwt.refresh-expiration-days:30}")
    private int refreshTokenExpirationDays;

    private SecretKey getAccessSigningKey() {
        return Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
    }

    private SecretKey getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    @Override
    public String generateAccessToken(String userId, String email, Set<Role> roles) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(accessTokenExpirationMinutes * 60L);

        return Jwts.builder()
                .subject(userId)
                .claim("type", "access")
                .claim("email", email)
                .claim("roles", roles.stream().map(Enum::name).toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getAccessSigningKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(String userId) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(refreshTokenExpirationDays * 24L * 60L * 60L);

        return Jwts.builder()
                .subject(userId)
                .claim("type", "refresh")
                .claim("jti", UUID.randomUUID().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getRefreshSigningKey())
                .compact();
    }

    @Override
    public Claims validateAccessToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getAccessSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Invalid access token: {}", e.getMessage());
            throw new RuntimeException("Invalid access token", e);
        }
    }

    @Override
    public Claims validateRefreshToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getRefreshSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn("Invalid refresh token: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh token", e);
        }
    }

    @Override
    public String getUserIdFromToken(String token, boolean isRefreshToken) {
        try {
            Claims claims = isRefreshToken ?
                    validateRefreshToken(token) : validateAccessToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error extracting user ID from token", e);
            return null;
        }
    }

    @Override
    public boolean isTokenExpired(String token, boolean isRefreshToken) {
        try {
            Claims claims = isRefreshToken ?
                    validateRefreshToken(token) : validateAccessToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public String getSessionIdFromRefreshToken(String refreshToken) {
        try {
            Claims claims = validateRefreshToken(refreshToken);
            return claims.get("sessionId", String.class);
        } catch (Exception e) {
            log.error("Error extracting session ID from refresh token", e);
            return null;
        }
    }

    @Override
    public String getTokenIdFromRefreshToken(String refreshToken) {
        try {
            Claims claims = validateRefreshToken(refreshToken);
            return claims.get("jti", String.class);
        } catch (Exception e) {
            log.error("Error extracting token ID from refresh token", e);
            return null;
        }
    }

    public Date getExpirationFromToken(String token, boolean isRefreshToken) {
        try {
            Claims claims = isRefreshToken ?
                    validateRefreshToken(token) : validateAccessToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }
}