package pkg;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa um aluno cadastrado.
 *
 * Precisa implementar Serializable para poder "trafegar" pela rede dentro
 * de um ObjectOutputStream/ObjectInputStream. O serialVersionUID fixo evita
 * InvalidClassException caso cliente e servidor sejam compilados em
 * momentos/JDKs ligeiramente diferentes.
 */
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String dataNascimento;
    private String email;

    public Pessoa(String nome, String dataNascimento, String email) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Pessoa{" + "nome=" + nome + ", dataNascimento=" + dataNascimento + ", email=" + email + '}';
    }

    /**
     * Duas Pessoas são consideradas "a mesma pessoa" quando têm o mesmo nome
     * e a mesma data de nascimento. O e-mail é um dado DERIVADO desses dois
     * campos (gerado pelo servidor) e por isso não entra na comparação de
     * identidade - assim o servidor consegue localizar um cadastro já
     * existente usando list.indexOf(candidata)/list.contains(candidata).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pessoa)) {
            return false;
        }
        final Pessoa other = (Pessoa) obj;
        return Objects.equals(this.nome, other.nome)
                && Objects.equals(this.dataNascimento, other.dataNascimento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, dataNascimento);
    }
}
