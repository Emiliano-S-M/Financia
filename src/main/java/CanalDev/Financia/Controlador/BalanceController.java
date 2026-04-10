package CanalDev.Financia.Controlador;


import CanalDev.Financia.Modelo.MovimientoModelo;
import CanalDev.Financia.Presentacion.FinanciaFx;
import CanalDev.Financia.Servicio.MovimientoServicio;
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
import java.util.ResourceBundle;

@Component
public class BalanceController implements Initializable {

    private final MovimientoServicio movimientoServicio;

    @FXML
    private Label saludoLabel;

    @FXML
    private Label labelBalance;

    @FXML
    private TableView<MovimientoModelo> movimientosTabla;

    @FXML
    private TableColumn<MovimientoModelo, Integer> idColumna;

    @FXML
    private TableColumn<MovimientoModelo, Integer> tipoColumna;

    @FXML
    private TableColumn<MovimientoModelo, String> montoColumna;

    @FXML
    private TableColumn<MovimientoModelo, String> categoriaColumna;

    @FXML
    private TableColumn<MovimientoModelo, String> fechaColumna;

    @FXML
    private TableColumn<MovimientoModelo, Void> eliminarColumna;

    private final ObservableList<MovimientoModelo> movimientosLista = FXCollections.observableArrayList();

    public BalanceController(MovimientoServicio movimientoServicio) {
        this.movimientoServicio = movimientoServicio;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        saludo();
        configurarColumnas();
        cargarTabla();
        actualizarBalance();
    }

    private void saludo(){
        saludoLabel.setText(SaludoUtil.generarSaludo());
    }

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

    private void configurarColumnas(){
        configurarColumnasDatos();
        configurarColumnaEliminar();
    }

    private void configurarColumnasDatos(){
        idColumna.setCellValueFactory(new PropertyValueFactory<>("idMovimiento"));
        tipoColumna.setCellValueFactory(new PropertyValueFactory<>("tipoMovimiento"));
        montoColumna.setCellValueFactory(new PropertyValueFactory<>("montoMovimiento"));
        categoriaColumna.setCellValueFactory(new PropertyValueFactory<>("categoriaMovimiento"));
        fechaColumna.setCellValueFactory(new PropertyValueFactory<>("fechaMovimiento"));
    }

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

    private Button crearBotonEliminar(TableColumn<MovimientoModelo, Void> col) {
        ImageView icono = new ImageView(
                new Image(getClass().getResourceAsStream("/Imagenes/BoteBasura.png"))
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

    private void cargarTabla(){
        movimientosLista.clear();
        movimientosLista.addAll(movimientoServicio.listarMovimientos());
        movimientosTabla.setItems(movimientosLista);
    }

    private void actualizarBalance() {
        float total = movimientoServicio.calcularBalance();
        labelBalance.setText("$ " + total);
    }
}
