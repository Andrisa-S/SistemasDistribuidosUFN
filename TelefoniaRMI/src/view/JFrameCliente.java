package view;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;
import model.Sessao;
import remoto.ServicoSessao;

public class JFrameCliente extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameCliente.class.getName());

    private ServicoSessao servico; // referência remota (stub) para o servidor

    public JFrameCliente() {
        initComponents();
        jPanelPesquisa.setVisible(false);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Telefonia - Cliente RMI");

        jLabel1 = new javax.swing.JLabel("CONSULTA DE SESSÕES (CLIENTE)");
        jLabel1.setFont(jLabel1.getFont().deriveFont(java.awt.Font.BOLD, 16f));

        jTextFieldHost = new javax.swing.JTextField("localhost", 12);
        jTextFieldPorta = new javax.swing.JTextField("1099", 5);
        jButtonConectar = new javax.swing.JButton("Conectar");
        jLabelStatus = new javax.swing.JLabel("Desconectado");

        jTextFieldNumeroCelularPesquisa = new javax.swing.JTextField(15);
        jButtonPesquisar = new javax.swing.JButton("Pesquisar");
        jTextAreaResultadoPesquisa = new javax.swing.JTextArea(6, 40);
        jTextAreaResultadoPesquisa.setEditable(false);

        jPanelPesquisa = new javax.swing.JPanel(new java.awt.BorderLayout(0, 8));
        javax.swing.JPanel linhaPesquisa = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linhaPesquisa.add(new javax.swing.JLabel("Número do celular:"));
        linhaPesquisa.add(jTextFieldNumeroCelularPesquisa);
        linhaPesquisa.add(jButtonPesquisar);
        javax.swing.JScrollPane sp = new javax.swing.JScrollPane(jTextAreaResultadoPesquisa);
        sp.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da sessão"));
        jPanelPesquisa.add(linhaPesquisa, java.awt.BorderLayout.NORTH);
        jPanelPesquisa.add(sp, java.awt.BorderLayout.CENTER);

        javax.swing.JPanel linha1 = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linha1.add(jLabel1);
        javax.swing.JPanel linha2 = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linha2.add(new javax.swing.JLabel("Servidor:"));
        linha2.add(jTextFieldHost);
        linha2.add(new javax.swing.JLabel("Porta:"));
        linha2.add(jTextFieldPorta);
        linha2.add(jButtonConectar);
        javax.swing.JPanel linha3 = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linha3.add(jLabelStatus);

        javax.swing.JPanel topo = new javax.swing.JPanel();
        topo.setLayout(new javax.swing.BoxLayout(topo, javax.swing.BoxLayout.Y_AXIS));
        topo.add(linha1);
        topo.add(linha2);
        topo.add(linha3);

        javax.swing.JPanel conteudo = new javax.swing.JPanel(new java.awt.BorderLayout(0, 8));
        conteudo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        conteudo.add(topo, java.awt.BorderLayout.NORTH);
        conteudo.add(jPanelPesquisa, java.awt.BorderLayout.CENTER);
        setContentPane(conteudo);

        jButtonConectar.addActionListener(evt -> jButtonConectarActionPerformed(evt));
        jButtonPesquisar.addActionListener(evt -> jButtonPesquisarActionPerformed(evt));
        jTextFieldNumeroCelularPesquisa.addActionListener(evt -> jButtonPesquisarActionPerformed(evt)); // Enter
        pack();
    }

    private void jButtonConectarActionPerformed(java.awt.event.ActionEvent evt) {
        int porta;
        try {
            porta = Integer.parseInt(jTextFieldPorta.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Porta inválida!");
            jTextFieldPorta.requestFocus();
            return;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(jTextFieldHost.getText().trim(), porta);
            servico = (ServicoSessao) registry.lookup(ServicoSessao.NOME);
        } catch (RemoteException | NotBoundException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            servico = null;
            jPanelPesquisa.setVisible(false);
            jLabelStatus.setText("Desconectado");
            JOptionPane.showMessageDialog(this, "Não foi possível conectar ao servidor: " + ex.getMessage());
            return;
        }
        jLabelStatus.setText("Conectado a " + jTextFieldHost.getText().trim() + ":" + porta);
        jPanelPesquisa.setVisible(true);
        jTextFieldNumeroCelularPesquisa.requestFocus();
        pack();
    }

    private void jButtonPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        if (jTextFieldNumeroCelularPesquisa.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Número de celular não informado");
            jTextFieldNumeroCelularPesquisa.requestFocus();
            return;
        }

        Sessao sessao;
        try {
            sessao = servico.buscarPorCelular(jTextFieldNumeroCelularPesquisa.getText());
        } catch (RemoteException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            jPanelPesquisa.setVisible(false);
            jLabelStatus.setText("Desconectado");
            JOptionPane.showMessageDialog(this, "Conexão com o servidor perdida: " + ex.getMessage());
            return;
        }
        if (sessao == null) {
            JOptionPane.showMessageDialog(this, "Número de celular não encontrado");
            jTextFieldNumeroCelularPesquisa.setText("");
            jTextAreaResultadoPesquisa.setText("");
            jTextFieldNumeroCelularPesquisa.requestFocus();
            return;
        }
        jTextAreaResultadoPesquisa.setText("Número do celular: " + sessao.getNumeroCelular()
                + System.lineSeparator() + "Dados da sessão: " + sessao.getDadosSessao());
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new JFrameCliente().setVisible(true));
    }

    private javax.swing.JButton jButtonConectar;
    private javax.swing.JButton jButtonPesquisar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JPanel jPanelPesquisa;
    private javax.swing.JTextArea jTextAreaResultadoPesquisa;
    private javax.swing.JTextField jTextFieldHost;
    private javax.swing.JTextField jTextFieldNumeroCelularPesquisa;
    private javax.swing.JTextField jTextFieldPorta;
}
