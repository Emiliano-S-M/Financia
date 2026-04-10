package CanalDev.Financia.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utilidad para mostrar cuadros de diálogo de confirmación en la interfaz.
 * Permite al usuario aceptar o cancelar una acción mediante botones personalizados.
 */
public class AlertUtil {

    /**
     * Muestra un cuadro de confirmación con dos opciones.
     *
     * @param titulo título de la ventana de alerta
     * @param mensaje mensaje principal a mostrar
     * @param textoSi texto del botón de confirmación (ej. "Sí")
     * @param textoNo texto del botón de cancelación (ej. "No")
     * @return true si el usuario selecciona la opción de confirmación, false en caso contrario
     */
    public static boolean confirmar(String titulo, String mensaje, String textoSi, String textoNo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        ButtonType btnSi = new ButtonType(textoSi);
        ButtonType btnNo = new ButtonType(textoNo);

        alert.getButtonTypes().setAll(btnSi, btnNo);

        Optional<ButtonType> resultado = alert.showAndWait();

        return resultado.isPresent() && resultado.get() == btnSi;
    }
}
