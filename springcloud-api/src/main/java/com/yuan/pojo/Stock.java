package com.yuan.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {
    @TableId(type=IdType.AUTO)
    private Integer id;
    private String num;
    private Integer total;
    private Double price;
    @TableField("good_name")
    private String goodName;
}
