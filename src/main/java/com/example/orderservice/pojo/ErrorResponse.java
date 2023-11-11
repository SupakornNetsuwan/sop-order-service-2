package com.example.orderservice.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrorResponse implements Serializable {
    private String message;
    private String cause;

    public ErrorResponse(String message, String cause) {
        this.message = message;
        this.cause = cause;
    }
}
