package bootcamp.mercado.foto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.web.multipart.MultipartFile;

import bootcamp.mercado.produto.Produto;

@Entity
public class Foto {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@Column(nullable = false)
	private MultipartFile foto;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto produto;
	
	public Foto(MultipartFile foto, Produto produto) {
		this.foto = foto;
		this.produto = produto;
	}
}
