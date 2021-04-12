package bootcamp.mercado.usuario;

import bootcamp.mercado.usuario.autenticacao.Perfil;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String login;
	@Column(nullable = false)
	private String senha;
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime cadastro;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Perfil> perfis = new ArrayList<Perfil>();
	
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;

		if (!(obj instanceof Usuario)) return false;

		return this.id.equals(((Usuario) obj).getId());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.login;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
