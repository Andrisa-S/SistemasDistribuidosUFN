import java.io.*;
import java.net.*;
import java.util.concurrent.*;

// Arquitetura Cliente-Servidor, usando Runnable
public class ServidorMultithread {
    private static final int PORTA = 12345;
    private static ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORTA);
        System.out.println("Servidor rodando na porta " + PORTA);

        while (true) {
            Socket clienteSocket = serverSocket.accept();
            pool.execute(new TratadorCliente(clienteSocket));
        }
    }
}

class TratadorCliente implements Runnable {
    private Socket socket;

    public TratadorCliente(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String linha;
            while ((linha = in.readLine()) != null) {
                System.out.println("Recebido: " + linha);
                out.println("Eco: " + linha);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}