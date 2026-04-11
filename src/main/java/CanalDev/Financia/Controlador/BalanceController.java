package CanalDev.Financia.Controlador;


import CanalDev.Financia.Modelo.MovimientoModelo;
import CanalDev.Financia.Presentacion.FinanciaFx;
import CanalDev.Financia.Servicio.IMovimientoServicio;
import CanalDev.Financia.Utils.MensajeUtil;
import CanalDev.Financia.Utils.SaludoUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana principal de balance financiero.
 * Se encarga de coordinar la interacción entre la interfaz gráfica (JavaFX)
 * y los servicios de negocio (Spring).
 *
 * Responsabilidades:
 * - Mostrar la lista de movimientos en la tabla.
 * - Calcular y mostrar el balance acumulado.
 * - Abrir el formulario para registrar nuevos movimientos.
 * - Permitir la eliminación de movimientos desde la tabla.
 * - Delegar la lógica de negocio al servicio de movimientos.
 */
@Component
public class BalanceController implements Initializable {

    /** Servicio de movimientos financieros (inyectado por Spring) */
    private final IMovimientoServicio movimientoServicio;

    /** Label para mostrar saludo dinámico */
    @FXML private Label saludoLabel;

    /** Label para mostrar el balance acumulado */
    @FXML private Label labelBalance;

    /** Tabla principal de movimientos */
    @FXML private TableView<MovimientoModelo> movimientosTabla;

    /** Columnas de la tabla */
    @FXML private TableColumn<MovimientoModelo, Integer> idColumna;
    @FXML private TableColumn<MovimientoModelo, Integer> tipoColumna;
    @FXML private TableColumn<MovimientoModelo, String> montoColumna;
    @FXML private TableColumn<MovimientoModelo, String> categoriaColumna;
    @FXML private TableColumn<MovimientoModelo, String> fechaColumna;
    @FXML private TableColumn<MovimientoModelo, Void> eliminarColumna;

    /** Lista observable que alimenta la tabla */
    private final ObservableList<MovimientoModelo> movimientosLista = FXCollections.observableArrayList();

    /**
     * Constructor con inyección de dependencias.
     * @param movimientoServicio servicio para gestionar movimientos financieros
     */
    public BalanceController(IMovimientoServicio movimientoServicio) {
        this.movimientoServicio = movimientoServicio;
    }

    /**
     * Inicializa la ventana al cargarse el FXML.
     * Configura saludo, columnas, datos de la tabla y balance inicial.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        saludo();
        configurarColumnas();
        cargarTabla();
        actualizarBalance();
    }

    /** Genera y muestra un saludo dinámico en la interfaz */
    private void saludo(){
        saludoLabel.setText(SaludoUtil.generarSaludo());
    }

    /**
     * Acción al presionar el botón "Agregar Movimiento".
     * Abre un formulario modal para registrar un nuevo movimiento.
     * Tras cerrarse, actualiza la tabla y el balance.
     */
    @FXML
    private void agregarMovimiento() {
        try {
            // Cargar el FXML del modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/nuevoMovimiento.fxml"));
            loader.setControllerFactory(FinanciaFx.getContext()::getBean);
            Parent root = loader.load();

            Stage modal = new Stage();
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.setTitle("Nuevo Movimiento");
            modal.setResizable(false);
            modal.setScene(new Scene(root));
            modal.showAndWait();
            cargarTabla();
            actualizarBalance();
        } catch (Exception e) {
            MensajeUtil.mostrarError("Error", "No se puede abrir el formulario para registrar Nuevo Movimiento");
        }
    }

    /** Configura todas las columnas de la tabla */
    private void configurarColumnas(){
        configurarColumnasDatos();
        configurarColumnaEliminar();
    }

    /** Configura las columnas de datos (id, tipo, monto, categoría, fecha) */
    private void configurarColumnasDatos(){
        idColumna.setCellValueFactory(new PropertyValueFactory<>("idMovimiento"));
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipoMovimiento"));
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("montoMovimiento"));
        categoriaColumna.setCellValueFactory(new PropertyValueFactory<>("categoriaMovimiento"));
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fechaMovimiento"));
    }

    /** Configura la columna de eliminación con un botón por fila */
    private void configurarColumnaEliminar() {
        eliminarColumna.setCellFactory(col -> new TableCell<>() {
            private final Button btn = crearBotonEliminar(col);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    /**
     * Crea el botón de eliminar con ícono de basura.
     * @param col columna donde se insertará el botón
     * @return botón configurado
     */
    private Button crearBotonEliminar(TableColumn<MovimientoModelo, Void> col) {
        ImageView icono = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Imagenes/BoteBasura.png")))
        );
        icono.setPreserveRatio(true);
        icono.setFitWidth(16);
        icono.setFitHeight(16);

        Button btn = new Button();
        btn.setGraphic(icono);
        btn.setContentDisplay(ContentDisplay.CENTER);
        btn.prefWidthProperty().bind(col.widthProperty());

        btn.setOnAction(e -> manejarEliminar(btn));

        return btn;
    }

    /**
     * Maneja la acción de eliminar un movimiento desde la tabla.
     * @param btn botón que disparó la acción
     */
    private void manejarEliminar(Button btn) {
        TableCell<?, ?> cell = (TableCell<?, ?>) btn.getParent();
        MovimientoModelo movimiento = (MovimientoModelo) cell.getTableView()
                .getItems()
                .get(cell.getIndex());

        if (movimientoServicio.eliminarMovimiento(movimiento)) {
            movimientosLista.remove(movimiento);
            actualizarBalance();
        } else {
          MensajeUtil.mostrarError("Error", "No se pudo eliminar el movimiento");
        }
    }

    /** Carga los movimientos desde el servicio y los muestra en la tabla */
    private void cargarTabla(){
        movimientosLista.clear();
        movimientosLista.addAll(movimientoServicio.listarMovimientos());
        movimientosTabla.setItems(movimientosLista);
    }

    /** Calcula y muestra el balance total en el label correspondiente */
    private void actualizarBalance() {
        float total = movimientoServicio.calcularBalance();
        labelBalance.setText("$ " + total);
    }
}
