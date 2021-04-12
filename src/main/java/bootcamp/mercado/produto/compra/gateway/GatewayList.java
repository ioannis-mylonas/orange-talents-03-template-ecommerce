package bootcamp.mercado.produto.compra.gateway;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class GatewayList {
    private Collection<Gateway> gateways;

    public GatewayList(Collection<Gateway> gateways) {
        this.gateways = gateways;
    }

    public Optional<Gateway> getGateway(String nome) {
        return gateways.stream()
                .filter(i -> i.isGateway(nome))
                .findFirst();
    }

    public Boolean exists(String nome) {
        return gateways.stream()
                .anyMatch(i ->  i.isGateway(nome));
    }
}
