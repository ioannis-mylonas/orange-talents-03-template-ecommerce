package bootcamp.mercado.usuario.autenticacao;

import org.junit.jupiter.api.BeforeEach;
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
class LoginRequestTest {
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
    public void testaLoginVazio(String nome) {
        LoginRequest request = new LoginRequest(nome, "123456");
        Set<ConstraintViolation<LoginRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testaSenhaVazia(String senha) {
        LoginRequest request = new LoginRequest("login", senha);
        Set<ConstraintViolation<LoginRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "login", "UMLOGIN", "login@login", "login@login.com", "um login"
    })
    public void testaLoginValido(String login) {
        LoginRequest request = new LoginRequest(login, "123456");
        Set<ConstraintViolation<LoginRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456", "12747", "12", "UMA SENHA", "uma senha", "sênhã5000"
    })
    public void testaSenhaValida(String senha) {
        LoginRequest request = new LoginRequest("login", senha);
        Set<ConstraintViolation<LoginRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }
}