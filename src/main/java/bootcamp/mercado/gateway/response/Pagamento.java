package bootcamp.mercado.gateway.response;

import bootcamp.mercado.compra.CompraStatus;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Pagamento {
    @Id
    private String pagamentoId;
    private Long compraId;
    private String gateway;
    @Enumerated(EnumType.STRING)
    private CompraStatus status;
    @CreationTimestamp
    private LocalDateTime dataProcessamento = LocalDateTime.now();

    public Pagamento(Long compraId, String gateway, String pagamentoId, CompraStatus status) {
        this.compraId = compraId;
        this.gateway = gateway;
        this.pagamentoId = pagamentoId;
        this.status = status;
    }
}
