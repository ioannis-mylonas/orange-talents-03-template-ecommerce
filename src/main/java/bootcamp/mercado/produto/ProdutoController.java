package bootcamp.mercado.produto;

import bootcamp.mercado.produto.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.produto.opiniao.Opiniao;
import bootcamp.mercado.produto.opiniao.OpiniaoRepository;
import bootcamp.mercado.produto.pergunta.Pergunta;
import bootcamp.mercado.produto.pergunta.PerguntaRepository;
import bootcamp.mercado.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	private ProdutoRepository repository;
	private CategoriaRepository categoriaRepository;
	private CaracteristicaRepository caracteristicaRepository;
	private PerguntaRepository perguntaRepository;
	private OpiniaoRepository opiniaoRepository;

	public ProdutoController(ProdutoRepository repository,
			CategoriaRepository categoriaRepository,
			CaracteristicaRepository caracteristicaRepository,
			PerguntaRepository perguntaRepository,
			OpiniaoRepository opiniaoRepository) {
		
		this.repository = repository;
		this.categoriaRepository = categoriaRepository;
		this.caracteristicaRepository = caracteristicaRepository;
		this.perguntaRepository = perguntaRepository;
		this.opiniaoRepository = opiniaoRepository;
	}
	
	@PostMapping
	@Transactional
	public Long cadastra(@RequestBody @Valid ProdutoRequest request,
						 @AuthenticationPrincipal Usuario dono) {

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
