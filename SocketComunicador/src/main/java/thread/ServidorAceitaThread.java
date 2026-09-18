package thread;

import model.CadastroAcademico;
import view.ServidorView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread que fica em loop aceitando conexões (accept() é bloqueante, por
 * isso não pode rodar na Event Dispatch Thread). Para cada cliente
 * aceito, delega o atendimento a uma NOVA {@link ServidorAtendeClienteThread},
 * permitindo vários clientes serem atendidos ao mesmo tempo.
 *
 * @author Andrisa Santos
 */
public class ServidorAceitaThread extends Thread {

    private final int porta;
    private final CadastroAcademico cadastro;
    private final ServidorView view;

    public ServidorAceitaThread(int porta, CadastroAcademico cadastro, ServidorView view) {
        super("thread-aceita-conexoes");
        this.porta = porta;
        this.cadastro = cadastro;
        this.view = view;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            view.log("Servidor ouvindo na porta " + porta);
            while (true) {
                Socket socketCliente = serverSocket.accept();
                view.log("Cliente conectado: " + socketCliente.getInetAddress().getHostAddress());

                ServidorAtendeClienteThread threadCliente =
                        new ServidorAtendeClienteThread(socketCliente, cadastro, view);
                threadCliente.setDaemon(true);
                threadCliente.start();
            }
        } catch (IOException e) {
            view.log("Servidor encerrado: " + e.getMessage());
        }
    }
}
