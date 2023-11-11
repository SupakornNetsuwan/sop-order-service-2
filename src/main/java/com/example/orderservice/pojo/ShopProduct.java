package com.example.orderservice.pojo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ShopProduct {
    private String order_id;
    private ArrayList<Product> products;

    public ShopProduct(){}
}
