package view;

import model.Pessoa;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Camada de Visão (View) do Servidor: uma área de log e uma lista gráfica
 * com as pessoas cadastradas. Assim como em ClienteView, aqui não existe
 * nenhuma lógica de rede nem de negócio — só exibição.
 * <p>
 * Como esses métodos são chamados a partir de threads de rede (não da
 * Event Dispatch Thread), cada um se protege sozinho com
 * {@code SwingUtilities.invokeLater}.
 *
 * @author Andrisa Santos
 */
public class ServidorView extends JFrame {

    private final JTextArea txtLog = new JTextArea(12, 45);
    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final JList<String> listaCadastrados = new JList<>(modeloLista);

    public ServidorView() {
        super("Cadastro Acadêmico - Servidor");
        montarTela();
    }

    private void montarTela() {
        txtLog.setEditable(false);

        setLayout(new BorderLayout());
        add(new JScrollPane(txtLog), BorderLayout.CENTER);

        JPanel painelDireita = new JPanel(new BorderLayout());
        painelDireita.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        painelDireita.add(new JLabel("Pessoas cadastradas:"), BorderLayout.NORTH);
        painelDireita.add(new JScrollPane(listaCadastrados), BorderLayout.CENTER);
        add(painelDireita, BorderLayout.EAST);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(650, 350));
        pack();
        setLocationRelativeTo(null);
    }

    public void log(String mensagem) {
        SwingUtilities.invokeLater(() -> txtLog.append(mensagem + "\n"));
    }

    public void atualizarListaCadastrados(List<Pessoa> pessoas) {
        SwingUtilities.invokeLater(() -> {
            modeloLista.clear();
            for (Pessoa p : pessoas) {
                modeloLista.addElement(p.getNome() + " - " + p.getEmail());
            }
        });
    }
}
