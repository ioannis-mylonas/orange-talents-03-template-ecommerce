package bootcamp.mercado.config;

import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthFilter extends OncePerRequestFilter {
	
	private TokenService tokenService;
	private UsuarioRepository repository;

	public TokenAuthFilter(TokenService tokenService,
			UsuarioRepository repository) {
		
		this.tokenService = tokenService;
		this.repository = repository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getToken(request);
		boolean valido = tokenService.isValid(token);
		if (valido) autentica(token);
		
		filterChain.doFilter(request, response);
	}
	
	private String getToken(HttpServletRequest request) {
		String raw = request.getHeader("Authorization");
		if (raw == null || raw.isEmpty() || !raw.startsWith("Bearer "))
			return null;
		
		return raw.substring(7, raw.length());
	}
	
	private void autentica(String token) {
		Long usuarioId = tokenService.getUsuarioId(token);
		Usuario usuario = repository.findById(usuarioId).get();
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
