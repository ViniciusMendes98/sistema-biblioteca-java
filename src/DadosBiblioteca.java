import java.util.List;

public class DadosBiblioteca {

    private final List<Livro> livros;
    private final List<Usuario> usuarios;
    private final int proximoCodigoLivro;
    private final int proximoCodigoUsuario;

    public DadosBiblioteca(List<Livro> livros, List<Usuario> usuarios,
            int proximoCodigoLivro, int proximoCodigoUsuario) {
        this.livros = livros;
        this.usuarios = usuarios;
        this.proximoCodigoLivro = proximoCodigoLivro;
        this.proximoCodigoUsuario = proximoCodigoUsuario;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public int getProximoCodigoLivro() {
        return proximoCodigoLivro;
    }

    public int getProximoCodigoUsuario() {
        return proximoCodigoUsuario;
    }
}
