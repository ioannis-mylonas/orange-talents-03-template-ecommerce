package bootcamp.mercado.autenticacao;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.mercado.config.TokenService;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	private AuthenticationManager manager;
	private TokenService tokenService;
	
	public AutenticacaoController(AuthenticationManager manager,
			TokenService tokenService) {
		this.manager = manager;
		this.tokenService = tokenService;
	}

	@PostMapping
	public ResponseEntity<?> autenticar(@RequestBody @Valid LoginRequest request) {
		UsernamePasswordAuthenticationToken userPassToken = request.converte();
		
		try {
			Authentication result = manager.authenticate(userPassToken);
			String token = tokenService.gerarToken(result);
			return ResponseEntity.ok(new TokenResponse(token));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
