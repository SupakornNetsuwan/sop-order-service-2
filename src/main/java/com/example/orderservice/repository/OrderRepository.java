package com.example.orderservice.repository;

import com.example.orderservice.pojo.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    @Query("{customer_id: '?0'}")
    Optional<ArrayList<Order>> findByCustomerID(String customerID);

    @Query(value = "{'products.shop_id' : '?0'}")
    Optional<ArrayList<Order>> getOrdersByShopID(String shopID);
}
