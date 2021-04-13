package bootcamp.mercado.endpointfake;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.produto.caracteristica.Caracteristica;
import bootcamp.mercado.produto.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.produto.categoria.Categoria;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraRepository;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.usuario.Senha;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
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
        usuarios = usuarioRepository.saveAll(List.of(
                new Usuario("usuario1.login@email.com", new Senha("123456", passwordEncoder)),
                new Usuario("usuario2.login@email.com", new Senha("654321", passwordEncoder)),
                new Usuario("usuario3.login@email.com", new Senha("546123", passwordEncoder))
        ));

        List<Caracteristica> caracteristicas = caracteristicaRepository.saveAll(List.of(
                new Caracteristica("Caracteristica", "Descricao")
        ));

        List<Categoria> categorias = categoriaRepository.saveAll(List.of(
                new Categoria("Categoria 1"),
                new Categoria("Categoria 2"),
                new Categoria("Categoria 3")
        ));

        List<Produto> produtos = produtoRepository.saveAll(List.of(
                new Produto("Produto 1", BigDecimal.valueOf(20.14), 5, "Descricao",
                        List.of(pickRandomValue(caracteristicas)), pickRandomValue(categorias),
                        pickRandomValue(usuarios)),

                new Produto("Produto 2", BigDecimal.valueOf(35.20), 1, "Descricao",
                        List.of(pickRandomValue(caracteristicas)), pickRandomValue(categorias),
                        pickRandomValue(usuarios))
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