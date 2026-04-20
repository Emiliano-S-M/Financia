package CanalDev.Financia.Repositorio;

import CanalDev.Financia.Modelo.MovimientoModelo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link MovimientoModelo}.
 *
 * Responsabilidades:
 * - Proporcionar operaciones CRUD básicas sobre movimientos financieros.
 * - Delegar la persistencia a Spring Data JPA.
 *
 * Notas:
 * - Hereda métodos como save, findAll, findById, delete, etc.
 * - La clave primaria de la entidad es de tipo {@link Integer}.
 */
public interface MovimientoRepositorio extends JpaRepository<MovimientoModelo, Integer> {
    List<MovimientoModelo> findByFechaMovimientoAfterOrderByIdMovimientoDesc(LocalDate fecha);
}
