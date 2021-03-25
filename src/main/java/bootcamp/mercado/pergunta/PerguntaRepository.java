package bootcamp.mercado.pergunta;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PerguntaRepository extends CrudRepository<Pergunta, Long> {
	List<Pergunta> findByProduto_Id(Long id);
}
