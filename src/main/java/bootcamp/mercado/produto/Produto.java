package bootcamp.mercado.produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import bootcamp.mercado.caracteristica.Caracteristica;
import bootcamp.mercado.categoria.Categoria;
import bootcamp.mercado.foto.Foto;
import bootcamp.mercado.usuario.Usuario;
import org.springframework.util.Assert;

@Entity
public class Produto {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private BigDecimal preco;
	@Column(nullable = false)
	private Integer quantidade;
	@Lob
	@Column(nullable = false, length = 1000)
	private String descricao;
	@ManyToMany
	@JoinColumn(nullable = false)
	List<Caracteristica> caracteristicas;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Categoria categoria;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Usuario dono;
	@CreationTimestamp
	@Column(nullable = false)
	private LocalDateTime criacao;
	@OneToMany
	@JoinColumn
	private List<Foto> fotos = new ArrayList<>();
	
	@Deprecated
	public Produto() {}
	
	public Produto(String nome, BigDecimal preco,
			Integer quantidade, String descricao,
			List<Caracteristica> caracteristicas,
			Categoria categoria,
			Usuario dono) {
		
		this.nome = nome;
		this.preco = preco;
		this.quantidade = quantidade;
		this.descricao = descricao;
		this.caracteristicas = caracteristicas;
		this.categoria = categoria;
		this.dono = dono;
	}

	public Long getId() {
		return this.id;
	}
	
	public String getNome() {
		return this.nome;
	}
	
	public BigDecimal getPreco() {
		return preco;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public String getDescricao() {
		return descricao;
	}

	public List<Foto> getFotos() {
		return fotos;
	}

	public Usuario getDono() {
		return this.dono;
	}
	
	public List<Caracteristica> getCaracteristicas() {
		return this.caracteristicas;
	}
	
	public void addFotos(List<Foto> fotos) {
		this.fotos.addAll(fotos);
	}

	public void diminuirQuantidade(int quantidade) {
		Assert.isTrue(this.quantidade >= quantidade,
				"Tentando diminuir quantidade maior que a quantidade disponível do produto!");
		this.quantidade -= quantidade;
	}
}
