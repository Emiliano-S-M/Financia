package CanalDev.Financia.Servicio;

import CanalDev.Financia.Modelo.MovimientoModelo;
import CanalDev.Financia.Repositorio.MovimientoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static CanalDev.Financia.Utils.MensajeUtil.mostrarError;

/**
 * Implementación del servicio de movimientos financieros.
 * Gestiona operaciones CRUD sobre los movimientos,
 * delegando el acceso a datos al repositorio.

 * Responsabilidades:
 * - Listar todos los movimientos.
 * - Guardar un nuevo movimiento.
 * - Eliminar movimientos por objeto o por ID.
 * - Calcular el balance total.
 * - Preparar un movimiento aplicando reglas de negocio (ej. monto positivo/negativo).
 */
@Service
public class MovimientoServicio implements IMovimientoServicio{

    /** Repositorio para acceder a la persistencia de movimientos */
    @Autowired
    private MovimientoRepositorio movimientoRepositorio;

    /**
     * Obtiene todos los movimientos registrados.
     * @return lista de movimientos
     */
    @Override
    public List<MovimientoModelo> listarMovimientos() {
        return movimientoRepositorio.findAll().reversed();
    }

    @Override
    public List<MovimientoModelo> listarMovimientosMes(Integer meses){
        // Vista simplificada: solo últimos 6 meses
        LocalDate inicio = LocalDate.now()
                .withDayOfMonth(1) // primer día del mes actual
                .minusMonths(meses - 1);
        return movimientoRepositorio.findByFechaMovimientoAfterOrderByIdMovimientoDesc(inicio);
    }

    /**
     * Guarda un movimiento en la base de datos.
     * @param movimiento modelo a guardar
     * @return movimiento guardado con su ID asignado
     */
    @Override
    public MovimientoModelo guardarMovimiento(MovimientoModelo movimiento) {
        return movimientoRepositorio.save(movimiento);
    }

    /**
     * Elimina un movimiento específico.
     * @param movimiento modelo a eliminar
     * @return true si se eliminó correctamente, false en caso de error
     */
    @Override
    public boolean eliminarMovimiento(MovimientoModelo movimiento) {
        try {
            movimientoRepositorio.delete(movimiento);
            return true;
        } catch (Exception e) {
            mostrarError("Error", "No se pudo eliminar movimiento en la base de datos");
            return false;
        }
    }

    /**
     * Elimina un movimiento por su ID.
     * @param id identificador del movimiento
     * @return true si se eliminó correctamente, false en caso de error
     */
    @Override
    public boolean eliminarMovimientoPorId(Integer id){
        try {
            movimientoRepositorio.deleteById(id);
            return true;
        } catch (Exception e) {
            mostrarError("Error", "No se pudo eliminar movimiento en la base de datos");
            return false;
        }

    }

    /**
     * Calcula el balance total sumando todos los montos.
     * @return balance acumulado
     */
    @Override
    public float calcularBalanceTotal(){
        return movimientoRepositorio.findAll()
                .stream()
                .map(MovimientoModelo::getMontoMovimiento)
                .reduce(0f, Float::sum);
    }

    @Override
    public float calcularBalanceMes(int meses) {
        LocalDate inicio = LocalDate.now()
                .withDayOfMonth(1) // primer día del mes actual
                .minusMonths(meses - 1);

        return movimientoRepositorio.findByFechaMovimientoAfterOrderByIdMovimientoDesc(inicio)
                .stream()
                .map(MovimientoModelo::getMontoMovimiento)
                .filter(monto -> monto > 0)
                .reduce(0f, Float::sum);
    }

    @Override
    public float calcularGastosMes(int meses) {
        LocalDate inicio = LocalDate.now()
                .withDayOfMonth(1) // primer día del mes actual
                .minusMonths(meses - 1);

        return movimientoRepositorio.findByFechaMovimientoAfterOrderByIdMovimientoDesc(inicio)
                .stream()
                .map(MovimientoModelo::getMontoMovimiento)
                .filter(monto -> monto < 0)
                .reduce(0f, Float::sum);
    }

    @Override
    public float calcularSaldoMes(int meses) {
        LocalDate inicio = LocalDate.now()
                .withDayOfMonth(1) // primer día del mes actual
                .minusMonths(meses - 1);

        return movimientoRepositorio.findByFechaMovimientoAfterOrderByIdMovimientoDesc(inicio)
                .stream()
                .map(MovimientoModelo::getMontoMovimiento)
                .reduce(0f, Float::sum);
    }

    /**
     * Prepara un movimiento aplicando reglas de negocio:
     * - Si es ingreso, el monto se mantiene positivo.
     * - Si es egreso, el monto se convierte en negativo.
     *
     * @param movimiento modelo a preparar
     * @param tipo tipo de movimiento (Ingreso/Egreso)
     * @param montoTexto monto en formato texto
     * @return modelo preparado con monto y tipo asignados
     */
    @Override
    public MovimientoModelo prepararMovimiento(MovimientoModelo movimiento, String tipo, String montoTexto) {
        float monto = Float.parseFloat(montoTexto);
        if ("Egreso".equals(tipo)) {
            monto *= -1;
        }
        movimiento.setMontoMovimiento(monto);
        movimiento.setTipoMovimiento(tipo);
        return movimiento;
    }
}
