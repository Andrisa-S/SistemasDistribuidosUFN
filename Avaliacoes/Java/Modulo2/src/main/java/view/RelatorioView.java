package view;

import model.ResultadoFilial;

/**
 * Camada de Visão (View) do Módulo 2.
 *
 * @author Andrisa Santos
 */
public class RelatorioView {

    public void exibirCabecalho(int numeroFiliais, int registrosPorFilial) {
        System.out.println("=======================================================");
        System.out.println(" MÓDULO 2 - RELATÓRIO DE VENDAS POR FILIAL");
        System.out.println("=======================================================");
        System.out.println("Filiais: " + numeroFiliais + " | Registros por filial: " + registrosPorFilial);
        System.out.println("-------------------------------------------------------");
    }

    public void exibirResultadoParcial(ResultadoFilial resultado) {
        System.out.printf("Filial-%d -> faturamento local = R$ %d%n",
                resultado.getIdFilial(), resultado.getFaturamentoTotal());
    }

    public void exibirResultadoFinal(long faturamentoTotal) {
        System.out.println("-------------------------------------------------------");
        System.out.println("FATURAMENTO TOTAL DA FRANQUIA: R$ " + faturamentoTotal);
        System.out.println("=======================================================");
    }
}
