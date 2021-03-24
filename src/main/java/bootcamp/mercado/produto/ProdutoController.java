package bootcamp.mercado.produto;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.mercado.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.categoria.CategoriaRepository;
import bootcamp.mercado.config.AutenticacaoService;
import bootcamp.mercado.usuario.Usuario;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	private ProdutoRepository repository;
	private CategoriaRepository categoriaRepository;
	private CaracteristicaRepository caracteristicaRepository;
	private AutenticacaoService authService;

	public ProdutoController(ProdutoRepository repository,
			CategoriaRepository categoriaRepository,
			CaracteristicaRepository caracteristicaRepository,
			AutenticacaoService authService) {
		this.repository = repository;
		this.categoriaRepository = categoriaRepository;
		this.caracteristicaRepository = caracteristicaRepository;
		this.authService = authService;
	}
	
	@PostMapping
	@Transactional
	public Long cadastra(@RequestBody @Valid ProdutoRequest request) {
		Usuario dono = authService.getLoggedUser();
		
		Produto produto = request.converte(categoriaRepository, dono);
		caracteristicaRepository.saveAll(produto.getCaracteristicas());
		repository.save(produto);
		return produto.getId();
	}
}
