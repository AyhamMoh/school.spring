package edu.fbansept.school.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtils {

    @Value(("${jwt.secret}"))
    private String secret;

    //on récupère les données du JWT (hors date de creation, date d'expiration)
    public Claims extractBody(String jwt) {

        return Jwts.parser()
                .setSigningKey("secret")
                .parseClaimsJws(jwt)
                .getBody();
    }

    public String generateJwt(MyUserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();

    }
}
