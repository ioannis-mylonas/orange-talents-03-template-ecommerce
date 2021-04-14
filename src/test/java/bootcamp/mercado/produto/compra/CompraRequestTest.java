package bootcamp.mercado.produto.compra;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRequestBuilder;
import bootcamp.mercado.produto.categoria.Categoria;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Rollback
class CompraRequestTest {
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ApplicationContext context;

    @PersistenceContext private EntityManager entityManager;
    private Usuario usuario;
    private Produto produto;
    private Categoria categoria;

    private LocalValidatorFactoryBean validator;

    private Long getProdutoIdInexistente() {
        long id = 1L;

        while (produto.getId().equals(id)) {
            id++;
        }
        return id;
    }

    @BeforeEach
    public void setup() {
        SpringConstraintValidatorFactory factory = new SpringConstraintValidatorFactory(
                context.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();

        categoria = new Categoria("Categoria");
        entityManager.persist(categoria);

        usuario = new UsuarioRequestBuilder()
                .setNome("usuario@login.com")
                .setSenha("123456").build()
                .converte(passwordEncoder);

        entityManager.persist(usuario);

        produto = new ProdutoRequestBuilder()
                .setNome("Produto").setDescricao("Descrição")
                .setQuantidade(500).setPreco(BigDecimal.valueOf(50.30))
                .setCategoria(categoria.getNome())
                .build().converte(categoriaRepository, usuario);

        entityManager.persist(produto);

        entityManager.clear();
    }

    @Test
    public void testaRequestValida() {
        CompraRequest compraRequest = new CompraRequest(
                "Pagseguro", produto.getId(), 2
        );

        Set<ConstraintViolation<CompraRequest>> errors = validator.validate(compraRequest);
        assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaGatewayVazioInvalido(String gateway) {
        CompraRequest request = new CompraRequest(
                gateway, produto.getId(), 2
        );

        Set<ConstraintViolation<CompraRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Inexistente", "Gateway Não Existe", "NÃO", "faltou"})
    public void testaGatewayInexistenteInvalido(String gateway) {
        CompraRequest request = new CompraRequest(
                gateway, produto.getId(), 2
        );

        Set<ConstraintViolation<CompraRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaProdutoVazioInvalido(Long produto) {
        CompraRequest request = new CompraRequest(
                "Pagseguro", produto, 2
        );

        Set<ConstraintViolation<CompraRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaProdutoInexistenteInvalido() {
        Long produto = getProdutoIdInexistente();
        CompraRequest request = new CompraRequest(
                "Pagseguro", produto, 2
        );

        Set<ConstraintViolation<CompraRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaQuantidadeVaziaInvalida(Integer quantidade) {
        CompraRequest request = new CompraRequest(
                "Pagseguro", produto.getId(), quantidade
        );

        Set<ConstraintViolation<CompraRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5, -3, -10})
    public void testaQuantidadeInvalida(Integer quantidade) {
        CompraRequest request = new CompraRequest(
                "Pagseguro", produto.getId(), quantidade
        );

        Set<ConstraintViolation<CompraRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }
}