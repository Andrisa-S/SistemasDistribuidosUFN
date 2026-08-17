using System.Threading;

/// <summary>
/// Thread responsável por calcular o faturamento de UMA filial, de forma
/// totalmente isolada — sem acessar nenhuma variável global ou
/// compartilhada com as outras threads.
/// </summary>
public class FilialThread
{
    private readonly Thread _thread;
    private readonly Filial _filial;
    private readonly ResultadoFilial _resultado = new ResultadoFilial();
    private readonly RelatorioView _view;

    public string Nome { get; }

    public FilialThread(Filial filial, RelatorioView view)
    {
        Nome = $"Filial-{filial.Id}";
        _filial = filial;
        _view = view;
        _thread = new Thread(Executar) { Name = Nome };
    }

    /// <summary>
    /// Soma o faturamento em uma variável LOCAL (não estática, não
    /// compartilhada) e guarda o resultado na própria instância de
    /// <see cref="ResultadoFilial"/>, que só esta thread escreve.
    /// </summary>
    private void Executar()
    {
        long somaLocal = 0;
        foreach (int valor in _filial.Vendas)
        {
            somaLocal += valor;
        }

        _resultado.IdFilial = _filial.Id;
        _resultado.FaturamentoTotal = somaLocal;

        _view.ExibirResultadoParcial(_resultado);
    }

    public void Start() => _thread.Start();
    public void Join() => _thread.Join();

    /// <summary>
    /// Retorna o resultado calculado por esta thread. Só deve ser chamado
    /// pelo Controller DEPOIS do <see cref="Join"/>, quando não há mais
    /// concorrência sobre o objeto.
    /// </summary>
    public ResultadoFilial GetResultado() => _resultado;

    public Filial GetFilial() => _filial;
}
