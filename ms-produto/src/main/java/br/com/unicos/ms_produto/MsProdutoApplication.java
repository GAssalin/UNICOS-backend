package br.com.unicos.ms_produto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "br.com.unicos.ms_produto.client")
public class MsProdutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProdutoApplication.class, args);
	}

}
