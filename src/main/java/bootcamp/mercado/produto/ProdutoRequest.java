package bootcamp.mercado.produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import bootcamp.mercado.caracteristica.Caracteristica;
import bootcamp.mercado.caracteristica.CaracteristicaRequest;
import bootcamp.mercado.categoria.Categoria;
import bootcamp.mercado.categoria.CategoriaRepository;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.validator.Exists;

public class ProdutoRequest {
	@NotBlank
	private String nome;
	@NotNull @DecimalMin(value = "0.00", inclusive = false)
	private BigDecimal preco;
	@NotNull @Min(value = 0)
	private Integer quantidade;
	@NotNull @Size(min = 3) @Valid
	private List<CaracteristicaRequest> caracteristicas;
	@NotBlank @Size(max = 1000)
	private String descricao;
	@NotBlank @Exists(target = Categoria.class, field = "nome")
	private String categoria;
	
	public ProdutoRequest(@NotBlank String nome,
			@NotNull @DecimalMin(value = "0.00", inclusive = false) BigDecimal preco,
			@NotNull @Min(0) Integer quantidade, @NotNull @Size(min = 3) List<CaracteristicaRequest> caracteristicas,
			@NotBlank @Size(max = 1000) String descricao, @NotBlank String categoria) {
		this.nome = nome;
		this.preco = preco;
		this.quantidade = quantidade;
		this.caracteristicas = caracteristicas;
		this.descricao = descricao;
		this.categoria = categoria;
	}
	
	public List<CaracteristicaRequest> getCaracteristicas() {
		return caracteristicas;
	}

	public Produto converte(CategoriaRepository categoriaRepository, Usuario dono) {
		List<Caracteristica> caracteristicasObj = caracteristicas
				.stream()
				.map(i -> { return i.converte(); })
				.collect(Collectors.toList());
		
		Categoria categoriaObj = categoriaRepository.findByNome(categoria).get();
		
		return new Produto(nome, preco, quantidade,
				descricao, caracteristicasObj, categoriaObj, dono);
	}
}
