package bootcamp.mercado.autenticacao;

import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioLogin implements UserDetailsService {
	
	private UsuarioRepository repository;
	
	public UsuarioLogin(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByLogin(username);
		if (usuario.isPresent()) return usuario.get();
		
		throw new UsernameNotFoundException("Usuário não encontrado");
	}
}
