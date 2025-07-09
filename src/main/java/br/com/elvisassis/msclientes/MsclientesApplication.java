package br.com.elvisassis.msclientes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsclientesApplication {

	public static void main(String[] args) {
		// Carrega as variáveis do .env e as define como propriedades do sistema
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(MsclientesApplication.class, args);
	}

}
