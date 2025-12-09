package br.com.unicos.ms_pessoas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsPessoasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPessoasApplication.class, args);
	}

}
