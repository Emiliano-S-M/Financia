package CanalDev.Financia.Servicio;

import java.util.List;

/**
 * Interfaz para el servicio de categorías financieras.
 * Define el contrato que deben cumplir las implementaciones
 * encargadas de proporcionar categorías según el tipo de movimiento.
 *
 * Responsabilidades:
 * - Exponer un método para obtener categorías asociadas a un tipo de movimiento.
 * - Permitir distintas implementaciones (ej. categorías fijas, dinámicas desde BD).
 */
public interface ICategoriaServicio {
    /**
     * Obtiene las categorías disponibles según el tipo de movimiento.
     *
     * @param tipoMovimiento tipo de movimiento ("Ingreso" o "Egreso")
     * @return lista de categorías asociadas al tipo
     */
    List<String> obtenerCategorias(String tipoMovimiento);
}
