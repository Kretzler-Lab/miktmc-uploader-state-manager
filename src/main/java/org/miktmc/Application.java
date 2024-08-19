package org.miktmc;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@ComponentScan(basePackages = { "org.miktmc" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}