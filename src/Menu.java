import java.io.IOException;
import java.nio.file.Path;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {

    private final Scanner leitor;
    private final List<Livro> livros;
    private final List<Usuario> usuarios;
    private final RepositorioDados repositorioDados;
    private int proximoCodigoLivro;
    private int proximoCodigoUsuario;

    public Menu() {
        this.leitor = new Scanner(System.in);
        this.repositorioDados = new RepositorioDados(Path.of("dados", "biblioteca.properties"));

        DadosBiblioteca dados = carregarDados();
        this.livros = dados.getLivros();
        this.usuarios = dados.getUsuarios();
        this.proximoCodigoLivro = dados.getProximoCodigoLivro();
        this.proximoCodigoUsuario = dados.getProximoCodigoUsuario();
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

        Livro novoLivro = new Livro(proximoCodigoLivro, titulo, autor, anoPublicacao);
        livros.add(novoLivro);
        proximoCodigoLivro++;
        salvarDados();

        System.out.println("Livro cadastrado com sucesso!");
    }

    private void listarLivros() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        System.out.println("=== LIVROS CADASTRADOS ===");

        int numeroLivro = 1;
        for (Livro livro : livros) {
            System.out.println("Livro " + numeroLivro);
            System.out.println(livro);
            System.out.println();
            numeroLivro++;
        }
    }

    private void cadastrarUsuario() {
        String nome = lerTextoObrigatorio("Digite o nome: ");
        String email = lerEmail();

        Usuario novoUsuario = new Usuario(proximoCodigoUsuario, nome, email);
        usuarios.add(novoUsuario);
        proximoCodigoUsuario++;
        salvarDados();

        System.out.println("Usuário cadastrado com sucesso!");
    }

    private void listarUsuarios() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        System.out.println("=== USUÁRIOS CADASTRADOS ===");

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
            System.out.println();
        }
    }

    private void exibirBuscaLivro() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = buscarLivroPorCodigo(codigo);

        if (livroEncontrado.isPresent()) {
            System.out.println(livroEncontrado.get());
        } else {
            System.out.println("Livro não encontrado.");
        }
    }

    private void exibirBuscaUsuario() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = buscarUsuarioPorCodigo(codigo);

        if (usuarioEncontrado.isPresent()) {
            System.out.println(usuarioEncontrado.get());
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private Optional<Livro> buscarLivroPorCodigo(int codigo) {
        for (Livro livro : livros) {
            if (livro.getCodigo() == codigo) {
                return Optional.of(livro);
            }
        }

        return Optional.empty();
    }

    private Optional<Usuario> buscarUsuarioPorCodigo(int codigo) {
        for (Usuario usuario : usuarios) {
            if (usuario.getCodigo() == codigo) {
                return Optional.of(usuario);
            }
        }

        return Optional.empty();
    }

    private void realizarEmprestimo() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigoUsuario = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = buscarUsuarioPorCodigo(codigoUsuario);

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
        Optional<Livro> livroEncontrado = buscarLivroPorCodigo(codigoLivro);

        if (livroEncontrado.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        try {
            Livro livro = livroEncontrado.get();
            usuario.emprestarLivro(livro);
            salvarDados();
            System.out.println("Empréstimo realizado com sucesso!");
        } catch (IllegalStateException excecao) {
            System.out.println(excecao.getMessage());
        }
    }

    private void realizarDevolucao() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigoUsuario = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = buscarUsuarioPorCodigo(codigoUsuario);

        if (usuarioEncontrado.isEmpty()) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        try {
            Livro livroDevolvido = usuarioEncontrado.get().devolverLivro();
            salvarDados();
            System.out.println("Livro devolvido com sucesso: " + livroDevolvido.getTitulo());
        } catch (IllegalStateException excecao) {
            System.out.println(excecao.getMessage());
        }
    }

    private void editarLivro() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = buscarLivroPorCodigo(codigo);

        if (livroEncontrado.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        Livro livro = livroEncontrado.get();
        String titulo = lerTextoObrigatorio("Digite o novo título: ");
        String autor = lerTextoObrigatorio("Digite o novo autor: ");
        int anoPublicacao = lerAnoPublicacao();

        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setAnoPublicacao(anoPublicacao);
        salvarDados();

        System.out.println("Livro atualizado com sucesso!");
    }

    private void editarUsuario() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = buscarUsuarioPorCodigo(codigo);

        if (usuarioEncontrado.isEmpty()) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        Usuario usuario = usuarioEncontrado.get();
        String nome = lerTextoObrigatorio("Digite o novo nome: ");
        String email = lerEmail(usuario);

        usuario.setNome(nome);
        usuario.setEmail(email);
        salvarDados();

        System.out.println("Usuário atualizado com sucesso!");
    }

    private void excluirLivro() {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do livro: ");
        Optional<Livro> livroEncontrado = buscarLivroPorCodigo(codigo);

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

        livros.remove(livro);
        salvarDados();
        System.out.println("Livro excluído com sucesso!");
    }

    private void excluirUsuario() {
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
            return;
        }

        int codigo = lerNumeroPositivo("Digite o código do usuário: ");
        Optional<Usuario> usuarioEncontrado = buscarUsuarioPorCodigo(codigo);

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

        usuarios.remove(usuario);
        salvarDados();
        System.out.println("Usuário excluído com sucesso!");
    }

    private void listarLivrosDisponiveis() {
        boolean encontrouLivro = false;

        System.out.println("=== LIVROS DISPONÍVEIS ===");
        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                System.out.println(livro.getCodigo() + " - " + livro.getTitulo());
                encontrouLivro = true;
            }
        }

        if (!encontrouLivro) {
            System.out.println("Nenhum livro disponível.");
        }
    }

    private void listarEmprestimosAtivos() {
        boolean encontrouEmprestimo = false;

        System.out.println("=== EMPRÉSTIMOS ATIVOS ===");
        for (Usuario usuario : usuarios) {
            if (usuario.possuiEmprestimoAtivo()) {
                Livro livro = usuario.getLivroEmprestado().orElseThrow();
                System.out.println("Usuário: " + usuario.getCodigo() + " - " + usuario.getNome());
                System.out.println("Livro: " + livro.getCodigo() + " - " + livro.getTitulo());
                System.out.println();
                encontrouEmprestimo = true;
            }
        }

        if (!encontrouEmprestimo) {
            System.out.println("Nenhum empréstimo ativo.");
        }
    }

    private void exibirResumo() {
        int livrosDisponiveis = 0;
        int emprestimosAtivos = 0;

        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                livrosDisponiveis++;
            }
        }

        for (Usuario usuario : usuarios) {
            if (usuario.possuiEmprestimoAtivo()) {
                emprestimosAtivos++;
            }
        }

        System.out.println("=== RESUMO DA BIBLIOTECA ===");
        System.out.println("Livros cadastrados: " + livros.size());
        System.out.println("Livros disponíveis: " + livrosDisponiveis);
        System.out.println("Livros emprestados: " + emprestimosAtivos);
        System.out.println("Usuários cadastrados: " + usuarios.size());
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
                if (!emailJaCadastrado(email, usuarioIgnorado)) {
                    return email;
                }

                System.out.println("Já existe um usuário com esse e-mail.");
                continue;
            }

            System.out.println("Digite um e-mail válido.");
        }
    }

    private boolean emailJaCadastrado(String email, Usuario usuarioIgnorado) {
        for (Usuario usuario : usuarios) {
            if (usuario != usuarioIgnorado && usuario.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }

        return false;
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
            repositorioDados.salvar(livros, usuarios, proximoCodigoLivro, proximoCodigoUsuario);
        } catch (IOException excecao) {
            System.out.println("Atenção: não foi possível salvar os dados: " + excecao.getMessage());
        }
    }
}
