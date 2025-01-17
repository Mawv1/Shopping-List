package com.example.shoppingList.product;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public void addNewProduct(Product product) {
        Optional<Product> productOptional = productRepository
                .findProductByName(product.getName());
        if (productOptional.isPresent()) {
            throw new IllegalStateException("Product already in shopping list, you can update it instead!");
        }
        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        boolean exists = productRepository.existsById(productId);
        if (!exists) {
            throw new IllegalStateException("Product with id " + productId + " does not exist!");
        }
        productRepository.deleteById(productId);
    }
    public Product saveProduct(Product product) {
        System.out.println("save Product");
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long productId, String name, Double price, Integer quantity, String unit, boolean isInteger) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException(
                        "Product with id " + productId + " does not exist!"
                ));
        if (name != null && name.length() > 0 && !Objects.equals(product.getName(), name)) {
            if (productRepository.findProductByName(name).isPresent()) {
                throw new IllegalStateException("Product with name " + name + " already exists!");
            }
            product.setName(name);
        }
        if (price != null && price > 0 && !Objects.equals(product.getPrice(), price)) {
            product.setPrice(price);
        }
        if (quantity != null && quantity > 0 && !Objects.equals(product.getQuantity(), quantity)) {
            product.setQuantity(quantity);
        }
        if (unit != null && unit.length() > 0 && !Objects.equals(product.getUnit(), unit)) {
            product.setUnit(unit);
        }
        return product;
    }


//    @Transactional
//    public void updateProduct(Long productId, String name, Double price, Integer quantity, String unit) {
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new IllegalStateException(
//                        "Product with id " + productId + " does not exist!"
//                ));
//        if(name != null && name.length() > 0 && !Objects.equals(product.getName(), name)){
//            if(productRepository.findProductByName(name).isPresent()){
//                throw new IllegalStateException("Product with name " + name + " already exists!");
//            }
//            product.setName(name);
//        }
//        if(price != null && price > 0 && !Objects.equals(product.getPrice(), price)){
//            product.setPrice(price);
//        }
//        if(quantity != null && quantity > 0 && !Objects.equals(product.getQuantity(), quantity)){
//            product.setQuantity(quantity);
//        }
//        if(unit != null && unit.length() > 0 && !Objects.equals(product.getUnit(), unit)){
//            product.setUnit(unit);
//        }
//    }

    public Product getProductById(Long Id) {
        return productRepository.findById(Id)
                .orElseThrow(() -> new IllegalStateException(
                        "Product with id " + Id + " does not exist!"
                ));
    }
}
