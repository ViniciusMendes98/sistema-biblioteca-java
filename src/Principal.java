import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);

        // Objeto temporário enquanto o cadastro de livros ainda não foi implementado.
        Livro livroExemplo = new Livro("Dom Casmurro", "Machado de Assis", 1899);

        int opcao;

        do {
            exibirMenu();

            opcao = leitor.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("O cadastro de livros será criado na próxima etapa.");
                    break;
                case 2:
                    livroExemplo.exibirInformacoes();
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

    // Separar o menu mantém o método principal focado no fluxo do programa.
    private static void exibirMenu() {
        System.out.println("=== SISTEMA DE BIBLIOTECA ===");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Listar livros");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }
}
