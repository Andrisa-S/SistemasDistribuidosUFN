package view;

import model.SaldoCentral;

/**
 * Camada de Visão (View) do Módulo 1.
 * 
 * @author Andrisa Santos
 */
public class CaixaView {

    public void exibirCabecalho(int numeroCaixas, int fichasPorCaixa, double precoFicha) {
        System.out.println("=======================================================");
        System.out.println(" MÓDULO 1 - CAIXA CENTRALIZADO DO EVENTO");
        System.out.println("=======================================================");
        System.out.println("Caixas: " + numeroCaixas
                + " | Fichas por caixa: " + fichasPorCaixa
                + " | Preço da ficha: R$ " + precoFicha);
        System.out.println("-------------------------------------------------------");
    }

    public void exibirCaixaFinalizado(String nomeCaixa) {
        System.out.println(nomeCaixa + " finalizou suas vendas.");
    }

    public void exibirResultado(SaldoCentral saldoCentral, double esperado) {
        double saldoFinal = saldoCentral.getSaldo();
        System.out.println("-------------------------------------------------------");
        System.out.printf("SALDO FINAL: R$ %.2f%n", saldoFinal);
        System.out.printf("SALDO ESPERADO: R$ %.2f%n", esperado);
        if (Double.compare(saldoFinal, esperado) == 0) {
            System.out.println("OK: o saldo bateu certinho com o esperado!");
        } else {
            System.out.println("ATENÇÃO: divergência! Revise a sincronização em SaldoCentral.");
        }
        System.out.println("=======================================================");
    }
}
