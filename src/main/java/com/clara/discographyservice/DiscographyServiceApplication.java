package com.clara.discographyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DiscographyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscographyServiceApplication.class, args);
    }

}
