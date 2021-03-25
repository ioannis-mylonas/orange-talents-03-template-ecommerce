package bootcamp.mercado.usuario;

public class ProdutoDetalheUsuarioResponse {
	private String login;

	public ProdutoDetalheUsuarioResponse(Usuario usuario) {
		this.login = usuario.getLogin();
	}

	public String getLogin() {
		return login;
	}
}
