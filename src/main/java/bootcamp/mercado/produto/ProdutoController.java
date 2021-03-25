package bootcamp.mercado.produto;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.mercado.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.categoria.CategoriaRepository;
import bootcamp.mercado.config.AutenticacaoService;
import bootcamp.mercado.opiniao.Opiniao;
import bootcamp.mercado.opiniao.OpiniaoRepository;
import bootcamp.mercado.pergunta.Pergunta;
import bootcamp.mercado.pergunta.PerguntaRepository;
import bootcamp.mercado.usuario.Usuario;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	private ProdutoRepository repository;
	private CategoriaRepository categoriaRepository;
	private CaracteristicaRepository caracteristicaRepository;
	private PerguntaRepository perguntaRepository;
	private OpiniaoRepository opiniaoRepository;
	private AutenticacaoService authService;

	public ProdutoController(ProdutoRepository repository,
			CategoriaRepository categoriaRepository,
			CaracteristicaRepository caracteristicaRepository,
			PerguntaRepository perguntaRepository,
			OpiniaoRepository opiniaoRepository,
			AutenticacaoService authService) {
		
		this.repository = repository;
		this.categoriaRepository = categoriaRepository;
		this.caracteristicaRepository = caracteristicaRepository;
		this.perguntaRepository = perguntaRepository;
		this.opiniaoRepository = opiniaoRepository;
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
	
	@GetMapping("/{id}")
	public ResponseEntity<ProdutoDetalheResponse> detalhe(
			@PathVariable(name = "id", required = true) Long id) {
		
		Optional<Produto> produto = repository.findById(id);
		if (produto.isEmpty()) return ResponseEntity.notFound().build();
		
		List<Pergunta> perguntas = perguntaRepository.findByProduto_Id(id);
		List<Opiniao> opinioes = opiniaoRepository.findByProduto_Id(id);
		ProdutoDetalheResponse response = new ProdutoDetalheResponse(
				produto.get(), perguntas, opinioes);
		
		return ResponseEntity.ok(response);
	}
}
