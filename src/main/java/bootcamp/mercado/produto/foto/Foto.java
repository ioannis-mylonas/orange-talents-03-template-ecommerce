package bootcamp.mercado.produto.foto;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Foto {
	@Id private String uri;

	@Deprecated
	public Foto() {}

	public Foto(String uri) {
		this.uri = uri;
	}
	
	public String getUri() {
		return this.uri;
	}
}
