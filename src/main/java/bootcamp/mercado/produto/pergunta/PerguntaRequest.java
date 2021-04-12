package bootcamp.mercado.produto.pergunta;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;

import javax.validation.constraints.NotBlank;

public class PerguntaRequest {
	@NotBlank
	private String titulo;

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public Pergunta converte(Usuario usuario, Produto produto) {
		return new Pergunta(titulo, usuario, produto);
	}
}
