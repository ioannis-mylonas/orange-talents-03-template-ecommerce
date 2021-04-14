package bootcamp.mercado.produto.foto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FotoRequestTest {
    @Autowired ApplicationContext context;
    private LocalValidatorFactoryBean validator;

    @Mock private MultipartFile foto;

    @BeforeEach
    public void setup() {
        SpringConstraintValidatorFactory factory = new SpringConstraintValidatorFactory(
                context.getAutowireCapableBeanFactory()
        );

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();
    }

    @Test
    public void testaListaValida() {
        FotoRequest request = new FotoRequest(List.of(foto));

        Set<ConstraintViolation<FotoRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @NullSource
    public void testaListaNullInvalida(List<MultipartFile> fotos) {
        FotoRequest request = new FotoRequest(fotos);

        Set<ConstraintViolation<FotoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaListaVaziaInvalida() {
        FotoRequest request = new FotoRequest(List.of());

        Set<ConstraintViolation<FotoRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }
}