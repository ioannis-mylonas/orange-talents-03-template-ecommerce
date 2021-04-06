package bootcamp.mercado.pergunta;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PerguntaRepository extends CrudRepository<Pergunta, Long> {
	List<Pergunta> findByProduto_Id(Long id);
}
