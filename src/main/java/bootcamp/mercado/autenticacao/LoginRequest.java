package bootcamp.mercado.autenticacao;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
	private String login;
	@NotBlank
	private String senha;
	
	public LoginRequest(@NotBlank String login, @NotBlank String senha) {
		this.login = login;
		this.senha = senha;
	}
	
	public UsernamePasswordAuthenticationToken converte() {
		return new UsernamePasswordAuthenticationToken(login, senha);
	}
}
