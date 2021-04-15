package bootcamp.mercado.testConfig;

import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRequestBuilder;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
class TestUserDetailsService implements UserDetailsService {
    Collection<Usuario> usuarios;

    public TestUserDetailsService(Collection<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarios.stream()
                .filter(i -> i.getUsername().equals(username))
                .findFirst();

        return usuario.isPresent() ? usuario.get() : null;
    }
}

@TestConfiguration
public class ConfigMockTestUser {

    @Bean @Primary
    public UserDetailsService userDetailsService() {
        Usuario usuario = Mockito.mock(Usuario.class);

        Mockito.when(usuario.getUsername()).thenReturn("user");
        Mockito.when(usuario.getId()).thenReturn(1L);

        return new TestUserDetailsService(List.of(usuario));
    }
}
