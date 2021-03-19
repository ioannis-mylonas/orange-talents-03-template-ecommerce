package bootcamp.mercado.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;

public class Senha {
	private final String senha;
	
	public Senha(String senha, PasswordEncoder encoder) {
		this.senha = encoder.encode(senha);
	}
	
	public String getSenha() {
		return senha;
	}
}
