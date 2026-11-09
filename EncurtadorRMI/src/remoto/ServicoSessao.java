/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package remoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Sessao;
/**
 *
 * @author Andrisa Santos
 */
public interface ServicoSessao extends Remote {
    
    String NOME = "ServicoSessao"; // nome registrado no RMI Registry

    // Retorna a sessão do número informado, ou null se não existir
    Sessao encurtarLink(String link) throws RemoteException;
    Sessao gerarQRCode (String link) throws RemoteException;
}
