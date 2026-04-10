package CanalDev.Financia.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utilidad para mostrar mensajes en la interfaz gráfica.
 * Proporciona métodos estáticos para mostrar alertas de información y error.
 */
public class MensajeUtil {

    /**
     * Muestra un mensaje informativo en un cuadro de diálogo.
     *
     * @param titulo título de la ventana de alerta
     * @param mensaje contenido del mensaje a mostrar
     */
    public static void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de error en un cuadro de diálogo.
     *
     * @param titulo título de la ventana de alerta
     * @param mensaje contenido del mensaje a mostrar
     */
    public static void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
