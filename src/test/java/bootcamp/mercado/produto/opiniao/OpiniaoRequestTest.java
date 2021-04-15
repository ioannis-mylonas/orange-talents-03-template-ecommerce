package bootcamp.mercado.produto.opiniao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpiniaoRequestTest {
    @Autowired private ApplicationContext context;
    private LocalValidatorFactoryBean validator;

    private final static List<String> validNames = List.of(
            "Uma opinião", "Uma Segunda Opinião", "TERCEIRA", "quarta opinião"
    );
    private final static List<String> validDescricoes = List.of(
            "Uma descrição", "Uma Segunda Descrição", "TERCEIRADESCRIÇÃO", "quarta descrição"
    );
    private final static List<Integer> validNotas = List.of(
            1, 2, 3, 4, 5
    );

    private static Stream<Arguments> provideValidOpiniaoRequests() {
        Stream.Builder<Arguments> builder = Stream.builder();

        for (String nome : validNames) {
            for (String descricao : validDescricoes) {
                for (Integer nota : validNotas) {
                    builder.add(Arguments.of(
                            nome, descricao, nota
                    ));
                }
            }
        }

        return builder.build();
    }

    @BeforeEach
    public void setup() {
        SpringConstraintValidatorFactory factory = new SpringConstraintValidatorFactory(context.getAutowireCapableBeanFactory());
        validator = new LocalValidatorFactoryBean();

        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaTituloVazio(String titulo) {
        OpiniaoRequest request = new OpiniaoRequest(titulo, "Descrição", 5);
        Set<ConstraintViolation<OpiniaoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaDescricaoVazia(String descricao) {
        OpiniaoRequest request = new OpiniaoRequest("Título", descricao, 5);
        Set<ConstraintViolation<OpiniaoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(ints = { 0, -10, 6, 10 })
    public void testaNotaInvalida(Integer nota) {
        OpiniaoRequest request = new OpiniaoRequest("Título", "Descrição", nota);
        Set<ConstraintViolation<OpiniaoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("provideValidOpiniaoRequests")
    public void testaOpiniaoValida(String titulo, String descricao, Integer nota) {
        OpiniaoRequest request = new OpiniaoRequest(titulo, descricao, nota);
        Set<ConstraintViolation<OpiniaoRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }
}