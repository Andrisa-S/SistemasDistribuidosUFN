package model;

/**
 * Representa o saldo centralizado do evento, compartilhado entre os 5
 * caixas (threads) que vendem fichas simultaneamente.
 * <p>
 * @author Andrisa Santos
 */
public class SaldoCentral {

    private double saldo;

    /**
     * Cria o saldo central zerado.
     */
    public SaldoCentral() {
        this.saldo = 0.0;
    }

    /**
     * @return saldo atual do evento
     */
    public synchronized double getSaldo() {
        return saldo;
    }

    /**
     * @param valorVenda valor a somar ao saldo (sempre positivo)
     */
    public synchronized void adicionarVenda(double valorVenda) {
        this.saldo += valorVenda;
    }
}
