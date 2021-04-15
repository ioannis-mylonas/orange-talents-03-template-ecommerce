package bootcamp.mercado.produto.pergunta;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PerguntaRequestTest {
    @Autowired private ApplicationContext context;
    private LocalValidatorFactoryBean validator;

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
    public void testaTituloVazioInvalido(String titulo) {
        PerguntaRequest request = new PerguntaRequest();
        request.setTitulo(titulo);

        Set<ConstraintViolation<PerguntaRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Uma pergunta", "GRANDE PERGUNTA", "outra pergunta alguma", "uma"
    })
    public void testaTituloValido(String titulo) {
        PerguntaRequest request = new PerguntaRequest();
        request.setTitulo(titulo);

        Set<ConstraintViolation<PerguntaRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }
}