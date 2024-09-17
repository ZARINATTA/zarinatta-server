package com.zarinatta.zarinattaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ZarinattaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZarinattaServerApplication.class, args);
    }

}
