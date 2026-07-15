import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
        executarTeste("Dados permanecem após salvar e carregar", TesteRegrasNegocio::deveSalvarECarregarDados);
        executarTeste("Edição mantém os códigos das entidades", TesteRegrasNegocio::deveEditarEntidades);
        executarTeste("Empréstimo bloqueia exclusão", TesteRegrasNegocio::deveControlarExclusao);
        executarTeste("Biblioteca gerencia os cadastros", TesteRegrasNegocio::deveGerenciarBiblioteca);

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

    private static void deveSalvarECarregarDados() throws Exception {
        Path pastaTemporaria = Files.createTempDirectory("biblioteca-teste-");
        Path arquivo = pastaTemporaria.resolve("biblioteca.properties");

        try {
            Livro primeiroLivro = criarLivro(1, "Livro Um");
            Livro segundoLivro = criarLivro(2, "Livro Dois");
            Usuario usuario = criarUsuario(1, "Ana", "ana@exemplo.com");
            usuario.emprestarLivro(segundoLivro);

            List<Livro> livros = new ArrayList<>();
            livros.add(primeiroLivro);
            livros.add(segundoLivro);

            List<Usuario> usuarios = new ArrayList<>();
            usuarios.add(usuario);

            RepositorioDados repositorio = new RepositorioDados(arquivo);
            repositorio.salvar(livros, usuarios, 3, 2);
            DadosBiblioteca dadosCarregados = repositorio.carregar();

            verificar(dadosCarregados.getLivros().size() == 2,
                    "Dois livros deveriam ser carregados.");
            verificar(dadosCarregados.getUsuarios().size() == 1,
                    "Um usuário deveria ser carregado.");
            verificar(dadosCarregados.getProximoCodigoLivro() == 3,
                    "O próximo código de livro deveria ser preservado.");
            verificar(dadosCarregados.getProximoCodigoUsuario() == 2,
                    "O próximo código de usuário deveria ser preservado.");

            Livro livroCarregado = dadosCarregados.getLivros().get(1);
            Usuario usuarioCarregado = dadosCarregados.getUsuarios().get(0);
            verificar(!livroCarregado.isDisponivel(),
                    "O livro emprestado deveria continuar indisponível.");
            verificar(usuarioCarregado.getLivroEmprestado().orElseThrow() == livroCarregado,
                    "O empréstimo deveria apontar para o livro carregado.");
        } finally {
            Files.deleteIfExists(arquivo);
            Files.deleteIfExists(pastaTemporaria);
        }
    }

    private static void deveEditarEntidades() {
        Livro livro = criarLivro(1, "Livro Um");
        Usuario usuario = criarUsuario(1, "Ana", "ana@exemplo.com");

        livro.setTitulo("Novo Título");
        livro.setAutor("Novo Autor");
        livro.setAnoPublicacao(2020);
        usuario.setNome("Ana Souza");
        usuario.setEmail("ana.souza@exemplo.com");

        verificar(livro.getCodigo() == 1, "O código do livro não deveria mudar.");
        verificar(livro.getTitulo().equals("Novo Título"), "O título deveria ser atualizado.");
        verificar(livro.getAutor().equals("Novo Autor"), "O autor deveria ser atualizado.");
        verificar(livro.getAnoPublicacao() == 2020, "O ano deveria ser atualizado.");
        verificar(usuario.getCodigo() == 1, "O código do usuário não deveria mudar.");
        verificar(usuario.getNome().equals("Ana Souza"), "O nome deveria ser atualizado.");
        verificar(usuario.getEmail().equals("ana.souza@exemplo.com"),
                "O e-mail deveria ser atualizado.");
    }

    private static void deveControlarExclusao() {
        Livro livro = criarLivro(1, "Livro Um");
        Usuario usuario = criarUsuario(1, "Ana", "ana@exemplo.com");

        verificar(livro.podeSerExcluido(), "Um livro disponível poderia ser excluído.");
        verificar(usuario.podeSerExcluido(), "Um usuário sem empréstimo poderia ser excluído.");

        usuario.emprestarLivro(livro);

        verificar(!livro.podeSerExcluido(), "Um livro emprestado não poderia ser excluído.");
        verificar(!usuario.podeSerExcluido(),
                "Um usuário com empréstimo ativo não poderia ser excluído.");

        usuario.devolverLivro();

        verificar(livro.podeSerExcluido(), "O livro devolvido poderia ser excluído.");
        verificar(usuario.podeSerExcluido(), "O usuário sem empréstimo poderia ser excluído.");
    }

    private static void deveGerenciarBiblioteca() {
        DadosBiblioteca dados = new DadosBiblioteca(new ArrayList<>(), new ArrayList<>(), 1, 1);
        Biblioteca biblioteca = new Biblioteca(dados);

        Livro livro = biblioteca.cadastrarLivro("Livro Um", "Autor", 2000);
        Usuario usuario = biblioteca.cadastrarUsuario("Ana", "ana@exemplo.com");

        verificar(livro.getCodigo() == 1, "O primeiro livro deveria receber o código 1.");
        verificar(usuario.getCodigo() == 1, "O primeiro usuário deveria receber o código 1.");
        verificar(biblioteca.buscarLivroPorCodigo(1).orElseThrow() == livro,
                "A busca deveria retornar o livro cadastrado.");
        verificar(biblioteca.buscarUsuarioPorCodigo(1).orElseThrow() == usuario,
                "A busca deveria retornar o usuário cadastrado.");
        verificar(biblioteca.emailJaCadastrado("ANA@EXEMPLO.COM", null),
                "A verificação de e-mail não deveria diferenciar maiúsculas de minúsculas.");

        usuario.emprestarLivro(livro);
        esperarExcecao(IllegalStateException.class, () -> biblioteca.excluirLivro(livro));
        esperarExcecao(IllegalStateException.class, () -> biblioteca.excluirUsuario(usuario));

        usuario.devolverLivro();
        biblioteca.excluirLivro(livro);
        biblioteca.excluirUsuario(usuario);

        verificar(biblioteca.getLivros().isEmpty(), "O livro deveria ser removido.");
        verificar(biblioteca.getUsuarios().isEmpty(), "O usuário deveria ser removido.");
        verificar(biblioteca.cadastrarLivro("Livro Dois", "Autor", 2001).getCodigo() == 2,
                "O código excluído não deveria ser reutilizado.");
    }

    private static Livro criarLivro(int codigo, String titulo) {
        return new Livro(codigo, titulo, "Autor", 2000);
    }

    private static Usuario criarUsuario(int codigo, String nome, String email) {
        return new Usuario(codigo, nome, email);
    }

    private static void executarTeste(String nome, AcaoTeste teste) {
        try {
            teste.executar();
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

    @FunctionalInterface
    private interface AcaoTeste {
        void executar() throws Exception;
    }
}
