package bootcamp.mercado.opiniao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface OpiniaoRepository extends CrudRepository<Opiniao, Long> {
	List<Opiniao> findByProduto_Id(Long id);
}
