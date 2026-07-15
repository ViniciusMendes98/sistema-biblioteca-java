package aplicacao;

import java.nio.file.Path;

import controlador.BibliotecaControlador;
import repositorio.RepositorioDados;
import visao.VisaoConsole;

public class Principal {

    public static void main(String[] args) {
        VisaoConsole visao = new VisaoConsole();
        RepositorioDados repositorio = new RepositorioDados(
                Path.of("dados", "biblioteca.properties"));
        BibliotecaControlador controlador = new BibliotecaControlador(visao, repositorio);

        controlador.iniciar();
    }
}
