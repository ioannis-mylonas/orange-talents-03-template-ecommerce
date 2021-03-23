package bootcamp.mercado.usuario;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
