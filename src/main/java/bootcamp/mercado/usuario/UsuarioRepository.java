package bootcamp.mercado.usuario;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	Optional<Usuario> findByLogin(String login);
}
