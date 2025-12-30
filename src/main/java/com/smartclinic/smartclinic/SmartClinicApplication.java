package com.smartclinic.smartclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaRepositories(basePackages = "com.smartclinic.smartclinic.repository.mysql")
@EnableMongoRepositories(basePackages = "com.smartclinic.smartclinic.repository.mongo")
@SpringBootApplication
public class SmartClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartClinicApplication.class, args);
    }

}
