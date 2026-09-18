package controller;

import model.CadastroAcademico;
import thread.ServidorAceitaThread;
import view.ServidorView;

/**
 * Controller do Servidor: cria a thread que aceita conexões e a inicia.
 *
 * @author Andrisa Santos
 */
public class ServidorController {

    private static final int PORTA = 50000;

    private final CadastroAcademico cadastro;
    private final ServidorView view;

    public ServidorController(CadastroAcademico cadastro, ServidorView view) {
        this.cadastro = cadastro;
        this.view = view;
    }

    public void iniciar() {
        ServidorAceitaThread thread = new ServidorAceitaThread(PORTA, cadastro, view);
        thread.setDaemon(true);
        thread.start();
    }
}
