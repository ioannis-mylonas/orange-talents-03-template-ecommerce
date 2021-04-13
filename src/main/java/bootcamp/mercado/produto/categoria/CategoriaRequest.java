package bootcamp.mercado.produto.categoria;

import bootcamp.mercado.validator.Exists;
import bootcamp.mercado.validator.Unique;

import javax.validation.constraints.NotBlank;

public class CategoriaRequest {
	@NotBlank @Unique(target = Categoria.class, field = "nome")
	private String nome;
	@Exists(target = Categoria.class, field = "nome")
	private String parente;
	
	public CategoriaRequest(@NotBlank String nome, String parente) {
		this.nome = nome;
		this.parente = parente;
	}
	
	public Categoria converte(CategoriaRepository repository) {
		Categoria categoria = new Categoria(nome);
		
		if (parente != null) categoria.setParente(
				repository.findByNomeIgnoreCase(parente).get());
		
		return categoria;
	}
}
