package bootcamp.mercado.usuario.autenticacao;

public class TokenResponse {
	private String token;
	private String type;
	
	public TokenResponse(String token, String type) {
		this.token = token;
		this.type = type;
	}

	public TokenResponse(String token) {
		this.token = token;
		this.type = "Bearer";
	}
	
	public String getToken() {
		return token;
	}
	
	public String getType() {
		return type;
	}
}
