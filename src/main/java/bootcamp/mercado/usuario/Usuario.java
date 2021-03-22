package bootcamp.mercado.usuario;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
public class Usuario {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String login;
	@Column(nullable = false)
	private String senha;
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime cadastro;
	
	@Deprecated
	public Usuario() {}
	
	public Usuario(String login, Senha senha) {
		this.login = login;
		this.senha = senha.getSenha();
	}
	
	public Long getId() {
		return id;
	}
	
	public String getLogin() {
		return login;
	}
}
