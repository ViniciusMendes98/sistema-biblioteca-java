import java.util.Optional;

public class Usuario {

    // O código representa a identidade do usuário e não deve mudar após o cadastro.
    private final int codigo;
    private String nome;
    private String email;
    private Livro livroEmprestado;

    public Usuario(int codigo, String nome, String email) {
        if (codigo < 1) {
            throw new IllegalArgumentException("O código deve ser maior que zero.");
        }

        this.codigo = codigo;
        setNome(nome);
        setEmail(email);
        this.livroEmprestado = null;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ficar vazio.");
        }

        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("O e-mail informado é inválido.");
        }

        this.email = email.trim();
    }

    public static boolean isEmailValido(String email) {
        if (email == null) {
            return false;
        }

        String emailNormalizado = email.trim();
        int primeiroArroba = emailNormalizado.indexOf('@');
        int ultimoArroba = emailNormalizado.lastIndexOf('@');
        int ultimoPonto = emailNormalizado.lastIndexOf('.');

        return primeiroArroba > 0
                && primeiroArroba == ultimoArroba
                && ultimoPonto > primeiroArroba + 1
                && ultimoPonto < emailNormalizado.length() - 1
                && !emailNormalizado.contains(" ");
    }

    public boolean possuiEmprestimoAtivo() {
        return livroEmprestado != null;
    }

    public Optional<Livro> getLivroEmprestado() {
        return Optional.ofNullable(livroEmprestado);
    }

    public void emprestarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("O livro não pode ser nulo.");
        }

        if (possuiEmprestimoAtivo()) {
            throw new IllegalStateException("O usuário precisa devolver o livro atual antes de pegar outro.");
        }

        if (!livro.isDisponivel()) {
            throw new IllegalStateException("O livro escolhido não está disponível.");
        }

        livro.emprestar();
        livroEmprestado = livro;
    }

    public Livro devolverLivro() {
        if (!possuiEmprestimoAtivo()) {
            throw new IllegalStateException("O usuário não possui empréstimo ativo.");
        }

        Livro livroDevolvido = livroEmprestado;
        livroDevolvido.devolver();
        livroEmprestado = null;

        return livroDevolvido;
    }

    @Override
    public String toString() {
        String emprestimo = possuiEmprestimoAtivo()
                ? livroEmprestado.getCodigo() + " - " + livroEmprestado.getTitulo()
                : "Nenhum";

        return "Código: " + codigo
                + System.lineSeparator() + "Nome: " + nome
                + System.lineSeparator() + "E-mail: " + email
                + System.lineSeparator() + "Empréstimo ativo: " + emprestimo;
    }
}
