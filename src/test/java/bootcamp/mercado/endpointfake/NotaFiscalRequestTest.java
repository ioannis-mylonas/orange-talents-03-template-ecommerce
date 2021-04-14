package bootcamp.mercado.endpointfake;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.produto.ProdutoRequestBuilder;
import bootcamp.mercado.produto.caracteristica.Caracteristica;
import bootcamp.mercado.produto.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.produto.caracteristica.CaracteristicaRequest;
import bootcamp.mercado.produto.caracteristica.CaracteristicaRequestBuilder;
import bootcamp.mercado.produto.categoria.Categoria;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraRepository;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.usuario.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotaFiscalRequestTest {
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private CaracteristicaRepository caracteristicaRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private CompraRepository compraRepository;
    @Autowired private ApplicationContext applicationContext;

    private List<Usuario> usuarios;
    private List<Compra> compras;
    private NotaFiscalRequestBuilder builder;

    private SpringConstraintValidatorFactory factory;
    private LocalValidatorFactoryBean validator;

    private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

    private int pickRandomIndex(List<?> values) {
        Random random = new Random();
        return random.nextInt(values.size());
    }

    private <T> T pickRandomValue(List<T> values) {
        return values.get(pickRandomIndex(values));
    }

    private NotaFiscalRequestBuilder generateRandomValidRequest() {
        return new NotaFiscalRequestBuilder()
                .setCompra(pickRandomValue(compras).getId())
                .setUsuario(pickRandomValue(usuarios).getId());
    }

    private Long getInexistentCompraId() {
        usuarios = usuarioRepository.findAll();

        long i = 1L;
        while(true) {
            Long id = i;
            boolean exists = compras.stream().anyMatch(compra -> compra.getId().equals(id));

            if (!exists) return id;
            i++;
        }
    }

    private Long getInexistentUsuarioId() {
        usuarios = usuarioRepository.findAll();

        long i = 1L;
        while(true) {
            Long id = i;
            boolean exists = usuarios.stream().anyMatch(usuario -> usuario.getId().equals(id));

            if (!exists) return id;
            i++;
        }
    }

    @BeforeEach
    public void setup() {
        UsuarioRequestBuilder usuarioBuilder = new UsuarioRequestBuilder();
        usuarioBuilder.setSenha("123456");

        List<UsuarioRequest> usuarioRequests = List.of(
                usuarioBuilder.setNome("usuario1.login@email.com").build(),
                usuarioBuilder.setNome("usuario2.login@email.com").build(),
                usuarioBuilder.setNome("usuario3.login@email.com").build()
        );

        usuarios = usuarioRepository.saveAll(
                usuarioRequests.stream()
                        .map(i -> i.converte(passwordEncoder))
                        .collect(Collectors.toList())
        );

        CaracteristicaRequestBuilder caracteristicaBuilder = new CaracteristicaRequestBuilder();
        caracteristicaBuilder.setDescricao("Descrição");

        List<CaracteristicaRequest> caracteristicaRequests = List.of(
                caracteristicaBuilder.setNome("Característica 1").build(),
                caracteristicaBuilder.setNome("Característica 2").build(),
                caracteristicaBuilder.setNome("Característica 3").build(),
                caracteristicaBuilder.setNome("Característica 4").build(),
                caracteristicaBuilder.setNome("Característica 5").build()
        );

        caracteristicaRepository.saveAll(
                caracteristicaRequests.stream()
                        .map(CaracteristicaRequest::converte)
                        .collect(Collectors.toList())
        );

        List<Categoria> categorias = categoriaRepository.saveAll(List.of(
                new Categoria("Categoria 1"),
                new Categoria("Categoria 2"),
                new Categoria("Categoria 3")
        ));

        ProdutoRequestBuilder produtoBuilder = new ProdutoRequestBuilder();
        produtoBuilder.setDescricao("Descrição");

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
                        .setQuantidade(1)
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

        compras = compraRepository.saveAll(List.of(
                new Compra(pickRandomValue(usuarios), pickRandomValue(produtos), "Paypal", 2, BigDecimal.valueOf(13.20), CompraStatus.SUCESSO)
        ));

        builder = generateRandomValidRequest();

        factory = new SpringConstraintValidatorFactory(applicationContext.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();
    }

    @Test
    public void testaNotaFiscalValida() {
        Set<ConstraintViolation<NotaFiscalRequest>> errors = validator.validate(builder.build());
        assertTrue(errors.isEmpty());
    }

    @Test
    public void testaCompraInexistente() {
        Long id = getInexistentCompraId();
        assertFalse(compraRepository.existsById(id), String.format(
                "ID %s está cadastrado e não deveria estar!", id
        ));

        builder.setCompra(id);
        Set<ConstraintViolation<NotaFiscalRequest>> errors = validator.validate(builder.build());
        assertFalse(errors.isEmpty(), String.format(
                "ID %s não foi cadastrado e deveria ser inválido!", id
        ));
    }

    @Test
    public void testaUsuarioInexistente() {
        Long id = getInexistentUsuarioId();
        assertFalse(usuarioRepository.existsById(id), String.format(
                "ID %s está cadastrado e não deveria estar!", id)
        );

        builder.setUsuario(id);
        Set<ConstraintViolation<NotaFiscalRequest>> errors = validator.validate(builder.build());
        assertFalse(errors.isEmpty(), String.format(
                "ID %s não foi cadastrado e deveria ser inválido!", id
        ));
    }

    @ParameterizedTest
    @NullSource
    public void testaCompraIdNull(Long id) {
        builder.setCompra(id);
        Set<ConstraintViolation<NotaFiscalRequest>> errors = validator.validate(builder.build());
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaUsuarioIdNull(Long id) {
        builder.setUsuario(id);
        Set<ConstraintViolation<NotaFiscalRequest>> errors = validator.validate(builder.build());
        assertFalse(errors.isEmpty());
    }

}