package com.zarinatta.zarinattaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ZarinattaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZarinattaServerApplication.class, args);
	}

	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new
				CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

}
