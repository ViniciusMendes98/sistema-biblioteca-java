public class TesteRegrasNegocio {

    private static int testesAprovados = 0;

    public static void main(String[] args) {
        executarTeste("Novo livro começa disponível", TesteRegrasNegocio::deveCriarLivroDisponivel);
        executarTeste("Empréstimo atualiza usuário e livro", TesteRegrasNegocio::deveEmprestarLivro);
        executarTeste("Usuário não pode pegar segundo livro", TesteRegrasNegocio::deveBloquearSegundoEmprestimo);
        executarTeste("Livro indisponível não pode ser emprestado", TesteRegrasNegocio::deveBloquearLivroIndisponivel);
        executarTeste("Devolução libera usuário e livro", TesteRegrasNegocio::deveDevolverLivro);
        executarTeste("Usuário sem empréstimo não pode devolver", TesteRegrasNegocio::deveBloquearDevolucaoSemEmprestimo);
        executarTeste("Livro disponível não pode ser devolvido", TesteRegrasNegocio::deveBloquearDevolucaoDeLivroDisponivel);
        executarTeste("Entidades rejeitam dados inválidos", TesteRegrasNegocio::deveRejeitarDadosInvalidos);

        System.out.println();
        System.out.println(testesAprovados + " testes aprovados com sucesso.");
    }

    private static void deveCriarLivroDisponivel() {
        Livro livro = criarLivro(1, "Livro Um");

        verificar(livro.isDisponivel(), "Um livro novo deveria estar disponível.");
    }

    private static void deveEmprestarLivro() {
        Livro livro = criarLivro(1, "Livro Um");
        Usuario usuario = criarUsuario(1, "Ana", "ana@exemplo.com");

        usuario.emprestarLivro(livro);

        verificar(!livro.isDisponivel(), "O livro deveria ficar indisponível.");
        verificar(usuario.possuiEmprestimoAtivo(), "O usuário deveria ter empréstimo ativo.");
        verificar(usuario.getLivroEmprestado().orElseThrow() == livro,
                "O usuário deveria guardar o mesmo objeto Livro.");
    }

    private static void deveBloquearSegundoEmprestimo() {
        Livro primeiroLivro = criarLivro(1, "Livro Um");
        Livro segundoLivro = criarLivro(2, "Livro Dois");
        Usuario usuario = criarUsuario(1, "Ana", "ana@exemplo.com");
        usuario.emprestarLivro(primeiroLivro);

        esperarExcecao(IllegalStateException.class, () -> usuario.emprestarLivro(segundoLivro));

        verificar(segundoLivro.isDisponivel(), "O segundo livro deveria continuar disponível.");
    }

    private static void deveBloquearLivroIndisponivel() {
        Livro livro = criarLivro(1, "Livro Um");
        Usuario primeiroUsuario = criarUsuario(1, "Ana", "ana@exemplo.com");
        Usuario segundoUsuario = criarUsuario(2, "Bruno", "bruno@exemplo.com");
        primeiroUsuario.emprestarLivro(livro);

        esperarExcecao(IllegalStateException.class, () -> segundoUsuario.emprestarLivro(livro));

        verificar(!segundoUsuario.possuiEmprestimoAtivo(),
                "O segundo usuário não deveria receber o livro.");
    }

    private static void deveDevolverLivro() {
        Livro livro = criarLivro(1, "Livro Um");
        Usuario usuario = criarUsuario(1, "Ana", "ana@exemplo.com");
        usuario.emprestarLivro(livro);

        Livro livroDevolvido = usuario.devolverLivro();

        verificar(livroDevolvido == livro, "A devolução deveria retornar o mesmo livro.");
        verificar(livro.isDisponivel(), "O livro deveria voltar a ficar disponível.");
        verificar(!usuario.possuiEmprestimoAtivo(), "O usuário deveria ficar sem empréstimo.");
    }

    private static void deveBloquearDevolucaoSemEmprestimo() {
        Usuario usuario = criarUsuario(1, "Ana", "ana@exemplo.com");

        esperarExcecao(IllegalStateException.class, usuario::devolverLivro);
    }

    private static void deveBloquearDevolucaoDeLivroDisponivel() {
        Livro livro = criarLivro(1, "Livro Um");

        esperarExcecao(IllegalStateException.class, livro::devolver);
    }

    private static void deveRejeitarDadosInvalidos() {
        esperarExcecao(IllegalArgumentException.class,
                () -> new Livro(1, " ", "Autor", 2000));
        esperarExcecao(IllegalArgumentException.class,
                () -> new Usuario(1, "Ana", "email-invalido"));
    }

    private static Livro criarLivro(int codigo, String titulo) {
        return new Livro(codigo, titulo, "Autor", 2000);
    }

    private static Usuario criarUsuario(int codigo, String nome, String email) {
        return new Usuario(codigo, nome, email);
    }

    private static void executarTeste(String nome, Runnable teste) {
        try {
            teste.run();
            testesAprovados++;
            System.out.println("APROVADO: " + nome);
        } catch (Throwable erro) {
            throw new AssertionError("FALHOU: " + nome, erro);
        }
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void esperarExcecao(Class<? extends RuntimeException> tipoEsperado, Runnable acao) {
        try {
            acao.run();
        } catch (RuntimeException excecao) {
            if (tipoEsperado.isInstance(excecao)) {
                return;
            }

            throw new AssertionError("Tipo de exceção diferente do esperado.", excecao);
        }

        throw new AssertionError("A operação deveria lançar " + tipoEsperado.getSimpleName() + ".");
    }
}
