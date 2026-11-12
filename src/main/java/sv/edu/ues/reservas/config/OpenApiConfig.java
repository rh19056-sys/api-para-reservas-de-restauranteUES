package sv.edu.ues.reservas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI reservasOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Reservas de Restaurante")
                        .description("API REST para la gestión de cuentas, restaurantes, sucursales, mesas, "
                                + "menús, reservas, consumos y pagos. Proyecto de POO - UES.")
                        .version("v1")
                        .license(new License().name("Uso académico")))
                .servers(List.of(new Server().url("/").description("Servidor actual")));
    }
}
