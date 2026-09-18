package thread;

import service.Comunicador;
import model.CadastroAcademico;
import model.Pessoa;
import view.ServidorView;

import java.net.Socket;

/**
 * Thread que atende UM cliente do início ao fim: recebe a Pessoa, pede ao
 * Model para cadastrar/localizar (é lá, dentro do método
 * {@code synchronized}, que a concorrência entre várias instâncias desta
 * thread é resolvida) e devolve a resposta.
 *
 * @author Andrisa Santos
 */
public class ServidorAtendeClienteThread extends Thread {

    private final Socket socket;
    private final CadastroAcademico cadastro;
    private final ServidorView view;

    public ServidorAtendeClienteThread(Socket socket, CadastroAcademico cadastro, ServidorView view) {
        super("thread-cliente-" + socket.getPort());
        this.socket = socket;
        this.cadastro = cadastro;
        this.view = view;
    }

    @Override
    public void run() {
        try (Socket s = socket) {
            Pessoa recebida = Comunicador.recebeObjeto(s);
            Pessoa resposta = cadastro.registrarOuLocalizar(recebida.getNome(), recebida.getDataNascimento());
            Comunicador.enviaObjeto(s, resposta);

            view.log("Respondido para " + s.getInetAddress().getHostAddress() + ": " + resposta);
            view.atualizarListaCadastrados(cadastro.getPessoas());
        } catch (Exception e) {
            view.log("Erro ao atender cliente: " + e.getMessage());
        }
    }
}
