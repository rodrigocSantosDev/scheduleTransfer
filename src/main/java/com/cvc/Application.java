package com.cvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.cvc.repository.TransferRepository;
import com.cvc.resources.TransferResource;
import com.cvc.service.TransferService;

@SpringBootApplication
@ComponentScan(basePackageClasses = TransferResource.class)
@EntityScan({"com.cvc.model"})
@ComponentScan(basePackageClasses = TransferService.class)
@EnableJpaRepositories(basePackageClasses = TransferRepository.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
