package thread;

import model.Filial;
import model.ResultadoFilial;
import view.RelatorioView;

/**
 * Thread responsável por calcular o faturamento de UMA filial, de forma
 * totalmente isolada — sem acessar nenhuma variável global ou
 * compartilhada com as outras threads.
 *
 * @author Andrisa Santos
 */
public class FilialThread extends Thread {

    /** Dados de entrada exclusivos desta thread (não compartilhados). */
    private final Filial filial;

    /** Resultado exclusivo desta thread (só ela escreve nele). */
    private final ResultadoFilial resultado;

    private final RelatorioView view;

    public FilialThread(Filial filial, RelatorioView view) {
        super("Filial-" + filial.getId());
        this.filial = filial;
        this.resultado = new ResultadoFilial();
        this.view = view;
    }

    /**
     * Regra do exercício: em nenhum momento desta execução esta thread
     * pode ler ou escrever em uma variável global/estática compartilhada
     * com as outras threads.
     */
    @Override
    public void run() {
        long somaLocal = 0;
        for (int valor : filial.getVendas()) {
            somaLocal += valor;
        }

        resultado.setIdFilial(filial.getId());
        resultado.setFaturamentoTotal(somaLocal);

        view.exibirResultadoParcial(resultado);
    }

    /**
     * Retorna o resultado calculado por esta thread.
     * Só deve ser chamado pelo Controller DEPOIS do {@code join()} desta
     * thread — momento em que não há mais concorrência sobre o objeto.
     *
     * @return resultado do cálculo desta filial
     */
    public ResultadoFilial getResultado() {
        return resultado;
    }

    /** @return os dados de entrada (filial) associados a esta thread */
    public Filial getFilial() {
        return filial;
    }
}
