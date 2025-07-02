package br.com.elvisassis.msavaliadorcredito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CreditEvaluatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreditEvaluatorApplication.class, args);
	}

}
