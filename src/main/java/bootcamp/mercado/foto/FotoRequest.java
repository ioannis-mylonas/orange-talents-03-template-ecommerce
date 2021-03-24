package bootcamp.mercado.foto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.validator.Exists;

public class FotoRequest {
	@NotNull @Size(min = 1)
	private List<MultipartFile> fotos;
	@NotNull @Exists(target = Produto.class, field = "id")
	private Long produtoId;
	
	public FotoRequest(List<MultipartFile> fotos, Long produtoId) {
		this.fotos = fotos;
		this.produtoId = produtoId;
	}
	
	public List<MultipartFile> getFotos() {
		return fotos;
	}
	
	public List<Foto> converte(ProdutoRepository produtoRepository) {
		Produto produto = produtoRepository.findById(produtoId).get();
		
		return fotos.stream()
				.map(i -> { return new Foto(i, produto); })
				.collect(Collectors.toList());
	}

	public Long getProdutoId() {
		return produtoId;
	}
}
