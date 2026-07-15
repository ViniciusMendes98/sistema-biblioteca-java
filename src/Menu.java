import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner leitor;
    private final List<Livro> livros;
    private final List<Usuario> usuarios;
    private int proximoCodigoUsuario;

    public Menu() {
        this.leitor = new Scanner(System.in);

        // Usar a interface List reduz o acoplamento com a implementação ArrayList.
        this.livros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.proximoCodigoUsuario = 1;
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

        Livro novoLivro = new Livro(titulo, autor, anoPublicacao);
        livros.add(novoLivro);

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

    private String lerEmail() {
        while (true) {
            String email = lerTextoObrigatorio("Digite o e-mail: ");

            if (Usuario.isEmailValido(email)) {
                if (!emailJaCadastrado(email)) {
                    return email;
                }

                System.out.println("Já existe um usuário com esse e-mail.");
                continue;
            }

            System.out.println("Digite um e-mail válido.");
        }
    }

    private boolean emailJaCadastrado(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
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
        System.out.println("0 - Sair");
    }
}
