package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import modelo.Biblioteca;
import modelo.DadosBiblioteca;
import modelo.Livro;
import modelo.Usuario;
import repositorio.RepositorioDados;
import visao.VisaoConsole;

public class BibliotecaControlador {

    private final VisaoConsole visao;
    private final RepositorioDados repositorioDados;
    private final Biblioteca biblioteca;

    public BibliotecaControlador(VisaoConsole visao, RepositorioDados repositorioDados) {
        this.visao = visao;
        this.repositorioDados = repositorioDados;
        this.biblioteca = new Biblioteca(carregarDados());
    }

    public void iniciar() {
        int opcao;

        do {
            visao.exibirMenu();
            opcao = visao.lerOpcao();
            executarOpcao(opcao);
            visao.exibirLinhaEmBranco();
        } while (opcao != 0);

        visao.fechar();
    }

    private void executarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                cadastrarLivro();
                break;
            case 2:
                visao.exibirLivros(biblioteca.getLivros());
                break;
            case 3:
                cadastrarUsuario();
                break;
            case 4:
                visao.exibirUsuarios(biblioteca.getUsuarios());
                break;
            case 5:
                buscarLivro();
                break;
            case 6:
                buscarUsuario();
                break;
            case 7:
                realizarEmprestimo();
                break;
            case 8:
                realizarDevolucao();
                break;
            case 9:
                editarLivro();
                break;
            case 10:
                editarUsuario();
                break;
            case 11:
                excluirLivro();
                break;
            case 12:
                excluirUsuario();
                break;
            case 13:
                visao.exibirLivrosDisponiveis(biblioteca.getLivrosDisponiveis());
                break;
            case 14:
                visao.exibirEmprestimos(biblioteca.getUsuariosComEmprestimo());
                break;
            case 15:
                exibirResumo();
                break;
            case 0:
                visao.exibirMensagem("Programa encerrado. Até logo!");
                break;
            default:
                visao.exibirMensagem("Opção inválida. Tente novamente.");
                break;
        }
    }

    private void cadastrarLivro() {
        String titulo = visao.lerTextoObrigatorio("Digite o título: ");
        String autor = visao.lerTextoObrigatorio("Digite o autor: ");
        int anoPublicacao = visao.lerAnoPublicacao();

        biblioteca.cadastrarLivro(titulo, autor, anoPublicacao);
        salvarDados();
        visao.exibirMensagem("Livro cadastrado com sucesso!");
    }

    private void cadastrarUsuario() {
        String nome = visao.lerTextoObrigatorio("Digite o nome: ");
        String email = lerEmail(null);

        biblioteca.cadastrarUsuario(nome, email);
        salvarDados();
        visao.exibirMensagem("Usuário cadastrado com sucesso!");
    }

    private void buscarLivro() {
        if (biblioteca.getLivros().isEmpty()) {
            visao.exibirMensagem("Nenhum livro cadastrado.");
            return;
        }

        int codigo = visao.lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livro = biblioteca.buscarLivroPorCodigo(codigo);
        visao.exibirMensagem(livro.isPresent() ? livro.get().toString() : "Livro não encontrado.");
    }

    private void buscarUsuario() {
        if (biblioteca.getUsuarios().isEmpty()) {
            visao.exibirMensagem("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = visao.lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuario = biblioteca.buscarUsuarioPorCodigo(codigo);
        visao.exibirMensagem(usuario.isPresent() ? usuario.get().toString() : "Usuário não encontrado.");
    }

    private void realizarEmprestimo() {
        if (biblioteca.getUsuarios().isEmpty()) {
            visao.exibirMensagem("Nenhum usuário cadastrado.");
            return;
        }
        if (biblioteca.getLivros().isEmpty()) {
            visao.exibirMensagem("Nenhum livro cadastrado.");
            return;
        }

        int codigoUsuario = visao.lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = biblioteca.buscarUsuarioPorCodigo(codigoUsuario);
        if (usuarioEncontrado.isEmpty()) {
            visao.exibirMensagem("Usuário não encontrado.");
            return;
        }

        Usuario usuario = usuarioEncontrado.get();
        if (usuario.possuiEmprestimoAtivo()) {
            visao.exibirMensagem("O usuário precisa devolver o livro atual antes de pegar outro.");
            return;
        }

        int codigoLivro = visao.lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = biblioteca.buscarLivroPorCodigo(codigoLivro);
        if (livroEncontrado.isEmpty()) {
            visao.exibirMensagem("Livro não encontrado.");
            return;
        }

        try {
            biblioteca.realizarEmprestimo(usuario, livroEncontrado.get());
            salvarDados();
            visao.exibirMensagem("Empréstimo realizado com sucesso!");
        } catch (IllegalStateException excecao) {
            visao.exibirMensagem(excecao.getMessage());
        }
    }

    private void realizarDevolucao() {
        if (biblioteca.getUsuarios().isEmpty()) {
            visao.exibirMensagem("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = visao.lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuario = biblioteca.buscarUsuarioPorCodigo(codigo);
        if (usuario.isEmpty()) {
            visao.exibirMensagem("Usuário não encontrado.");
            return;
        }

        try {
            Livro livro = biblioteca.realizarDevolucao(usuario.get());
            salvarDados();
            visao.exibirMensagem("Livro devolvido com sucesso: " + livro.getTitulo());
        } catch (IllegalStateException excecao) {
            visao.exibirMensagem(excecao.getMessage());
        }
    }

    private void editarLivro() {
        if (biblioteca.getLivros().isEmpty()) {
            visao.exibirMensagem("Nenhum livro cadastrado.");
            return;
        }

        int codigo = visao.lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livro = biblioteca.buscarLivroPorCodigo(codigo);
        if (livro.isEmpty()) {
            visao.exibirMensagem("Livro não encontrado.");
            return;
        }

        String titulo = visao.lerTextoObrigatorio("Digite o novo título: ");
        String autor = visao.lerTextoObrigatorio("Digite o novo autor: ");
        int ano = visao.lerAnoPublicacao();
        biblioteca.editarLivro(livro.get(), titulo, autor, ano);
        salvarDados();
        visao.exibirMensagem("Livro atualizado com sucesso!");
    }

    private void editarUsuario() {
        if (biblioteca.getUsuarios().isEmpty()) {
            visao.exibirMensagem("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = visao.lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuario = biblioteca.buscarUsuarioPorCodigo(codigo);
        if (usuario.isEmpty()) {
            visao.exibirMensagem("Usuário não encontrado.");
            return;
        }

        String nome = visao.lerTextoObrigatorio("Digite o novo nome: ");
        String email = lerEmail(usuario.get());
        biblioteca.editarUsuario(usuario.get(), nome, email);
        salvarDados();
        visao.exibirMensagem("Usuário atualizado com sucesso!");
    }

    private void excluirLivro() {
        if (biblioteca.getLivros().isEmpty()) {
            visao.exibirMensagem("Nenhum livro cadastrado.");
            return;
        }

        int codigo = visao.lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livro = biblioteca.buscarLivroPorCodigo(codigo);
        if (livro.isEmpty()) {
            visao.exibirMensagem("Livro não encontrado.");
            return;
        }
        if (!livro.get().podeSerExcluido()) {
            visao.exibirMensagem("Um livro emprestado não pode ser excluído.");
            return;
        }
        if (!visao.lerConfirmacao("Confirma a exclusão do livro? (S/N): ")) {
            visao.exibirMensagem("Exclusão cancelada.");
            return;
        }

        biblioteca.excluirLivro(livro.get());
        salvarDados();
        visao.exibirMensagem("Livro excluído com sucesso!");
    }

    private void excluirUsuario() {
        if (biblioteca.getUsuarios().isEmpty()) {
            visao.exibirMensagem("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = visao.lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuario = biblioteca.buscarUsuarioPorCodigo(codigo);
        if (usuario.isEmpty()) {
            visao.exibirMensagem("Usuário não encontrado.");
            return;
        }
        if (!usuario.get().podeSerExcluido()) {
            visao.exibirMensagem("Um usuário com empréstimo ativo não pode ser excluído.");
            return;
        }
        if (!visao.lerConfirmacao("Confirma a exclusão do usuário? (S/N): ")) {
            visao.exibirMensagem("Exclusão cancelada.");
            return;
        }

        biblioteca.excluirUsuario(usuario.get());
        salvarDados();
        visao.exibirMensagem("Usuário excluído com sucesso!");
    }

    private void exibirResumo() {
        visao.exibirResumo(
                biblioteca.getLivros().size(),
                biblioteca.getLivrosDisponiveis().size(),
                biblioteca.getUsuariosComEmprestimo().size(),
                biblioteca.getUsuarios().size());
    }

    private String lerEmail(Usuario usuarioIgnorado) {
        while (true) {
            String email = visao.lerTextoObrigatorio("Digite o e-mail: ");
            if (!Usuario.isEmailValido(email)) {
                visao.exibirMensagem("Digite um e-mail válido.");
            } else if (biblioteca.emailJaCadastrado(email, usuarioIgnorado)) {
                visao.exibirMensagem("Já existe um usuário com esse e-mail.");
            } else {
                return email;
            }
        }
    }

    private DadosBiblioteca carregarDados() {
        try {
            return repositorioDados.carregar();
        } catch (IOException excecao) {
            visao.exibirMensagem("Não foi possível carregar os dados salvos: " + excecao.getMessage());
            visao.exibirMensagem("O programa será iniciado sem dados.");
            return new DadosBiblioteca(new ArrayList<>(), new ArrayList<>(), 1, 1);
        }
    }

    private void salvarDados() {
        try {
            repositorioDados.salvar(biblioteca.criarDados());
        } catch (IOException excecao) {
            visao.exibirMensagem("Atenção: não foi possível salvar os dados: " + excecao.getMessage());
        }
    }
}
