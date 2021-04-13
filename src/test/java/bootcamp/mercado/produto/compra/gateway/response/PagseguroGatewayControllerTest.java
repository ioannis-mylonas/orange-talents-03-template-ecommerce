package bootcamp.mercado.produto.compra.gateway.response;

import bootcamp.mercado.produto.compra.Compra;
import bootcamp.mercado.produto.compra.gateway.GatewayList;
import bootcamp.mercado.usuario.Senha;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.autenticacao.TokenParser;
import bootcamp.mercado.usuario.autenticacao.UsuarioLogin;
import bootcamp.mercado.validator.Exists;
import bootcamp.mercado.validator.ExistsValidator;
import bootcamp.mercado.validator.Unique;
import bootcamp.mercado.validator.UniqueValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

@WebMvcTest(PagseguroGatewayController.class)
class PagseguroGatewayControllerTest {
    @Autowired private MockMvc mvc;

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

    // TODO : Consertar NPE dentro de isValid
    @MockBean(classes = UniqueValidator.class) private UniqueValidator unique;
    @MockBean(classes = ExistsValidator.class) private ExistsValidator exists;

    @Mock private Compra compra;

    @WithMockUser
    @Test
    public void testaProcessaSucesso() throws Exception {
        Mockito.when(unique.isValid(Mockito.eq("ID"), Mockito.any())).thenReturn(true);
        Mockito.when(exists.isValid(Mockito.eq(1L), Mockito.any())).thenReturn(true);

        PagseguroRequest request = new PagseguroRequest(1L, "ID", PagseguroStatus.SUCESSO);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mvc.perform(MockMvcRequestBuilders.post("/pagamentos/pagseguro")
                .header("Content-Type", "application/json")
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @WithMockUser
    @Test
    public void testaProcessaErro() throws Exception {
        PagseguroRequest request = new PagseguroRequest(1L, "ID", PagseguroStatus.ERRO);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mvc.perform(MockMvcRequestBuilders.post("/pagamentos/pagseguro")
                .header("Content-Type", "application/json")
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}