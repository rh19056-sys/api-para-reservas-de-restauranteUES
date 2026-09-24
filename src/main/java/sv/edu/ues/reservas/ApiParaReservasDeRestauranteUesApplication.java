package sv.edu.ues.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiParaReservasDeRestauranteUesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiParaReservasDeRestauranteUesApplication.class, args);
        System.out.println("API para Reservas de Restaurante iniciada correctamente.");
    }
}

