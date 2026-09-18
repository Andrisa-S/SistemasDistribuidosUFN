package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o cadastro acadêmico centralizado do servidor, compartilhado
 * entre TODAS as threads que atendem clientes simultaneamente: é o único ponto
 * de dado compartilhado, e por isso é o único lugar onde a sincronização
 * precisa acontecer.
 * <p>
 * A verificação de duplicidade e a inserção acontecem dentro do MESMO método sincronizado —
 * se fossem sincronizadas separadamente, ainda haveria uma janela entre o
 * "existe?" e o "insere" em que duas threads poderiam intercalar e duplicar
 * o cadastro.
 *
 * @author Andrisa Santos
 */
public class CadastroAcademico {

    private final List<Pessoa> pessoas = new ArrayList<>();

    /**
     * Gera o e-mail (primeiro_nome.ultimo_sobrenome.ano@ufn.edu.br) e
     * cadastra a pessoa, ou localiza e devolve o cadastro já existente
     * (mesmo nome + mesma data de nascimento).
     *
     * @param nome            nome completo informado pelo cliente
     * @param dataNascimento  data no formato dd/MM/yyyy
     * @return a Pessoa cadastrada (nova ou já existente), sempre com e-mail preenchido
     */
    public synchronized Pessoa registrarOuLocalizar(String nome, String dataNascimento) {
        String nomeTratado = nome.trim();
        String dataTratada = dataNascimento.trim();

        String[] vetorNome = nomeTratado.split("\\s+");
        String primeiroNome = vetorNome[0].toLowerCase();
        String ultimoSobrenome = vetorNome[vetorNome.length - 1].toLowerCase();
        String ano = dataTratada.substring(6, 10);
        String email = primeiroNome + "." + ultimoSobrenome + "." + ano + "@ufn.edu.br";

        Pessoa candidata = new Pessoa(nomeTratado, dataTratada, email);

        int idx = pessoas.indexOf(candidata); // usa Pessoa.equals (nome + data)
        if (idx >= 0) {
            return pessoas.get(idx);
        }

        pessoas.add(candidata);
        return candidata;
    }

    /** @return cópia defensiva da lista de pessoas cadastradas até o momento */
    public synchronized List<Pessoa> getPessoas() {
        return new ArrayList<>(pessoas);
    }

    /** @return quantas pessoas já foram cadastradas até o momento */
    public synchronized int getTotalCadastrado() {
        return pessoas.size();
    }
}
