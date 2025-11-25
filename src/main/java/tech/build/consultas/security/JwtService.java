package tech.build.consultas.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "e7b82f95c1a74d28b4a9a3d7e0d9f6f2e7b82f95c1a74d28b4a9a3d7e0d9f6f2";

    public String gerarToken(String email, String tipo) {
        return Jwts.builder()
                .setSubject(email)
                .claim("tipo", tipo) // salva o tipo do usuário no token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public String validarToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
