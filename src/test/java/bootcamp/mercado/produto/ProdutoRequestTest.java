package bootcamp.mercado.produto;

import bootcamp.mercado.produto.caracteristica.CaracteristicaRequest;
import bootcamp.mercado.produto.categoria.Categoria;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRequestTest {
    @PersistenceContext private EntityManager entityManager;
    @Autowired private ApplicationContext context;

    private final List<Categoria> categorias = List.of(
            new Categoria("Categoria 1"),
            new Categoria("Categoria 2"),
            new Categoria("Categoria 3")
    );

    private final List<CaracteristicaRequest> caracteristicas = List.of(
            new CaracteristicaRequest("Característica 1", "Descrição"),
            new CaracteristicaRequest("Característica 2", "Descrição"),
            new CaracteristicaRequest("Característica 3", "Descrição")
    );

    private final Map<String, Map<Class<?>, List<?>>> validValues = Map.of(
            "setNome", Map.of(String.class, List.of("Produto", "umproduto", "NOME", "Meu produto")),
            "setDescricao", Map.of(String.class, List.of("Descrição", "Uma Descrição", "descrição")),
            "setCategoria", Map.of(String.class, List.of("Categoria 1", "Categoria 2", "Categoria 3")),
            "setPreco", Map.of(Double.class, List.of(13.46, 52.12, 76.24, 34.59)),
            "setQuantidade", Map.of(Integer.class, List.of(50, 13, 94, 43)),
            "addAllCaracteristica", Map.of(Collection.class, List.of(caracteristicas))
    );

    private ProdutoRequestBuilder builder;
    private LocalValidatorFactoryBean validator;

    private int pickRandomIndex(List<?> values) {
        return new Random().nextInt(values.size());
    }

    private <T> T pickRandomValue(List<T> values) {
        return values.get(pickRandomIndex(values));
    }

    private ProdutoRequestBuilder generateValidProdutoRequestBuilder() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        ProdutoRequestBuilder builder = new ProdutoRequestBuilder();

        for (String methodName : validValues.keySet()) {
            for (Class<?> paramClass : validValues.get(methodName).keySet()) {
                Method method = ProdutoRequestBuilder.class.getMethod(methodName, paramClass);
                method.setAccessible(true);

                List<?> values = validValues.get(methodName).get(paramClass);
                method.invoke(builder, paramClass.cast(pickRandomValue(values)));
            }
        }

        return builder;
    }

    @BeforeEach
    public void setup() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        categorias.forEach(i -> entityManager.persist(i));
        builder = generateValidProdutoRequestBuilder();

        SpringConstraintValidatorFactory factory = new SpringConstraintValidatorFactory(context.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setApplicationContext(context);
        validator.setConstraintValidatorFactory(factory);
        validator.afterPropertiesSet();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaNomeVazioInvalido(String nome) {
        ProdutoRequest request = builder.setNome(nome).build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaPrecoVazio(BigDecimal preco) {
        ProdutoRequest request = builder.setPreco(preco).build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(doubles =  { 0.00, -1.00, -5.00 })
    public void testaPrecoInvalido(Double preco) {
        ProdutoRequest request = builder.setPreco(preco).build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = { -1, -5, -10 })
    public void testaQuantidadeInvalida(Integer quantidade) {
        ProdutoRequest request = builder.setQuantidade(quantidade).build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaCaracteristicasVazia(ArrayList<CaracteristicaRequest> caracteristicas) {
        ProdutoRequest request = builder.setCaracteristicas(caracteristicas).build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaCaracteristicaSizeInvalido() {
        CaracteristicaRequest caracteristica = pickRandomValue(caracteristicas);
        builder.setCaracteristicas(new ArrayList<>(List.of(caracteristica)));
        ProdutoRequest request = builder.build();

        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaCaracteristicaInvalida() {
        ArrayList<CaracteristicaRequest> caracteristicas = new ArrayList<>(List.of(
                new CaracteristicaRequest("", ""),
                new CaracteristicaRequest("", ""),
                new CaracteristicaRequest("", "")
        ));
        builder.setCaracteristicas(caracteristicas);
        ProdutoRequest request = builder.build();

        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaDescricaoVazia(String descricao) {
        ProdutoRequest request = builder.setDescricao(descricao).build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Categoria INEXISTENTE", "inexistente", "NÃO EXISTE"})
    public void testaCategoriaInvalida(String categoria) {
        ProdutoRequest request = builder.setCategoria(categoria).build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaRequestValida() {
        ProdutoRequest request = builder.build();
        Set<ConstraintViolation<ProdutoRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }
}