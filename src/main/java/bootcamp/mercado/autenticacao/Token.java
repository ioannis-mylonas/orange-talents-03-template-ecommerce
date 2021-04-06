package bootcamp.mercado.autenticacao;

import bootcamp.mercado.usuario.Usuario;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class Token {
    private final String token;
    private final Long id;

    public Token(Usuario usuario, String secret, String expiration, String issuer) throws JwtException {
        Long expireMillis = Long.parseLong(expiration);

        this.id = usuario.getId();

        Date hoje = new Date();
        Date expiracao = new Date(hoje.getTime() + expireMillis);

        this.token = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(this.id.toString())
                .setIssuedAt(hoje)
                .setExpiration(expiracao)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Token(String token, String secret) throws JwtException {
        this.token = token;
        this.id = Long.parseLong(Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public String getToken() {
        return token;
    }

    public Long getId() {
        return id;
    }
}
