package bootcamp.mercado.opiniao;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OpiniaoRepository extends CrudRepository<Opiniao, Long> {
	List<Opiniao> findByProduto_Id(Long id);
}
