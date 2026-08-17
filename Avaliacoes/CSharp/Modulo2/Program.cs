using System;
using System.Collections.Generic;

/// <summary>
/// Ponto de entrada do Módulo 2 - Relatório de Vendas por Filial.
/// </summary>
public class Program
{
    public static void Main(string[] args)
    {
        int numeroFiliais = 4;
        int registrosPorFilial = 10_000;
        var random = new Random();

        var filiais = new List<Filial>();
        for (int id = 1; id <= numeroFiliais; id++)
        {
            var vendas = new int[registrosPorFilial];
            for (int i = 0; i < registrosPorFilial; i++)
            {
                vendas[i] = random.Next(1, 501); // valores de 1 a 500
            }

            filiais.Add(new Filial(id, vendas));
        }

        var view = new RelatorioView();
        view.ExibirCabecalho(numeroFiliais, registrosPorFilial);

        var controller = new RelatorioController(filiais, view);
        controller.GerarRelatorio();
    }
}
