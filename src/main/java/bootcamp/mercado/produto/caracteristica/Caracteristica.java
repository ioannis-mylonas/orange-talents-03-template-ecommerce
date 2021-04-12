package bootcamp.mercado.produto.caracteristica;

import javax.persistence.*;

@Entity
public class Caracteristica {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private String descricao;
	
	@Deprecated
	public Caracteristica() {}
	
	public Caracteristica(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
}
