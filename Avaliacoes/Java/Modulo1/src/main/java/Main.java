import controller.EventoController;
import model.SaldoCentral;
import view.CaixaView;

/**
 * Classe principal do Módulo 1 - Caixa Centralizado do Evento.
 * Já vem pronta: monta o MVC e chama o Controller.
 *
 * @author Andrisa Santos - Trabalho de Sistemas Distribuídos
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        SaldoCentral saldoCentral = new SaldoCentral();
        CaixaView view = new CaixaView();
        EventoController controller = new EventoController(saldoCentral, view);

        int numeroCaixas = 5;
        int fichasPorCaixa = 1000;
        double precoFicha = 10.0;

        controller.iniciarEvento(numeroCaixas, fichasPorCaixa, precoFicha);
    }
}
