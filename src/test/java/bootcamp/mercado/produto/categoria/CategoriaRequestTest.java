package bootcamp.mercado.produto.categoria;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRequestTest {
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private ApplicationContext context;

    private List<Categoria> categorias;
    private SpringConstraintValidatorFactory factory;
    private LocalValidatorFactoryBean validator;

    public int pickRandomIndex(List<?> values) {
        return new Random().nextInt(values.size());
    }

    public <T> T pickRandomValue(List<T> values) {
        return values.get(pickRandomIndex(values));
    }

    public void setRandomParente() {
        Categoria parente = pickRandomValue(categorias);
        Categoria filha = pickRandomValue(categorias);

        while(parente.getId().equals(filha.getId())) {
            filha = pickRandomValue(categorias);
        }

        filha.setParente(parente);
        categoriaRepository.save(filha);
    }

    public Categoria getCategoriaComParente() {
        List<Categoria> filhas = categorias.stream()
                .filter(categoria -> categoria.getParente() != null)
                .collect(Collectors.toList());

        return pickRandomValue(filhas);
    }

    @BeforeEach
    public void setup() {
        categorias = categoriaRepository.saveAll(List.of(
                new Categoria("Categoria 1"),
                new Categoria("Categoria 2"),
                new Categoria("Categoria 3"),
                new Categoria("Categoria 4"),
                new Categoria("Categoria 5")
        ));

        setRandomParente();

        factory = new SpringConstraintValidatorFactory(context.getAutowireCapableBeanFactory());
        validator = new LocalValidatorFactoryBean();

        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaNomeInvalido(String nome) {
        CategoriaRequest request = new CategoriaRequest(nome, null);
        Set<ConstraintViolation<CategoriaRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Nova Categoria", "Outra Categoria", "Alguma Categoria"
    })
    public void testaParenteValido(String nome) {
        CategoriaRequest request = new CategoriaRequest(
                nome, pickRandomValue(categorias).getNome()
        );

        Set<ConstraintViolation<CategoriaRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Nova Categoria", "Outra Categoria", "Alguma Categoria"
    })
    public void testaParenteNaoExistente(String parente) {
        CategoriaRequest request = new CategoriaRequest(
                "Categoria Inválida", parente
        );

        Set<ConstraintViolation<CategoriaRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Nova Categoria", "Outra Categoria", "Alguma Categoria"
    })
    public void testaCategoriaNomeDuplicado(String nome) {
        Categoria categoria = new Categoria(nome);
        categoriaRepository.save(categoria);

        CategoriaRequest request = new CategoriaRequest(nome, null);
        Set<ConstraintViolation<CategoriaRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }
}