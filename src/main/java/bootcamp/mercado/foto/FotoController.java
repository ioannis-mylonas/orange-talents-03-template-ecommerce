package bootcamp.mercado.foto;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.mercado.config.AutenticacaoService;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;

@RestController
@RequestMapping("/fotos")
public class FotoController {
	
	private ProdutoRepository produtoRepository;
	private AutenticacaoService authService;

	public FotoController(ProdutoRepository produtoRepository, AutenticacaoService authService) {
		this.produtoRepository = produtoRepository;
		this.authService = authService;
	}

	@PostMapping
	public ResponseEntity<?> insere(@Valid FotoRequest request) {
		Produto produto = produtoRepository
				.findById(request.getProdutoId()).get();
		
		if (authService.getLoggedUser().getId() != produto.getDono().getId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		request.getFotos().forEach(i -> {
			System.out.println(i.getResource().getFilename());
		});
		
		System.out.println(request.getProdutoId());
		
		return ResponseEntity.ok().build();
	}
}
