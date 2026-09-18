package controller;

import model.Pessoa;
import thread.ClienteEnvioThread;
import view.ClienteView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Controller do Cliente: liga a View ao restante da aplicação. É aqui que
 * mora a validação dos campos e a decisão de disparar a
 * {@link ClienteEnvioThread}; a View não sabe nada disso, só exibe.
 *
 * @author Andrisa Santos
 */
public class ClienteController {

    private static final String HOST = "localhost";
    private static final int PORTA = 50000;

    private final ClienteView view;

    // "uuuu" (ano ISO) em vez de "yyyy" (ano-da-era): com ResolverStyle.STRICT,
    // "yyyy" sozinho não resolve o ano sem uma era explícita, e QUALQUER data
    // seria rejeitada. "uuuu" resolve isso e ainda rejeita corretamente datas
    // absurdas como 31/02/2026 ou 29/02 em ano não bissexto.
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public ClienteController(ClienteView view) {
        this.view = view;
        this.view.setBotaoEnviarListener(evt -> enviar());
    }

    private void enviar() {
        String nome = view.getNomeDigitado();
        String data = view.getDataDigitada();

        if (nome.isEmpty() || nome.split("\\s+").length < 2) {
            view.exibirErroValidacao("Informe o nome completo (nome e sobrenome).");
            return;
        }

        try {
            LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            view.exibirErroValidacao("Data de nascimento inválida. Use o formato dd/MM/yyyy.");
            return;
        }

        view.habilitarBotaoEnviar(false);
        Pessoa pessoaParaEnviar = new Pessoa(nome, data, null);

        new ClienteEnvioThread(HOST, PORTA, pessoaParaEnviar, view).start();
    }
}
