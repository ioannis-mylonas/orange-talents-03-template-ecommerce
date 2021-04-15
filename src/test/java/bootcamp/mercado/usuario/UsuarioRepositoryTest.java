package bootcamp.mercado.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {
    @Autowired private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setup() {
        usuarioRepository.save(
                new UsuarioRequestBuilder()
                        .setNome("login")
                        .setSenha("123456")
                        .build().converte(new Argon2PasswordEncoder())
        );
    }

    @Test
    public void testaEncontraUsuarioCadastradoPorLogin() {
        Optional<Usuario> usuario = usuarioRepository.findByLogin("login");
        assertTrue(usuario.isPresent());
    }

    @Test
    public void testaNaoEncontraUsuarioNaoCadastradoPorLogin() {
        Optional<Usuario> usuario = usuarioRepository.findByLogin("inexistente");
        assertTrue(usuario.isEmpty());
    }
}