package bootcamp.mercado.usuario;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import bootcamp.mercado.validator.Unique;

public class UsuarioRequest {
	@NotBlank @Email @Unique(target = Usuario.class, field = "login")
	private String login;
	@NotBlank @Size(min = 6)
	private String senha;
	
	public UsuarioRequest(@NotBlank @Email String login, @NotBlank @Size(min = 6) String senha) {
		this.login = login;
		this.senha = senha;
	}

	public Usuario converte(PasswordEncoder encoder) {
		return new Usuario(login.toLowerCase(), encoder.encode(senha));
	}
}
