package com.medvedev.backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        try {
            System.out.println("Initializing ModelMapper...");



            System.out.println("ModelMapper configured successfully.");
            return modelMapper;

        } catch (Exception e) {
            System.err.println("Error initializing ModelMapper: " + e.getMessage());
            e.printStackTrace();
            throw e; // Rethrow exception for clarity
        }
    }
}
