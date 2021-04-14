package bootcamp.mercado.usuario;

public class UsuarioRequestBuilder {
    private String nome, senha;

    public UsuarioRequestBuilder setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public UsuarioRequestBuilder setSenha(String senha) {
        this.senha = senha;
        return this;
    }

    public UsuarioRequest build() {
        return new UsuarioRequest(nome, senha);
    }
}
