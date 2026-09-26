package servidor1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Cliente de teste para o ComunicadorUDP
 */
public class EnviadorUDP {

    private static final String HOST_SERVIDOR = "localhost";
    private static final int PORTA_SERVIDOR = 1234;

    private DatagramSocket socket;

    public EnviadorUDP() {
        criaClienteSocket();
        solicitarToken();
        socket.close();
    }

    private void solicitarToken() {
        try {
            InetAddress endereco = InetAddress.getByName(HOST_SERVIDOR);

            DatagramPacket pedido = ComunicadorUDP.montaMensagem(
                    "SOLICITA_TOKEN", endereco, PORTA_SERVIDOR
            );
            socket.send(pedido);
            System.out.println("Solicitacao de token enviada, aguardando resposta...");

            DatagramPacket resposta = new DatagramPacket(new byte[512], 512);
            socket.receive(resposta);

            String token = new String(
                    resposta.getData(), 0, resposta.getLength()
            ).trim();
            System.out.println("Token recebido: " + token);

        } catch (UnknownHostException ex) {
            System.err.println("Host desconhecido: " + HOST_SERVIDOR);
        } catch (IOException ex) {
            System.err.println("Erro de I/O: " + ex.getMessage());
        }
    }

    private void criaClienteSocket() {
        try {
            socket = new DatagramSocket(); // porta efêmera, escolhida pelo SO
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EnviadorUDP();
    }
}