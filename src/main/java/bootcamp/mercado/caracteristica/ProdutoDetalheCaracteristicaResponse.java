package bootcamp.mercado.caracteristica;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDetalheCaracteristicaResponse {
	private String nome;
	private String descricao;
	
	public ProdutoDetalheCaracteristicaResponse(Caracteristica caracteristica) {
		this.nome = caracteristica.getNome();
		this.descricao = caracteristica.getDescricao();
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static List<ProdutoDetalheCaracteristicaResponse> fromList(
			List<Caracteristica> caracteristicas) {
		
		return caracteristicas.stream()
				.map(ProdutoDetalheCaracteristicaResponse::new)
				.collect(Collectors.toList());
	}
}
