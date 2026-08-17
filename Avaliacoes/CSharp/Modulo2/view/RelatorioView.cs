using System;

/// <summary>
/// Camada de Visão (View) do Módulo 2.
/// </summary>
public class RelatorioView
{
    public void ExibirCabecalho(int numeroFiliais, int registrosPorFilial)
    {
        Console.WriteLine("=======================================================");
        Console.WriteLine(" MÓDULO 2 - RELATÓRIO DE VENDAS POR FILIAL");
        Console.WriteLine("=======================================================");
        Console.WriteLine($"Filiais: {numeroFiliais} | Registros por filial: {registrosPorFilial}");
        Console.WriteLine("-------------------------------------------------------");
    }

    public void ExibirResultadoParcial(ResultadoFilial resultado)
    {
        Console.WriteLine($"Filial-{resultado.IdFilial} -> faturamento local = R$ {resultado.FaturamentoTotal}");
    }

    public void ExibirResultadoFinal(long faturamentoTotal)
    {
        Console.WriteLine("-------------------------------------------------------");
        Console.WriteLine($"FATURAMENTO TOTAL DA FRANQUIA: R$ {faturamentoTotal}");
        Console.WriteLine("=======================================================");
    }
}
