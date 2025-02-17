package bootcamp.mercado.config;

import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.autenticacao.Token;
import bootcamp.mercado.usuario.autenticacao.TokenParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class TokenAuthFilter extends OncePerRequestFilter {
	
	private UsuarioRepository repository;
	private TokenParser tokenParser;

	public TokenAuthFilter(UsuarioRepository repository, TokenParser tokenParser) {
		this.repository = repository;
		this.tokenParser = tokenParser;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getToken(request);
		autentica(token);
		
		filterChain.doFilter(request, response);
	}
	
	private String getToken(HttpServletRequest request) {
		String raw = request.getHeader("Authorization");
		if (raw == null || raw.isEmpty() || !raw.startsWith("Bearer "))
			return null;
		
		return raw.substring(7, raw.length());
	}
	
	private void autentica(String token) {
		Token res = tokenParser.parse(token);
		if (res == null) return;

		Optional<Usuario> usuario = repository.findById(res.getId());
		if (usuario.isEmpty()) return;

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario.get(),
				null, usuario.get().getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
