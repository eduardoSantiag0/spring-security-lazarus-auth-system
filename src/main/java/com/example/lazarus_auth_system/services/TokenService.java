package com.example.lazarus_auth_system.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;


    private static final Long EXPIRATION_TIME_TOKEN = 1L;
    private static final Long EXPIRATION_TIME_REFRESH_TOKEN = 2L;


    public String generateToken(UserEntity user) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-lazarus")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDate(EXPIRATION_TIME_TOKEN))
                    .withClaim("mission_code",user.getMissionCode())
                    .withClaim("role", user.getRole().toString())
                    .sign(algorithm);

        } catch (Exception ex) {
            throw new Exception("Error while generating token");
        }
    }

    public String generateRefreshToken(UserEntity user) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-lazarus")
                    .withSubject(user.getId().toString()) //! Subject
                    .withExpiresAt(generateExpirationDate(EXPIRATION_TIME_REFRESH_TOKEN))
                    .withClaim("mission_code",user.getMissionCode())
                    .withClaim("role", user.getRole().toString())
                    .sign(algorithm);

        } catch (Exception ex) {
            throw new Exception("Error while generating token");
        }
    }

    public String validateToken(String token) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-lazarus")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception ex) {
            return "";
        }
    }

    public Long validateRefreshTokenAndGetId(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer("auth-lazarus")
                    .build()
                    .verify(token)
                    .getSubject();
            return Long.valueOf(subject);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid token", ex);
        }
    }



    private Instant generateExpirationDate(Long time) {
        return LocalDateTime.now().plusHours(time).toInstant(ZoneOffset.of("-03:00"));
    }

    public String extractToken(HttpServletRequest request) {
        System.out.println(request);

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "";
        }
        return authHeader.substring(7);
    }


    public long getExpiration(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT jwt = JWT.require(algorithm)
                    .withIssuer("auth-lazarus")
                    .build()
                    .verify(token);

            long expiresAtMillis = jwt.getExpiresAt().getTime();
            long nowMillis = System.currentTimeMillis();
            long diffInSeconds = (expiresAtMillis - nowMillis) / 1000;

            return Math.max(diffInSeconds, 0);
        } catch (Exception ex) {
            return 0;
        }
    }
}
