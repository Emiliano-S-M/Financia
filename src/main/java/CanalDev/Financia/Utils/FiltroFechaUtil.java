package CanalDev.Financia.Utils;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;

public class FiltroFechaUtil {

    public static void llenarFiltrosFecha(ComboBox<String> comboAnio,
                                          ComboBox<Month> comboMes,
                                          ComboBox<Integer> comboDia) {

        int anioActual = LocalDate.now().getYear();

        // =========================
        // 🔹 AÑOS (null = Todos)
        // =========================
        comboAnio.getItems().clear();
        comboAnio.getItems().add(null); // Todos

        for (int i = anioActual - 5; i <= anioActual; i++) {
            comboAnio.getItems().add(String.valueOf(i));
        }

        comboAnio.setValue(null);

        // Render
        comboAnio.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : (item == null ? "Todos" : item));
            }
        });

        comboAnio.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Todos" : item);
            }
        });

        // =========================
        // 🔹 MESES (null = Todos)
        // =========================
        var meses = FXCollections.<Month>observableArrayList();
        meses.add(null); // Todos
        meses.addAll(Month.values());

        comboMes.setItems(meses);
        comboMes.setValue(null);

        comboMes.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Month item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        (item == null ? "Todos" :
                                item.getDisplayName(TextStyle.FULL, new Locale("es", "MX"))));
            }
        });

        comboMes.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Month item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Todos" :
                        item.getDisplayName(TextStyle.FULL, new Locale("es", "MX")));
            }
        });

        // =========================
        // 🔹 DÍAS (null = Todos)
        // =========================
        comboDia.getItems().clear();
        comboDia.getItems().add(null); // Todos

        for (int i = 1; i <= 31; i++) {
            comboDia.getItems().add(i);
        }

        comboDia.setValue(null);

        comboDia.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : (item == null ? "Todos" : item.toString()));
            }
        });

        comboDia.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "Todos" : item.toString());
            }
        });

        // =========================
        // 🔥 LISTENERS
        // =========================
        comboMes.valueProperty().addListener((obs, oldVal, newVal) ->
                actualizarDias(comboAnio, comboMes, comboDia)
        );

        comboAnio.valueProperty().addListener((obs, oldVal, newVal) ->
                actualizarDias(comboAnio, comboMes, comboDia)
        );
    }

    private static void actualizarDias(ComboBox<String> comboAnio,
                                       ComboBox<Month> comboMes,
                                       ComboBox<Integer> comboDia) {

        Integer diaActual = comboDia.getValue(); // 🔥 guardar selección

        comboDia.getItems().clear();
        comboDia.getItems().add(null); // Todos

        Month mes = comboMes.getValue();
        String anioStr = comboAnio.getValue();

        int maxDias = 31;

        if (mes != null && anioStr != null) {
            int anio = Integer.parseInt(anioStr);
            maxDias = mes.length(Year.isLeap(anio));
        }

        for (int i = 1; i <= maxDias; i++) {
            comboDia.getItems().add(i);
        }

        // 🔥 restaurar selección si sigue siendo válida
        if (diaActual != null && diaActual <= maxDias) {
            comboDia.setValue(diaActual);
        } else {
            comboDia.setValue(null);
        }
    }
}