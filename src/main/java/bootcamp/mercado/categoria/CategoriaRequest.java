package bootcamp.mercado.categoria;

import javax.validation.constraints.NotBlank;

import bootcamp.mercado.validator.Exists;
import bootcamp.mercado.validator.Unique;

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
		Categoria parenteObj = parente == null ?
				null : repository.findByNome(parente).get();
		
		return new Categoria(nome, parenteObj);
	}
}
