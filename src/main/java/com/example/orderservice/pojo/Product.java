package com.example.orderservice.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document("ProductService")
public class Product implements Serializable {
    private String _id;
    private String name;
    private String description;
    private String img_path;
    private double price;
    private String category;
    private String shop_id;
    private double rating;
    private String order_status = "ORDERED";

    public Product() {
    }

    public Product(String name, String description, String img_path, double price, String category, String shop_id, String order_status) {
        this(null, name, description, img_path, price, category, shop_id);
    }

    public Product(String name) {
        this(null, name, null, null, 0.0, null, null);
    }

    public Product(String _id, String name, String description, String img_path, double price, String category, String shop_id) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.img_path = img_path;
        this.price = price;
        this.category = category;
        this.shop_id = shop_id;
        this.order_status = "ORDERED";
    }
}