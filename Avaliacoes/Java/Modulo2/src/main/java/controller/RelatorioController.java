package controller;

import model.Filial;
import thread.FilialThread;
import view.RelatorioView;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsável por aplicar o padrão Fork-Join: disparar uma thread por filial (fork), aguardar todas
 * (join) e somar os 4 resultados finais.
 *
 * @author Andrisa Santos
 */
public class RelatorioController {

    private List<Filial> filiais;
    private RelatorioView view;
    private List<FilialThread> threads;

    public RelatorioController(List<Filial> filiais, RelatorioView view) {
        this.filiais = filiais;
        this.view = view;
        this.threads = new ArrayList<>();
    }

    public List<Filial> getFiliais() {
        return filiais;
    }

    public void setFiliais(List<Filial> filiais) {
        this.filiais = filiais;
    }

    public RelatorioView getView() {
        return view;
    }

    public void setView(RelatorioView view) {
        this.view = view;
    }

    /**
     * @throws InterruptedException se a thread principal for interrompida
     * enquanto aguarda o término das demais
     */
    public void gerarRelatorio() throws InterruptedException {
        for (Filial f : filiais) {
            threads.add(new FilialThread(f, view));
        }

        for (FilialThread t : threads) t.start();   // fork
        for (FilialThread t : threads) t.join();    // join

        long faturamentoTotal = 0;
        for (FilialThread t : threads) {
            faturamentoTotal += t.getResultado().getFaturamentoTotal(); // merge, só após join
        }

        view.exibirResultadoFinal(faturamentoTotal);
    }
}
