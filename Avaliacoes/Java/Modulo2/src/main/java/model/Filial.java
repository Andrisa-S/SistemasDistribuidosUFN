package model;

/**
 * Representa os dados de vendas de UMA filial — um dado local, isolado,
 * que não é compartilhado com nenhuma outra thread.
 *
 * @author Andrisa Santos
 */
public class Filial {

    private final int id;
    private final int[] vendas;

    /**
     * @param id     identificador da filial (1 a 4)
     * @param vendas vetor de valores de venda pertencente exclusivamente
     *               a esta filial
     */
    public Filial(int id, int[] vendas) {
        this.id = id;
        this.vendas = vendas;
    }

    public int getId() {
        return id;
    }

    public int[] getVendas() {
        return vendas;
    }
}
