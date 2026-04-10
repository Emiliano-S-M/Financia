package CanalDev.Financia.Servicio;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de categorías financieras.
 * Proporciona las categorías disponibles según el tipo de movimiento.

 * Responsabilidades:
 * - Devolver categorías para ingresos.
 * - Devolver categorías para egresos.
 * - Retornar lista vacía si el tipo no es reconocido.
 */
@Service
public class CategoriaServicio implements ICategoriaServicio{

    /**
     * Obtiene las categorías según el tipo de movimiento.
     *
     * @param tipo tipo de movimiento ("Ingreso" o "Egreso")
     * @return lista de categorías asociadas al tipo
     */
    public List<String> obtenerCategorias(String tipo) {
        if ("Ingreso".equals(tipo)) {
            return List.of("Salario", "Bonos", "Freelance", "Ventas", "Otro");
        } else if ("Egreso".equals(tipo)) {
            return List.of("Comida", "Transporte", "Internet", "Renta", "Salud", "Entretenimiento", "Otro");
        }
        return List.of();
    }
}
