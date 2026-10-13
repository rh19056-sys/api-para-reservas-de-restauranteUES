package sv.edu.ues.reservas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiParaReservasDeRestauranteUesApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(ApiParaReservasDeRestauranteUesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ApiParaReservasDeRestauranteUesApplication.class, args);
        logger.info("API para Reservas de Restaurante iniciada correctamente.");
    }
}