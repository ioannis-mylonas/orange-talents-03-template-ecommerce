package bootcamp.mercado.categoria;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
	private CategoriaRepository repository;

	public CategoriaController(CategoriaRepository repository) {
		this.repository = repository;
	}
	
	@PostMapping
	public Long cadastra(@RequestBody @Valid CategoriaRequest request) {
		Categoria categoria = request.converte(repository);
		repository.save(categoria);
		return categoria.getId();
	}
}
