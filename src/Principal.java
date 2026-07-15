import java.util.Scanner;

// Classe responsável por iniciar e controlar o programa.
public class Principal {

    // Método principal: é o primeiro método executado pelo Java.
    public static void main(String[] args) {
        // O Scanner permite ler o que o usuário digita no terminal.
        Scanner leitor = new Scanner(System.in);

        // A opção escolhida pelo usuário será guardada nesta variável.
        int opcao;

        // O bloco do-while executa o menu pelo menos uma vez.
        do {
            exibirMenu();

            // Lê um número inteiro digitado pelo usuário.
            opcao = leitor.nextInt();

            // O switch escolhe uma ação de acordo com a opção digitada.
            switch (opcao) {
                case 1:
                    System.out.println("O cadastro de livros será criado na próxima etapa.");
                    break;
                case 2:
                    System.out.println("A listagem de livros será criada em uma etapa futura.");
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

        // Fecha o Scanner depois que o programa termina.
        leitor.close();
    }

    // Método separado para mostrar as opções disponíveis no menu.
    private static void exibirMenu() {
        System.out.println("=== SISTEMA DE BIBLIOTECA ===");
        System.out.println("1 - Cadastrar livro");
        System.out.println("2 - Listar livros");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }
}
