package bootcamp.mercado.pergunta;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import bootcamp.mercado.email.EmailService;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;

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
	
	public Pergunta(String titulo, Usuario usuario, Produto produto) {
		this.titulo = titulo;
		this.usuario = usuario;
		this.produto = produto;
	}
	
	public void envia(EmailService emailService) {
		System.out.println("Email enviado por " + usuario.getLogin());
		emailService.envia(titulo, produto.getDono().getLogin());
	}
}
