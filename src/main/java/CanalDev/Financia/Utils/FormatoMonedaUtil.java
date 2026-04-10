package CanalDev.Financia.Utils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

/**
 * Utilidad para aplicar formato decimal en campos de texto.
 * Restringe la entrada a números con hasta dos decimales,
 * útil para representar montos de dinero en formularios.
 */
public class FormatoMonedaUtil {
    /**
     * Aplica un formato decimal a un TextField.
     * Solo permite números con hasta dos decimales.
     *
     * @param textField campo de texto al que se aplicará el formato
     */
    public static void aplicarFormatoDecimal(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change; // aceptar
            } else {
                return null;   // rechazar
            }
        });
        textField.setTextFormatter(formatter);
    }
}
