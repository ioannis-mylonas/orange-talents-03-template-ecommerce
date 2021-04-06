package bootcamp.mercado.gateway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GatewayList {
    private static List<Gateway> gateways = new ArrayList<>(Arrays.asList(
            new PaypalGateway(),
            new PagseguroGateway()
    ));

    public static Gateway getGateway(String nome) {
        for (Gateway gateway : gateways) {
            if (gateway.isGateway(nome)) return gateway;
        }
        return null;
    }

    public static Boolean exists(String nome) {
        for (Gateway gateway : gateways) {
            if (gateway.isGateway(nome)) return true;
        }
        return false;
    }
}
