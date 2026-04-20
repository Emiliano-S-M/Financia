package CanalDev.Financia.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

/**
 *
 * @author Emiliano
 *
 */

/**
 * Entidad JPA que representa un movimiento financiero.

 * Responsabilidades:
 * - Mapear la tabla de movimientos en la base de datos.
 * - Almacenar información básica: tipo, monto, categoría y fecha.

 * Notas:
 * - El monto se guarda como positivo para ingresos y negativo para egresos.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovimientoModelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimiento;   // Identificador único del movimiento
    private String tipoMovimiento;  // Tipo: Ingreso o Egreso
    private float montoMovimiento;  // Monto del movimiento
    private String categoriaMovimiento; // Categoría asociada (ej. comida, salario)
    private LocalDate fechaMovimiento; // Fecha del movimiento en formato texto
}
