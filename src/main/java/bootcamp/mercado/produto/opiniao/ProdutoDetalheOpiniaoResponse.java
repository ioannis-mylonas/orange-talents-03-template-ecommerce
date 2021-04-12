package bootcamp.mercado.produto.opiniao;

import bootcamp.mercado.usuario.ProdutoDetalheUsuarioResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDetalheOpiniaoResponse {
	private String titulo;
	private String descricao;
	private Integer nota;
	private ProdutoDetalheUsuarioResponse usuario;
	
	public ProdutoDetalheOpiniaoResponse(Opiniao opiniao) {
		this.titulo = opiniao.getTitulo();
		this.descricao = opiniao.getDescricao();
		this.nota = opiniao.getNota();
		this.usuario = new ProdutoDetalheUsuarioResponse(opiniao.getUsuario());
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getNota() {
		return nota;
	}

	public ProdutoDetalheUsuarioResponse getUsuario() {
		return usuario;
	}
	
	public static List<ProdutoDetalheOpiniaoResponse> fromList(
			List<Opiniao> opinioes) {
		
		return opinioes.stream()
				.map(ProdutoDetalheOpiniaoResponse::new)
				.collect(Collectors.toList());
	}
}
