package bootcamp.mercado.gateway;

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
                .filter(i -> { return i.isGateway(nome); })
                .findFirst();
    }

    public Boolean exists(String nome) {
        return gateways.stream()
                .anyMatch(i -> { return i.isGateway(nome); });
    }
}
