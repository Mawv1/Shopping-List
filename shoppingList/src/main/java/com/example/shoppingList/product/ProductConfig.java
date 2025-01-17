package com.example.shoppingList.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository productRepository){
        return args ->{
            Product milk = new Product(
                    "Milk",
                    3.3,
                    2,
                    "liters",
                    true
            );

            Product bread = new Product(
                    "Bread",
                    4,
                    1,
                    "pieces",
                    true
            );

            productRepository.saveAll(
                    List.of(milk, bread)
            );
        };
    }
}
