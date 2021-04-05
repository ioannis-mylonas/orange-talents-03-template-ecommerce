package bootcamp.mercado.pergunta;

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
import bootcamp.mercado.email.EmailService;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.usuario.Usuario;

@RestController
@RequestMapping("/perguntas")
public class PerguntaController {
	
	private AutenticacaoService autenticacaoService;
	private ProdutoRepository produtoRepository;
	private PerguntaRepository perguntaRepository;
	private EmailService emailService;
	
	public PerguntaController(AutenticacaoService autenticacaoService,
			ProdutoRepository produtoRepository,
			PerguntaRepository perguntaRepository,
			EmailService emailService) {
		
		this.autenticacaoService = autenticacaoService;
		this.produtoRepository = produtoRepository;
		this.perguntaRepository = perguntaRepository;
		this.emailService = emailService;
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> envia(@PathVariable(name = "id", required = true) Long id,
								   @RequestBody @Valid PerguntaRequest request,
								   @AuthenticationPrincipal Usuario usuario) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty()) return ResponseEntity.notFound().build();
		
		Pergunta pergunta = request.converte(usuario, produto.get());
		
		perguntaRepository.save(pergunta);
		pergunta.envia(emailService);
		
		return ResponseEntity.ok().build();
	}

}
