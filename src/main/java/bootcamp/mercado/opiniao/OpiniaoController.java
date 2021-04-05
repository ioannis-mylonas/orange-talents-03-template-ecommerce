package bootcamp.mercado.opiniao;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.mercado.config.AutenticacaoService;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.usuario.Usuario;

@RestController
@RequestMapping("/opinioes")
public class OpiniaoController {
	
	private AutenticacaoService authService;
	private ProdutoRepository produtoRepository;
	private OpiniaoRepository opiniaoRepository;

	public OpiniaoController(AutenticacaoService authService,
			ProdutoRepository produtoRepository,
			OpiniaoRepository opiniaoRepository) {
		
		this.authService = authService;
		this.produtoRepository = produtoRepository;
		this.opiniaoRepository = opiniaoRepository;
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<Long> cadastra(@PathVariable(name = "id", required = true) Long id,
										 @RequestBody @Valid OpiniaoRequest request,
										 @AuthenticationPrincipal Usuario usuario) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty()) return ResponseEntity.notFound().build();
		
		Opiniao opiniao = request.converte(usuario, produto.get());
		
		opiniaoRepository.save(opiniao);
		
		return ResponseEntity.ok(opiniao.getId());
	}
}
