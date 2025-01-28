package c23_99_m_webapp.backend.security;

import c23_99_m_webapp.backend.models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("API Reservation Resources School")
                    .withSubject(user.getEmail())
                    .withExpiresAt(dateExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating JWT token", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.require(algorithm)
                    .withIssuer("API Reservation Resources School")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Invalid or expired JWT token", exception);
        }
    }

    // Obtiene la fecha de expiración usando la zona horaria predeterminada del servidor
    private Instant dateExpiration() {
        ZoneId zone = ZoneId.systemDefault();  // Obtener la zona horaria del sistema
        return LocalDateTime.now(zone).plusMinutes(30).toInstant(ZoneOffset.ofTotalSeconds(zone.getRules().getOffset(LocalDateTime.now()).getTotalSeconds()));
    }
}

