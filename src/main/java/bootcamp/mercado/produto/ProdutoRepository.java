package bootcamp.mercado.produto;

import org.springframework.data.repository.CrudRepository;

import bootcamp.mercado.categoria.Categoria;

public interface ProdutoRepository extends CrudRepository<Produto, Long>{

}
