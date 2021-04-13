package bootcamp.mercado.produto.compra;

import bootcamp.mercado.produto.compra.CompraStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pagamento {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String pagamentoId;
    @ManyToOne
    private Compra compra;
    private String gateway;
    @Enumerated(EnumType.STRING)
    private CompraStatus status;
    @CreationTimestamp
    private LocalDateTime dataProcessamento = LocalDateTime.now();

    @Deprecated
    public Pagamento() {}

    public Pagamento(Compra compra, String gateway, String pagamentoId, CompraStatus status) {
        this.compra = compra;
        this.gateway = gateway;
        this.pagamentoId = pagamentoId;
        this.status = status;
    }

    public CompraStatus getStatus() {
        return status;
    }
}
