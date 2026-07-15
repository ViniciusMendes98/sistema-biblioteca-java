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

            opcao = Integer.parseInt(leitor.nextLine());

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
        System.out.print("Digite o título: ");
        String titulo = leitor.nextLine().trim();

        System.out.print("Digite o autor: ");
        String autor = leitor.nextLine().trim();

        System.out.print("Digite o ano de publicação: ");
        int anoPublicacao = Integer.parseInt(leitor.nextLine());

        Livro novoLivro = new Livro(titulo, autor, anoPublicacao);
        livros.add(novoLivro);

        System.out.println("Livro cadastrado com sucesso!");
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
        System.out.print("Escolha uma opção: ");
    }
}
