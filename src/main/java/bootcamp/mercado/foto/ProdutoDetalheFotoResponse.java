package bootcamp.mercado.foto;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDetalheFotoResponse {
	private String uri;
	
	public ProdutoDetalheFotoResponse(Foto foto) {
		this.uri = foto.getUri();
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public static List<ProdutoDetalheFotoResponse> fromList(List<Foto> fotos) {
		return fotos.stream().map(ProdutoDetalheFotoResponse::new)
				.collect(Collectors.toList());
	}
}
