package bootcamp.mercado.produto.pergunta;

import bootcamp.mercado.email.MercadoMailSender;
import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.testConfig.ConfigMockTestUser;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.autenticacao.TokenParser;
import bootcamp.mercado.usuario.autenticacao.UsuarioLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PerguntaController.class)
@Import(ConfigMockTestUser.class)
class PerguntaControllerTest {
    // Mock SecurityConfig beans
    @MockBean private UsuarioLogin usuarioLogin;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private TokenParser tokenParser;

    // Mock controller beans
    @MockBean private ProdutoRepository produtoRepository;
    @MockBean private PerguntaRepository perguntaRepository;
    @MockBean private MercadoMailSender mailSender;

    @Autowired private MockMvc mvc;

    @Test
    @WithMockUser
    public void testaProdutoInexistenteNotFound() throws Exception {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        PerguntaRequest request = new PerguntaRequest();
        request.setTitulo("Um Título");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mvc.perform(MockMvcRequestBuilders.post("/perguntas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails
    public void testaProdutoExistenteSucesso() throws Exception {
        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(usuario.getLogin()).thenReturn("outro");

        Produto produto = Mockito.mock(Produto.class);
        Mockito.when(produto.getDono()).thenReturn(usuario);
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        PerguntaRequest request = new PerguntaRequest();
        request.setTitulo("Um Título");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mvc.perform(MockMvcRequestBuilders.post("/perguntas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}