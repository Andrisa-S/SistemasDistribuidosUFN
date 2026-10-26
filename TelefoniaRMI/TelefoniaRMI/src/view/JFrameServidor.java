package view;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.TabelaHash;
import remoto.ServicoSessao;
import remoto.ServicoSessaoImpl;
import utilidades.Util;

public class JFrameServidor extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JFrameServidor.class.getName());

    TabelaHash hashSessoes;
    private Registry registry;
    private ServicoSessaoImpl servico;

    public JFrameServidor() {
        initComponents();
        hashSessoes = new TabelaHash();
        jPanelResultados.setVisible(false);
        jButtonIniciar.setEnabled(false);
        jButtonParar.setEnabled(false);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Telefonia - Servidor RMI");

        jLabel1 = new javax.swing.JLabel("BASE DE SESSÕES (SERVIDOR)");
        jLabel1.setFont(jLabel1.getFont().deriveFont(java.awt.Font.BOLD, 16f));

        jTextFieldCaminhoArquivo = new javax.swing.JTextField("Base.txt", 22);
        jButtonCarregarArquivo = new javax.swing.JButton("Carregar arquivo");
        jTextFieldIpServidor = new javax.swing.JTextField(10);
        jTextFieldPorta = new javax.swing.JTextField("1099", 5);
        jButtonIniciar = new javax.swing.JButton("Iniciar servidor");
        jButtonParar = new javax.swing.JButton("Parar");
        jLabelStatus = new javax.swing.JLabel("Servidor parado");

        jTextAreaDadosSessoes = new javax.swing.JTextArea(10, 45);
        jTextAreaDadosSessoes.setEditable(false);
        jTextAreaLog = new javax.swing.JTextArea(6, 45);
        jTextAreaLog.setEditable(false);

        jPanelResultados = new javax.swing.JPanel(new java.awt.GridLayout(2, 1, 0, 8));
        javax.swing.JScrollPane sp1 = new javax.swing.JScrollPane(jTextAreaDadosSessoes);
        sp1.setBorder(javax.swing.BorderFactory.createTitledBorder("Sessões carregadas"));
        javax.swing.JScrollPane sp2 = new javax.swing.JScrollPane(jTextAreaLog);
        sp2.setBorder(javax.swing.BorderFactory.createTitledBorder("Consultas recebidas"));
        jPanelResultados.add(sp1);
        jPanelResultados.add(sp2);

        javax.swing.JPanel linha1 = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linha1.add(jLabel1);
        javax.swing.JPanel linha2 = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linha2.add(new javax.swing.JLabel("Arquivo:"));
        linha2.add(jTextFieldCaminhoArquivo);
        linha2.add(jButtonCarregarArquivo);
        javax.swing.JPanel linha3 = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linha3.add(new javax.swing.JLabel("IP do servidor (opcional):"));
        linha3.add(jTextFieldIpServidor);
        linha3.add(new javax.swing.JLabel("Porta:"));
        linha3.add(jTextFieldPorta);
        javax.swing.JPanel linha4 = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        linha4.add(jButtonIniciar);
        linha4.add(jButtonParar);
        linha4.add(jLabelStatus);

        javax.swing.JPanel topo = new javax.swing.JPanel();
        topo.setLayout(new javax.swing.BoxLayout(topo, javax.swing.BoxLayout.Y_AXIS));
        topo.add(linha1);
        topo.add(linha2);
        topo.add(linha3);
        topo.add(linha4);

        javax.swing.JPanel conteudo = new javax.swing.JPanel(new java.awt.BorderLayout(0, 8));
        conteudo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        conteudo.add(topo, java.awt.BorderLayout.NORTH);
        conteudo.add(jPanelResultados, java.awt.BorderLayout.CENTER);
        setContentPane(conteudo);

        jButtonCarregarArquivo.addActionListener(evt -> jButtonCarregarArquivoActionPerformed(evt));
        jButtonIniciar.addActionListener(evt -> jButtonIniciarActionPerformed(evt));
        jButtonParar.addActionListener(evt -> jButtonPararActionPerformed(evt));
        pack();
    }

    private void jButtonCarregarArquivoActionPerformed(java.awt.event.ActionEvent evt) {
        StringBuilder dadosSessoes = new StringBuilder();
        if (jTextFieldCaminhoArquivo.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do arquivo!");
            jTextFieldCaminhoArquivo.requestFocus();
            return;
        }
        hashSessoes.sessoes.clear(); // recarregar não duplica dados
        String resposta = Util.carregarArquivoEmHashMap(jTextFieldCaminhoArquivo.getText(), hashSessoes.sessoes, dadosSessoes);
        if (!resposta.equals("")) {
            JOptionPane.showMessageDialog(this, resposta);
            jTextFieldCaminhoArquivo.setText("");
            return;
        }

        jTextAreaDadosSessoes.setText(dadosSessoes.toString());
        jPanelResultados.setVisible(true);
        jButtonIniciar.setEnabled(true);
        jLabelStatus.setText(hashSessoes.sessoes.size() + " sessões carregadas");
        pack();
    }

    private void jButtonIniciarActionPerformed(java.awt.event.ActionEvent evt) {
        int porta;
        try {
            porta = Integer.parseInt(jTextFieldPorta.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Porta inválida!");
            jTextFieldPorta.requestFocus();
            return;
        }
        String ip = jTextFieldIpServidor.getText().trim();
        if (!ip.isEmpty()) {
            // necessário quando o cliente está em outra máquina
            System.setProperty("java.rmi.server.hostname", ip);
        }
        try {
            servico = new ServicoSessaoImpl(hashSessoes.sessoes,
                    msg -> SwingUtilities.invokeLater(() -> jTextAreaLog.append(msg + System.lineSeparator())));
            registry = LocateRegistry.createRegistry(porta);
            registry.rebind(ServicoSessao.NOME, servico);
        } catch (RemoteException ex) {
            liberarRecursos();
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Erro ao iniciar o servidor: " + ex.getMessage());
            return;
        }
        jButtonIniciar.setEnabled(false);
        jButtonCarregarArquivo.setEnabled(false); // base não muda com o servidor no ar
        jButtonParar.setEnabled(true);
        jLabelStatus.setText("Servidor no ar na porta " + porta);
    }

    private void jButtonPararActionPerformed(java.awt.event.ActionEvent evt) {
        liberarRecursos();
        jButtonIniciar.setEnabled(true);
        jButtonCarregarArquivo.setEnabled(true);
        jButtonParar.setEnabled(false);
        jLabelStatus.setText("Servidor parado");
    }

    private void liberarRecursos() {
        try {
            if (registry != null) {
                try {
                    registry.unbind(ServicoSessao.NOME);
                } catch (NotBoundException ex) {
                    // já não estava registrado
                }
                UnicastRemoteObject.unexportObject(registry, true);
            }
            if (servico != null) {
                UnicastRemoteObject.unexportObject(servico, true);
            }
        } catch (RemoteException ex) {
            logger.log(java.util.logging.Level.WARNING, null, ex);
        }
        registry = null;
        servico = null;
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
        java.awt.EventQueue.invokeLater(() -> new JFrameServidor().setVisible(true));
    }

    private javax.swing.JButton jButtonCarregarArquivo;
    private javax.swing.JButton jButtonIniciar;
    private javax.swing.JButton jButtonParar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JPanel jPanelResultados;
    private javax.swing.JTextArea jTextAreaDadosSessoes;
    private javax.swing.JTextArea jTextAreaLog;
    private javax.swing.JTextField jTextFieldCaminhoArquivo;
    private javax.swing.JTextField jTextFieldIpServidor;
    private javax.swing.JTextField jTextFieldPorta;
}
