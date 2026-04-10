package CanalDev.Financia.Config;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Inicializador de almacenamiento para la aplicación.
 * Se asegura de que exista la carpeta definida en la propiedad
 * `app.storage.path` (o por defecto en ${user.home}/Financia).

 * Este componente se ejecuta automáticamente al iniciar la aplicación.
 */
@Component
public class StorageInitializer {

    /** Ruta de almacenamiento configurable vía application.properties */
    @Value("${app.storage.path:${user.home}/Financia}")
    private String path;

    /**
     * Método de inicialización ejecutado después de construir el bean.
     * Crea la carpeta de almacenamiento si no existe.
     */
    @PostConstruct
    public void init() {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}