package bootcamp.mercado.produto.categoria;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoriaRepositoryTest {
    @Autowired private CategoriaRepository repository;

    @ParameterizedTest
    @ValueSource(strings = {"Categoria 1", "  Outra Categoria   ", "uma categoria", "cat"})
    public void testaEncontraCategoriaNome(String nome) {
        repository.save(new Categoria(nome));

        Optional<Categoria> categoria = repository.findByNomeIgnoreCase(nome);
        assertTrue(categoria.isPresent());
    }

    @Test
    public void testaEncontraCategoriaDeVarias() {
        List<String> nomes = List.of("Categoria 1", "Outra Categoria", "uma categoria", "cat");
        repository.saveAll(nomes.stream().map(Categoria::new).collect(Collectors.toList()));

        for (String nome : nomes) {
            Optional<Categoria> categoria = repository.findByNomeIgnoreCase(nome.trim().toLowerCase());
            assertTrue(categoria.isPresent(), String.format(
                    "Nome %s não foi encontrado pelo repositório!", nome
            ));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Categoria 1", "  Outra Categoria   ", "uma categoria", "cat"})
    public void testaNaoEncontraCategoriaNome(String nome) {
        Optional<Categoria> categoria = repository.findByNomeIgnoreCase(nome);
        assertTrue(categoria.isEmpty());
    }
}