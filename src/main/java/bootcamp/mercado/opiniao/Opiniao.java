package bootcamp.mercado.opiniao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;

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
}
