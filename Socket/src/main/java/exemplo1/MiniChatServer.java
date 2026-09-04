/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exemplo1;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiniChatServer {

    static BufferedReader tecladoTerminal = new BufferedReader(new InputStreamReader(System.in));
    static ServerSocket socketServidor;
    static Socket socketCliente;
    static ObjectOutputStream saida;
    static ObjectInputStream entrada;

    public static void main(String[] args) throws IOException {
        String apelido = "SERVIDOR";
        int portaNumero = 12345; //Integer.parseInt(JOptionPane.showInputDialog(null, "Informe a porta logica liberada"));
        long tempoInicio;
        try {
            socketServidor = new ServerSocket(portaNumero); //CONSTRUTOR DO SERVERSOCKT RECEBE A PORTA DE TRABALHO
            System.out.println("Servidor em funcionamento");

            tempoInicio = System.nanoTime();
            socketCliente = socketServidor.accept(); //ACAO BLOQUEANTE QUE O SERVIDOR SÓ LIBERA QUANDO UM CLIENTE FIZER CONEXÃO
            System.out.println("Tempo de conexão do primeiro cliente ao servidor: " + (System.nanoTime() - tempoInicio) / 1000000);

            saida = new ObjectOutputStream(socketCliente.getOutputStream()); //INSTANCIAR PRIMEIRO O ESCRITOR
            entrada = new ObjectInputStream(socketCliente.getInputStream()); //INSTANCIAR EM SEGUNDO O LEITOR

            new Thread() {
                @Override
                public void run() {
                    String fraseDoCliente;
                    try {
                        //ENQUANTO O CLIENTE ENVIAR TEXTO, EXIBIR NA TELA
                        while ((fraseDoCliente = (String) entrada.readObject()) != null) {
                            System.out.println("Msg do cliente: " + fraseDoCliente);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(MiniChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();

            String fraseDoServidor;
            do {
                System.out.println("Escreva sua mensagem: ");
                fraseDoServidor = tecladoTerminal.readLine();
                saida.flush();
                saida.writeObject((apelido + ": " + fraseDoServidor));
            } while (fraseDoServidor != null);
        } catch (IOException e) {
            System.out.println("Exceção capturada ao ouvir/ler a porta "
                    + portaNumero + " ou ao tentar conectar");
            System.out.println(e.getMessage());
        }
        saida.close();
        entrada.close();
        socketCliente.close();
        socketServidor.close();
        System.exit(0);
    }
}
