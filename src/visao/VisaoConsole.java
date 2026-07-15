package visao;

import java.time.Year;
import java.util.List;
import java.util.Scanner;

import modelo.Livro;
import modelo.Usuario;

public class VisaoConsole {

    private final Scanner leitor;

    public VisaoConsole() {
        this.leitor = new Scanner(System.in);
    }

    public void exibirMenu() {
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

    public int lerOpcao() {
        return lerNumeroInteiro("Escolha uma opção: ");
    }

    public int lerNumeroPositivo(String mensagem) {
        while (true) {
            int numero = lerNumeroInteiro(mensagem);
            if (numero > 0) {
                return numero;
            }
            exibirMensagem("Digite um número maior que zero.");
        }
    }

    public String lerTextoObrigatorio(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = leitor.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            exibirMensagem("O campo não pode ficar vazio.");
        }
    }

    public int lerAnoPublicacao() {
        int anoAtual = Year.now().getValue();
        while (true) {
            int anoPublicacao = lerNumeroInteiro("Digite o ano de publicação: ");
            if (anoPublicacao >= 1 && anoPublicacao <= anoAtual) {
                return anoPublicacao;
            }
            exibirMensagem("O ano deve estar entre 1 e " + anoAtual + ".");
        }
    }

    public boolean lerConfirmacao(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String resposta = leitor.nextLine().trim();
            if (resposta.equalsIgnoreCase("S")) {
                return true;
            }
            if (resposta.equalsIgnoreCase("N")) {
                return false;
            }
            exibirMensagem("Digite S para confirmar ou N para cancelar.");
        }
    }

    public void exibirLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            exibirMensagem("Nenhum livro cadastrado.");
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

    public void exibirUsuarios(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            exibirMensagem("Nenhum usuário cadastrado.");
            return;
        }

        System.out.println("=== USUÁRIOS CADASTRADOS ===");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
            System.out.println();
        }
    }

    public void exibirLivrosDisponiveis(List<Livro> livros) {
        System.out.println("=== LIVROS DISPONÍVEIS ===");
        for (Livro livro : livros) {
            System.out.println(livro.getCodigo() + " - " + livro.getTitulo());
        }
        if (livros.isEmpty()) {
            exibirMensagem("Nenhum livro disponível.");
        }
    }

    public void exibirEmprestimos(List<Usuario> usuarios) {
        System.out.println("=== EMPRÉSTIMOS ATIVOS ===");
        for (Usuario usuario : usuarios) {
            Livro livro = usuario.getLivroEmprestado().orElseThrow();
            System.out.println("Usuário: " + usuario.getCodigo() + " - " + usuario.getNome());
            System.out.println("Livro: " + livro.getCodigo() + " - " + livro.getTitulo());
            System.out.println();
        }
        if (usuarios.isEmpty()) {
            exibirMensagem("Nenhum empréstimo ativo.");
        }
    }

    public void exibirResumo(int totalLivros, int livrosDisponiveis,
            int livrosEmprestados, int totalUsuarios) {
        System.out.println("=== RESUMO DA BIBLIOTECA ===");
        System.out.println("Livros cadastrados: " + totalLivros);
        System.out.println("Livros disponíveis: " + livrosDisponiveis);
        System.out.println("Livros emprestados: " + livrosEmprestados);
        System.out.println("Usuários cadastrados: " + totalUsuarios);
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void exibirLinhaEmBranco() {
        System.out.println();
    }

    public void fechar() {
        leitor.close();
    }

    private int lerNumeroInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = leitor.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException excecao) {
                exibirMensagem("Digite um número inteiro válido.");
            }
        }
    }
}
