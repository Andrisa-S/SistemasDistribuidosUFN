using System;

/// <summary>
/// Camada de Visão (View) do Módulo 1.
/// </summary>
public class CaixaView
{
    public void ExibirCabecalho(int numeroCaixas, int fichasPorCaixa, double precoFicha)
    {
        Console.WriteLine("=======================================================");
        Console.WriteLine(" MÓDULO 1 - CAIXA CENTRALIZADO DO EVENTO");
        Console.WriteLine("=======================================================");
        Console.WriteLine($"Caixas: {numeroCaixas} | Fichas por caixa: {fichasPorCaixa} " +
                           $"| Preço da ficha: R$ {precoFicha}");
        Console.WriteLine("-------------------------------------------------------");
    }

    public void ExibirCaixaFinalizado(string nomeCaixa)
    {
        Console.WriteLine($"{nomeCaixa} finalizou suas vendas.");
    }

    public void ExibirResultado(SaldoCentral saldoCentral, double esperado)
    {
        double saldoFinal = saldoCentral.GetSaldo();

        Console.WriteLine("-------------------------------------------------------");
        Console.WriteLine($"SALDO FINAL: R$ {saldoFinal:F2}");
        Console.WriteLine($"SALDO ESPERADO: R$ {esperado:F2}");

        if (saldoFinal == esperado)
        {
            Console.WriteLine("OK: o saldo bateu certinho com o esperado!");
        }
        else
        {
            Console.WriteLine("ATENÇÃO: divergência! Revise a sincronização em SaldoCentral.");
        }

        Console.WriteLine("=======================================================");
    }
}
