public class Livro {

    // Encapsulamento: os dados só podem ser acessados pelos métodos da classe.
    private String titulo;
    private String autor;
    private int anoPublicacao;
    private boolean disponivel;

    public Livro(String titulo, String autor, int anoPublicacao) {
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacao = anoPublicacao;

        // Regra de negócio: todo livro novo começa disponível para empréstimo.
        this.disponivel = true;
    }

    public String obterTitulo() {
        return titulo;
    }

    public String obterAutor() {
        return autor;
    }

    public int obterAnoPublicacao() {
        return anoPublicacao;
    }

    public boolean estaDisponivel() {
        return disponivel;
    }

    public void exibirInformacoes() {
        System.out.println("Título: " + titulo);
        System.out.println("Autor: " + autor);
        System.out.println("Ano de publicação: " + anoPublicacao);
        System.out.println("Disponível: " + (disponivel ? "Sim" : "Não"));
    }
}
