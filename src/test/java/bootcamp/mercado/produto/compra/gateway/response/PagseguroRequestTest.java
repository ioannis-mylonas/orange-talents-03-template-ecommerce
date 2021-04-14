package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.produto.ProdutoRequestBuilder;
import bootcamp.mercado.produto.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.produto.caracteristica.CaracteristicaRequest;
import bootcamp.mercado.produto.caracteristica.CaracteristicaRequestBuilder;
import bootcamp.mercado.produto.categoria.Categoria;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.produto.compra.*;
import bootcamp.mercado.produto.compra.gateway.Gateway;
import bootcamp.mercado.produto.compra.gateway.GatewayList;
import bootcamp.mercado.produto.compra.gateway.PagseguroGateway;
import bootcamp.mercado.produto.compra.gateway.PagseguroGatewayDev;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.UsuarioRequest;
import bootcamp.mercado.usuario.UsuarioRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.*;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PagseguroRequestTest {
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private CompraRepository compraRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private CaracteristicaRepository caracteristicaRepository;
    @PersistenceContext private EntityManager entityManager;

    @Mock private Gateway gateway;

    private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    private List<Usuario> usuarios;
    private List<Produto> produtos;
    private List<Compra> compras;
    private List<Pagamento> pagamentos;

    @Autowired private ApplicationContext context;
    private LocalValidatorFactoryBean validator;

    private int pickRandomIndex(List<?> values) {
        return new Random().nextInt(values.size());
    }

    private <T> T pickRandomValue(List<T> values) {
        return values.get(pickRandomIndex(values));
    }

    private Long getInexistentCompraId() {
        long i = 1L;

        while (true) {
            Long id = i;

            boolean exists = compras.stream().anyMatch(compra -> compra.getId().equals(id));
            if (!exists) return id;
        }
    }

    @BeforeEach
    public void setup() {
        Mockito.when(gateway.getNome()).thenReturn("Pagseguro");

        UsuarioRequestBuilder usuarioBuilder = new UsuarioRequestBuilder();
        usuarioBuilder.setSenha("123456");

        List<UsuarioRequest> usuarioRequests = List.of(
                usuarioBuilder.setNome("usuario1.login@email.com").build(),
                usuarioBuilder.setNome("usuario2.login@email.com").build(),
                usuarioBuilder.setNome("usuario3.login@email.com").build()
        );

        usuarios = usuarioRepository.saveAll(usuarioRequests.stream()
                .map(i -> i.converte(passwordEncoder))
                .collect(Collectors.toList())
        );

        List<Categoria> categorias = categoriaRepository.saveAll(List.of(
                new Categoria("Categoria 1"),
                new Categoria("Categoria 2"),
                new Categoria("Categoria 3")
        ));

        categorias.forEach(i -> categoriaRepository.save(i));

        ProdutoRequestBuilder produtoBuilder = new ProdutoRequestBuilder();
        produtoBuilder.setDescricao("Descrição");

        CaracteristicaRequestBuilder caracteristicaBuilder = new CaracteristicaRequestBuilder();
        caracteristicaBuilder.setDescricao("Descrição");

        List<CaracteristicaRequest> caracteristicaRequests = List.of(
                caracteristicaBuilder.setNome("Característica 1").build(),
                caracteristicaBuilder.setNome("Característica 2").build(),
                caracteristicaBuilder.setNome("Característica 3").build(),
                caracteristicaBuilder.setNome("Característica 4").build(),
                caracteristicaBuilder.setNome("Característica 5").build()
        );

        List<Produto> produtos = produtoRepository.saveAll(List.of(
                produtoBuilder
                        .setNome("Produto 1")
                        .setPreco(BigDecimal.valueOf(20.14))
                        .setQuantidade(5)
                        .addCaracteristica(pickRandomValue(caracteristicaRequests))
                        .setCategoria(pickRandomValue(categorias).getNome())
                        .build().converte(categoriaRepository, pickRandomValue(usuarios)),

                produtoBuilder
                        .setNome("Produto 2")
                        .setPreco(BigDecimal.valueOf(35.20))
                        .setQuantidade(2)
                        .addCaracteristica(pickRandomValue(caracteristicaRequests))
                        .setCategoria(pickRandomValue(categorias).getNome())
                        .build().converte(categoriaRepository, pickRandomValue(usuarios)),

                produtoBuilder
                        .setNome("Produto 3")
                        .setPreco(BigDecimal.valueOf(55.10))
                        .setQuantidade(2)
                        .addCaracteristica(pickRandomValue(caracteristicaRequests))
                        .setCategoria(pickRandomValue(categorias).getNome())
                        .build().converte(categoriaRepository, pickRandomValue(usuarios))
        ));

        CompraRequestBuilder compraBuilder = new CompraRequestBuilder();
        compraBuilder.setGateway("Pagseguro");

        Produto produto = pickRandomValue(produtos);
        compras = compraRepository.saveAll(List.of(
                compraBuilder
                        .setQuantidade(1)
                        .setProduto(produto.getId())
                        .build().converte(pickRandomValue(usuarios), produto),

                compraBuilder
                        .setQuantidade(1)
                        .setProduto(produto.getId())
                        .build().converte(pickRandomValue(usuarios), produto)
        ));

        SpringConstraintValidatorFactory factory = new SpringConstraintValidatorFactory(context.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();

        entityManager.clear();
    }

    @Test
    public void testaPagamentoSucessoValido() {
        Compra compra = pickRandomValue(compras);
        String id = "ID Qualquer";
        PagseguroStatus status = PagseguroStatus.SUCESSO;

        PagseguroRequest request = new PagseguroRequest(compra.getId(), id, status);
        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testaPagamentoErroValido() {
        Compra compra = pickRandomValue(compras);
        String id = "ID Qualquer";
        PagseguroStatus status = PagseguroStatus.ERRO;

        PagseguroRequest request = new PagseguroRequest(compra.getId(), id, status);
        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaCompraIDVazioInvalido(Long compra) {
        String id = "ID Qualquer";
        PagseguroStatus status = PagseguroStatus.SUCESSO;

        PagseguroRequest request = new PagseguroRequest(compra, id, status);
        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaCompraIDInexistenteInvalido() {
        Long compra = getInexistentCompraId();
        String id = "ID Qualquer";
        PagseguroStatus status = PagseguroStatus.SUCESSO;

        PagseguroRequest request = new PagseguroRequest(compra, id, status);
        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaCompraFinalizadaInvalido() {
        Compra compra = pickRandomValue(compras);
        String id = "ID Qualquer";
        PagseguroStatus status = PagseguroStatus.SUCESSO;

        PagseguroRequest request = new PagseguroRequest(compra.getId(), id, status);
        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty(), "Primeira request deveria ser válida!");

        Pagamento pagamento = request.converte(gateway, compra);
        compra.adicionaPagamento(pagamento);
        compraRepository.save(compra);

        id = "Um ID Diferente";
        request = new PagseguroRequest(compra.getId(), id, status);
        errors = validator.validate(request);
        assertFalse(errors.isEmpty(), "Segunda request deveria ser inválida!");
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaPagamentoIDVazioInvalido(String id) {
        Compra compra = pickRandomValue(compras);
        PagseguroStatus status = PagseguroStatus.ERRO;

        PagseguroRequest request = new PagseguroRequest(compra.getId(), id, status);
        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"ID QUAlquer", "Qualquer ID", "UM ID", "um id"})
    public void testaPagamentoIDDuplicadoInvalido(String id) {
        Compra compra = pickRandomValue(compras);
        PagseguroStatus status = PagseguroStatus.ERRO;

        PagseguroRequest request = new PagseguroRequest(compra.getId(), id, status);
        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty(), "Primeira request deveria ser válida!");

        Pagamento pagamento = request.converte(gateway, compra);

        compra.adicionaPagamento(pagamento);
        compraRepository.save(compra);

        PagseguroRequest duplicada = new PagseguroRequest(compra.getId(), id.toLowerCase(), status);
        errors = validator.validate(duplicada);
        assertFalse(errors.isEmpty(), "Segunda request deveria ser inválida!");
    }

    @ParameterizedTest
    @NullSource
    public void testaStatusVazioInvalido(PagseguroStatus status) {
        Compra compra = pickRandomValue(compras);
        String id = "Um ID qualquer";
        PagseguroRequest request = new PagseguroRequest(compra.getId(), id, status);

        Set<ConstraintViolation<PagseguroRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }
}