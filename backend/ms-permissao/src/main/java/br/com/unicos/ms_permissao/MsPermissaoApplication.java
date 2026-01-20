package br.com.unicos.ms_permissao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "br.com.unicos.ms_permissao.client")
public class MsPermissaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPermissaoApplication.class, args);
	}

}
