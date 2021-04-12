package bootcamp.mercado.usuario.autenticacao;

import bootcamp.mercado.usuario.Usuario;
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

    public Token parse(String token) {
        try {
            return new Token(token, secret);
        } catch (Exception e) {
            return null;
        }
    }
}
