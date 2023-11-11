package com.example.orderservice.controller;

import com.example.orderservice.pojo.ErrorResponse;
import com.example.orderservice.pojo.Order;
import com.example.orderservice.pojo.Product;
import com.example.orderservice.pojo.UpdateStatus;
import com.example.orderservice.repository.JwtService;
import com.example.orderservice.repository.OrderService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private JwtService jwtservice;
    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    /* ------------- Create ------------- */

    // Create order
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody Order newOrder,@RequestHeader(value = "Authorization", required = true) String token
    ){
        String[] token2 = token.split(" ");

        String userId = null;
        if(token2.length > 1){
            Claims claims = jwtservice.parseToken(token2[1]);
            userId = claims.getSubject();
        }

        try {
            newOrder.setCustomer_id(userId);
            this.validateOrderData(newOrder);
            Order result = orderService.createOrder(newOrder);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Cannot create new order", ie.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Cannot create new order", e.getMessage()));
        }
    }

    /* ------------- Read ------------- */

    // Get orders
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Order>> getOrders(){
        ArrayList<Order> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);

    }

    // Get order
    @RequestMapping(value = "/orders/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderByOrderID(@PathVariable("orderID") String orderID){

        Order foundOrder = orderService.getOrderByOrderID(orderID);
        if (foundOrder == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Cannot update order", "orderID not found"));
        }

        return ResponseEntity.ok(foundOrder);
    }

    // Get orders (By user ID)
    @RequestMapping(value = "/orders/users/{customerID}", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Order>> getOrdersByCustomerID(@PathVariable("customerID") String customerID){
        return ResponseEntity.ok(orderService.getOrdersByCustomerID(customerID));
    }

    // Get orders (By store owner)
    @RequestMapping(value = "/orders/shops/{storeID}", method = RequestMethod.GET)
    public ResponseEntity<?> getProductOrdersByShopID(@PathVariable("storeID") String shopID, @RequestHeader(value = "Authorization", required = true) String token){
        String[] token2 = token.split(" ");

        String ownerId = null;
        if(token2.length > 1){
            Claims claims = jwtservice.parseToken(token2[1]);
            ownerId = claims.getSubject();
        }

        try{
            return ResponseEntity.ok(orderService.getProductOrdersByShopID(shopID));
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Cannot update order", ie.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Cannot update order", e.getMessage()));
        }

    }

    /* ------------- Update ------------- */

    // Update order
    @RequestMapping(value = "/orders/{orderID}/{productID}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateOrder(@PathVariable("orderID") String orderID, @PathVariable("productID") String productID, @RequestBody UpdateStatus toUpdatePayload, @RequestHeader(value = "Authorization", required = true) String token){
        String[] token2 = token.split(" ");
        if(token2.length > 1){
            Claims claims = jwtservice.parseToken(token2[1]);
            claims.getSubject();
        }

        try {
            this.validateUpdateStatus(toUpdatePayload);
            // Order นี้ และ Product นี้ ให้ทำการแก้ไข
            Order updateResult = orderService.updateOrder(orderID, productID, toUpdatePayload.getOrder_status());

            if (updateResult == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Cannot update order", "orderID not found"));
            }
            return ResponseEntity.ok(updateResult);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Cannot update order", ie.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Cannot update order", e.getMessage()));
        }
    }

    /* ------------- Delete ------------- */

    // Delete order
    @RequestMapping(value = "/orders/{orderID}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("orderID") String orderID,@RequestHeader(value = "Authorization", required = true) String token){
        String[] token2 = token.split(" ");
        if(token2.length > 1){
            Claims claims = jwtservice.parseToken(token2[1]);
            claims.getSubject();
        }

        Boolean deletedResult = orderService.deleteOrder(orderID);
        return  ResponseEntity.ok(deletedResult);
    }

    public void validateOrderData(Order order) throws Exception{
        if (order.getTotal_cost() < 0) {
            throw new IllegalArgumentException("Total price must be greater than Zero or equal to Zero");
        }
        if (order.getProducts().size() == 0) {
            throw new IllegalArgumentException("Products must have at least 1 product");
        }
        for (Product product : order.getProducts()) {
            if (product.getPrice() < 0) {
                throw new IllegalArgumentException("Invalid Product \uD83D\uDD34, Price must be greater than or equal to Zero");
            }
            if (product.getName() == null || product.getName().isBlank()) {
                throw new IllegalArgumentException("Invalid Product \uD83D\uDD34, Name is Required");
            }
            if (product.getDescription() == null || product.getDescription().isBlank()) {
                throw new IllegalArgumentException("Invalid Product \uD83D\uDD34, Description is Required");
            }
        }
    }

    public void validateUpdateStatus(UpdateStatus toUpdateStatus) throws Exception {
        ArrayList<String> statuses = new ArrayList<String>(Arrays.asList("DELIVERED", "SHIPPED", "CANCEL", "ORDERED"));
        boolean check = false;

        for(String status : statuses){
            if (toUpdateStatus.getOrder_status().equals(status)) {
                check = true;
            }
        }

        if(!check){
            throw new IllegalArgumentException("The order status does not match any of a valid status");
        }
    }
}
