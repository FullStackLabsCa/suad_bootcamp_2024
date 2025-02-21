package io.reactivestax.active_life_canada.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.reactivestax.active_life_canada.constant.SecurityConstants;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final Algorithm algorithm = Algorithm.HMAC512(SecurityConstants.SECRET);

    public String generateToken(Map<String, String> userData) {
        return JWT.create()
                .withSubject(userData.get("userName"))
                .withClaim("role", userData.get("role"))
                .withClaim(SecurityConstants.USER_ID, Long.parseLong(userData.get("userId")))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(algorithm);
    }

    public String extractUsername(String token) {
        return decodeToken(token).getSubject();
    }

    public String extractUserRole(String token) {
        return decodeToken(token).getClaim("role").asString();
    }

    public long extractUserId(String token) {
        return decodeToken(token).getClaim(SecurityConstants.USER_ID).asLong();
    }

    public boolean validateToken(String token, String username, long userId) {
        try {
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .withSubject(username)
                    .withClaim(SecurityConstants.USER_ID, userId)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private DecodedJWT decodeToken(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}
