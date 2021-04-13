package bootcamp.mercado.produto.categoria;

import javax.persistence.*;

@Entity
public class Categoria {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String nome;
	@ManyToOne
	private Categoria parente;
	
	@Deprecated
	public Categoria() {}
	
	public Categoria(String nome) {
		this.nome = nome;
	}
	
	public void setParente(Categoria parente) {
		this.parente = parente;
	}

	public Categoria getParente() {
		return parente;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}
}
