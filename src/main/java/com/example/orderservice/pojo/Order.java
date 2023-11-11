package com.example.orderservice.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Document("Order")
public class Order implements Serializable {
    @Id
    private String _id;
    private String customer_id;
    private double total_cost;
    private ArrayList<Product> products;

    public Order(){}

//    public Order(String _id, String customer_id, double total_cost,ArrayList<Product> products){
//        this._id = _id;
//        this.customer_id = customer_id;
//        this.total_cost = total_cost;
//        this.products = products;
//    }
//    public Order(String _id, String customer_id, double total_cost){
//        this._id = _id;
//        this.customer_id = customer_id;
//        this.total_cost = total_cost;
//    }
//
//    public Order(String customer_id, double total_cost, ArrayList<Product> products) {
//        this.customer_id = customer_id;
//        this.total_cost = total_cost;
//        this.products = products;
//    }

    public Order(double total_cost, ArrayList<Product> products) {
        this.total_cost = total_cost;
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

}
