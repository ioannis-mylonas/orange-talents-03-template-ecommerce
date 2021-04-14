package bootcamp.mercado.produto.compra.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GatewayListTest {
    private GatewayList gatewayList;

    @Test
    public void testaEncontraGatewayEsperada() {
        gatewayList = new GatewayList(List.of(
                new PagseguroGatewayDev(),
                new PaypalGatewayDev()
        ));

        Optional<Gateway> paypal = gatewayList.getGateway("Paypal");
        Optional<Gateway> pagseguro = gatewayList.getGateway("Pagseguro");

        assertTrue(paypal.isPresent());
        assertTrue(pagseguro.isPresent());

        assertTrue(gatewayList.exists("Paypal"));
        assertTrue(gatewayList.exists("Pagseguro"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Gateway Inexistente", "não existe", "NÃO HÁ", "Pagseguro"})
    public void testaNaoEncontraGatewayInexistente(String id) {
        gatewayList = new GatewayList(List.of(
                new PaypalGatewayDev()
        ));

        Optional<Gateway> inexistente = gatewayList.getGateway(id);

        assertTrue(inexistente.isEmpty());
        assertFalse(gatewayList.exists(id));
    }
}