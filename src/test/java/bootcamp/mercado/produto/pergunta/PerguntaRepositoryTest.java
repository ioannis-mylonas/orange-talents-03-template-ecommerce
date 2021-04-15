package bootcamp.mercado.produto.pergunta;

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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PerguntaRepositoryTest {
    @PersistenceContext private EntityManager entityManager;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private PerguntaRepository perguntaRepository;

    private Produto produto;
    private Pergunta pergunta;

    private Long getProdutoIdInexistente() {
        Long i = 1L;
        while(produto.getId().equals(i)) i++;

        return i;
    }

    @BeforeEach
    public void setup() {
        Categoria categoria = new Categoria("Categoria");
        entityManager.persist(categoria);

        Usuario usuario = new UsuarioRequestBuilder()
                .setNome("user").setSenha("123456")
                .build().converte(new Argon2PasswordEncoder());
        entityManager.persist(usuario);

        produto = new ProdutoRequestBuilder()
                .setNome("Produto").setDescricao("Descrição")
                .setQuantidade(50).setPreco(35.20)
                .setCategoria("Categoria").build().converte(categoriaRepository, usuario);
        entityManager.persist(produto);

        pergunta = new Pergunta("Uma Pergunta", usuario, produto);
        entityManager.persist(pergunta);
    }

    @Test
    public void testaEncontraPerguntaProdutoExistente() {
        List<Pergunta> res = perguntaRepository.findByProduto_Id(produto.getId());
        assertEquals(1, res.size());
        assertEquals(pergunta.getTitulo(), res.get(0).getTitulo());
    }

    @Test
    public void testaNaoEncontraPerguntaProdutoInexistente() {
        List<Pergunta> res = perguntaRepository.findByProduto_Id(getProdutoIdInexistente());
        assertTrue(res.isEmpty());
    }
}