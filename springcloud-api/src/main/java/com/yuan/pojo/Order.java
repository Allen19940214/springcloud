package com.yuan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private String orderId;
    private Integer userId;
    private Integer num;
    private Double total;
    private Integer mqStatus;
    private String goodName;

    public Order(String orderId, Integer mqStatus) {
        this.orderId = orderId;
        this.mqStatus = mqStatus;
    }
}
