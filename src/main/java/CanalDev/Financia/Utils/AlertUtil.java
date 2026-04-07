package CanalDev.Financia.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtil {
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
