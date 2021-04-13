package bootcamp.mercado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MercadoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MercadoApplication.class, args);
	}

}
