package remoto;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.function.Consumer;
import model.Sessao;

// Implementação que roda no servidor e acessa o HashMap com a base de sessões
public class ServicoSessaoImpl extends UnicastRemoteObject implements ServicoSessao {

    private final HashMap<String, Sessao> sessoes;
    private final Consumer<String> log; // usado pela tela do servidor para exibir as consultas

    public ServicoSessaoImpl(HashMap<String, Sessao> sessoes, Consumer<String> log) throws RemoteException {
        super(); // exporta o objeto para receber chamadas remotas
        this.sessoes = sessoes;
        this.log = log;
    }

    @Override
    public Sessao buscarPorCelular(String numeroCelular) throws RemoteException {
        String numero = (numeroCelular == null) ? null : numeroCelular.trim();
        Sessao sessao = sessoes.get(numero);
        log.accept("Consulta: " + numero + (sessao == null ? " -> não encontrado" : " -> encontrado"));
        return sessao;
    }
}
