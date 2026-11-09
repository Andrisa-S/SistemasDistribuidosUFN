/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package remoto;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.function.Consumer;
import model.Sessao;

/**
 *
 * @author laboratorio
 */
public class ServicoSessaoImpl extends UnicastRemoteObject implements ServicoSessao {
    
    private final HashMap<String, Sessao> sessoes;
    private final Consumer<String> log; // usado pela tela do servidor para exibir as consultas

    public ServicoSessaoImpl(HashMap<String, Sessao> sessoes, Consumer<String> log) throws RemoteException {
        super(); // exporta o objeto para receber chamadas remotas
        this.sessoes = sessoes;
        this.log = log;
    }
    
    @Override
    public Sessao encurtarLink(String link) throws RemoteException {
        String linkLimpo = (link == null) ? null : link.trim();
        Sessao sessao = sessoes.get(link);
        String linkEncurtado = Integer.toHexString(link.hashCode());
        log.accept(linkEncurtado);
        return sessao;
    }
    
    @Override
    public Sessao gerarQRCode(String link) throws RemoteException {
        String linkLimpo = (link == null) ? null : link.trim();
        Sessao sessao = sessoes.get(link);
        String linkEncurtado = Integer.toHexString(link.hashCode());
        log.accept(linkEncurtado); //CONCLUIR
        return sessao;
    }
}
