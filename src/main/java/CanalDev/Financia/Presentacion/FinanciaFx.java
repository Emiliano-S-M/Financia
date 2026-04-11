package CanalDev.Financia.Presentacion;

import CanalDev.Financia.FinanciaApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Clase principal de presentación que integra JavaFX con Spring Boot.
 *
 * Responsabilidades:
 * - Inicializar el contexto de Spring al arrancar la aplicación.
 * - Cargar la interfaz gráfica principal (Balance.fxml).
 * - Configurar la ventana principal de la aplicación (icono, título, escena).
 * - Cerrar correctamente el contexto de Spring al finalizar.
 *
 * Notas:
 * - Extiende {@link javafx.application.Application} para gestionar el ciclo de vida de JavaFX.
 * - Utiliza {@link SpringApplicationBuilder} para levantar el contexto de Spring Boot.
 */
public class FinanciaFx extends Application {

    /** Contexto de la aplicación Spring, compartido entre controladores */
    private static ConfigurableApplicationContext applicationContext;

    /**
     * Inicializa el contexto de Spring Boot antes de iniciar la interfaz gráfica.
     */
    @Override
    public void init(){
        applicationContext = new SpringApplicationBuilder(FinanciaApplication.class).run();
    }

    /**
     * Configura y muestra la ventana principal de la aplicación.
     * @param primaryStage escenario principal de JavaFX
     * @throws Exception si ocurre un error al cargar el FXML
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader =
                new FXMLLoader(FinanciaApplication.class.getResource("/templates/Balance.fxml"));

        // Permite que los controladores sean gestionados por Spring
        loader.setControllerFactory(applicationContext::getBean);

        Scene escena = new Scene(loader.load());

        // Configuración de icono y título de la ventana
        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/Imagenes/FinanciaLogo.png"))
        );
        primaryStage.setScene(escena);
        primaryStage.setTitle("Financia");
        primaryStage.show();
    }

    /**
     * Cierra el contexto de Spring y finaliza la aplicación JavaFX.
     */
    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }

    /**
     * Obtiene el contexto de Spring para ser usado en otras partes de la aplicación.
     * @return contexto de la aplicación Spring
     */
    public static ConfigurableApplicationContext getContext() {
        return applicationContext;
    }
}
