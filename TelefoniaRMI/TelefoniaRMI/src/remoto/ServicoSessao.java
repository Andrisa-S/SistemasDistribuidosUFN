package remoto;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Sessao;

// Interface remota: é o "contrato" que o cliente enxerga do servidor
public interface ServicoSessao extends Remote {

    String NOME = "ServicoSessao"; // nome registrado no RMI Registry

    // Retorna a sessão do número informado, ou null se não existir
    Sessao buscarPorCelular(String numeroCelular) throws RemoteException;
}
