package CanalDev.Financia;

import CanalDev.Financia.Presentacion.FinanciaFx;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.*;

/**
 * Clase principal de la aplicación Financia.
 * Marca el punto de entrada del programa y combina
 * la inicialización de Spring Boot con el arranque de JavaFX.
 *
 * Responsabilidades:
 * - Configurar el contexto de Spring Boot.
 * - Lanzar la aplicación JavaFX definida en {@link FinanciaFx}.
 */
@SpringBootApplication
public class FinanciaApplication {

	/**
	 * Método principal de la aplicación.
	 * Inicializa el contexto de Spring Boot y lanza la interfaz JavaFX.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		Application.launch(FinanciaFx.class, args);
	}
}
