package com.example.shoppingList.product;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;
    private String name;
    private double price;
    private double quantity;
    @Transient
    private double totalPrice;
    private String unit;
    boolean isInteger;
    public Product() {
    }

    public Product(Long id, String name, double price, double quantity, String unit, boolean isInteger) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.isInteger = isInteger;
    }

    public Product(String name, double price, double quantity, String unit, boolean isInteger) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.isInteger = isInteger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public boolean getIsInteger() {
        return isInteger;
    }

    public void setIsInteger(boolean isInteger) {
        this.isInteger = isInteger;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", unit='" + unit + '\'' +
                ", isInteger='" + isInteger + "'"+
                '}';
    }
}
