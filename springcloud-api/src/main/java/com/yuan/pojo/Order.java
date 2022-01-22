package com.yuan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("torder")
public class Order implements Serializable {
    @TableId(value = "orderid",type = IdType.ASSIGN_UUID)
    private String orderId;
    @TableField("userid")
    private Integer userId;
    private Integer num;
    private Double total;
    @TableField("mqstatus")
    private Integer mqStatus;
    @TableField("goodname")
    private String goodName;

    public Order(String orderId, Integer mqStatus) {
        this.orderId = orderId;
        this.mqStatus = mqStatus;
    }
}
