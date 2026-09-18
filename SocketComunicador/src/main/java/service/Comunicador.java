package service;

import model.Pessoa;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Classe responsável por encapsular o protocolo de comunicação entre
 * Cliente e Servidor — no mesmo espírito da {@code ComunicadorObjetos} do
 * material de apoio da disciplina (exemplosTCP_UDP / "Sockets TCP com
 * comunicador"). Nenhuma outra classe do projeto acessa ObjectInputStream
 * ou ObjectOutputStream diretamente; tudo passa por aqui.
 * <p>
 * Diferença proposital em relação ao exemplo do professor: lá, o método
 * enviaObjeto fecha o stream de saída logo após escrever (o que fecha o
 * socket inteiro e impediria o mesmo socket de ser reutilizado para uma
 * leitura em seguida). Aqui só damos flush(), sem fechar — cada conexão é
 * usada para exatamente um envio + uma resposta, então o socket precisa
 * continuar aberto entre as duas chamadas.
 *
 * @author Andrisa Santos
 */
public class Comunicador {

    /**
     * Envia um objeto Pessoa pelo socket informado.
     *
     * @param socket conexão já aberta com o outro lado
     * @param pessoa objeto a ser enviado
     */
    public static void enviaObjeto(Socket socket, Pessoa pessoa) throws IOException {
        ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
        saida.flush();
        saida.writeObject(pessoa);
    }

    /**
     * Recebe um objeto Pessoa do socket informado. Bloqueia até o outro
     * lado enviar algo.
     *
     * @param socket conexão já aberta com o outro lado
     * @return o objeto Pessoa recebido
     */
    public static Pessoa recebeObjeto(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
        return (Pessoa) entrada.readObject();
    }
}
