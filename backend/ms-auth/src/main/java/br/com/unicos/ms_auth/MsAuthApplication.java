package br.com.unicos.ms_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "br.com.unicos.ms_auth.client")
public class MsAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAuthApplication.class, args);
	}

}
