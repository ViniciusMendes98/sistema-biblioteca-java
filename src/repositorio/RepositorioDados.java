package repositorio;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import modelo.DadosBiblioteca;
import modelo.Livro;
import modelo.Usuario;

public class RepositorioDados {

    private final Path caminhoArquivo;

    public RepositorioDados(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public void salvar(DadosBiblioteca dados) throws IOException {
        Properties propriedades = new Properties();
        propriedades.setProperty("proximoCodigoLivro", String.valueOf(dados.getProximoCodigoLivro()));
        propriedades.setProperty("proximoCodigoUsuario", String.valueOf(dados.getProximoCodigoUsuario()));
        propriedades.setProperty("livros.quantidade", String.valueOf(dados.getLivros().size()));
        propriedades.setProperty("usuarios.quantidade", String.valueOf(dados.getUsuarios().size()));

        salvarLivros(propriedades, dados.getLivros());
        salvarUsuarios(propriedades, dados.getUsuarios());

        Path pasta = caminhoArquivo.getParent();
        if (pasta != null) {
            Files.createDirectories(pasta);
        }

        try (Writer escritor = Files.newBufferedWriter(caminhoArquivo, StandardCharsets.UTF_8)) {
            propriedades.store(escritor, "Dados do sistema de biblioteca");
        }
    }

    private void salvarLivros(Properties propriedades, List<Livro> livros) {
        for (int indice = 0; indice < livros.size(); indice++) {
            Livro livro = livros.get(indice);
            String prefixo = "livro." + indice + ".";

            propriedades.setProperty(prefixo + "codigo", String.valueOf(livro.getCodigo()));
            propriedades.setProperty(prefixo + "titulo", livro.getTitulo());
            propriedades.setProperty(prefixo + "autor", livro.getAutor());
            propriedades.setProperty(prefixo + "anoPublicacao", String.valueOf(livro.getAnoPublicacao()));
        }
    }

    private void salvarUsuarios(Properties propriedades, List<Usuario> usuarios) {
        for (int indice = 0; indice < usuarios.size(); indice++) {
            Usuario usuario = usuarios.get(indice);
            String prefixo = "usuario." + indice + ".";
            int codigoLivro = usuario.getLivroEmprestado().map(Livro::getCodigo).orElse(0);

            propriedades.setProperty(prefixo + "codigo", String.valueOf(usuario.getCodigo()));
            propriedades.setProperty(prefixo + "nome", usuario.getNome());
            propriedades.setProperty(prefixo + "email", usuario.getEmail());
            propriedades.setProperty(prefixo + "livroEmprestado", String.valueOf(codigoLivro));
        }
    }

    public DadosBiblioteca carregar() throws IOException {
        if (!Files.exists(caminhoArquivo)) {
            return new DadosBiblioteca(new ArrayList<>(), new ArrayList<>(), 1, 1);
        }

        Properties propriedades = new Properties();
        try (Reader leitor = Files.newBufferedReader(caminhoArquivo, StandardCharsets.UTF_8)) {
            propriedades.load(leitor);
        }

        try {
            return criarDados(propriedades);
        } catch (IllegalArgumentException | IllegalStateException excecao) {
            throw new IOException("O arquivo de dados está inválido ou corrompido.", excecao);
        }
    }

    private DadosBiblioteca criarDados(Properties propriedades) {
        List<Livro> livros = new ArrayList<>();
        List<Usuario> usuarios = new ArrayList<>();
        Map<Integer, Livro> livrosPorCodigo = new HashMap<>();
        List<Integer> codigosEmprestados = new ArrayList<>();

        int quantidadeLivros = lerInteiro(propriedades, "livros.quantidade");
        int quantidadeUsuarios = lerInteiro(propriedades, "usuarios.quantidade");

        for (int indice = 0; indice < quantidadeLivros; indice++) {
            String prefixo = "livro." + indice + ".";
            Livro livro = new Livro(
                    lerInteiro(propriedades, prefixo + "codigo"),
                    lerTexto(propriedades, prefixo + "titulo"),
                    lerTexto(propriedades, prefixo + "autor"),
                    lerInteiro(propriedades, prefixo + "anoPublicacao"));
            livros.add(livro);
            livrosPorCodigo.put(livro.getCodigo(), livro);
        }

        for (int indice = 0; indice < quantidadeUsuarios; indice++) {
            String prefixo = "usuario." + indice + ".";
            Usuario usuario = new Usuario(
                    lerInteiro(propriedades, prefixo + "codigo"),
                    lerTexto(propriedades, prefixo + "nome"),
                    lerTexto(propriedades, prefixo + "email"));
            usuarios.add(usuario);
            codigosEmprestados.add(lerInteiro(propriedades, prefixo + "livroEmprestado"));
        }

        for (int indice = 0; indice < usuarios.size(); indice++) {
            int codigoLivro = codigosEmprestados.get(indice);
            if (codigoLivro != 0) {
                Livro livro = livrosPorCodigo.get(codigoLivro);
                if (livro == null) {
                    throw new IllegalArgumentException("Empréstimo aponta para um livro inexistente.");
                }
                usuarios.get(indice).emprestarLivro(livro);
            }
        }

        return new DadosBiblioteca(
                livros,
                usuarios,
                lerInteiro(propriedades, "proximoCodigoLivro"),
                lerInteiro(propriedades, "proximoCodigoUsuario"));
    }

    private int lerInteiro(Properties propriedades, String chave) {
        return Integer.parseInt(lerTexto(propriedades, chave));
    }

    private String lerTexto(Properties propriedades, String chave) {
        String valor = propriedades.getProperty(chave);
        if (valor == null) {
            throw new IllegalArgumentException("Propriedade ausente: " + chave);
        }
        return valor;
    }
}
