package thread;

import model.SaldoCentral;

/**
 * Representa um caixa físico do evento. Cada instância é uma thread que
 * vende uma quantidade fixa de fichas e soma o valor arrecadado ao
 * {@link SaldoCentral}, que é compartilhado com as outras 4 threads.
 *
 * @author Andrisa Santos
 */
public class CaixaThread extends Thread {

    /** Saldo compartilhado entre TODAS as threads de caixa. */
    private final SaldoCentral saldoCentral;

    /** Quantidade de fichas que este caixa deve vender. */
    private final int quantidadeFichas;

    /** Preço de cada ficha. */
    private final double precoFicha;

    /**
     * Cria uma nova thread de caixa.
     *
     * @param nome             nome da thread (ex.: "Caixa1")
     * @param saldoCentral     saldo compartilhado do evento
     * @param quantidadeFichas quantidade de fichas a vender
     * @param precoFicha       preço unitário da ficha
     */
    public CaixaThread(String nome, SaldoCentral saldoCentral, int quantidadeFichas, double precoFicha) {
        super(nome);
        this.saldoCentral = saldoCentral;
        this.quantidadeFichas = quantidadeFichas;
        this.precoFicha = precoFicha;
    }

    /**
     * Vende ficha por ficha, chamando o método sincronizado a cada venda
     */
    @Override
    public void run() {
        for (int i = 0; i < quantidadeFichas; i++) {
            saldoCentral.adicionarVenda(precoFicha);
        }
    }

    /** @return o saldo central associado a esta thread */
    public SaldoCentral getSaldoCentral() {
        return saldoCentral;
    }

    /** @return a quantidade de fichas que esta thread deve vender */
    public int getQuantidadeFichas() {
        return quantidadeFichas;
    }

    /** @return o preço de cada ficha vendida por esta thread */
    public double getPrecoFicha() {
        return precoFicha;
    }
}
