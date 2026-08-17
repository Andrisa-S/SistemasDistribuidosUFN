/// <summary>
/// Representa os dados de vendas de UMA filial — um dado local, isolado,
/// que não é compartilhado com nenhuma outra thread.
/// </summary>
public class Filial
{
    public int Id { get; }
    public int[] Vendas { get; }

    public Filial(int id, int[] vendas)
    {
        Id = id;
        Vendas = vendas;
    }
}
