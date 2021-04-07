package bootcamp.mercado.compra;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.usuario.Usuario;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Produto produto;
    @Column(nullable = false)
    private String gateway;
    @Column(nullable = false)
    private Integer quantidade;
    @Column(nullable = false)
    private BigDecimal precoUnidade;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompraStatus status;

    @Deprecated
    public Compra() {}

    public Compra(Usuario usuario, Produto produto, String gateway,
                  Integer quantidade, BigDecimal precoUnidade, CompraStatus status) {

        this.usuario = usuario;
        this.produto = produto;
        this.gateway = gateway;
        this.quantidade = quantidade;
        this.precoUnidade = precoUnidade;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getGateway() {
        return gateway;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public CompraStatus getStatus() {
        return status;
    }

    public void mudaStatus(CompraStatus status) {
        if (this.status != CompraStatus.SUCESSO)
            this.status = status;
    }
}
