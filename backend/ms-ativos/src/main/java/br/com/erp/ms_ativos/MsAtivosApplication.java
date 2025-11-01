package br.com.erp.ms_ativos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsAtivosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAtivosApplication.class, args);
	}

}
