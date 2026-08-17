import controller.RelatorioController;
import model.Filial;
import view.RelatorioView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe principal do Módulo 2 - Relatório de Vendas por Filial.
 *
 * @author Andrisa Santos - Trabalho de Sistemas Distribuídos
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        int numeroFiliais = 4;
        int registrosPorFilial = 10000;
        Random random = new Random();

        List<Filial> filiais = new ArrayList<>();

        for (int id = 1; id <= numeroFiliais; id++) {
            int[] vendas = new int[registrosPorFilial];
            for (int i = 0; i < registrosPorFilial; i++) {
                vendas[i] = random.nextInt(500) + 1;
            }
            filiais.add(new Filial(id, vendas));
        }

        RelatorioView view = new RelatorioView();
        view.exibirCabecalho(numeroFiliais, registrosPorFilial);

        RelatorioController controller = new RelatorioController(filiais, view);
        controller.gerarRelatorio();
    }
}
