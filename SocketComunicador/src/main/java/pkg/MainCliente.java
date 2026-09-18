import controller.ClienteController;
import view.ClienteView;

import javax.swing.SwingUtilities;

/**
 * Classe principal do Cliente. Monta o MVC e exibe a tela.
 *
 * @author Andrisa Santos - Trabalho de Sistemas Distribuídos
 */
public class MainCliente {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClienteView view = new ClienteView();
            new ClienteController(view);
            view.setVisible(true);
        });
    }
}
