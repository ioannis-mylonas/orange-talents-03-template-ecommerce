package bootcamp.mercado.config;

import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.autenticacao.TokenParser;
import bootcamp.mercado.usuario.autenticacao.UsuarioLogin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private UsuarioLogin usuarioLogin;
	private UsuarioRepository usuarioRepository;
	private TokenParser tokenParser;

	public SecurityConfig(UsuarioLogin usuarioLogin,
						  UsuarioRepository usuarioRepository,
						  TokenParser tokenParser) {

		this.usuarioLogin = usuarioLogin;
		this.usuarioRepository = usuarioRepository;
		this.tokenParser = tokenParser;
	}

	@Bean
	public PasswordEncoder encoder() {
		return new Argon2PasswordEncoder();
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioLogin);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/usuarios").permitAll()
				.antMatchers(HttpMethod.POST, "/auth").permitAll()
				.antMatchers(HttpMethod.POST, "/nf").permitAll()
				.antMatchers(HttpMethod.POST, "/ranking").permitAll()
				.antMatchers(HttpMethod.GET, "/pagseguro-endpointfake").permitAll()
				.antMatchers(HttpMethod.GET, "/paypal-endpointfake").permitAll()
				.antMatchers(HttpMethod.POST, "/pagamentos/paypal").permitAll()
				.antMatchers(HttpMethod.POST, "/pagamentos/pagseguro").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.anyRequest().authenticated()
				.and().headers().frameOptions().disable()
				.and().csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().addFilterBefore(new TokenAuthFilter(usuarioRepository, tokenParser),
				UsernamePasswordAuthenticationFilter.class);
	}
}
