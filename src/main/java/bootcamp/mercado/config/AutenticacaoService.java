package bootcamp.mercado.config;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {
	
	private UsuarioRepository repository;
	
	public AutenticacaoService(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByLogin(username);
		if (usuario.isPresent()) return usuario.get();
		
		throw new UsernameNotFoundException("Usuário não encontrado");
	}
	
	public Usuario getLoggedUser() {
		return (Usuario) SecurityContextHolder
					.getContext()
					.getAuthentication()
					.getPrincipal();
	}
	
}
