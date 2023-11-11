package com.example.orderservice.repository;

import com.example.orderservice.pojo.Order;
import com.example.orderservice.pojo.Product;
import com.example.orderservice.pojo.ShopProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public OrderService(OrderRepository repository){
        this.repository = repository;
    }

    public Order createOrder(Order newOrder){
        try{
            return repository.save(newOrder);
        }catch (Exception error){
            System.out.println(error);
            return null;
        }
    }

    public ArrayList<Order> getOrders(){
        return (ArrayList<Order>) repository.findAll();
    }

    public Order getOrderByOrderID(String orderID){
        Optional<Order> result = repository.findById(orderID);

        if(result.isPresent()) return result.get();

        return null;
    }

    public ArrayList<Order> getOrdersByCustomerID(String customerID){
        Optional<ArrayList<Order>> result = repository.findByCustomerID(customerID);

        if(result.isPresent()) return result.get();

        return null;
    }

    public ArrayList<ShopProduct> getProductOrdersByShopID(String shopID){

        Optional<ArrayList<Order>> ordersContainShopID = repository.getOrdersByShopID(shopID);

        if(!ordersContainShopID.isPresent()) return null;

        ArrayList<ShopProduct> shopProducts = new ArrayList<ShopProduct>();

        for(int i = 0; i < ordersContainShopID.get().size(); i++){
            ArrayList<Product> productsOfTheShop = new ArrayList<Product>();
            Order currentOrder = ordersContainShopID.get().get(i);

            for(Product p : ordersContainShopID.get().get(i).getProducts()){
                // ทำการดึงเฉพาะ Products
                if(p.getShop_id().equals(shopID)){
                    productsOfTheShop.add(p);
                }
            }

            ShopProduct shopProduct = new ShopProduct();
            shopProduct.setOrder_id(currentOrder.get_id());
            shopProduct.setProducts(productsOfTheShop);
            shopProducts.add(shopProduct);
        }

        return shopProducts;
    }

    public Order updateOrder(String orderID, String productID, String toUpdateStatus){
        Optional<Order> foundOrder = repository.findById(orderID); // หา Order ที่จะแก้ไข
        if(!foundOrder.isPresent()) return null;
        ArrayList<Product> allProductsUnderOrder = foundOrder.get().getProducts();
        Integer indexOfProductToUpdate = -1;

        for (int i = 0; i < allProductsUnderOrder.size(); i++){
            if(allProductsUnderOrder.get(i).get_id().equals(productID)){
                indexOfProductToUpdate = i;
            }
        }
        allProductsUnderOrder.get(indexOfProductToUpdate).setOrder_status(toUpdateStatus);
        foundOrder.get().setProducts(allProductsUnderOrder);
        return repository.save(foundOrder.get());
    }

    public Boolean deleteOrder(String toDeleteOrderID){
        try{
            long before = repository.count();
            repository.deleteById(toDeleteOrderID);
            long after = repository.count();
            if(before - after == 1) return true;

            return false;

        }catch (Exception error){
            System.out.println(error);
            return false;
        }

    }
}