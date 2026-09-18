import controller.ServidorController;
import model.CadastroAcademico;
import view.ServidorView;

import javax.swing.SwingUtilities;

/**
 * Classe principal do Servidor. Monta o MVC e inicia o servidor.
 *
 * @author Andrisa Santos - Trabalho de Sistemas Distribuídos
 */
public class MainServidor {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServidorView view = new ServidorView();
            CadastroAcademico cadastro = new CadastroAcademico();
            ServidorController controller = new ServidorController(cadastro, view);

            view.setVisible(true);
            controller.iniciar();
        });
    }
}
