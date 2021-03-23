package bootcamp.mercado.caracteristica;

import javax.validation.constraints.NotBlank;

public class CaracteristicaRequest {
	@NotBlank
	private String nome;
	@NotBlank
	private String descricao;
	
	public CaracteristicaRequest(String nome, String descricao) {
		this.nome = nome;
		this.descricao = descricao;
	}
	
	public Caracteristica converte() {
		return new Caracteristica(nome, descricao);
	}
}
