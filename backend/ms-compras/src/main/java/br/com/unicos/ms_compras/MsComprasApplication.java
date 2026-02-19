package br.com.unicos.ms_compras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "br.com.unicos.ms_compras.client")
public class MsComprasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsComprasApplication.class, args);
	}

}
