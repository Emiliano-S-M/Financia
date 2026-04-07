package CanalDev.Financia;

import CanalDev.Financia.Presentacion.FinanciaFx;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javafx.application.*;

@SpringBootApplication
public class FinanciaApplication {

	public static void main(String[] args) {
		Application.launch(FinanciaFx.class, args);
	}
}
