package CanalDev.Financia.Servicio;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServicio {
    public List<String> obtenerCategorias(String tipo) {
        if ("Ingreso".equals(tipo)) {
            return List.of("Salario", "Bonos", "Freelance", "Ventas", "Otro");
        } else if ("Egreso".equals(tipo)) {
            return List.of("Comida", "Transporte", "Internet", "Renta", "Salud", "Entretenimiento", "Otro");
        }
        return List.of();
    }
}
