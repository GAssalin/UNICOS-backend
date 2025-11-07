package br.com.unicos.ms_compras;

import lombok.EqualsAndHashCode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EqualsAndHashCode
public class MsComprasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsComprasApplication.class, args);
	}

}
