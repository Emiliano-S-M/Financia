package CanalDev.Financia.Utils;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import java.lang.reflect.Field;
import java.util.Map;

public class TablaColorUtil {

    /**
     * Aplica colores condicionales a una columna de la tabla.
     * @param columna    la columna donde se mostrará el color
     * @param referencia nombre del campo del modelo que se usará para comparar
     * @param reglas     mapa de reglas [texto -> color en hex o CSS] que definen el estilo
     */
    public static <T, V> void aplicarColores(TableColumn<T, V> columna,
                                             String referencia,
                                             Map<String, String> reglas) {

        columna.setCellFactory(col -> new TableCell<T, V>() {
            @Override
            protected void updateItem(V item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    T fila = getTableView().getItems().get(getIndex());
                    try {
                        // Se usa reflexión para obtener el valor del campo de referencia
                        Field field = fila.getClass().getDeclaredField(referencia);
                        field.setAccessible(true);
                        Object valor = field.get(fila);

                        if (valor != null) {
                            String color = reglas.get(valor.toString());
                            if (color != null) {
                                // Se aceptan valores hexadecimales (#RRGGBB) o cualquier valor CSS válido para configuracion
                                setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
                            } else {
                                setStyle("");
                            }
                        }
                    } catch (Exception e) {
                        setStyle("");
                    }
                }
            }
        });
    }
}
