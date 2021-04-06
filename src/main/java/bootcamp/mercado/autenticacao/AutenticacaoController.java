package bootcamp.mercado.autenticacao;

import bootcamp.mercado.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	private AuthenticationManager manager;

	public AutenticacaoController(AuthenticationManager manager) {
		this.manager = manager;
	}

	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody @Valid LoginRequest request) {
		UsernamePasswordAuthenticationToken userPassToken = request.converte();
		
		try {
			Usuario usuario = (Usuario) manager.authenticate(userPassToken).getPrincipal();
			String token = new Token(usuario).getToken();
			return ResponseEntity.ok(new TokenResponse(token));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
