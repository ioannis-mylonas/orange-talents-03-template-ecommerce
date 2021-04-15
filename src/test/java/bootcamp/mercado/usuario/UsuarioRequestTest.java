package bootcamp.mercado.usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional @Rollback
class UsuarioRequestTest {
    @PersistenceContext private EntityManager entityManager;

    @Autowired private ApplicationContext context;

    private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
    private LocalValidatorFactoryBean validator;
    private List<Usuario> usuarios;

    private Map<String, List<String>> validValues = Map.of(
            "login", List.of("login@email.com", "email@login.com", "email@email"),
            "senha", List.of("123456", "654321", "abc123", "abcdef")
    );

    private UsuarioRequestBuilder builder;

    private int pickRandomIndex(List<?> values) {
        return new Random().nextInt(values.size());
    }

    private <T> T pickRandomValue(List<T> values) {
        return values.get(pickRandomIndex(values));
    }

    private UsuarioRequestBuilder generateRandomValidBuilder() {
        UsuarioRequestBuilder builder = new UsuarioRequestBuilder();

        builder.setNome(pickRandomValue(validValues.get("login")));
        builder.setSenha(pickRandomValue(validValues.get("senha")));

        return builder;
    }

    @BeforeEach
    public void setup() {
        builder = generateRandomValidBuilder();

        usuarios = List.of(
                builder.setNome("login1@email.com").build().converte(passwordEncoder),
                builder.setNome("login2@email.com").build().converte(passwordEncoder),
                builder.setNome("login3@email.com").build().converte(passwordEncoder),
                builder.setNome("login4@email.com").build().converte(passwordEncoder),
                builder.setNome("login5@email.com").build().converte(passwordEncoder)
        );

        usuarios.forEach(i -> entityManager.persist(i));

        SpringConstraintValidatorFactory factory = new SpringConstraintValidatorFactory(context.getAutowireCapableBeanFactory());

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(factory);
        validator.setApplicationContext(context);
        validator.afterPropertiesSet();

        builder = generateRandomValidBuilder();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "login1@email.com", "login2@email.com",
            "login3@email.com", "login4@email.com",
            "login5@email.com"
    })
    public void testaLoginInvalido(String login) {
        UsuarioRequest request = builder.setNome(login).build();
        Set<ConstraintViolation<UsuarioRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "1", "12345", "umase", "nha"
    })
    public void testaSenhaInvalida(String senha) {
        UsuarioRequest request = builder.setSenha(senha).build();
        Set<ConstraintViolation<UsuarioRequest>> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    public void testaRequestValida() {
        UsuarioRequest request = builder.build();
        Set<ConstraintViolation<UsuarioRequest>> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }
}