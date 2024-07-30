package francescocristiano.CapstoneProject.security;


import francescocristiano.CapstoneProject.exceptions.UnauthorizedException;
import francescocristiano.CapstoneProject.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(User user) {
        return Jwts.builder().
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 60)).
                subject(String.valueOf(user.getId())).
                claim("userType", user.getUserType().toString()).
                signWith(Keys.hmacShaKeyFor(secret.getBytes())).
                compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public String extractIdFrom(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public String extractUserTypeFrom(String token) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token).getPayload().get("userType", String.class);
    }

}
