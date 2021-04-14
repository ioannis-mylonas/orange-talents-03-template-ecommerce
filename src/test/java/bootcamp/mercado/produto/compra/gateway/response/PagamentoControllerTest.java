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

abstract class PagamentoControllerTest<S extends TransacaoStatus, T extends TransacaoRequest<S>> {
    @Autowired MockMvc mvc;

    /**
     * Mock SecurityConfig beans
     */
    @MockBean private UsuarioLogin usuarioLogin;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private TokenParser tokenParser;

    /**
     * Mock PagseguroGatewayController beans
     */
    @MockBean private EntityManager entityManager;
    @MockBean private ProcessaPagamento processaPagamento;
    @MockBean private GatewayList gatewayList;
    @Mock private Gateway gateway;

    /**
     * Mock validator beans
     */
    @Mock private Query existsQuery;
    @Mock private List<?> existsList;
    @Mock private Query uniqueQuery;
    @Mock private List<?> uniqueList;

    @Mock private Compra compra;

    private final String url;
    private final T requestSucesso;
    private final T requestErro;

    public PagamentoControllerTest(String url, T requestSucesso, T requestErro) {
        this.url = url;
        this.requestSucesso = requestSucesso;
        this.requestErro = requestErro;
    }

    @BeforeEach
    public void setup() {
        //Mock @Exists constraint
        Mockito.when(entityManager.createQuery(Mockito.matches(
                "(.*" + Compra.class.getName() + ".*)"
                ))
        ).thenReturn(existsQuery);

        Mockito.when(existsQuery.getResultList()).thenReturn(existsList);
        Mockito.when(existsList.isEmpty()).thenReturn(false);


        //Mock @Unique constraint
        Mockito.when(entityManager.createQuery(Mockito.matches(
                "(.*" + Pagamento.class.getName() + ".*)"
                ))
        ).thenReturn(uniqueQuery);

        Mockito.when(uniqueQuery.getResultList()).thenReturn(uniqueList);
        Mockito.when(uniqueList.isEmpty()).thenReturn(true);

        //Mock Compra
        Mockito.when(entityManager.find(Compra.class, 1L)).thenReturn(compra);
        Mockito.when(compra.getStatus()).thenReturn(CompraStatus.INICIADA);
        Mockito.when(gatewayList.getGateway(Mockito.anyString())).thenReturn(Optional.of(gateway));
    }

    @WithMockUser
    @Test
    public void testaProcessaSucessoPagseguro() throws Exception {
        // Build JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(requestSucesso);

        // Test
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    public void testaProcessaErroPagseguro() throws Exception {
        // Build JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(requestErro);

        // Test
        mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}