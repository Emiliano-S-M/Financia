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

public class FinanciaFx extends Application {

    private static ConfigurableApplicationContext applicationContext;

    @Override
    public void  init(){
        applicationContext = new SpringApplicationBuilder(FinanciaApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader =
                new FXMLLoader(FinanciaApplication.class.getResource("/templates/Balance.fxml"));

        loader.setControllerFactory(applicationContext::getBean);

        Scene escena = new Scene(loader.load());
        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/Imagenes/FinanciaLogo.png"))
        );
        primaryStage.setScene(escena);
        primaryStage.setTitle("Financia");
        primaryStage.show();
    }

    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }

    public static ConfigurableApplicationContext getContext() {
        return applicationContext;
    }
}
