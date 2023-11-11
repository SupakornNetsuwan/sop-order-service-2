package com.example.orderservice.pojo;

import lombok.Data;

@Data
public class UpdateStatus {
    private String order_status;
    public UpdateStatus(){}

    public UpdateStatus(String order_status){
        this.order_status = order_status;
    }
}
