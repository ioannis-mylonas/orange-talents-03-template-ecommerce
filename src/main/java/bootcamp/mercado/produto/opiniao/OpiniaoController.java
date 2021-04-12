package bootcamp.mercado.produto.opiniao;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/opinioes")
public class OpiniaoController {
	
	private ProdutoRepository produtoRepository;
	private OpiniaoRepository opiniaoRepository;

	public OpiniaoController(ProdutoRepository produtoRepository,
							 OpiniaoRepository opiniaoRepository) {
		
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
