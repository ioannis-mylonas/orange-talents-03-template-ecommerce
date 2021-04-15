package bootcamp.mercado.produto.foto;

import bootcamp.mercado.produto.Produto;
import bootcamp.mercado.produto.ProdutoRepository;
import bootcamp.mercado.storage.FileDownloader;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@WebMvcTest(FotoController.class)
@Import(ConfigMockTestUser.class)
class FotoControllerTest {
    // Mock SecurityConfig beans
    @MockBean private UsuarioLogin usuarioLogin;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private TokenParser tokenParser;

    // Mock controller beans
    @MockBean private ProdutoRepository produtoRepository;
    @MockBean private FotoRepository fotoRepository;
    @MockBean private FileDownloader fileDownloader;

    @Autowired private MockMvc mvc;

    @Test
    @WithMockUser
    public void testaProdutoInexistenteNotFound() throws Exception {
        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        MockMultipartFile foto = new MockMultipartFile("fotos", "foto1.png", "image/png", "fotodata".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/fotos/1")
                .file(foto))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithUserDetails
    public void testaProdutoDonoDiferenteForbidden() throws Exception {
        Produto produto = Mockito.mock(Produto.class);

        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(usuario.getUsername()).thenReturn("outro");
        Mockito.when(usuario.getId()).thenReturn(2L);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(produto.getDono()).thenReturn(usuario);

        MockMultipartFile foto = new MockMultipartFile("fotos", "foto1.png", "image/png", "Dados".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/fotos/1")
                .file(foto))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithUserDetails
    public void testaProdutoDonoCorretoSucesso() throws Exception {
        Produto produto = Mockito.mock(Produto.class);

        Usuario usuario = Mockito.mock(Usuario.class);
        Mockito.when(usuario.getUsername()).thenReturn("user");
        Mockito.when(usuario.getId()).thenReturn(1L);

        Mockito.when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        Mockito.when(produto.getDono()).thenReturn(usuario);

        MockMultipartFile foto = new MockMultipartFile("fotos", "foto1.png", "image/png", "Dados".getBytes());

        mvc.perform(MockMvcRequestBuilders.multipart("/fotos/1")
                .file(foto))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}