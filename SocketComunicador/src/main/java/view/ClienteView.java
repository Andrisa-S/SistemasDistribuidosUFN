package view;

import model.Pessoa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Camada de Visão (View) do Cliente. Só monta a tela e expõe os dados
 * digitados / métodos de exibição — nenhuma regra de negócio, validação
 * ou comunicação de rede acontece aqui. Quem decide o que fazer com o
 * clique do botão é o {@link controller.ClienteController}.
 *
 * @author Andrisa Santos
 */
public class ClienteView extends JFrame {

    private final JTextField txtNomeCompleto = new JTextField(20);
    private final JTextField txtDataNascimento = new JTextField("dd/mm/yyyy", 10);
    private final JTextField txtNomeCompletoView = new JTextField(20);
    private final JTextField txtDataNascimentoView = new JTextField(10);
    private final JTextField txtEmailCliente = new JTextField(20);
    private final JButton btnEnviar = new JButton("Enviar");

    public ClienteView() {
        super("Cadastro Acadêmico - Cliente");
        montarTela();
    }

    private void montarTela() {
        txtNomeCompletoView.setEditable(false);
        txtDataNascimentoView.setEditable(false);
        txtEmailCliente.setEditable(false);

        // Seleciona todo o texto ao focar, para que digitar sobrescreva o
        // "dd/mm/yyyy" de exemplo em vez de se misturar com ele.
        txtDataNascimento.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDataNascimento.selectAll();
            }
        });

        JPanel painelCampos = new JPanel(new GridLayout(0, 2, 8, 8));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        painelCampos.add(new JLabel("Nome Completo:"));
        painelCampos.add(txtNomeCompleto);
        painelCampos.add(new JLabel("Data de Nascimento:"));
        painelCampos.add(txtDataNascimento);
        painelCampos.add(new JLabel("Nome (retornado):"));
        painelCampos.add(txtNomeCompletoView);
        painelCampos.add(new JLabel("Data (retornado):"));
        painelCampos.add(txtDataNascimentoView);
        painelCampos.add(new JLabel("E-mail:"));
        painelCampos.add(txtEmailCliente);

        JPanel painelBotao = new JPanel();
        painelBotao.add(btnEnviar);

        setLayout(new BorderLayout());
        add(painelCampos, BorderLayout.CENTER);
        add(painelBotao, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public String getNomeDigitado() {
        return txtNomeCompleto.getText().trim();
    }

    public String getDataDigitada() {
        return txtDataNascimento.getText().trim();
    }

    public void setBotaoEnviarListener(ActionListener listener) {
        btnEnviar.addActionListener(listener);
    }

    public void habilitarBotaoEnviar(boolean habilitado) {
        SwingUtilities.invokeLater(() -> btnEnviar.setEnabled(habilitado));
    }

    /** Preenche os três campos bloqueados com o que voltou do servidor. */
    public void exibirRespostaServidor(Pessoa pessoa) {
        SwingUtilities.invokeLater(() -> {
            txtNomeCompletoView.setText(pessoa.getNome());
            txtDataNascimentoView.setText(pessoa.getDataNascimento());
            txtEmailCliente.setText(pessoa.getEmail());
        });
    }

    public void exibirErroValidacao(String mensagem) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, mensagem, "Validação", JOptionPane.WARNING_MESSAGE));
    }

    public void exibirErroConexao(String mensagem) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, "Não foi possível falar com o servidor: " + mensagem,
                        "Erro de rede", JOptionPane.ERROR_MESSAGE));
    }
}
