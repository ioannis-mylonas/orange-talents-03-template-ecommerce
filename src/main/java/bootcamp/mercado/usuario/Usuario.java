package bootcamp.mercado.usuario;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

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
	private LocalDateTime cadastro = LocalDateTime.now();
	
	public Usuario(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}
	
	public Long getId() {
		return id;
	}
}
