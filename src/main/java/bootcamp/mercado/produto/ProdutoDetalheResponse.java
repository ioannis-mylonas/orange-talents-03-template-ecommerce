package bootcamp.mercado.produto;

import bootcamp.mercado.produto.caracteristica.ProdutoDetalheCaracteristicaResponse;
import bootcamp.mercado.produto.foto.ProdutoDetalheFotoResponse;
import bootcamp.mercado.produto.opiniao.Opiniao;
import bootcamp.mercado.produto.opiniao.ProdutoDetalheOpiniaoResponse;
import bootcamp.mercado.produto.pergunta.Pergunta;
import bootcamp.mercado.produto.pergunta.ProdutoDetalhePerguntaResponse;
import bootcamp.mercado.usuario.ProdutoDetalheUsuarioResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ProdutoDetalheResponse {
	private List<ProdutoDetalheFotoResponse> fotos;
	private String nome;
	private String descricao;
	private BigDecimal valor;
	private Integer quantidade;
	private List<ProdutoDetalheCaracteristicaResponse> caracteristicas;
	private ProdutoDetalheUsuarioResponse dono;
	
	private List<ProdutoDetalhePerguntaResponse> perguntas;
	private List<ProdutoDetalheOpiniaoResponse> opinioes;
	
	private Double mediaNotas;
	private Integer numeroNotas;
	
	public ProdutoDetalheResponse(Produto produto,
			List<Pergunta> perguntas,
			List<Opiniao> opinioes) {
		
		this.nome = produto.getNome();
		this.descricao = produto.getDescricao();
		this.valor = produto.getPreco();
		this.quantidade = produto.getQuantidade();
		this.dono = new ProdutoDetalheUsuarioResponse(produto.getDono());
		this.fotos = ProdutoDetalheFotoResponse.fromList(produto.getFotos());
		this.caracteristicas = ProdutoDetalheCaracteristicaResponse.fromList(produto.getCaracteristicas());
		this.perguntas = ProdutoDetalhePerguntaResponse.fromList(perguntas);
		this.opinioes = ProdutoDetalheOpiniaoResponse.fromList(opinioes);
		
		updateNumeroNotas();
		updateMediaNotas();
	}
	
	private void updateNumeroNotas() {
		this.numeroNotas = opinioes.size();
	}
	
	private void updateMediaNotas() {
		if (opinioes.size() == 0) {
			this.mediaNotas = 0.0;
			return;
		}
		
		double total = 0.0;
		for (ProdutoDetalheOpiniaoResponse opiniao : opinioes) {
			total += opiniao.getNota();
		}
		this.mediaNotas = total / (double) numeroNotas;
		
		BigDecimal bigdecimal = new BigDecimal(Double.toString(this.mediaNotas));
		bigdecimal = bigdecimal.setScale(2, RoundingMode.HALF_UP);
		this.mediaNotas = bigdecimal.doubleValue();
	}

	public List<ProdutoDetalheFotoResponse> getFotos() {
		return fotos;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public List<ProdutoDetalheCaracteristicaResponse> getCaracteristicas() {
		return caracteristicas;
	}

	public ProdutoDetalheUsuarioResponse getDono() {
		return dono;
	}

	public List<ProdutoDetalhePerguntaResponse> getPerguntas() {
		return perguntas;
	}

	public List<ProdutoDetalheOpiniaoResponse> getOpinioes() {
		return opinioes;
	}

	public Double getMediaNotas() {
		return mediaNotas;
	}

	public Integer getNumeroNotas() {
		return numeroNotas;
	}
}
