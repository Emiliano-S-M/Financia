package CanalDev.Financia.Controlador;


import CanalDev.Financia.Modelo.MovimientoModelo;
import CanalDev.Financia.Modelo.TipoMovimiento;
import CanalDev.Financia.Presentacion.FinanciaFx;
import CanalDev.Financia.Servicio.ICategoriaServicio;
import CanalDev.Financia.Servicio.IMovimientoServicio;
import CanalDev.Financia.Utils.FiltroFechaUtil;
import CanalDev.Financia.Utils.MensajeUtil;
import CanalDev.Financia.Utils.SaludoUtil;
import CanalDev.Financia.Utils.TablaColorUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * Controlador para la ventana principal de balance financiero.
 * Se encarga de coordinar la interacción entre la interfaz gráfica (JavaFX)
 * y los servicios de negocio (Spring).
 * Responsabilidades:
 * - Mostrar la lista de movimientos en la tabla.
 * - Calcular y mostrar el balance acumulado.
 * - Abrir el formulario para registrar nuevos movimientos.
 * - Permitir la eliminación de movimientos desde la tabla.
 * - Delegar la lógica de negocio al servicio de movimientos.
 */
@Component
public class BalanceController implements Initializable {

    private FilteredList<MovimientoModelo> filteredData;

    /** Label para mostrar saludo dinámico */
    @FXML private Label saludoLabel;

    /** Label para mostrar el balance acumulado */
    @FXML private Label labelBalanceTotal;

    /** Label para mostrar el balance acumulado en el último mes */
    @FXML private Label labelBalanceMes;

    /** Label para mostrar los gastos acumulados en el último mes */
    @FXML private Label labelEgresoMes;

    /** Label para mostrar el saldo restante del mes */
    @FXML private Label labelSaldoMes;

    /** ComboBox para hacer de filtro en la tabla basado en el "tipo" que se quiere filtrar (Ingreso, Egreso) */
    @FXML private ComboBox<TipoMovimiento> tipoComboFiltro;

    @FXML private ComboBox<String> categoriaComboFiltro;

    @FXML private TextField BuscarTextFieldFiltro;

        @FXML private ComboBox<Integer> diaComboBoxFiltro;

    @FXML private ComboBox<Month> mesComboBoxFiltro;

    @FXML private  ComboBox<String> anioComboBoxFiltro;

    /** Tabla principal de movimientos */
    @FXML private TableView<MovimientoModelo> movimientosTabla;

    /** Columnas de la tabla */
    @FXML private TableColumn<MovimientoModelo, Integer> idColumna;
    @FXML private TableColumn<MovimientoModelo, Integer> tipoColumna;
    @FXML private TableColumn<MovimientoModelo, String> montoColumna;
    @FXML private TableColumn<MovimientoModelo, String> categoriaColumna;
    @FXML private TableColumn<MovimientoModelo, String> fechaColumna;
    @FXML private TableColumn<MovimientoModelo, Void> eliminarColumna;

    /**  Dependencias de negocio (inyectadas por Spring) */
    private final ICategoriaServicio categoriaServicio;
    private final IMovimientoServicio movimientoServicio;

    /** Lista observable que alimenta la tabla */
    private final ObservableList<MovimientoModelo> movimientosLista = FXCollections.observableArrayList();

    /**
     * Constructor con inyección de dependencias.
     * @param movimientoServicio servicio para gestionar movimientos financieros
     */
    public BalanceController(ICategoriaServicio categoriaServicio, IMovimientoServicio movimientoServicio) {
        this.movimientoServicio = movimientoServicio;
        this.categoriaServicio = categoriaServicio;
    }

    /**
     * Inicializa la ventana al cargarse el FXML.
     * Configura saludo, columnas, datos de la tabla y balance inicial.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb){
        saludo();
        configurarColumnas();
        llenarCombos();
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

        // Definir reglas con colores en hexadecimal
        Map<String, String> reglas = new HashMap<>();
        reglas.put("Ingreso", "#22C55E");
        reglas.put("Egreso", "#FF0000");

        // Aplicar colores a la columna de monto, usando tipoMovimiento como referencia
        TablaColorUtil.aplicarColores(montoColumna, "tipoMovimiento", reglas);
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
        movimientosLista.addAll(movimientoServicio.listarMovimientos());
        aplicarFiltros();
    }

    /** Calcula y muestra el balance total en el label correspondiente */
    private void actualizarBalance() {
        float total = movimientoServicio.calcularBalanceTotal();
        float totalUltimoMes = movimientoServicio.calcularBalanceMes(1 );
        float totalGastosMes = movimientoServicio.calcularGastosMes(1);
        float totalSaldoMes = movimientoServicio.calcularSaldoMes(1);

        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

        labelBalanceTotal.setText(formatoMoneda.format(total));
        labelBalanceMes.setText(formatoMoneda.format(totalUltimoMes));
        labelEgresoMes.setText(formatoMoneda.format(totalGastosMes));
        labelSaldoMes.setText(formatoMoneda.format(totalSaldoMes));
    }

