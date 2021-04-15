package bootcamp.mercado.produto.opiniao;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.testConfig.ConfigMockTestUser;
import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.autenticacao.TokenParser;
import bootcamp.mercado.usuario.autenticacao.UsuarioLogin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OpiniaoController.class)
@Import(ConfigMockTestUser.class)
class OpiniaoControllerTest {
    // Mock SecurityConfig beans
    @MockBean private UsuarioLogin usuarioLogin;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private TokenParser tokenParser;

    // Mock controller beans
    @MockBean private ProdutoRepository produtoRepository;
    @MockBean private OpiniaoRepository opiniaoRepository;

    @Autowired private MockMvc mvc;

    @Test
    @WithMockUser
    public void testaCadastroProdutoInexistenteNotFound() throws Exception {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.empty());
        OpiniaoRequest opiniaoRequest = new OpiniaoRequest("Título", "Descrição", 5);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(opiniaoRequest);

        mvc.perform(MockMvcRequestBuilders.post("/opinioes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testaCadastroProdutoExistenteSucesso() throws Exception {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(Mockito.mock(Produto.class)));
        OpiniaoRequest opiniaoRequest = new OpiniaoRequest("Título", "Descrição", 5);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(opiniaoRequest);

        mvc.perform(MockMvcRequestBuilders.post("/opinioes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}