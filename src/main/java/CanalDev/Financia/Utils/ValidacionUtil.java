package CanalDev.Financia.Utils;

/**
 * Utilidad para validaciones básicas de campos y objetos.
 * Contiene métodos estáticos para verificar si un texto está vacío
 * o si un objeto es nulo.
 */
public class ValidacionUtil {

    /**
     * Verifica si un texto está vacío, nulo o compuesto solo por espacios.
     *
     * @param valor cadena a validar
     * @return true si el texto es nulo o vacío, false en caso contrario
     */
    public static boolean campoVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    /**
     * Verifica si un objeto es nulo.
     *
     * @param valor objeto a validar
     * @return true si es nulo, false en caso contrario
     */
    public static boolean objetoNulo(Object valor) {
        return valor == null;
    }
}
