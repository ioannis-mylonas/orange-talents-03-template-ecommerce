package bootcamp.mercado.produto.opiniao;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRequestBuilder;
import bootcamp.mercado.produto.categoria.Categoria;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OpiniaoRepositoryTest {
    @PersistenceContext private EntityManager entityManager;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private OpiniaoRepository opiniaoRepository;

    private Produto produto;
    private Opiniao opiniao;

    private Long getProdutoIdInexistente() {
        long id = 1L;
        while (produto.getId().equals(id)) id++;

        return id;
    }

    @BeforeEach
    public void setup() {
        Categoria categoria = new Categoria("Categoria");
        entityManager.persist(categoria);

        Usuario usuario = new UsuarioRequestBuilder()
                .setNome("user")
                .setSenha("123456")
                .build().converte(new Argon2PasswordEncoder());
        entityManager.persist(usuario);

        produto = new ProdutoRequestBuilder()
                .setNome("Produto")
                .setDescricao("Descrição")
                .setCategoria("Categoria")
                .setPreco(BigDecimal.valueOf(50.13))
                .setQuantidade(30).build().converte(categoriaRepository, usuario);
        entityManager.persist(produto);

        opiniao = new Opiniao("Título",
                "Descrição", 5, usuario, produto);
        entityManager.persist(opiniao);
    }

    @Test
    public void testaOpiniaoEncontradaPorProduto() {
        List<Opiniao> resultado = opiniaoRepository.findByProduto_Id(produto.getId());
        assertEquals(1, resultado.size());
    }

    @Test
    public void testaOpiniaoNaoEncontradaProdutoInexistente() {
        List<Opiniao> resultado = opiniaoRepository.findByProduto_Id(getProdutoIdInexistente());
        assertTrue(resultado.isEmpty());
    }
}