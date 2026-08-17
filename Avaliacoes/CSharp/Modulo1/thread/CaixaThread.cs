using System.Threading;

/// <summary>
/// Representa um caixa físico do evento. Cada instância vende uma
/// quantidade fixa de fichas e soma o valor arrecadado ao
/// <see cref="SaldoCentral"/>, que é compartilhado com as outras 4 threads.
/// </summary>
public class CaixaThread
{
    private readonly Thread _thread;
    private readonly SaldoCentral _saldoCentral;
    private readonly int _quantidadeFichas;
    private readonly double _precoFicha;

    /// <summary>Nome do caixa (equivalente ao <c>getName()</c> do Java).</summary>
    public string Nome { get; }

    public CaixaThread(string nome, SaldoCentral saldoCentral, int quantidadeFichas, double precoFicha)
    {
        Nome = nome;
        _saldoCentral = saldoCentral;
        _quantidadeFichas = quantidadeFichas;
        _precoFicha = precoFicha;

        // O delegate "Executar" é o equivalente ao run() do Java: é o
        // código que efetivamente roda em uma thread separada.
        _thread = new Thread(Executar) { Name = nome };
    }

    /// <summary>
    /// Corpo da thread: simula a venda das fichas, uma a uma, chamando o
    /// método sincronizado do saldo central a cada venda.
    /// </summary>
    private void Executar()
    {
        for (int i = 0; i < _quantidadeFichas; i++)
        {
            _saldoCentral.AdicionarVenda(_precoFicha);
        }
    }

    /// <summary>Inicia a thread (equivalente a <c>start()</c> no Java).</summary>
    public void Start() => _thread.Start();

    /// <summary>Aguarda o término da thread (equivalente a <c>join()</c> no Java).</summary>
    public void Join() => _thread.Join();

    public SaldoCentral GetSaldoCentral() => _saldoCentral;
    public int GetQuantidadeFichas() => _quantidadeFichas;
    public double GetPrecoFicha() => _precoFicha;
}
