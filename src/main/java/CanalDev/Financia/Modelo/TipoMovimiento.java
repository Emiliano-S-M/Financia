package CanalDev.Financia.Modelo;

/**
 * Enumeración que representa los tipos de movimiento financiero.
 *
 * Responsabilidades:
 * - Definir los valores posibles de un movimiento: Ingreso o Egreso.
 * - Asociar cada valor con su representación en texto.
 *
 * Notas:
 * - Se utiliza en la interfaz gráfica para llenar combos.
 * - El valor "Egreso" implica que el monto se guarda como negativo.
 */
public enum TipoMovimiento {

    /** Movimiento de tipo ingreso (monto positivo) */
    INGRESO("Ingreso"),

    /** Movimiento de tipo egreso (monto negativo) */
    EGRESO("Egreso");

    /** Texto descriptivo del tipo de movimiento */
    private final String valor;

    /**
     * Constructor privado del enum.
     * @param valor representación en texto del tipo de movimiento
     */
    TipoMovimiento(String valor) {
        this.valor = valor;
    }

    /**
     * Obtiene el valor en texto del tipo de movimiento.
     * @return cadena con la descripción (ej. "Ingreso", "Egreso")
     */
    public String getValor() {
        return valor;
    }
}
