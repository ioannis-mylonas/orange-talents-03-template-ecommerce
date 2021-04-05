package bootcamp.mercado.foto;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import bootcamp.mercado.usuario.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bootcamp.mercado.config.AutenticacaoService;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.storage.StorageService;

@RestController
@RequestMapping("/fotos")
public class FotoController {
	
	private ProdutoRepository produtoRepository;
	private FotoRepository fotoRepository;
	private AutenticacaoService authService;
	private StorageService storage;

	public FotoController(ProdutoRepository produtoRepository,
			FotoRepository fotoRepository,
			AutenticacaoService authService,
			StorageService storage) {
		
		this.produtoRepository = produtoRepository;
		this.authService = authService;
		this.storage = storage;
		this.fotoRepository = fotoRepository;
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> insere(@PathVariable(name="id", required=true) Long id,
									@Valid FotoRequest request,
									@AuthenticationPrincipal Usuario usuario) {
				
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if (produto.isEmpty()) return ResponseEntity.notFound().build();
		
		if (!authService.isUser(produto.get().getDono(), usuario)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		List<String> uriList = storage.saveAll(request.getFotos());
		List<Foto> fotos = FotoRequest.converte(uriList);
		
		fotoRepository.saveAll(fotos);
		produto.get().addFotos(fotos);
		produtoRepository.save(produto.get());
		
		return ResponseEntity.ok().build();
	}
}
