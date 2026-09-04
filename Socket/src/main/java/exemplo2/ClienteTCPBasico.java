package exemplo2;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;
import javax.swing.JOptionPane;

public class ClienteTCPBasico {
  public static void main(String[] args) {
    try {
      int porta = 12345;
      Socket cliente = new Socket("10.103.16.2",porta);
      
      ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
     
      Date data_atual = (Date)entrada.readObject();
      JOptionPane.showMessageDialog(null,"Data recebida do servidor: " + data_atual.toString());
      entrada.close();
      JOptionPane.showMessageDialog(null, "Conexão encerrada");
    }
    catch(HeadlessException | IOException | ClassNotFoundException e) {
      System.out.println("Erro: " + e.getMessage());
    }
  }
}