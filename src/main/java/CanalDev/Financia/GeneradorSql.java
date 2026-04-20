package CanalDev.Financia;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class GeneradorSql {
    static List<String> categoriasIngreso = List.of("Salario", "Bonos", "Freelance", "Ventas", "Otro");
    static List<String> categoriasEgreso = List.of("Comida", "Transporte", "Internet", "Renta", "Salud", "Entretenimiento", "Otro");

    public static void main(String[] args) {

        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.now();

        Random rand = new Random();

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO MOVIMIENTO_MODELO (CATEGORIA_MOVIMIENTO, FECHA_MOVIMIENTO, MONTO_MOVIMIENTO, TIPO_MOVIMIENTO) VALUES\n");

        LocalDate fecha = inicio;
        boolean primero = true;

        while (!fecha.isAfter(fin)) {

            boolean esIngreso = rand.nextBoolean();
            String tipo = esIngreso ? "Ingreso" : "Egreso";

            String categoria;
            double monto;

            if (esIngreso) {
                categoria = categoriasIngreso.get(rand.nextInt(categoriasIngreso.size()));
                monto = generarMontoIngreso(categoria, rand);
            } else {
                categoria = categoriasEgreso.get(rand.nextInt(categoriasEgreso.size()));
                monto = generarMontoEgreso(categoria, rand);
            }

            if (!primero) {
                sb.append(",\n");
            }

            sb.append(String.format("('%s', '%s', %.2f, '%s')",
                    categoria,
                    fecha,
                    monto,
                    tipo));

            primero = false;
            fecha = fecha.plusDays(1);
        }

        sb.append(";");

        System.out.println(sb);
    }

    static double generarMontoIngreso(String categoria, Random rand) {
        return switch (categoria) {
            case "Salario" -> 10000 + rand.nextInt(5000);
            case "Bonos" -> 1000 + rand.nextInt(3000);
            case "Freelance" -> 500 + rand.nextInt(5000);
            case "Ventas" -> 300 + rand.nextInt(4000);
            default -> 200 + rand.nextInt(2000);
        };
    }

    static double generarMontoEgreso(String categoria, Random rand) {
        return switch (categoria) {
            case "Renta" -> (3000 + rand.nextInt(2000)) * -1;
            case "Comida" -> (100 + rand.nextInt(400)) * -1;
            case "Transporte" -> (50 + rand.nextInt(200)) * -1;
            case "Internet" -> (400 + rand.nextInt(200)) * -1;
            case "Salud" -> (200 + rand.nextInt(1500)) * -1;
            case "Entretenimiento" -> (150 + rand.nextInt(800)) * -1;
            default -> (100 + rand.nextInt(500)) * -1;
        };
    }

}
