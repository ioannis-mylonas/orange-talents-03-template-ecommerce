package bootcamp.mercado.produto;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.mercado.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.categoria.CategoriaRepository;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	private ProdutoRepository repository;
	private CategoriaRepository categoriaRepository;
	private CaracteristicaRepository caracteristicaRepository;

	public ProdutoController(ProdutoRepository repository,
			CategoriaRepository categoriaRepository,
			CaracteristicaRepository caracteristicaRepository) {
		this.repository = repository;
		this.categoriaRepository = categoriaRepository;
		this.caracteristicaRepository = caracteristicaRepository;
	}
	
	@PostMapping
	@Transactional
	public Long cadastra(@RequestBody @Valid ProdutoRequest request) {
		Produto produto = request.converte(categoriaRepository);
		caracteristicaRepository.saveAll(produto.getCaracteristicas());
		repository.save(produto);
		return produto.getId();
	}
}
