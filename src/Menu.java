import java.io.IOException;
import java.nio.file.Path;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private final Scanner leitor;
    private final Biblioteca biblioteca;
    private final RepositorioDados repositorioDados;

    public Menu() {
        this.leitor = new Scanner(System.in);
        this.repositorioDados = new RepositorioDados(Path.of("dados", "biblioteca.properties"));

        DadosBiblioteca dados = carregarDados();
        this.biblioteca = new Biblioteca(dados);
    }

    public void iniciar() {
        int opcao;

        do {
            exibirMenu();
            opcao = lerNumeroInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarLivro();
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    cadastrarUsuario();
                    break;
                case 4:
                    listarUsuarios();
                    break;
                case 5:
                    exibirBuscaLivro();
                    break;
                case 6:
                    exibirBuscaUsuario();
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
                    listarLivrosDisponiveis();
                    break;
                case 14:
                    listarEmprestimosAtivos();
                    break;
                case 15:
                    exibirResumo();
                    break;
                case 0:
                    System.out.println("Programa encerrado. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }

            System.out.println();
        } while (opcao != 0);

        leitor.close();
    }

    private void cadastrarLivro() {
        String titulo = lerTextoObrigatorio("Digite o título: ");
        String autor = lerTextoObrigatorio("Digite o autor: ");
        int anoPublicacao = lerAnoPublicacao();

        biblioteca.cadastrarLivro(titulo, autor, anoPublicacao);
        salvarDados();

        System.out.println("Livro cadastrado com sucesso!");
    }

    private void listarLivros() {
        if (biblioteca.getLivros().isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        System.out.println("=== LIVROS CADASTRADOS ===");

        int numeroLivro = 1;
        for (Livro livro : biblioteca.getLivros()) {
            System.out.println("Livro " + numeroLivro);
            System.out.println(livro);
            System.out.println();
            numeroLivro++;
        }
    }

    private void cadastrarUsuario() {
        String nome = lerTextoObrigatorio("Digite o nome: ");
        String email = lerEmail();

        biblioteca.cadastrarUsuario(nome, email);
        salvarDados();

        System.out.println("Usuário cadastrado com sucesso!");
    }

    private void listarUsuarios() {
        if (biblioteca.getUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        System.out.println("=== USUÁRIOS CADASTRADOS ===");

        for (Usuario usuario : biblioteca.getUsuarios()) {
            System.out.println(usuario);
            System.out.println();
        }
    }

    private void exibirBuscaLivro() {
        if (biblioteca.getLivros().isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = biblioteca.buscarLivroPorCodigo(codigo);

        if (livroEncontrado.isPresent()) {
            System.out.println(livroEncontrado.get());
        } else {
            System.out.println("Livro não encontrado.");
        }
    }

    private void exibirBuscaUsuario() {
        if (biblioteca.getUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = biblioteca.buscarUsuarioPorCodigo(codigo);

        if (usuarioEncontrado.isPresent()) {
            System.out.println(usuarioEncontrado.get());
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private void realizarEmprestimo() {
        if (biblioteca.getUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        if (biblioteca.getLivros().isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigoUsuario = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = biblioteca.buscarUsuarioPorCodigo(codigoUsuario);

        if (usuarioEncontrado.isEmpty()) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        Usuario usuario = usuarioEncontrado.get();
        if (usuario.possuiEmprestimoAtivo()) {
            System.out.println("O usuário precisa devolver o livro atual antes de pegar outro.");
            return;
        }

        int codigoLivro = lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = biblioteca.buscarLivroPorCodigo(codigoLivro);

        if (livroEncontrado.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        try {
            Livro livro = livroEncontrado.get();
            biblioteca.realizarEmprestimo(usuario, livro);
            salvarDados();
            System.out.println("Empréstimo realizado com sucesso!");
        } catch (IllegalStateException excecao) {
            System.out.println(excecao.getMessage());
        }
    }

    private void realizarDevolucao() {
        if (biblioteca.getUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigoUsuario = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = biblioteca.buscarUsuarioPorCodigo(codigoUsuario);

        if (usuarioEncontrado.isEmpty()) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        try {
            Livro livroDevolvido = biblioteca.realizarDevolucao(usuarioEncontrado.get());
            salvarDados();
            System.out.println("Livro devolvido com sucesso: " + livroDevolvido.getTitulo());
        } catch (IllegalStateException excecao) {
            System.out.println(excecao.getMessage());
        }
    }

    private void editarLivro() {
        if (biblioteca.getLivros().isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = biblioteca.buscarLivroPorCodigo(codigo);

        if (livroEncontrado.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        Livro livro = livroEncontrado.get();
        String titulo = lerTextoObrigatorio("Digite o novo título: ");
        String autor = lerTextoObrigatorio("Digite o novo autor: ");
        int anoPublicacao = lerAnoPublicacao();

        biblioteca.editarLivro(livro, titulo, autor, anoPublicacao);
        salvarDados();

        System.out.println("Livro atualizado com sucesso!");
    }

    private void editarUsuario() {
        if (biblioteca.getUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = biblioteca.buscarUsuarioPorCodigo(codigo);

        if (usuarioEncontrado.isEmpty()) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        Usuario usuario = usuarioEncontrado.get();
        String nome = lerTextoObrigatorio("Digite o novo nome: ");
        String email = lerEmail(usuario);

        biblioteca.editarUsuario(usuario, nome, email);
        salvarDados();

        System.out.println("Usuário atualizado com sucesso!");
    }

    private void excluirLivro() {
        if (biblioteca.getLivros().isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = biblioteca.buscarLivroPorCodigo(codigo);

        if (livroEncontrado.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        Livro livro = livroEncontrado.get();
        if (!livro.podeSerExcluido()) {
            System.out.println("Um livro emprestado não pode ser excluído.");
            return;
        }

        if (!lerConfirmacao("Confirma a exclusão do livro? (S/N): ")) {
            System.out.println("Exclusão cancelada.");
            return;
        }

        biblioteca.excluirLivro(livro);
        salvarDados();
        System.out.println("Livro excluído com sucesso!");
    }

    private void excluirUsuario() {
        if (biblioteca.getUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = biblioteca.buscarUsuarioPorCodigo(codigo);

        if (usuarioEncontrado.isEmpty()) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        Usuario usuario = usuarioEncontrado.get();
        if (!usuario.podeSerExcluido()) {
            System.out.println("Um usuário com empréstimo ativo não pode ser excluído.");
            return;
        }

        if (!lerConfirmacao("Confirma a exclusão do usuário? (S/N): ")) {
            System.out.println("Exclusão cancelada.");
            return;
        }

        biblioteca.excluirUsuario(usuario);
        salvarDados();
        System.out.println("Usuário excluído com sucesso!");
    }

    private void listarLivrosDisponiveis() {
        List<Livro> livrosDisponiveis = biblioteca.getLivrosDisponiveis();

        System.out.println("=== LIVROS DISPONÍVEIS ===");
        for (Livro livro : livrosDisponiveis) {
            System.out.println(livro.getCodigo() + " - " + livro.getTitulo());
        }

        if (livrosDisponiveis.isEmpty()) {
            System.out.println("Nenhum livro disponível.");
        }
    }

    private void listarEmprestimosAtivos() {
        List<Usuario> usuariosComEmprestimo = biblioteca.getUsuariosComEmprestimo();

        System.out.println("=== EMPRÉSTIMOS ATIVOS ===");
        for (Usuario usuario : usuariosComEmprestimo) {
            Livro livro = usuario.getLivroEmprestado().orElseThrow();
            System.out.println("Usuário: " + usuario.getCodigo() + " - " + usuario.getNome());
            System.out.println("Livro: " + livro.getCodigo() + " - " + livro.getTitulo());
            System.out.println();
        }

        if (usuariosComEmprestimo.isEmpty()) {
            System.out.println("Nenhum empréstimo ativo.");
        }
    }

    private void exibirResumo() {
        System.out.println("=== RESUMO DA BIBLIOTECA ===");
        System.out.println("Livros cadastrados: " + biblioteca.getLivros().size());
        System.out.println("Livros disponíveis: " + biblioteca.getLivrosDisponiveis().size());
        System.out.println("Livros emprestados: " + biblioteca.getUsuariosComEmprestimo().size());
        System.out.println("Usuários cadastrados: " + biblioteca.getUsuarios().size());
    }

    // Centralizar as validações evita duplicação e mantém o fluxo principal legível.
    private int lerNumeroInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = leitor.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException excecao) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private int lerNumeroPositivo(String mensagem) {
        while (true) {
            int numero = lerNumeroInteiro(mensagem);

            if (numero > 0) {
                return numero;
            }

            System.out.println("Digite um número maior que zero.");
        }
    }

    private String lerTextoObrigatorio(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = leitor.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("O campo não pode ficar vazio.");
        }
    }

    private boolean lerConfirmacao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = leitor.nextLine().trim();

            if (resposta.equalsIgnoreCase("S")) {
                return true;
            }

            if (resposta.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Digite S para confirmar ou N para cancelar.");
        }
    }

    private String lerEmail() {
        return lerEmail(null);
    }

    private String lerEmail(Usuario usuarioIgnorado) {
        while (true) {
            String email = lerTextoObrigatorio("Digite o e-mail: ");

            if (Usuario.isEmailValido(email)) {
                if (!biblioteca.emailJaCadastrado(email, usuarioIgnorado)) {
                    return email;
                }

                System.out.println("Já existe um usuário com esse e-mail.");
                continue;
            }

            System.out.println("Digite um e-mail válido.");
        }
    }

    private int lerAnoPublicacao() {
        int anoAtual = Year.now().getValue();

        while (true) {
            int anoPublicacao = lerNumeroInteiro("Digite o ano de publicação: ");

            if (anoPublicacao >= 1 && anoPublicacao <= anoAtual) {
                return anoPublicacao;
            }

            System.out.println("O ano deve estar entre 1 e " + anoAtual + ".");
        }
    }

    private void exibirMenu() {
        System.out.println("=== SISTEMA DE BIBLIOTECA ===");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Listar livros");
        System.out.println("3 - Cadastrar usuário");
        System.out.println("4 - Listar usuários");
        System.out.println("5 - Buscar livro por código");
        System.out.println("6 - Buscar usuário por código");
        System.out.println("7 - Emprestar livro");
        System.out.println("8 - Devolver livro");
        System.out.println("9 - Editar livro");
        System.out.println("10 - Editar usuário");
        System.out.println("11 - Excluir livro");
        System.out.println("12 - Excluir usuário");
        System.out.println("13 - Listar livros disponíveis");
        System.out.println("14 - Listar empréstimos ativos");
        System.out.println("15 - Exibir resumo");
        System.out.println("0 - Sair");
    }

    private DadosBiblioteca carregarDados() {
        try {
            return repositorioDados.carregar();
        } catch (IOException excecao) {
            System.out.println("Não foi possível carregar os dados salvos: " + excecao.getMessage());
            System.out.println("O programa será iniciado sem dados.");
            return new DadosBiblioteca(new ArrayList<>(), new ArrayList<>(), 1, 1);
        }
    }

    private void salvarDados() {
        try {
            DadosBiblioteca dados = biblioteca.criarDados();
            repositorioDados.salvar(
                    dados.getLivros(),
                    dados.getUsuarios(),
                    dados.getProximoCodigoLivro(),
                    dados.getProximoCodigoUsuario());
        } catch (IOException excecao) {
            System.out.println("Atenção: não foi possível salvar os dados: " + excecao.getMessage());
        }
    }
}
