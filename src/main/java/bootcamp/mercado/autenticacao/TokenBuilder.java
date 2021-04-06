package bootcamp.mercado.autenticacao;

import bootcamp.mercado.usuario.Usuario;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenBuilder {
    @Value("${orange.ecommerce-treino.secret}")
    private String secret;
    @Value("${orange.ecommerce-treino.expiration}")
    private String expiration;
    @Value("${orange.ecommerce-treino.issuer}")
    private String issuer;

    public Token build(Usuario usuario) {
        return new Token(usuario, secret, expiration, issuer);
    }

    public Token build(String token) {
        try {
            return new Token(token, secret);
        } catch (JwtException e) {
            return null;
        }
    }
}
