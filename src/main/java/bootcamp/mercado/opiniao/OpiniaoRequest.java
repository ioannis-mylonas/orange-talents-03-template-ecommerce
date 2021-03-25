package bootcamp.mercado.opiniao;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;

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
