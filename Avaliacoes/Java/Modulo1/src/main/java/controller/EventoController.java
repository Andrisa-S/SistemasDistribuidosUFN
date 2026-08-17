package controller;

import model.SaldoCentral;
import thread.CaixaThread;
import view.CaixaView;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por criar as 5 threads de caixa, iniciá-las, aguardar o término de todas e exibir o
 * resultado final através da View.
 *
 * @author Andrisa Santos
 */
public class EventoController {

    private SaldoCentral saldoCentral;
    private CaixaView view;
    private List<CaixaThread> caixas;

    public EventoController(SaldoCentral saldoCentral, CaixaView view) {
        this.saldoCentral = saldoCentral;
        this.view = view;
        this.caixas = new ArrayList<>();
    }

    public SaldoCentral getSaldoCentral() {
        return saldoCentral;
    }

    public void setSaldoCentral(SaldoCentral saldoCentral) {
        this.saldoCentral = saldoCentral;
    }

    public CaixaView getView() {
        return view;
    }

    public void setView(CaixaView view) {
        this.view = view;
    }

    /**
     *
     * @param numeroCaixas     quantidade de caixas/threads (5, pelo enunciado)
     * @param fichasPorCaixa   quantidade de fichas que cada caixa vende (1000)
     * @param precoFicha       preço de cada ficha (R$ 10,00)
     * @throws InterruptedException se a thread principal for interrompida
     */
    public void iniciarEvento(int numeroCaixas, int fichasPorCaixa, double precoFicha) throws InterruptedException {
        view.exibirCabecalho(numeroCaixas, fichasPorCaixa, precoFicha);

        for (int i = 1; i <= numeroCaixas; i++) {
            caixas.add(new CaixaThread("Caixa-" + i, saldoCentral, fichasPorCaixa, precoFicha));
        }

        for (CaixaThread c : caixas) c.start();      // fork
        for (CaixaThread c : caixas) {
            c.join();                                 // join
            view.exibirCaixaFinalizado(c.getName());
        }

        double esperado = numeroCaixas * fichasPorCaixa * precoFicha;
        view.exibirResultado(saldoCentral, esperado);
    }
}
