package bootcamp.mercado.produto.opiniao;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;

import javax.validation.constraints.*;

public class OpiniaoRequest {
	@NotBlank
	public String titulo;
	@NotBlank @Size(max = 500)
	public String descricao;
	@NotNull @Min(value = 1) @Max(value = 5)
	public Integer nota;
	
	public OpiniaoRequest(@NotBlank String titulo, @NotBlank @Size(max = 500) String descricao,
			@Min(1) @Max(5) Integer nota) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.nota = nota;
	}
	
	public Opiniao converte(Usuario usuario, Produto produto) {
		return new Opiniao(titulo, descricao, nota, usuario, produto);
	}
}
