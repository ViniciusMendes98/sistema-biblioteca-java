import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);

        // Usar a interface List reduz o acoplamento com a implementação ArrayList.
        List<Livro> livros = new ArrayList<>();

        int opcao;

        do {
            exibirMenu();

            opcao = lerNumeroInteiro(leitor, "Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarLivro(leitor, livros);
                    break;
                case 2:
                    listarLivros(livros);
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

    private static void cadastrarLivro(Scanner leitor, List<Livro> livros) {
        String titulo = lerTextoObrigatorio(leitor, "Digite o título: ");
        String autor = lerTextoObrigatorio(leitor, "Digite o autor: ");
        int anoPublicacao = lerAnoPublicacao(leitor);

        Livro novoLivro = new Livro(titulo, autor, anoPublicacao);
        livros.add(novoLivro);

        System.out.println("Livro cadastrado com sucesso!");
    }

    // Centralizar as validações evita duplicação e mantém o fluxo principal legível.
    private static int lerNumeroInteiro(Scanner leitor, String mensagem) {
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

    private static String lerTextoObrigatorio(Scanner leitor, String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = leitor.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("O campo não pode ficar vazio.");
        }
    }

    private static int lerAnoPublicacao(Scanner leitor) {
        int anoAtual = Year.now().getValue();

        while (true) {
            int anoPublicacao = lerNumeroInteiro(leitor, "Digite o ano de publicação: ");

            if (anoPublicacao >= 1 && anoPublicacao <= anoAtual) {
                return anoPublicacao;
            }

            System.out.println("O ano deve estar entre 1 e " + anoAtual + ".");
        }
    }

    private static void listarLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        System.out.println("=== LIVROS CADASTRADOS ===");

        int numeroLivro = 1;
        for (Livro livro : livros) {
            System.out.println("Livro " + numeroLivro);
            livro.exibirInformacoes();
            System.out.println();
            numeroLivro++;
        }
    }

    // Separar o menu mantém o método principal focado no fluxo do programa.
    private static void exibirMenu() {
        System.out.println("=== SISTEMA DE BIBLIOTECA ===");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Listar livros");
        System.out.println("0 - Sair");
    }
}
