package bootcamp.mercado.produto.caracteristica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CaracteristicaRequestTest {
    @Autowired private CaracteristicaRepository caracteristicaRepository;
    @Autowired private ApplicationContext context;

    private SpringConstraintValidatorFactory factory;
    private LocalValidatorFactoryBean validator;

    private final CaracteristicaRequestBuilder builder = new CaracteristicaRequestBuilder();

    @BeforeEach
    public void setup() {
        factory = new SpringConstraintValidatorFactory(
                context.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();

        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();

        builder
                .setNome("Um Nome")
                .setDescricao("Uma Descricao");
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaNomeInvalido(String nome) {
        CaracteristicaRequest request = builder.setNome(nome).build();
        Set<ConstraintViolation<CaracteristicaRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaDescricaoInvalida(String descricao) {
        CaracteristicaRequest request = builder.setDescricao(descricao).build();
        Set<ConstraintViolation<CaracteristicaRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"Um Nome,Uma Descrição",
            "Característica 1,Descrição 1",
            "UmA CaRaCtErÍstica,UmA DesCrIçâO",
            "    UmA CarAcTerìStica    ,  Descrição   "})
    public void testaCaracteristicaValida(String nome, String descricao) {
        CaracteristicaRequest request = builder.setNome(nome).setDescricao(descricao).build();
        Set<ConstraintViolation<CaracteristicaRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }
}