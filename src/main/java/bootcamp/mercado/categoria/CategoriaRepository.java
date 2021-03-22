package bootcamp.mercado.categoria;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CategoriaRepository extends CrudRepository<Categoria, Long>{
	Optional<Categoria> findByNome(String nome);
}
