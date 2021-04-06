package bootcamp.mercado.autenticacao;

import bootcamp.mercado.usuario.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public class Token {
    @Value("${orange.ecommerce-treino.expiration}")
    private static String expirationStr;
    @Value("${orange.ecommerce-treino.secret}")
    private static String secret;
    private static final String issuer = "Orange Talents ECommerce Treino";

    private final String token;
    private final Long id;

    public Token(Usuario usuario) {
        this.id = usuario.getId();

        Date hoje = new Date();
        Date expiracao = new Date(hoje.getTime() + Long.parseLong(expirationStr));

        this.token = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(this.id.toString())
                .setIssuedAt(hoje)
                .setExpiration(expiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }

    public static Long parseId(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        } catch (JwtException e) {
            return null;
        }
    }
}
