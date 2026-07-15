import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Biblioteca {

    private final List<Livro> livros;
    private final List<Usuario> usuarios;
    private int proximoCodigoLivro;
    private int proximoCodigoUsuario;

    public Biblioteca(DadosBiblioteca dados) {
        this.livros = dados.getLivros();
        this.usuarios = dados.getUsuarios();
        this.proximoCodigoLivro = dados.getProximoCodigoLivro();
        this.proximoCodigoUsuario = dados.getProximoCodigoUsuario();
    }

    public Livro cadastrarLivro(String titulo, String autor, int anoPublicacao) {
        Livro livro = new Livro(proximoCodigoLivro, titulo, autor, anoPublicacao);
        livros.add(livro);
        proximoCodigoLivro++;
        return livro;
    }

    public Usuario cadastrarUsuario(String nome, String email) {
        Usuario usuario = new Usuario(proximoCodigoUsuario, nome, email);
        usuarios.add(usuario);
        proximoCodigoUsuario++;
        return usuario;
    }

    public Optional<Livro> buscarLivroPorCodigo(int codigo) {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return Optional.of(livro);
            }
        }

        return Optional.empty();
    }

    public Optional<Usuario> buscarUsuarioPorCodigo(int codigo) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCodigo() == codigo) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }

    public boolean emailJaCadastrado(String email, Usuario usuarioIgnorado) {
        for (Usuario usuario : usuarios) {
            if (usuario != usuarioIgnorado && usuario.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
    }

    public void excluirLivro(Livro livro) {
        if (!livro.podeSerExcluido()) {
            throw new IllegalStateException("Um livro emprestado não pode ser excluído.");
        }

        livros.remove(livro);
    }

    public void excluirUsuario(Usuario usuario) {
        if (!usuario.podeSerExcluido()) {
            throw new IllegalStateException("Um usuário com empréstimo ativo não pode ser excluído.");
        }

        usuarios.remove(usuario);
    }

    public List<Livro> getLivros() {
        return Collections.unmodifiableList(livros);
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Livro> getLivrosDisponiveis() {
        List<Livro> livrosDisponiveis = new ArrayList<>();

        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                livrosDisponiveis.add(livro);
            }
        }

        return livrosDisponiveis;
    }

    public List<Usuario> getUsuariosComEmprestimo() {
        List<Usuario> usuariosComEmprestimo = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (usuario.possuiEmprestimoAtivo()) {
                usuariosComEmprestimo.add(usuario);
            }
        }

        return usuariosComEmprestimo;
    }

    public DadosBiblioteca criarDados() {
        return new DadosBiblioteca(
                new ArrayList<>(livros),
                new ArrayList<>(usuarios),
                proximoCodigoLivro,
                proximoCodigoUsuario);
    }
}
