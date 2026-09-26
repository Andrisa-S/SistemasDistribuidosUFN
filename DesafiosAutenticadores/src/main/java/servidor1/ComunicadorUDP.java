package servidor1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Servidor 1: Relógio de Xs, gera TOKEN e envia para quem solicita.
 */
public class ComunicadorUDP {

    private static final int PORTA = 1234;
    private static final int INTERVALO = 30; // Intervalo do relógio, em segundos

    private DatagramSocket socket;
    private String tokenAtual;
    private long instanteGeracao;

    public ComunicadorUDP() {
        criaServerSocket();
        gerarNovoToken(); // Gera o primeiro token assim que o servidor ativa
        iniciarRelogio(); // Dispara a thread que renova o token a cada X segundos
        System.out.println("Servidor ativo na porta: " + PORTA + ", esperando solicitacoes...");
        aguardaSolicitacoes();
    }

    // --- Loop principal ---

    private void aguardaSolicitacoes() {
        DatagramPacket pacoteRecebido;

        while(true) {
            pacoteRecebido = recebeMensagem(); // Bloqueia até chegar algo
            if (pacoteRecebido == null) {
                continue; // Erro na leitura, continua escutando
            }

            String mensagem = new String(
                    pacoteRecebido.getData(), 0, pacoteRecebido.getLength()).trim(); // Verifica o tamanho do pacote

            System.out.println("Recebi uma solicitacao: \"" + mensagem + "\"");
            System.out.println("De: " + pacoteRecebido.getAddress().getHostName() + ":" + pacoteRecebido.getPort());

            String token = getTokenAtual();

            DatagramPacket resposta = montaMensagem(
                    token, pacoteRecebido.getAddress(), pacoteRecebido.getPort());
            enviaMensagem(resposta);

            System.out.println("Enviei o token: \"" + token + "\"");
        }
    }

    // --- Geração de Token ---

    private synchronized void gerarNovoToken() {
        long agora = System.currentTimeMillis();
        int hash = Long.valueOf(agora).hashCode();
        this.tokenAtual = Integer.toHexString(hash);
        this.instanteGeracao = agora;
        System.out.println("Novo token gerado: " + tokenAtual);
    }

    private synchronized String getTokenAtual() {
        long agora = System.currentTimeMillis();
        if(agora - instanteGeracao >= INTERVALO * 1000L) {
            gerarNovoToken();
        }
        return tokenAtual;
    }

    private void iniciarRelogio() {
        Thread relogio = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(INTERVALO * 1000L);
                    gerarNovoToken();
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        relogio.setDaemon(true); // Executa em segundo plano
        relogio.start();
    }

    // --- Comunicação UDP ---

    public static DatagramPacket montaMensagem(String mensagem, InetAddress endereco, int porta) {
        byte[] dados = mensagem.getBytes();
        return new DatagramPacket(dados, dados.length, endereco, porta);
    }

    public DatagramPacket recebeMensagem() {
        try {
            DatagramPacket pacote = new DatagramPacket(new byte[512], 512);
            socket.receive(pacote);
            return pacote;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void enviaMensagem(DatagramPacket pacote) {
        try {
            socket.send(pacote);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void criaServerSocket() {
        try {
            socket = new DatagramSocket(PORTA);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // --- Main ---

    public static void main(String[] args) {
        new ComunicadorUDP();
    }
}