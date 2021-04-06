package bootcamp.mercado.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private final PasswordEncoder encoder;
	private final UsuarioRepository repository;
	
	public UsuarioController(PasswordEncoder encoder, UsuarioRepository repository) {
		this.encoder = encoder;
		this.repository = repository;
	}
	
	@PostMapping
	@Transactional
	public Long cadastra(@RequestBody @Valid UsuarioRequest request) {
		Usuario usuario = request.converte(encoder);
		repository.save(usuario);
		return usuario.getId();
	}
}
