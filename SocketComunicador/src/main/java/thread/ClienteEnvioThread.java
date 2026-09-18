package thread;

import service.Comunicador;
import model.Pessoa;
import view.ClienteView;

import java.net.Socket;

/**
 * Thread responsável por UM envio ao servidor. Existe para que a operação
 * de rede (bloqueante) não trave a Event Dispatch Thread da interface do
 * Cliente, aqui a thread não mexe em nenhum estado compartilhado
 * (cada envio abre seu próprio socket), então
 * não há necessidade de sincronização deste lado.
 *
 * @author Andrisa Santos
 */
public class ClienteEnvioThread extends Thread {

    private final String host;
    private final int porta;
    private final Pessoa pessoaParaEnviar;
    private final ClienteView view;

    public ClienteEnvioThread(String host, int porta, Pessoa pessoaParaEnviar, ClienteView view) {
        super("thread-envio-cliente");
        this.host = host;
        this.porta = porta;
        this.pessoaParaEnviar = pessoaParaEnviar;
        this.view = view;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, porta)) {
            Comunicador.enviaObjeto(socket, pessoaParaEnviar);
            Pessoa resposta = Comunicador.recebeObjeto(socket);
            view.exibirRespostaServidor(resposta);
        } catch (Exception e) {
            view.exibirErroConexao(e.getMessage());
        } finally {
            view.habilitarBotaoEnviar(true);
        }
    }
}
