package bootcamp.mercado.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import bootcamp.mercado.config.SecurityConfig;
import bootcamp.mercado.dataBuilder.UsuarioBuilder;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;

@DataJpaTest
@Import(SecurityConfig.class)
class UniqueValidatorTest {
	
	@PersistenceContext private EntityManager manager;
	@Autowired private UsuarioRepository repository;
	@Autowired private PasswordEncoder encoder;
	
	private Usuario usuarioExistente;
	private UniqueValidator validator;
	
	private UsuarioBuilder builder = new UsuarioBuilder();
	private String field = "login";
	
	@BeforeEach
	public void setup() {
		Usuario usuarioExistente = builder
				.comLogin("existente@email.com.br")
				.comSenha("123456")
				.comEncoder(encoder)
				.build();
		
		Usuario maria = builder
				.comLogin("maria@email.com.br")
				.comSenha("654321")
				.comEncoder(encoder)
				.build();
		
		Usuario marcos = builder
				.comLogin("marcos@email.com.br")
				.comSenha("546123")
				.comEncoder(encoder)
				.build();
		
		this.usuarioExistente = usuarioExistente;
		repository.saveAll(Arrays.asList(usuarioExistente, maria, marcos));
		
		validator = new UniqueValidator(manager);
	}
	
	@Test
	public void deveFalharValidarUsuario() {
		String login = usuarioExistente.getLogin();
		assertFalse(validator.validate(login, Usuario.class, field));
	}
	
	@Test
	public void deveSucederValidarUsuario() {
		String login = "ana@email.com.br";
		assertTrue(validator.validate(login, Usuario.class, field));
	}
}
