package bootcamp.mercado.produto;

import bootcamp.mercado.produto.caracteristica.CaracteristicaRepository;
import bootcamp.mercado.produto.categoria.CategoriaRepository;
import bootcamp.mercado.produto.opiniao.OpiniaoRepository;
import bootcamp.mercado.produto.pergunta.PerguntaRepository;
import bootcamp.mercado.testConfig.ConfigMockTestUser;
import bootcamp.mercado.usuario.Usuario;
import bootcamp.mercado.usuario.UsuarioRepository;
import bootcamp.mercado.usuario.autenticacao.TokenParser;
import bootcamp.mercado.usuario.autenticacao.UsuarioLogin;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {
    @Autowired private MockMvc mvc;

    // Mock SecurityConfig beans
    @MockBean private UsuarioLogin usuarioLogin;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private TokenParser tokenParser;

    // Mock controller beans
    @MockBean private ProdutoRepository repository;
    @MockBean private CategoriaRepository categoriaRepository;
    @MockBean private CaracteristicaRepository caracteristicaRepository;
    @MockBean private PerguntaRepository perguntaRepository;
    @MockBean private OpiniaoRepository opiniaoRepository;

    @Test
    @WithMockUser
    public void testaNaoEncontraProdutoNaoExistente() throws Exception {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.get("/produtos/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser
    public void testaEncontraProdutoExistente() throws Exception {
        Usuario usuario = Mockito.mock(Usuario.class);

        Produto produto = Mockito.mock(Produto.class);
        Mockito.when(produto.getDono()).thenReturn(usuario);
        Mockito.when(produto.getFotos()).thenReturn(List.of());
        Mockito.when(produto.getCaracteristicas()).thenReturn(List.of());

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(produto));

        Mockito.when(perguntaRepository.findByProduto_Id(1L)).thenReturn(List.of());
        Mockito.when(opiniaoRepository.findByProduto_Id(1L)).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/produtos/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}