import java.time.Year;

public class Livro {

    // Encapsulamento: os dados só podem ser acessados pelos métodos da classe.
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private boolean disponivel;

    public Livro(String titulo, String autor, int anoPublicacao) {
        setTitulo(titulo);
        setAutor(autor);
        setAnoPublicacao(anoPublicacao);

        // Regra de negócio: todo livro novo começa disponível para empréstimo.
        this.disponivel = true;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ficar vazio.");
        }

        this.titulo = titulo.trim();
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("O autor não pode ficar vazio.");
        }

        this.autor = autor.trim();
    }

    public int getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(int anoPublicacao) {
        int anoAtual = Year.now().getValue();

        if (anoPublicacao < 1 || anoPublicacao > anoAtual) {
            throw new IllegalArgumentException("O ano de publicação é inválido.");
        }

        this.anoPublicacao = anoPublicacao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    @Override
    public String toString() {
        return "Título: " + titulo
                + System.lineSeparator() + "Autor: " + autor
                + System.lineSeparator() + "Ano de publicação: " + anoPublicacao
                + System.lineSeparator() + "Disponível: " + (disponivel ? "Sim" : "Não");
    }
}
