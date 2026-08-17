using System.Collections.Generic;

/// <summary>
/// Cria as 5 threads de caixa,
/// inicia todas, aguarda o término de todas e exibe o resultado final.
/// </summary>
public class EventoController
{
    private SaldoCentral _saldoCentral;
    private CaixaView _view;
    private readonly List<CaixaThread> _caixas = new List<CaixaThread>();

    public EventoController(SaldoCentral saldoCentral, CaixaView view)
    {
        _saldoCentral = saldoCentral;
        _view = view;
    }

    public SaldoCentral SaldoCentral
    {
        get => _saldoCentral;
        set => _saldoCentral = value;
    }

    public CaixaView View
    {
        get => _view;
        set => _view = value;
    }

    /// <summary>
    /// Fluxo completo da simulação: cria as threads (fork), inicia todas,
    /// aguarda o término de todas (join) e exibe o resultado final.
    /// Note que não há "throws InterruptedException": C# não tem exceções
    /// checadas, então o método nem precisa declarar isso.
    /// </summary>
    public void IniciarEvento(int numeroCaixas, int fichasPorCaixa, double precoFicha)
    {
        _view.ExibirCabecalho(numeroCaixas, fichasPorCaixa, precoFicha);

        for (int i = 1; i <= numeroCaixas; i++)
        {
            _caixas.Add(new CaixaThread($"Caixa-{i}", _saldoCentral, fichasPorCaixa, precoFicha));
        }

        foreach (var c in _caixas)
        {
            c.Start(); // fork
        }

        foreach (var c in _caixas)
        {
            c.Join(); // join
            _view.ExibirCaixaFinalizado(c.Nome);
        }

        double esperado = numeroCaixas * fichasPorCaixa * precoFicha;
        _view.ExibirResultado(_saldoCentral, esperado);
    }
}
