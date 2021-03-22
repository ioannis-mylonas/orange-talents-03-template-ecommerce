package bootcamp.mercado.dataBuilder;

import org.springframework.security.crypto.password.PasswordEncoder;

import bootcamp.mercado.usuario.Senha;
import bootcamp.mercado.usuario.Usuario;

public class UsuarioBuilder {
	private String login;
	private String senha;
	private PasswordEncoder encoder;
	
	public UsuarioBuilder comLogin(String login) {
		this.login = login;
		return this;
	}
	
	public UsuarioBuilder comSenha(String senha) {
		this.senha = senha;
		return this;
	}
	
	public UsuarioBuilder comEncoder(PasswordEncoder encoder) {
		this.encoder = encoder;
		return this;
	}
	
	public Usuario build() {
		return new Usuario(login, new Senha(senha, encoder));
	}
}
