package bootcamp.mercado.produto.opiniao;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;

import javax.persistence.*;

@Entity
public class Opiniao {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String titulo;
	@Lob
	@Column(nullable = false, length = 500)
	private String descricao;
	@Column(nullable = false)
	private Integer nota;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto produto;
	
	@Deprecated
	public Opiniao() {}
	
	public Opiniao(String titulo, String descricao,
			Integer nota, Usuario usuario, Produto produto) {
		
		this.titulo = titulo;
		this.descricao = descricao;
		this.nota = nota;
		this.usuario = usuario;
		this.produto = produto;
	}
	
	public Long getId() {
		return this.id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getNota() {
		return nota;
	}

	public Usuario getUsuario() {
		return usuario;
	}
}
