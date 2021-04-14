package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.CompraStatus;
import bootcamp.mercado.produto.compra.Pagamento;
import bootcamp.mercado.produto.compra.gateway.Gateway;
import bootcamp.mercado.produto.compra.gateway.GatewayList;
import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.autenticacao.TokenParser;
import bootcamp.mercado.usuario.autenticacao.UsuarioLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@WebMvcTest(PaypalGatewayController.class)
public class PaypalGatewayControllerTest extends PagamentoControllerTest<PaypalStatus, PaypalRequest>{
    PaypalGatewayControllerTest() {
        super("/pagamentos/paypal",
                new PaypalRequest(1L, "ID", PaypalStatus.SUCESSO),
                new PaypalRequest(1L, "ID", PaypalStatus.SUCESSO));
    }
}
