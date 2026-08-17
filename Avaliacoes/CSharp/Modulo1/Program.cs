/// <summary>
/// Ponto de entrada do Módulo 1 - Caixa Centralizado do Evento.
/// </summary>
public class Program
{
    public static void Main(string[] args)
    {
        var saldoCentral = new SaldoCentral();
        var view = new CaixaView();
        var controller = new EventoController(saldoCentral, view);

        int numeroCaixas = 5;
        int fichasPorCaixa = 1000;
        double precoFicha = 10.0;

        controller.IniciarEvento(numeroCaixas, fichasPorCaixa, precoFicha);
    }
}
