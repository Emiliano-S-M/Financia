package CanalDev.Financia.Servicio;

import CanalDev.Financia.Modelo.MovimientoModelo;

import java.util.List;

/**
 * Interfaz para el servicio de movimientos financieros.
 * Define el contrato que deben cumplir las implementaciones
 * encargadas de gestionar operaciones sobre los movimientos.

 * Responsabilidades:
 * - Listar todos los movimientos registrados.
 * - Guardar un nuevo movimiento.
 * - Eliminar movimientos por objeto o por ID.
 * - Calcular el balance total.
 * - Preparar un movimiento aplicando reglas de negocio (ej. monto positivo/negativo).
 */
public interface IMovimientoServicio {

    /**
     * Obtiene todos los movimientos registrados.
     * @return lista de movimientos
     */
    public List<MovimientoModelo> listarMovimientos();

    /**
     * Obtiene todos los movimientos registrados en los últimos 6 meses.
     * @return lista de movimientos de los últimos 6 meses
     */
    public List<MovimientoModelo> listarMovimientosMes(Integer mes);

    /**
     * Guarda un movimiento en la base de datos.
     * @param movimientoModelo modelo a guardar
     * @return movimiento guardado con su ID asignado
     */
    public MovimientoModelo guardarMovimiento(MovimientoModelo movimientoModelo);

    /**
     * Elimina un movimiento específico.
     * @param movimientoModelo modelo a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarMovimiento(MovimientoModelo movimientoModelo);

    /**
     * Elimina un movimiento por su ID.
     * @param id identificador del movimiento
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminarMovimientoPorId(Integer id);

    /**
     * Calcula el balance total sumando todos los montos.
     * @return balance acumulado
     */
    public float calcularBalanceTotal();

    /**
     * Calcula el balance total de los meses especificados, sumando todos los montos.
     * @return balance acumulado de los meses especificados
     */
    public float calcularBalanceMes(int meses);

    /**
     * Calcula el balance total de los meses especificados, sumando todos los montos.
     * @return balance acumulado de los meses especificados
     */
    public float calcularGastosMes(int meses);

    /**
     * Calcula el balance total de los meses especificados, sumando todos los montos.
     * @return balance acumulado de los meses especificados
     */
    public float calcularSaldoMes(int meses);

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
    MovimientoModelo prepararMovimiento(MovimientoModelo movimiento, String tipo, String montoTexto);
}
