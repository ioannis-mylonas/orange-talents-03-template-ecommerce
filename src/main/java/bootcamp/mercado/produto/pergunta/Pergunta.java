package bootcamp.mercado.produto.pergunta;

import bootcamp.mercado.email.MercadoMailSender;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pergunta {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String titulo;
	@CreationTimestamp
	private LocalDateTime criacao;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Usuario usuario;
	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto produto;
	
	@Deprecated
	public Pergunta() {}
	
	public Pergunta(String titulo, Usuario usuario, Produto produto) {
		this.titulo = titulo;
		this.usuario = usuario;
		this.produto = produto;
	}
	
	public void envia(MercadoMailSender mailSender) {
		System.out.println("Email enviado por " + usuario.getLogin());
		mailSender.envia(titulo, "Pergunta", produto.getDono().getLogin());
	}

	public String getTitulo() {
		return titulo;
	}

	public LocalDateTime getCriacao() {
		return criacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}
}
