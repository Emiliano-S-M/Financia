package CanalDev.Financia.Controlador;

import CanalDev.Financia.Modelo.MovimientoModelo;
import CanalDev.Financia.Servicio.ICategoriaServicio;
import CanalDev.Financia.Servicio.IMovimientoServicio;
import CanalDev.Financia.Utils.FormatoMonedaUtil;
import CanalDev.Financia.Utils.MensajeUtil;
import CanalDev.Financia.Utils.AlertUtil;
import CanalDev.Financia.Utils.ValidacionUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import CanalDev.Financia.Modelo.TipoMovimiento;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de creación de un nuevo movimiento financiero.
 * Se encarga de coordinar la interacción entre la interfaz gráfica (JavaFX)
 * y los servicios de negocio (Spring).

 * Responsabilidades:
 * - Validar campos de entrada.
 * - Delegar la lógica de negocio a los servicios.
 * - Mostrar mensajes de éxito o error.
 * - Cerrar la ventana cuando corresponde.
 */

@Component
public class nuevoMovimientoController implements Initializable {

    /** Acumulador de mensajes de validación */
    StringBuilder mensaje = new StringBuilder();

    /** Campos de la interfaz gráfica (inyectados por FXML) */
    @FXML private TextField montoTexto;
    @FXML private ComboBox<TipoMovimiento> tipoCombo;
    @FXML private ComboBox<String> categoriaCombo;
    @FXML private DatePicker fechaDate;
    @FXML private Button botonGuardar;
    @FXML private Button botonCancelar;

    /**  Dependencias de negocio (inyectadas por Spring) */
    private final ICategoriaServicio categoriaServicio;
    private final IMovimientoServicio movimientoServicio;

    /**
     * Constructor con inyección de dependencias.
     * @param categoriaServicio servicio para obtener categorías según tipo de movimiento
     * @param movimientoServicio servicio para gestionar movimientos financieros
     */
    public nuevoMovimientoController(ICategoriaServicio categoriaServicio, IMovimientoServicio movimientoServicio) {
        this.categoriaServicio = categoriaServicio;
        this.movimientoServicio = movimientoServicio;
    }

    /**
     * Inicializa la ventana al cargarse el FXML.
     * @param url recurso de inicialización
     * @param rb bundle de recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        formatearCampos();
    }

    /**
     * Acción al presionar el botón Guardar.
     * Valida los campos, construye el modelo y lo guarda mediante el servicio.
     */
    @FXML
    private void guardarMovimiento(){
        if(comprobarCampos()){
            MovimientoModelo movimientoModelo = new MovimientoModelo();
            obtenerDatos(movimientoModelo);
            MovimientoModelo guardar = movimientoServicio.guardarMovimiento(movimientoModelo);
            if(guardar.getIdMovimiento() != null){
                MensajeUtil.mostrarInfo("Éxito", "Movimiento guardado correctamente");
                ((Stage) botonGuardar.getScene().getWindow()).close();
            } else{
                MensajeUtil.mostrarError("Error", "No se pudo registrar el movimiento");
            }
        }
    }

    /**
     * Acción al presionar el botón Cancelar.
     * Muestra un cuadro de confirmación y cierra la ventana si el usuario acepta.
     */
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

    /**
     * Obtiene los datos de la interfaz y los asigna al modelo.
     * @param movimientoModelo modelo de movimiento a llenar
     */
    private void obtenerDatos(MovimientoModelo movimientoModelo) {
        movimientoModelo.setCategoriaMovimiento(categoriaCombo.getValue());
        movimientoModelo.setFechaMovimiento(String.valueOf(fechaDate.getValue()));

        // Delegamos el cálculo de monto y tipo al servicio
        movimientoServicio.prepararMovimiento(
                movimientoModelo,
                tipoCombo.getValue().getValor(),
                montoTexto.getText()
        );
    }

    /**
     * Aplica formato tipo moneda a los campos de entrada y llena los combos iniciales.
     */
    private void formatearCampos(){
        FormatoMonedaUtil.aplicarFormatoDecimal(montoTexto);
        llenarCombos();
    }

    /**
     * Llena los combos de tipo y categoría, y configura el listener
     * para actualizar categorías según el tipo seleccionado.
     */
    private void llenarCombos() {

        tipoCombo.setItems(FXCollections.observableArrayList(TipoMovimiento.values()));

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
        tipoCombo.setValue(TipoMovimiento.INGRESO);
    }

    /**
     * Verifica que los campos obligatorios no estén vacíos o nulos.
     * @return true si todos los campos son válidos, false en caso contrario
     */
    private boolean comprobarCampos(){
        if (ValidacionUtil.objetoNulo(tipoCombo.getValue())) {
            mensaje.append("Ocurrió un error al obtener el tipo de movimiento\n");
        }
        if (ValidacionUtil.campoVacio(montoTexto.getText())) {
            mensaje.append("Ingresa la cantidad implicada en el movimiento\n");
        }
        if (ValidacionUtil.objetoNulo(categoriaCombo.getValue())) {
            mensaje.append("Ocurrió un error al obtener la categoría\n");
        }
        if (ValidacionUtil.objetoNulo(fechaDate.getValue())) {
            mensaje.append("Selecciona la fecha de movimiento\n");
        }
        if (!mensaje.isEmpty()) {
            MensajeUtil.mostrarError("Campos Vacíos", mensaje.toString());
            mensaje.setLength(0);
            return false;
        } else {
            mensaje.setLength(0);
            return true;
        }
    }
}

