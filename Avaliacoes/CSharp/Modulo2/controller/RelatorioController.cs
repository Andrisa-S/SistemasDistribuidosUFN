using System.Collections.Generic;

/// <summary>
/// Aplica o padrão Fork-Join:
/// dispara uma thread por filial (fork), aguarda todas (join) e soma os
/// 4 resultados finais (merge).
/// </summary>
public class RelatorioController
{
    private List<Filial> _filiais;
    private RelatorioView _view;
    private readonly List<FilialThread> _threads = new List<FilialThread>();

    public RelatorioController(List<Filial> filiais, RelatorioView view)
    {
        _filiais = filiais;
        _view = view;
    }

    public List<Filial> Filiais
    {
        get => _filiais;
        set => _filiais = value;
    }

    public RelatorioView View
    {
        get => _view;
        set => _view = value;
    }

    public void GerarRelatorio()
    {
        // FORK: uma thread por filial, cada uma com seus próprios dados.
        foreach (var f in _filiais)
        {
            _threads.Add(new FilialThread(f, _view));
        }

        foreach (var t in _threads)
        {
            t.Start();
        }

        // JOIN: aguarda todas terminarem antes de seguir.
        foreach (var t in _threads)
        {
            t.Join();
        }

        // MERGE: só lê os resultados depois que todas já terminaram.
        long faturamentoTotal = 0;
        foreach (var t in _threads)
        {
            faturamentoTotal += t.GetResultado().FaturamentoTotal;
        }

        _view.ExibirResultadoFinal(faturamentoTotal);
    }
}
