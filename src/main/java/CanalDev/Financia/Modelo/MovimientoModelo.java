package CanalDev.Financia.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Emiliano
 *
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MovimientoModelo {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idMovimiento;
    private String tipoMovimiento;
    private float montoMovimiento;
    private String categoriaMovimiento;
    private String fechaMovimiento;
}
