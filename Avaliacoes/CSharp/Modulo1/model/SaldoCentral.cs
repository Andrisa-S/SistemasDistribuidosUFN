using System;

/// <summary>
/// Representa o saldo centralizado do evento, compartilhado entre os 5
/// caixas (threads) que vendem fichas simultaneamente.
/// </summary>
public class SaldoCentral
{
    private double _saldo;

    // Objeto dedicado para o lock. Nunca trave em "this" nem em um objeto
    // público: qualquer código externo poderia travar no mesmo objeto e
    // causar deadlock sem você perceber.
    private readonly object _lock = new object();

    /// <summary>
    /// Retorna o saldo atual, de forma segura entre threads.
    /// </summary>
    public double GetSaldo()
    {
        lock (_lock)
        {
            return _saldo;
        }
    }

    /// <summary>
    /// Soma um valor de venda ao saldo central de forma thread-safe.
    /// Equivalente ao método <c>synchronized</c> do Java.
    /// </summary>
    /// <param name="valorVenda">Valor a somar ao saldo (sempre positivo).</param>
    public void AdicionarVenda(double valorVenda)
    {
        lock (_lock)
        {
            _saldo += valorVenda;
        }
    }
}
