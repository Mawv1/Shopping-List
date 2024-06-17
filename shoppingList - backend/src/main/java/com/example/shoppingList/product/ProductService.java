package com.example.shoppingList.product;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    public List<Product> getProducts() {
        return List.of(
                new Product(1L,
                        "Milk",
                        1.07,
                        2.0)
        );
    }
}
