package CanalDev.Financia.Utils;

import java.time.LocalTime;

/**
 * Utilidad para generar saludos dinámicos según la hora del día.
 * Se usa en la interfaz para mostrar un mensaje personalizado
 * (buenos días, buenas tardes o buenas noches).
 */
public class SaludoUtil {

    /**
     * Genera un saludo basado en la hora actual.
     * - Antes del mediodía: "Hola, buenos días"
     * - Entre mediodía y 18:00: "Hola, buenas tardes"
     * - Después de las 18:00: "Hola, buenas noches"
     *
     * @return saludo correspondiente a la hora actual
     */
    public static String generarSaludo() {
        LocalTime hora = LocalTime.now();
        String saludo;
        if (hora.isBefore(LocalTime.NOON)) {
            saludo = "Hola, buenos días";
        } else if (hora.isBefore(LocalTime.of(18, 0))) {
            saludo = "Hola, buenas tardes";
        } else {
            saludo = "Hola, buenas noches";
        }
        return saludo;
    }
}