    /**
     * Llena los combos de tipo y categoría, y configura el listener
     * para actualizar categorías según el tipo seleccionado.
     * Aplica los filtros seleccionados en la tabla
     */
    private void llenarCombos() {

        tipoComboFiltro.setItems(FXCollections.observableArrayList(TipoMovimiento.values()));
        FiltroFechaUtil.llenarFiltrosFecha(anioComboBoxFiltro, mesComboBoxFiltro, diaComboBoxFiltro);


        tipoComboFiltro.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(TipoMovimiento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getValor());
            }
        });
        tipoComboFiltro.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TipoMovimiento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getValor());
            }
        });
        tipoComboFiltro.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                categoriaComboFiltro.setItems(
                        FXCollections.observableArrayList(
                                categoriaServicio.obtenerCategorias(newVal.getValor())
                        )
                );
                categoriaComboFiltro.getSelectionModel().selectFirst();
            }
        });
        tipoComboFiltro.setValue(TipoMovimiento.TODO);

        filteredData = new FilteredList<>(movimientosLista, p -> true);
        movimientosTabla.setItems(filteredData);

        tipoComboFiltro.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        categoriaComboFiltro.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        BuscarTextFieldFiltro.textProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        anioComboBoxFiltro.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        mesComboBoxFiltro.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());
        diaComboBoxFiltro.valueProperty().addListener((obs, oldVal, newVal) -> aplicarFiltros());

    }


    private void aplicarFiltros() {
        TipoMovimiento tipoSeleccionado = tipoComboFiltro.getValue();
        String categoriaSeleccionada = categoriaComboFiltro.getValue();
        String textoBusqueda = BuscarTextFieldFiltro.getText();
        String anioSeleccionado = anioComboBoxFiltro.getValue();
        Month mesSeleccionado = mesComboBoxFiltro.getValue();
        Integer diaSeleccionado = diaComboBoxFiltro.getValue();

        filteredData.setPredicate(movimiento -> {

            LocalDate fecha = movimiento.getFechaMovimiento();

            // Filtro por tipo
            if (tipoSeleccionado != null && tipoSeleccionado != TipoMovimiento.TODO) {
                if (!movimiento.getTipoMovimiento().equalsIgnoreCase(tipoSeleccionado.getValor())) {
                    return false;
                }
            }

            // Filtro por categoría (solo si no es TODO y hay categoría seleccionada)
            if (tipoSeleccionado != TipoMovimiento.TODO && categoriaSeleccionada != null) {
                if (!movimiento.getCategoriaMovimiento().equalsIgnoreCase(categoriaSeleccionada)) {
                    return false;
                }
            }

            // Filtro por texto libre (ID, monto, categoría, fecha)
            if (textoBusqueda != null && !textoBusqueda.isBlank()) {
                String lower = textoBusqueda.toLowerCase();

                boolean coincide =
                        String.valueOf(movimiento.getIdMovimiento()).contains(lower) ||
                                String.valueOf(movimiento.getMontoMovimiento()).contains(lower) ||
                                movimiento.getCategoriaMovimiento().toLowerCase().contains(lower) ||
                                movimiento.getTipoMovimiento().toLowerCase().contains(lower) ||
                                movimiento.getFechaMovimiento().toString().contains(lower);

                if (!coincide) {
                    return false;
                }
            }

            if (anioSeleccionado != null) {
                if (fecha.getYear() != Integer.parseInt(anioSeleccionado)) return false;
            }

            if (mesSeleccionado != null) {
                if (!fecha.getMonth().equals(mesSeleccionado)) return false;
            }

            if (diaSeleccionado != null) {
                if (fecha.getDayOfMonth() != diaSeleccionado) return false;
            }

            // Si pasa todos los filtros, se muestra
            return true;
        });
    }


}
