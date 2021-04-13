package bootcamp.mercado.produto.pergunta;

import bootcamp.mercado.email.MercadoMailSender;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.usuario.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/perguntas")
public class PerguntaController {
	
	private ProdutoRepository produtoRepository;
	private PerguntaRepository perguntaRepository;
	private MercadoMailSender mailSender;
	
	public PerguntaController(ProdutoRepository produtoRepository,
							  PerguntaRepository perguntaRepository,
							  MercadoMailSender mailSender) {
		
		this.produtoRepository = produtoRepository;
		this.perguntaRepository = perguntaRepository;
		this.mailSender = mailSender;
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<?> envia(@PathVariable(name = "id", required = true) Long id,
								   @RequestBody @Valid PerguntaRequest request,
								   @AuthenticationPrincipal Usuario usuario) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isEmpty()) return ResponseEntity.notFound().build();
		
		Pergunta pergunta = request.converte(usuario, produto.get());
		
		perguntaRepository.save(pergunta);
		pergunta.envia(mailSender);
		
		return ResponseEntity.ok().build();
	}

}
