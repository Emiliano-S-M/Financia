package CanalDev.Financia.Controlador;

import CanalDev.Financia.Modelo.MovimientoModelo;
import CanalDev.Financia.Servicio.CategoriaServicio;
import CanalDev.Financia.Servicio.MovimientoServicio;
import CanalDev.Financia.Utils.MensajeUtil;
import CanalDev.Financia.Utils.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import CanalDev.Financia.Modelo.TipoMovimiento;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class nuevoMovimientoController implements Initializable {

    StringBuilder mensaje = new StringBuilder();

    @Autowired
    private MovimientoServicio movimientoServicio;

    @FXML
    private TextField montoTexto;

    @FXML
    private ComboBox<TipoMovimiento> tipoCombo;

    @FXML
    private ComboBox<String> categoriaCombo;

    @FXML
    private DatePicker fechaDate;

    @FXML
    private Button botonGuardar;

    @FXML
    private Button botonCancelar;

    private final CategoriaServicio categoriaServicio = new CategoriaServicio();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatearCampos();
    }

    @FXML
    private void guardarMovimiento(){
        if(comprobarCampos()){
            MovimientoModelo movimientoModelo = new MovimientoModelo();
            obtenerDatos(movimientoModelo);
            MovimientoModelo guardar = movimientoServicio.guardarMovimiento(movimientoModelo);
            if(guardar.getIdMovimiento() != null){
                MensajeUtil.mostrarInfo("Exito", "Movimiento guardado correctamente");
                ((Stage) botonGuardar.getScene().getWindow()).close();
            } else{
                MensajeUtil.mostrarError("Error", "No se pudo registrar el movimiento");
            }
        }
    }

    @FXML
    private void cancelarMovimiento(){
        boolean confirmar = AlertUtil.confirmar(
                "Salir",
                "¿Seguro que deseas cancelar el movimiento?\nSe perderán los datos no guardados.",
                "Sí, salir",
                "Cancelar"
        );

        if (confirmar) {
            ((Stage) botonCancelar.getScene().getWindow()).close();
        }
    }

    private void obtenerDatos(MovimientoModelo movimientoModelo) {
        movimientoModelo.setCategoriaMovimiento(categoriaCombo.getValue());
        String tipoMovimiento = tipoCombo.getValue().getValor();

        if(tipoMovimiento.equals("Ingreso")){
            movimientoModelo.setMontoMovimiento(Float.parseFloat(montoTexto.getText()));
        } else {
            movimientoModelo.setMontoMovimiento(Float.parseFloat(montoTexto.getText()) * -1);
        }
        movimientoModelo.setTipoMovimiento(tipoCombo.getValue().getValor());
        movimientoModelo.setFechaMovimiento(String.valueOf(fechaDate.getValue()));
    }

    private void formatearCampos(){
        aplicarFormatoDecimal(montoTexto);
        llenarCombos();
    }

    public void aplicarFormatoDecimal(TextField textField) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d{0,2})?")) {
                return change; // aceptar
            } else {
                return null;   // rechazar
            }
        });
        textField.setTextFormatter(formatter);
    }

    private void llenarCombos() {
        // Llenar con enum directamente
        tipoCombo.setItems(FXCollections.observableArrayList(TipoMovimiento.values()));
        // Mostrar texto bonito
        tipoCombo.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(TipoMovimiento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getValor());
            }
        });
        tipoCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipoMovimiento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getValor());
            }
        });
        // Listener
        tipoCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                categoriaCombo.setItems(
                        FXCollections.observableArrayList(
                                categoriaServicio.obtenerCategorias(newVal.getValor())
                        )
                );
                categoriaCombo.getSelectionModel().selectFirst();
            }
        });
        // Valor inicial (IMPORTANTE: ahora es el enum, no String)
        tipoCombo.setValue(TipoMovimiento.INGRESO);
    }

    private boolean comprobarCampos(){
        if (tipoCombo.getValue() == null) {
            mensaje.append("Ocurrio un error al obtener el tipo de movimiento\n");
        }
        if (montoTexto.getText().isEmpty()){
            mensaje.append("Ingresa la candidad implicada en el movimiento\n");
        }
        if (categoriaCombo.getValue() == null) {
            mensaje.append("Ocurrio un error al obtener la categoria\n");
        }
        if (fechaDate.getValue() == null){
            mensaje.append("Selecciona la fecha de movimiento");
        }
        if (!mensaje.isEmpty()){
            MensajeUtil.mostrarError("Campos Vacios",mensaje.toString());
            mensaje.setLength(0);
            return false;
        } else{
            mensaje.setLength(0);
            return true;
        }
    }
}
