package com.example.lazarus_auth_system.securiy;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.lazarus_auth_system.infra.persistance.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserEntity user) throws Exception {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("auth-lazarus")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDate())
                    .withClaim("mission_code",user.getMissionCode())
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

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
