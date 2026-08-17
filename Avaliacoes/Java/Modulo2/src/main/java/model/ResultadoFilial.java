package model;

/**
 * Armazena o resultado do cálculo de faturamento de UMA filial.
 *
 * @author Andrisa Santos
 */
public class ResultadoFilial {

    private int idFilial;
    private long faturamentoTotal;

    public ResultadoFilial() {
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public long getFaturamentoTotal() {
        return faturamentoTotal;
    }

    public void setFaturamentoTotal(long faturamentoTotal) {
        this.faturamentoTotal = faturamentoTotal;
    }
}
