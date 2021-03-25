package bootcamp.mercado.pergunta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import bootcamp.mercado.usuario.ProdutoDetalheUsuarioResponse;

public class ProdutoDetalhePerguntaResponse {
	private String titulo;
	private LocalDateTime criacao;
	private ProdutoDetalheUsuarioResponse usuario;
	
	public ProdutoDetalhePerguntaResponse(Pergunta pergunta) {
		this.titulo = pergunta.getTitulo();
		this.criacao = pergunta.getCriacao();
		this.usuario = new ProdutoDetalheUsuarioResponse(pergunta.getUsuario());
	}

	public String getTitulo() {
		return titulo;
	}

	public LocalDateTime getCriacao() {
		return criacao;
	}

	public ProdutoDetalheUsuarioResponse getUsuario() {
		return usuario;
	}
	
	public static List<ProdutoDetalhePerguntaResponse> fromList(
			List<Pergunta> perguntas) {
		
		return perguntas.stream()
				.map(ProdutoDetalhePerguntaResponse::new)
				.collect(Collectors.toList());
	}
}
